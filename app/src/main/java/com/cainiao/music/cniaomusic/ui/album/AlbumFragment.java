package com.cainiao.music.cniaomusic.ui.album;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.base.BaseFragment;

import java.util.List;

import butterknife.InjectView;
import magicasakura.widgets.TintToolbar;

/**
 * 描述：唱片
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午9:20
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gxianlin@126.com
 */
public class AlbumFragment extends BaseFragment {


    @InjectView(R.id.view_pager)
    ViewPager mViewPager;
    @InjectView(R.id.tab_layout)
    TintToolbar mTabLayout;


    private List<Fragment> mFragmentList;
    private List<String> titles;

    public AlbumFragment() {
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_album;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {


    }

    @Override
    public void onClick(View v) {
    }
}
