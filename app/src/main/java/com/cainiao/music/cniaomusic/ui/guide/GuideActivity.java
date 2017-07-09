package com.cainiao.music.cniaomusic.ui.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;

import com.cainiao.music.cniaomusic.ui.MainActivity;
import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.base.BaseAvtivity;
import com.ilynn.base.PageIndicator;
import com.ilynn.base.ViewPagerHelper;
import com.ilynn.base.syle.CircleNavigator;

import java.util.ArrayList;

import butterknife.InjectView;

/**
 * 功能：视频引导页
 * 作者:gong.xl
 * 邮箱:gxianlin@126.com
 * 创建时间:2017/7/3
 */
public class GuideActivity extends BaseAvtivity {

    @InjectView(R.id.vp)
    ViewPager vp;
    @InjectView(R.id.bt_start)
    Button btStart;
    @InjectView(R.id.page_indicator)
    PageIndicator mPageIndicator;
    private ArrayList<Fragment> fragments;


    /**
     * 设置监听事件
     */
    public void setListener() {
        setOnClick(btStart);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initData() {
        fragments = new ArrayList<>();

        GuideFragment fragment1 = new GuideFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("index", 1);
        fragment1.setArguments(bundle1);
        fragments.add(fragment1);

        GuideFragment fragment2 = new GuideFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("index", 2);
        fragment2.setArguments(bundle2);
        fragments.add(fragment2);

        GuideFragment fragment3 = new GuideFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("index", 3);
        fragment3.setArguments(bundle3);
        fragments.add(fragment3);

        //设置viewpager缓存个数
        vp.setOffscreenPageLimit(2);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        vp.addOnPageChangeListener(new MyPagerListener());

        CircleNavigator elasticCircleNavigator = new CircleNavigator(this);

//        elasticCircleNavigator.setNormaCircleColor(ContextCompat.getColor(this,R.color.theme_color_c2_common_bg));
        elasticCircleNavigator.setCircleColor(ContextCompat.getColor(this,R.color.themeColor));
        elasticCircleNavigator.setCircleCount(fragments.size());

        elasticCircleNavigator.setCircleClickListener(new CircleNavigator.OnCircleClickListener() {
            @Override
            public void onClick(int position) {
                vp.setCurrentItem(position);
            }
        });
        mPageIndicator.setPageNavigator(elasticCircleNavigator);
        ViewPagerHelper.bind(mPageIndicator,vp);
    }

    @Override
    public void onClick(View v) {
        //跳转页面
        startToActivity(MainActivity.class);
        finish();
    }

    /**
     * viewpager页面适配器
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    /**
     * viewpager滑动监听事件
     */
    public class MyPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        /**
         * 切换下标点
         *
         * @param position
         */
        @Override
        public void onPageSelected(int position) {
            btStart.setVisibility(View.GONE);

            if (position == 2) {
                btStart.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}
