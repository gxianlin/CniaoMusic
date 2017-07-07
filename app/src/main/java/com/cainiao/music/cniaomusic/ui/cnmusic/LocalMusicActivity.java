package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.adapter.TabFragmentAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

public class LocalMusicActivity extends BaseAvtivity {

    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.tab_layout)
    TabLayout mTabLayout;
    @InjectView(R.id.viewpager)
    ViewPager mViewpager;


    @Override
    public int getLayoutId() {
        return R.layout.activity_local_music;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(LocalMusicFragment.newInstance());
        fragments.add(LocalAlbumFragment.newInstance());
        TabFragmentAdapter fragmentAdapter = new TabFragmentAdapter(getSupportFragmentManager(),fragments);
        fragmentAdapter.setTitles(new String[]{"单曲","专辑"});
        mViewpager.setAdapter(fragmentAdapter);
        mViewpager.setCurrentItem(0);
        mViewpager.setOffscreenPageLimit(2);
        mTabLayout.setupWithViewPager(mViewpager);
    }

    @Override
    public void onClick(View v) {

    }
}
