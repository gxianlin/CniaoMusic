package com.cainiao.music.cniaomusic.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.music.MusicRecentPlayList;

/**
 * 描述：音乐播放服务
 * 1.开启服务
 * 2.MediaSession框架
 * 3.监听的接口--歌曲的切换
 * 4.音乐播放的列表管理
 * <p>
 * <p>
 * 作者：gong.xl
 * 创建日期：2017/8/12 0012 9:27
 * 修改日期: 2017/8/12 0012
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class MusicService extends Service implements OnSongchangeListener{

    public final Binder mBinder = new MyBinder();

    private MusicPlayerManager mPlayerManager;
    private MediaSessionCompat mMediaSession;
    private PlaybackStateCompat mState;

    @Override
    public void onSongChanged(Song song) {
        //添加播放过的歌曲
        MusicRecentPlayList.getInstance().addPlaySong(song);
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }

    public class MyBinder extends Binder {
        public MusicService getMusicService() {
            return MusicService.this;
        }
    }

    //create,new
    public static MediaPlayer sMediaPlayer = new MediaPlayer();

    /**
     * 初始化
     */
    public void setUp() {
        //
        mPlayerManager = MusicPlayerManager.from(this);
        setUpMediaSession();
    }

    /**
     * 使用MediaButtonReceiver来兼容api21之前的版本
     */
    private void setUpMediaSession() {
        ComponentName componentName = new ComponentName(getPackageName(), MediaButtonReceiver.class.getName());
        mMediaSession = new MediaSessionCompat(this, "fd", componentName, null);
        //设置处理media button的flag
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        //设置回调
        mMediaSession.setCallback(new MediaSessionCallBack());

        MusicPlayerManager.getInstance().registerListener(this);

        setState(PlaybackStateCompat.STATE_NONE);
    }

    /**
     * 设置播放状态
     */
    public void setState(int state) {
        mState = new PlaybackStateCompat.Builder()
                .setActions(PlaybackStateCompat.ACTION_PLAY |
                        PlaybackStateCompat.ACTION_PLAY_PAUSE |
                        PlaybackStateCompat.ACTION_PAUSE |
                        PlaybackStateCompat.ACTION_SKIP_TO_NEXT |
                        PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                        PlaybackStateCompat.ACTION_STOP |
                        PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID |
                        PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH |
                        PlaybackStateCompat.ACTION_SEEK_TO |
                        PlaybackStateCompat.ACTION_SKIP_TO_QUEUE_ITEM
                )
                .setState(state, PlaybackStateCompat.PLAYBACK_POSITION_UNKNOWN,
                        1.0f, SystemClock.elapsedRealtime())
                .build();

        mMediaSession.setPlaybackState(mState);
        mMediaSession.setActive(state != PlaybackStateCompat.STATE_NONE &&
                state != PlaybackStateCompat.STATE_STOPPED);
    }

    public int getState(){
       return  mState.getState();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MediaButtonReceiver.handleIntent(mMediaSession, intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMediaSession.release();
    }

    public void stopService(){
        stopSelf();
        MusicPlayerManager.getInstance().unregisterListener(this);
    }

    //自定义回调
    public class MediaSessionCallBack extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
        }

        @Override
        public void onPause() {
            super.onPause();
        }

        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
        }

        @Override
        public void onStop() {
            super.onStop();
        }

        @Override
        public void onSeekTo(long pos) {
            super.onSeekTo(pos);
        }
    }
}
