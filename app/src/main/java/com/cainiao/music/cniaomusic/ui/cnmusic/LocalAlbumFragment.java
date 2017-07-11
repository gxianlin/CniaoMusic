package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.data.Album;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.ui.adapter.LocalMusicListAdapter;
import com.cainiao.music.cniaomusic.ui.base.BaseFragment;
import com.cainiao.music.cniaomusic.ui.model.LocalIview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;


public class LocalAlbumFragment extends BaseFragment implements LocalIview.LocalMusic {

    @InjectView(R.id.local_recylerview)
    RecyclerView mRecylerview;


    private LocalLibraryPresenter mLibraryPresenter;
    private LocalMusicListAdapter mMusicAdapter;

    public static LocalAlbumFragment newInstance(){
        return new LocalAlbumFragment();
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
        mLibraryPresenter = new LocalLibraryPresenter(getActivity(),this);
        mLibraryPresenter.requestMusic();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void getLocalMusic(List<Song> musics) {
        List<String> albumNames = new ArrayList<>();
        Map<String, Integer> songCount = new HashMap<>();
        Map<String, Integer> yesrs = new HashMap<>();
        Map<String, Long> albumIds = new HashMap<>();
        Map<String, Long> artistIds = new HashMap<>();
        Map<String,String> covers = new HashMap<>();
        Map<String,String> artistNames = new HashMap<>();


        for (int i = 0, j = musics.size(); i < j; i++) {
            String albumName = musics.get(i).getAlbumName();
            if (!albumNames.contains(albumName)) {
                albumNames.add(albumName);
                songCount.put(albumName, 1);
                albumIds.put(albumName,musics.get(i).getAlbumId());
                artistIds.put(albumName,musics.get(i).getArtistId());
                yesrs.put(albumName,musics.get(i).getDuration());
                covers.put(albumName,musics.get(i).getCoverUrl());
                artistNames.put(albumName,musics.get(i).getArtistName());
            } else {
                Integer integer = songCount.get(albumName);
                songCount.put(albumName, integer + 1);
            }
        }


        List<Album> list = new ArrayList<>();
        for (int i = 0, j = albumNames.size(); i < j; i++){
            String title = albumNames.get(i);
            long albumId = albumIds.get(title);
            long artistId = artistIds.get(title);
            int count = songCount.get(title);
            int year = yesrs.get(title);
            String artistName = artistNames.get(title);
            String cover = covers.get(title);

            Album album = new Album(albumId,title,artistName,artistId,count,year,cover);
            list.add(album);
        }

        mMusicAdapter = new LocalMusicListAdapter(getActivity());
        mMusicAdapter.setAlbums(list);
        mRecylerview.setAdapter(mMusicAdapter);
    }
}
