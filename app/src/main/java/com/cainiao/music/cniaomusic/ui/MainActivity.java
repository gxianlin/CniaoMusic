package com.cainiao.music.cniaomusic.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.album.AlbumFragment;
import com.cainiao.music.cniaomusic.ui.cnmusic.LocalMusicActivity;
import com.cainiao.music.cniaomusic.ui.cnmusic.SearchActivity;
import com.cainiao.music.cniaomusic.ui.friends.FriendsFragment;
import com.cainiao.music.cniaomusic.ui.radio.RadioFragment;
import com.cainiao.music.cniaomusic.ui.widget.CircleImageView;
import com.lapism.searchview.SearchView;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import magicasakura.widgets.TintToolbar;

/**
 * 功能：主页面
 * 作者:gong.xl
 * 邮箱:gxianlin@126.com
 * 创建时间:2017/7/3
 */
public class MainActivity extends SearchActivity implements NavigationView.OnNavigationItemSelectedListener {

    CircleImageView avatar;
    TextView nicknameTv;
    TextView aboutTv;
    @InjectView(R.id.bar_menu)
    ImageView mBarMenu;
    @InjectView(R.id.bar_net)
    ImageView mBarNet;
    @InjectView(R.id.bar_music)
    ImageView mBarMusic;
    @InjectView(R.id.bar_friends)
    ImageView mBarFriends;
    @InjectView(R.id.bar_search)
    ImageView mBarSearch;
    @InjectView(R.id.toolbar)
    TintToolbar mToolbar;
    @InjectView(R.id.main_viewpager)
    ViewPager mMainViewpager;
    @InjectView(R.id.searchView)
    SearchView mSearchView;
    @InjectView(R.id.navigation)
    NavigationView mNavigation;
    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    private ActionBar mActionBar;
    private ArrayList<ImageView> tabs = new ArrayList<>();

    private long time;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        showQuickControl(true);
        initToolbar();
        initDrawer();
    }

    @Override
    public void setListener() {
        mMainViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //切换tab标签
                changeTabs(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 设置tab标签状态
     *
     * @param position
     */
    private void changeTabs(int position) {
        for (int i = 0; i < tabs.size(); i++) {
            if (position == i) {
                tabs.get(i).setSelected(true);
            } else {
                tabs.get(i).setSelected(false);
            }
        }
    }

    @Override
    public void initData() {
        setCustomViewPager();
    }

    /***
     * 初始化toolbar
     */
    private void initToolbar() {
        mToolbar.setNavigationIcon(R.drawable.ic_drawer_home);
        mToolbar.setContentInsetsAbsolute(0, 0);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    /**
     * 设置自定义viewpager
     */
    private void setCustomViewPager() {
        //添加tab标签
        tabs.add(mBarNet);
        tabs.add(mBarMusic);
        tabs.add(mBarFriends);

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        AlbumFragment albumFragment = new AlbumFragment();
        RadioFragment radioFragment = new RadioFragment();
        FriendsFragment friendsFragment = new FriendsFragment();

        myPagerAdapter.addFragment(albumFragment);
        myPagerAdapter.addFragment(radioFragment);
        myPagerAdapter.addFragment(friendsFragment);

        mMainViewpager.setAdapter(myPagerAdapter);
        mMainViewpager.setCurrentItem(1);
    }

    /***
     * 初始化侧滑菜单
     */
    private void initDrawer() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout
                , R.string.open_string, R.string.close_string) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (searchView != null && searchView.isSearchOpen()) {
                    searchView.close(true);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };

        //        toggle.syncState();
        //        mDrawerLayout.addDrawerListener(toggle);
        mNavigation.setNavigationItemSelectedListener(this);

        avatar = (CircleImageView) mNavigation.getHeaderView(0).findViewById(R.id.drawer_avatar);
        nicknameTv = (TextView) mNavigation.getHeaderView(0).findViewById(R.id.drawer_nickname);
        aboutTv = (TextView) mNavigation.getHeaderView(0).findViewById(R.id.drawer_about);
        avatar.setOnClickListener(this);
    }

    /**
     * 双击返回桌面
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - time > 1000) {
                Toast.makeText(this, "再按一次返回桌面", Toast.LENGTH_SHORT).show();
                time = System.currentTimeMillis();
            } else {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        initSearch();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_beats, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            searchView.open(true);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initSearch() {
        setSearchView();
        customSearchView(true);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_library) {
            //跳转到本地歌曲的界面
            startToActivity(LocalMusicActivity.class);
        }

        //关闭侧滑栏
        mDrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View v) {

    }


    @OnClick({R.id.bar_menu, R.id.bar_net, R.id.bar_music, R.id.bar_friends, R.id.bar_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.bar_menu:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.bar_net:
                mMainViewpager.setCurrentItem(0);
                break;
            case R.id.bar_music:
                mMainViewpager.setCurrentItem(1);
                break;
            case R.id.bar_friends:
                mMainViewpager.setCurrentItem(1);
                break;
            case R.id.bar_search:
                break;
        }
    }


    class MyPagerAdapter extends FragmentPagerAdapter {
        ArrayList<Fragment> mFragments = new ArrayList<>();

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mFragments.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }

}
