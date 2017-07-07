package com.cainiao.music.cniaomusic.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 描述：主页面fragment适配器
 * 作者：gong.xl
 * 创建日期：2017/7/6 0006 23:49
 * 修改日期: 2017/7/6 0006
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private String[] titles;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null) {
            return super.getPageTitle(position);
        } else {
            return titles[position];
        }

    }
}
