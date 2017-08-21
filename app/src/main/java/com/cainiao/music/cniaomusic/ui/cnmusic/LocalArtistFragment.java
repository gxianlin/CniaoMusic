package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.data.Artist;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.ui.adapter.LocalMusicListAdapter;
import com.cainiao.music.cniaomusic.ui.base.BaseFragment;
import com.cainiao.music.cniaomusic.ui.model.LocalIview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * 描述：本地音乐 歌手,专辑
 * 作者：gong.xl
 * 创建日期：2017/7/8  10:09
 * 修改日期: 2017/7/8
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class LocalArtistFragment extends BaseFragment implements LocalIview.LocalMusic {

    @InjectView(R.id.local_recylerview)
    RecyclerView mRecylerview;

    private LocalMusicListAdapter mMusicAdapter;

    private LocalLibraryPresenter mLibraryPresenter;


    public static LocalArtistFragment newInstance() {
        LocalArtistFragment fragment = new LocalArtistFragment();
        return fragment;
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_local_music;
    }

    @Override
    public void initViews() {
        mRecylerview.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        mLibraryPresenter = new LocalLibraryPresenter(getActivity(), this);
        mLibraryPresenter.requestMusic();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getLocalMusic(List<Song> musics) {
        List<String> names = new ArrayList<>();
        Map<String, Integer> songCount = new HashMap<>();
        Map<String, Long> artisId = new HashMap<>();
        Map<String,String> covers = new HashMap<>();

        for (int i = 0, j = musics.size(); i < j; i++) {
            String artistName = musics.get(i).getArtistName();
            if (!names.contains(artistName)) {
                names.add(artistName);
                songCount.put(artistName, 1);
                artisId.put(artistName,musics.get(i).getArtistId());
                covers.put(artistName,musics.get(i).getCoverUrl());
            } else {
                Integer integer = songCount.get(artistName);
                songCount.put(artistName, integer + 1);
            }
        }

        List<Artist> list = new ArrayList<>();
        for (int i = 0, j = names.size(); i < j; i++){
            String artistName = names.get(i);
            long artistId = artisId.get(artistName);
            int count = songCount.get(artistName);
            String cover = covers.get(artistName);
            Artist artist = new Artist(artistId,artistName,1,count,cover);
            list.add(artist);
        }
        mMusicAdapter = new LocalMusicListAdapter(getActivity());
        mRecylerview.setAdapter(mMusicAdapter);
    }
}
