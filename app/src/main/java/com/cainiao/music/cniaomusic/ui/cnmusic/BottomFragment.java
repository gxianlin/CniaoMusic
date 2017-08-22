package com.cainiao.music.cniaomusic.ui.cnmusic;


import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.common.utils.ImageUtils;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.service.MusicPlayerManager;
import com.cainiao.music.cniaomusic.service.OnSongchangeListener;
import com.cainiao.music.cniaomusic.ui.base.BaseFragment;
import com.cainiao.music.cniaomusic.ui.play.PlayActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.InjectView;
import butterknife.OnClick;
import magicasakura.widgets.TintImageView;
import rx.Subscription;

/**
 * A simple {@link Fragment} subclass.
 * 底部状态栏的片段
 */
public class BottomFragment extends BaseFragment implements OnSongchangeListener {
    @InjectView(R.id.playbar_img)
    SimpleDraweeView mPlaybarImg;
    @InjectView(R.id.playbar_info)
    TextView mTitle;
    @InjectView(R.id.playbar_singer)
    TextView mArtist;
    @InjectView(R.id.play_list)
    TintImageView mPlayList;
    @InjectView(R.id.control)
    TintImageView mPlayPause;
    @InjectView(R.id.play_next)
    TintImageView mPlayNext;
    @InjectView(R.id.linear)
    LinearLayout mLinear;
    @InjectView(R.id.song_progress_normal)
    SeekBar mSongProgressNormal;
    @InjectView(R.id.nav_play)
    LinearLayout mNavPlay;

    private boolean duetoplaypause = false;
    private Song song;
    private Subscription progressSub, timerSub;
    private boolean isPaused;

    public static BottomFragment newInstance() {
        return new BottomFragment();
    }

    @Override
    public int getLayoutId() {
        //音乐信息的更新的监听注册
        MusicPlayerManager.getInstance().registerListener(this);

        return R.layout.bottom_nav;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {

    }


    @Override
    public void onClick(View view) {

    }

    @OnClick({R.id.play_list, R.id.control, R.id.play_next,R.id.nav_play})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play_list:
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PlayQueueFragment queueFragment = new PlayQueueFragment();
                        queueFragment.show(getFragmentManager(), "playqueuefragment");
                    }
                }, 60);

                break;
            case R.id.control:
                if (MusicPlayerManager.getInstance().getState() == PlaybackStateCompat.STATE_PLAYING) {
                    MusicPlayerManager.getInstance().puase();
                    mPlayPause.setImageResource(R.drawable.play_rdi_btn_play);
                } else if (MusicPlayerManager.getInstance().getState() == PlaybackStateCompat.STATE_PAUSED) {
                    MusicPlayerManager.getInstance().play();
                    mPlayPause.setImageResource(R.drawable.play_rdi_btn_pause);
                }
                break;
            case R.id.play_next:
                MusicPlayerManager.getInstance().playNext();
                break;
            case R.id.nav_play:
                Intent intent = new Intent(getActivity(), PlayActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
                break;
        }
    }

    /***
     * 更新数据:封面,标题,图标
     */
    private void updateData() {
        //歌曲的封面
        String coverUrl = song.getCoverUrl();
        ImageUtils.GlideWith(mContext, coverUrl, R.drawable.ah1, mPlaybarImg);

        //设置标题
        if (!TextUtils.isEmpty(song.getAlbumName())) {
            String title = song.getAlbumName();
            mTitle.setText(title);
        }
        //设置歌手
        if (!TextUtils.isEmpty(song.getArtistName())) {
            String title = song.getAlbumName();
            mArtist.setText(title);
        }

        if (MusicPlayerManager.getInstance().getPlayingSong() != null) {
            mPlayPause.setImageResource(R.drawable.playbar_btn_pause);
        }

    }


    @Override
    public void onSongChanged(Song song) {
        this.song = song;
        updateData();
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {
        if (MusicPlayerManager.getInstance().getState() == PlaybackStateCompat.STATE_PLAYING) {

            mPlayPause.setImageResource(R.drawable.playbar_btn_pause);
        } else if (MusicPlayerManager.getInstance().getState() == PlaybackStateCompat.STATE_PAUSED) {
            mPlayPause.setImageResource(R.drawable.playbar_btn_play);

        }
    }


    @Override
    public void onResume() {
        super.onResume();
        isPaused = false;
        //        updateData();
    }

    @Override
    public void onPause() {
        isPaused = true;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MusicPlayerManager.getInstance().unregisterListener(this);
        //        progressSub.unsubscribe();
    }

}
