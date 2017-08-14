package com.cainiao.music.cniaomusic.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 描述：TODO
 * 作者：gong.xl
 * 创建日期：2017/7/7 下午5:45
 * 修改日期: 2017/7/7
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragments;
    private String[] titles;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (titles == null) {
            return super.getPageTitle(position);
        }else {
            return titles[position];
        }
    }

    public void setFragments(List<Fragment> fragments) {
        mFragments = fragments;
    }

    public void setTitles(String[] titles) {
        this.titles = titles;
    }
}
