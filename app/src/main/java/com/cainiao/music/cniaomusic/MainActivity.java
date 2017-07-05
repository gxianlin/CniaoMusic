package com.cainiao.music.cniaomusic;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cainiao.music.cniaomusic.ui.album.AlbumFragment;
import com.cainiao.music.cniaomusic.ui.cnmusic.BaseAvtivity;
import com.cainiao.music.cniaomusic.ui.radio.RadioFragment;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import magicasakura.widgets.TintToolbar;

/**
 * 功能：主页面
 * 作者:gong.xl
 * 邮箱:gxianlin@126.com
 * 创建时间:2017/7/3
 */
public class MainActivity extends BaseAvtivity {

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
    ViewPager mCustomViewpager;
    @InjectView(R.id.bottom_container)
    FrameLayout mBottomContainer;
    @InjectView(R.id.a)
    RelativeLayout mA;
    @InjectView(R.id.id_lv_left_menu)
    ListView mIdLvLeftMenu;
    @InjectView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.bar_menu)
    ImageView mBarMenu;

    private ActionBar mActionBar;
    private ArrayList<ImageView> tabs = new ArrayList<>();

    private long time;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        ButterKnife.inject(this);
        setToolBar();
        setDrawerLayout();
    }

    @Override
    public void setListener() {
        mCustomViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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


    private void setToolBar() {
        setSupportActionBar(mToolbar);
        mActionBar = getSupportActionBar();
        mActionBar.setDisplayUseLogoEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
    }

    /**
     * 设置自定义viewpager
     */
    private void setCustomViewPager() {
        //添加tab标签
        tabs.add(mBarNet);
        tabs.add(mBarMusic);


        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        AlbumFragment albumFragment = new AlbumFragment();
        RadioFragment radioFragment = new RadioFragment();

        myPagerAdapter.addFragment(albumFragment);
        myPagerAdapter.addFragment(radioFragment);

        mCustomViewpager.setAdapter(myPagerAdapter);
        mCustomViewpager.setCurrentItem(1);
    }

    @OnClick({R.id.bar_menu,R.id.bar_net, R.id.bar_music, R.id.bar_friends, R.id.bar_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bar_menu:
                mDrawerLayout.openDrawer(Gravity.START);
                break;
            case R.id.bar_net:
                mCustomViewpager.setCurrentItem(0);
                break;
            case R.id.bar_music:
                mCustomViewpager.setCurrentItem(1);
                break;
            case R.id.bar_friends:
                mCustomViewpager.setCurrentItem(2);
                break;
            case R.id.bar_search:
                //跳转搜索页面
                break;
        }
    }

    /**
     * 初始化侧滑菜单
     */
    private void setDrawerLayout() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View drawerView = inflater.inflate(R.layout.nav_header_main, null);
        mIdLvLeftMenu.addHeaderView(drawerView);
        mIdLvLeftMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        break;
                    case 2:
                        break;
                    default:
                        break;
                }
            }
        });
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
