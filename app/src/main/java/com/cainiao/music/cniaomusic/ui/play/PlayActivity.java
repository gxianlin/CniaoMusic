package com.cainiao.music.cniaomusic.ui.play;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.common.utils.ImageUtils;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.service.MusicPlayerManager;
import com.cainiao.music.cniaomusic.ui.base.BaseAvtivity;

import java.text.DecimalFormat;
import java.util.concurrent.TimeUnit;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * 描述：TODO
 * 作者：gong.xl
 * 创建日期：2017/8/12 0012 9:27
 * 修改日期: 2017/8/12 0012
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class PlayActivity extends BaseAvtivity {

    @InjectView(R.id.music_duration_played)
    TextView mMusicDurationPlayed;
    @InjectView(R.id.play_seek)
    SeekBar mPlaySeek;
    @InjectView(R.id.music_duration)
    TextView mMusicDuration;
    @InjectView(R.id.playing_pre)
    ImageView mPlayingPre;
    @InjectView(R.id.playing_play)
    ImageView mPlayingPlay;
    @InjectView(R.id.playing_next)
    ImageView mPlayingNext;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.coverImage)
    ImageView mCoverImage;
    @InjectView(R.id.needle)
    ImageView mNeedle;

    private int index;


    @Override
    protected void receiveData() {
        index = getIntent().getIntExtra("index", 0);
        Log.e("tag", "index=" + index);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_play;
    }

    @Override
    public void initViews() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    @Override
    public void initData() {
        if (MusicPlayerManager.getInstance().getPlayingSong() == null) {
            finish();
        }

        mPlayingPlay.setImageResource(R.drawable.play_rdi_btn_pause);

        updateProgress();

        updateData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mNeedle.animate().rotation(0f).setDuration(300);
            }
        },500);
    }


    @Override
    public void setListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mPlaySeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //seekBar有两种进度改变的方式:1.人为拖动;2.代码改变
                if (fromUser) {
//                    MusicPlayerManager.getInstance().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    @OnClick({R.id.playing_pre, R.id.playing_play, R.id.playing_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playing_pre:
                MusicPlayerManager.getInstance().playPre();
                updateData();
                break;
            case R.id.playing_play:
                if (MusicPlayerManager.getInstance().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    MusicPlayerManager.getInstance().puase();
                    mPlayingPlay.setImageResource(R.drawable.play_rdi_btn_play);
                    mNeedle.animate().rotation(-30f).setDuration(300);
                } else if (MusicPlayerManager.getInstance().getState() == PlaybackStateCompat.STATE_PAUSED) {
                    MusicPlayerManager.getInstance().play();
                    mPlayingPlay.setImageResource(R.drawable.play_rdi_btn_pause);
                    mNeedle.animate().rotation(0f).setDuration(300);
                }

                break;
            case R.id.playing_next:
                MusicPlayerManager.getInstance().playNext();
                updateData();
                break;
        }
    }


    /**
     * 更新进度等信息
     */
    private void updateProgress() {
        //更新进度条
        Observable.interval(1000, TimeUnit.MICROSECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        mPlaySeek.setMax(MusicPlayerManager.getInstance().getCurrentMaxDuration());
                        mPlaySeek.setProgress(MusicPlayerManager.getInstance().getCurrentPosition());
                        mMusicDuration.setText(formatChange(MusicPlayerManager.getInstance().getCurrentMaxDuration()));
                        mMusicDurationPlayed.setText(formatChange(MusicPlayerManager.getInstance().getCurrentPosition
                                ()));

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                });
    }

    /**
     * 更新数据:封面,标题,图标,歌词等
     */
    private void updateData() {
        //获取当前播放的歌曲
        Song playingSong = MusicPlayerManager.getInstance().getPlayingSong();
        //歌曲封面
        String coverUrl = playingSong.getCoverUrl();
        ImageUtils.GlideWith(this, coverUrl, R.drawable.ah1, mCoverImage);

        //歌曲名称
        mToolbar.setTitle(playingSong.getTitle());
    }


    /**
     * 对歌曲长度的格式进行转换
     */
    public String formatChange(int millSeconds) {
        int seconds = millSeconds / 1000;
        int second = seconds % 60;
        int munite = (seconds - second) / 60;
        DecimalFormat format = new DecimalFormat("00");
        return format.format(munite) + ":" + format.format(second);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, PlayActivity.class);
        context.startActivity(intent);
    }

}