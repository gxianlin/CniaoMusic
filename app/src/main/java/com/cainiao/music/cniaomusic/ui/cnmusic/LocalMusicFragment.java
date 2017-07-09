package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.music.MusicPlaylist;
import com.cainiao.music.cniaomusic.ui.adapter.LocalMusicListAdapter;
import com.cainiao.music.cniaomusic.ui.base.BaseFragment;
import com.cainiao.music.cniaomusic.ui.model.LocalIview;

import java.util.List;

import butterknife.InjectView;

/**
 * 描述：本地歌曲页面
 * 作者：gong.xl
 * 创建日期：2017/7/8 0008 0:45
 * 修改日期: 2017/7/8 0008
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class LocalMusicFragment extends BaseFragment implements LocalIview.LocalMusic {

    @InjectView(R.id.local_recylerview)
    RecyclerView mRecylerview;

    private LocalLibraryPresenter mLibraryPresenter;
    private LocalMusicListAdapter mMusicAdapter;

    private MusicPlaylist mMusicPlaylist;

  public static LocalMusicFragment newInstance() {
        LocalMusicFragment fragment = new LocalMusicFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLibraryPresenter = new LocalLibraryPresenter(getActivity(), this);
        mMusicPlaylist = new MusicPlaylist();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_local_music;
    }

    @Override
    public void initViews() {
        mMusicAdapter = new LocalMusicListAdapter(getActivity());
        mRecylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecylerview.setAdapter(mMusicAdapter);


    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        mLibraryPresenter.requestMusic();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getLocalMusic(List<Song> musics) {
        mMusicPlaylist.addQueue(musics,true);
        mMusicPlaylist.setTitle(getString(R.string.local_library));
        mMusicAdapter.setData(musics);
    }


}
