package com.cainiao.music.cniaomusic.ui.album;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.adapter.ViewPagerAdapter;
import com.cainiao.music.cniaomusic.ui.base.BaseFragment;
import com.cainiao.music.cniaomusic.ui.radio.RadioFragment;
import com.ilynn.base.CommonNavigatorAdapter;
import com.ilynn.base.IPagerIndicator;
import com.ilynn.base.PageIndicator;
import com.ilynn.base.ViewPagerHelper;
import com.ilynn.base.indicators.BezierPageIndicator;
import com.ilynn.base.syle.CommonNavigator;
import com.ilynn.base.title.IPageTitleView;
import com.ilynn.base.title.ScaleTranstionPageTitleView;
import com.ilynn.base.title.SimplePageTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 描述：唱片
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午9:20
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gxianlin@126.com
 */
public class AlbumFragment extends BaseFragment {


    @InjectView(R.id.page_indicator)
    PageIndicator mPageIndicator;
    @InjectView(R.id.view_pager)
    ViewPager mViewPager;


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
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new RadioFragment());
        mFragmentList.add(new RadioFragment());
        mFragmentList.add(new RadioFragment());
        mFragmentList.add(new RadioFragment());

        titles = new ArrayList<>();
        titles.add("单曲");
        titles.add("歌手");
        titles.add("专辑");
        titles.add("文件夹");
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.setFragments(mFragmentList);
        mViewPager.setAdapter(adapter);

        mPageIndicator.setBackgroundColor(Color.parseColor("#fafafa"));
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles == null ? 0 : titles.size();
            }

            @Override
            public IPagerIndicator getIndicator(Context compat) {
                BezierPageIndicator indicator = new BezierPageIndicator(compat);
                indicator.setColors(Color.parseColor("#ff4a42"), Color.parseColor("#fcde64"), Color.parseColor
                        ("#73e8f4"), Color.parseColor("#76e8f4"));
                return indicator;
            }

            @Override
            public IPageTitleView getTitleView(Context context, final int position) {
                SimplePageTitleView simplePageTitleView = new ScaleTranstionPageTitleView(context);
                simplePageTitleView.setText(titles.get(position));
                simplePageTitleView.setTextSize(18);
                simplePageTitleView.setNormaColor(Color.parseColor("#9e9e9e"));
                simplePageTitleView.setSelectedColor(Color.parseColor("#00c853"));
                simplePageTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mViewPager.setCurrentItem(position);
                    }
                });

                return simplePageTitleView;
            }
        });
        mPageIndicator.setPageNavigator(commonNavigator);
        ViewPagerHelper.bind(mPageIndicator,mViewPager);

    }

    @Override
    public void onClick(View v) {
    }

}
