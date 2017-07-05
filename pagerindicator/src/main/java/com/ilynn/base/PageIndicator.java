package com.ilynn.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ilynn.base.abs.IPageNavigator;

/**
 * 描述：ViewPager指示器控件
 *
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午5:29
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class PageIndicator extends FrameLayout {
    private IPageNavigator mPageNavigator;

    public PageIndicator(Context context) {
        super(context);
    }

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void onPageScrolled(int position,float positionOffset,int positionOffsetPixels){
        if (mPageNavigator != null){
            mPageNavigator.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    public void onPageSelected(int position){
        if (mPageNavigator != null){
            mPageNavigator.onPageSelected(position);
        }
    }

    public void onPageScrollStateChanged(int state){
        if (mPageNavigator != null){
            mPageNavigator.onPageScrollStateChanged(state);
        }
    }

    public IPageNavigator getPageNavigator(){
        return mPageNavigator;
    }

    public void setPageNavigator(IPageNavigator pageNavigator){
        if (mPageNavigator == pageNavigator){
            return;
        }

        if (mPageNavigator != null){
            mPageNavigator.onDetachFromIndicator();
        }

        mPageNavigator = pageNavigator;
        removeAllViews();
        if (mPageNavigator instanceof View){
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            addView((View) mPageNavigator,lp);
            mPageNavigator.onAttachToIndicator();
        }
    }
}
