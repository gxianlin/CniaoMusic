package com.cainiao.music.cniaomusic.service;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.music.MusicPlaylist;

import java.io.IOException;
import java.util.ArrayList;

import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;


/**
 * 描述：音乐播放管理类
 * 作者：gong.xl
 * 创建日期：2017/8/12 0012 13:10
 * 修改日期: 2017/8/12 0012
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class MusicPlayerManager implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnCompletionListener, MediaPlayer.OnSeekCompleteListener,
        MediaPlayer.OnErrorListener {

    private Context mContext;
    public MusicService mService;
    private MediaPlayer mMediaPlayer;
    private final static MusicPlayerManager instance = new MusicPlayerManager();
    private MusicPlaylist mMusicPlaylist;

    private long currentMediaId = -1;
    private int currentProgress;

    public static final int MAX_DURATION_FOR_REPEAT = 3000;
    private int currentMaxDuration = MAX_DURATION_FOR_REPEAT;

    private ArrayList<OnSongchangeListener> changeListeners = new ArrayList<>();

    private MusicPlayerManager() {
    }

    /**
     * 是否需要重启服务
     *
     * @param context
     */
    public static void startServiceIfNecessary(Context context) {
        if (getInstance().mService == null) {
            MusicServiceHelper.get(context).initService();
            getInstance().mService = MusicServiceHelper.get(context).getService();
        }
    }

    public static MusicPlayerManager getInstance() {
        return instance;
    }

    public static MusicPlayerManager from(MusicService service) {
        return MusicPlayerManager.getInstance().setContext(service).setService(service);
    }

    public MusicPlayerManager setContext(Context context) {
        this.mContext = context;
        return this;
    }

    public MusicPlayerManager setService(MusicService service) {
        this.mService = service;
        return this;
    }


    /**
     * 直接播放
     */
    public void play() {
        if (mMusicPlaylist == null || mMusicPlaylist.getCurrentPlay() == null) {
            return;
        }
        play(mMusicPlaylist.getCurrentPlay());
    }


    /**
     * 传递播放列表,并根据下标播放歌曲
     *
     * @param musicPlaylist
     * @param position
     */
    public void playQueue(MusicPlaylist musicPlaylist, int position) {
        this.mMusicPlaylist = musicPlaylist;
        mMusicPlaylist.setCurrentPlay(position);
        play(mMusicPlaylist.getCurrentPlay());
    }

    /**
     * 根据下标播放已有列表歌曲
     *
     * @param position
     */
    public void playQueueItem(int position) {
        mMusicPlaylist.setCurrentPlay(position);
        play(mMusicPlaylist.getCurrentPlay());
    }


    private void play(Song song) {
        if (song == null) {
            return;
        }

        //判断是否是当期播放的歌曲
        boolean musicHasChange = !(song.getId() == currentMediaId);
        if (musicHasChange) {
            currentProgress = 0;
            currentMediaId = song.getId();
        }

        //判断播放状态:暂停
        if (mService.getState() == PlaybackStateCompat.STATE_PAUSED &&
                !musicHasChange && mMediaPlayer != null) {
            configMediaPlayerState();

        } else {

            try {
                createMediaPlayerIfNeeded();
                mService.setState(PlaybackStateCompat.STATE_PLAYING);
                Log.e("uri: ", "uri: " + song.getUri());
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mMediaPlayer.setDataSource(mContext, song.getUri());
                mMediaPlayer.prepareAsync();

                for (OnSongchangeListener l : changeListeners){
                    l.onSongChanged(song);
                }

            } catch (IOException e) {
                Log.e("tag", "playing song:", e);
            }
        }
    }

    /**
     * 创建MediaPlayer
     */
    private void createMediaPlayerIfNeeded() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnSeekCompleteListener(this);
            mMediaPlayer.setOnErrorListener(this);
        } else {
            mMediaPlayer.reset();
        }
    }

    /**
     * 确定播放
     */
    private void configMediaPlayerState() {
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            if (currentProgress == mMediaPlayer.getCurrentPosition()) {
                mMediaPlayer.start();
                mService.setState(PlaybackStateCompat.STATE_PLAYING);
            } else {
                mMediaPlayer.seekTo(currentProgress);
                mMediaPlayer.start();
                mService.setState(PlaybackStateCompat.STATE_PLAYING);
            }
        }

    }

    /**
     * 播放下一首
     */
    public void playNext() {
        currentProgress = 0;
        play(mMusicPlaylist.getNextSong());
    }

    /**
     * 播放上一首
     */
    public void playPre() {
        currentProgress = 0;
        play(mMusicPlaylist.getPreSong());
    }

    /**
     * 暂停
     */
    public void puase() {
        if (mService.getState() == PlaybackStateCompat.STATE_PLAYING) {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                mMediaPlayer.pause();
                currentProgress = mMediaPlayer.getCurrentPosition();
            }
            //释放焦点,通知
            relaxResources(false);
            giveUpAudioFocus();
        }
        mService.setState(PlaybackStateCompat.STATE_PAUSED);
    }

    /**
     * 选择进度
     */
    public void seekTo(int progress) {
        if (mMediaPlayer == null) {
            currentProgress = progress;
        } else {
            if (getCurrentProgressInSong() > progress) {
                mService.setState(PlaybackStateCompat.STATE_REWINDING);
            } else {
                mService.setState(PlaybackStateCompat.STATE_FAST_FORWARDING);
            }
            currentProgress = progress;
            mMediaPlayer.seekTo(currentProgress);
        }

    }

    /**
     * 停止
     */
    public void stop() {
        mService.setState(PlaybackStateCompat.STATE_STOPPED);
        currentProgress = getCurrentProgressInSong();
        giveUpAudioFocus();
        relaxResources(true);
        mService.stopService();
    }



    /**
     * 清除列表
     */
    public void clearPlayer() {
        if (getMusicPlayList() != null) {
            getMusicPlayList().clear();
        }

        mService.setState(PlaybackStateCompat.STATE_STOPPED);

        if (mMediaPlayer != null) {
            mMediaPlayer.reset();
        }
        giveUpAudioFocus();
    }

    /**
     * 恢复播放
     */
    public void resume() {
        if (mService.getState() == PlaybackStateCompat.STATE_PAUSED &&
                mMediaPlayer != null) {
            mMediaPlayer.start();
            mService.setState(PlaybackStateCompat.STATE_PLAYING);
        }
    }


    public final static int CYCLETYPE = 0;      //列表循环播放
    public final static int SINGLETYPE = 1;      //单曲播放
    public final static int RANDOMTYPE = 2;      //随机播放
    public final static int ORDERLYTYPE = 3;      //顺序播放
    private int currentPlayMode = CYCLETYPE;


    /**
     * 切换播放模式
     */
    private void switchPlayMode() {
        if (currentPlayMode == CYCLETYPE) {
            setPlayMode(CYCLETYPE);
        } else if (currentPlayMode == SINGLETYPE) {
            setPlayMode(SINGLETYPE);
        } else if (currentPlayMode == RANDOMTYPE) {
            setPlayMode(RANDOMTYPE);
        } else if (currentPlayMode == ORDERLYTYPE) {
            setPlayMode(ORDERLYTYPE);
        }
    }

    /**
     * 设置播放模式
     *
     * @param mode 播放模式
     */
    private void setPlayMode(int mode) {
        if (mode < 0 || mode > 2) {
            throw new IllegalArgumentException("incorrect type");
        }

        createMediaPlayerIfNeeded();

        currentPlayMode = mode;
        if (mode == SINGLETYPE) {
            mMediaPlayer.setLooping(true);
        } else {
            mMediaPlayer.setLooping(false);
        }
    }

    /**
     * 获取播放模式
     */
    public int getPlayMode() {
        return currentPlayMode;
    }


    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        currentMaxDuration = mediaPlayer.getDuration();
        configMediaPlayerState();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG, "onCompletion from MediaPlayer");
        if (!mediaPlayer.isLooping()) {
            // The media player finished playing the current song, so we go ahead and start the next.
            currentProgress = 0;
            playNext();
        }
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
        return false;
    }

    public MusicPlaylist getMusicPlayList() {
        return mMusicPlaylist;
    }

    /**
     * 获取当前歌曲进度
     *
     * @return
     */
    public int getCurrentProgressInSong() {
        return mMediaPlayer != null ? mMediaPlayer.getCurrentPosition() : currentProgress;
    }


    /****
     * 获取当前播放歌曲
     *
     * @return
     */
    public Song getPlayingSong() {
        if (mMusicPlaylist != null)
            return mMusicPlaylist.getCurrentPlay();
        else
            return null;
    }

    /**
     * 获取歌曲长度
     * @return
     */
    public int getCurrentMaxDuration() {
        return currentMaxDuration;
    }

    /**
     * 获取进度
     * @return
     */
    public int getCurrentPosition() {
        if (mMediaPlayer != null)
            return mMediaPlayer.getCurrentPosition();
        return 0;
    }


    /**
     * 获取播放状态
     * @return
     */
    public int getState() {
        if (mService != null){
            return mService.getState();
        }
        return PlaybackStateCompat.STATE_STOPPED;
    }

    /**
     * 释放焦点
     */
    private void giveUpAudioFocus() {
    }

    /***
     * 获取播放列表
     *
     * @return
     */
    public MusicPlaylist getMusicPlaylist() {
        return mMusicPlaylist;
    }

    /***
     * 设置播放列表
     *
     * @param musicPlaylist
     */
    public void setMusicPlaylist(MusicPlaylist musicPlaylist) {
        this.mMusicPlaylist = musicPlaylist;
    }



    /**
     * 释放资源服务用于回放。这包括“前台服务”的地位和可能的媒体播放器。
     *
     * @param releaseMediaPlayer Indicates whether the Media Player should also
     *                           be released or not
     */
    private void relaxResources(boolean releaseMediaPlayer) {
        // stop and release the Media Player, if it's available
        if (releaseMediaPlayer && mMediaPlayer != null) {
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void registerListener(OnSongchangeListener listener){
        changeListeners.add(listener);
    }

    public void unregisterListener(OnSongchangeListener listener){
        changeListeners.remove(listener);
    }


}
