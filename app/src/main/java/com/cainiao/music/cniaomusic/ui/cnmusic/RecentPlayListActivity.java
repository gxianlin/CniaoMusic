package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.music.MusicPlaylist;
import com.cainiao.music.cniaomusic.music.MusicRecentPlayList;
import com.cainiao.music.cniaomusic.service.MusicPlayerManager;
import com.cainiao.music.cniaomusic.service.OnSongchangeListener;
import com.cainiao.music.cniaomusic.ui.adapter.OnItemClickListener;
import com.cainiao.music.cniaomusic.ui.adapter.RecentPlayAdapter;
import com.cainiao.music.cniaomusic.ui.base.BaseAvtivity;
import com.lb.materialdesigndialog.base.DialogBase;
import com.lb.materialdesigndialog.base.DialogWithTitle;
import com.lb.materialdesigndialog.impl.MaterialDialogNormal;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import magicasakura.widgets.TintToolbar;

/**
 * 描述：最近播放列表页面
 * 作者：gong.xl
 * 创建日期：2017/8/19 0019 9:33
 * 修改日期: 2017/8/19 0019
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class RecentPlayListActivity extends BaseAvtivity implements OnSongchangeListener {

    @InjectView(R.id.btnRight)
    Button mBtnRight;
    @InjectView(R.id.toolbar)
    TintToolbar mToolbar;
    @InjectView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ActionBar mActionBar;
    private RecentPlayAdapter mPlayAdapter;
    private MusicPlaylist musicPlaylist;

    public static void open(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, RecentPlayListActivity.class);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_recent_play_list;
    }

    @Override
    public void initViews() {
        initToolbar();
        mBtnRight.setText("清空");

        initRecyclerView();
    }

    private void initRecyclerView() {
        mBtnRight.setText("清空");

        mPlayAdapter = new RecentPlayAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mPlayAdapter);
        mPlayAdapter.setData(MusicRecentPlayList.getInstance().getQueue());
        musicPlaylist = new MusicPlaylist(MusicRecentPlayList.getInstance().getQueue());
        mPlayAdapter.setSongClickListener(new OnItemClickListener<Song>() {
            @Override
            public void onItemClick(Song song, int position) {
                MusicPlayerManager.getInstance().playQueue(musicPlaylist, position);
                gotoSongPlayerActivity();
            }

            @Override
            public void onItemSettingClick(View v, Song song, int position) {
                showPopupMenu(v, song, position);
            }
        });
    }

    private void showPopupMenu(final View v, final Song song, final int position) {

        final PopupMenu menu = new PopupMenu(this, v);
        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.popup_song_play:
                        MusicPlayerManager.getInstance().playQueue(musicPlaylist, position);
                        gotoSongPlayerActivity();
                        break;
                    case R.id.popup_song_addto_playlist:
                        MusicPlaylist mp = MusicPlayerManager.getInstance().getMusicPlaylist();
                        if (mp == null) {
                            mp = new MusicPlaylist();
                            MusicPlayerManager.getInstance().setMusicPlaylist(mp);
                        }
                        mp.addSong(song);
                        break;
                    case R.id.popup_song_fav:
                        showCollectionDialog(song);
                        break;
                    case R.id.popup_song_download:
                        break;
                }
                return false;
            }
        });
        menu.inflate(R.menu.popup_recently_playlist_setting);
        menu.show();
    }

    /**
     * 显示选择收藏夹列表的弹窗
     *
     * @param song
     */
    public void showCollectionDialog(final Song song) {

    }


    private void initToolbar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.actionbar_back);
        mActionBar.setTitle("最近播放");
    }

    @Override
    public void initData() {
    }

    @Override
    public void setListener() {
        MusicPlayerManager.getInstance().registerListener(this);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btnRight)
    public void onClick() {
        MaterialDialogNormal dialog = new MaterialDialogNormal(this);
        dialog.setTitle("");
        dialog.setMessage("清空全部最近播放歌曲?");
        dialog.setNegativeButton("取消", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("确定", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                MusicRecentPlayList.getInstance().clearRecentPlayList();
                mPlayAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

    }

    @Override
    public void onSongChanged(Song song) {
        mPlayAdapter.setData(MusicRecentPlayList.getInstance().getQueue());
        musicPlaylist = new MusicPlaylist(MusicRecentPlayList.getInstance().getQueue());
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }
}
