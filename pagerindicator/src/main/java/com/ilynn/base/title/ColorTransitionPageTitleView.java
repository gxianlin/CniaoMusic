package com.ilynn.base.title;

import android.content.Context;

import com.ilynn.base.ArgbEvaluatorHolder;

/**
 * 描述：两种颜色过度的指示器标题
 * 作者：gong.xl
 * 创建日期：2017/7/7 下午2:24
 * 修改日期: 2017/7/7
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class ColorTransitionPageTitleView extends SimplePageTitleView {
    public ColorTransitionPageTitleView(Context context) {
        super(context);
    }

    @Override
    public void onLeave(int position, int totalCount, float leavePercent, boolean leftToRight) {
        int color= ArgbEvaluatorHolder.eval(leavePercent,mSelectedColor,mNormaColor);
        setTextColor(color);
    }

    @Override
    public void onEnter(int position, int totalCount, float enterPercent, boolean leftToRigh) {
        int color= ArgbEvaluatorHolder.eval(enterPercent,mNormaColor,mSelectedColor);
        setTextColor(color);
    }

    @Override
    public void onSelected(int position, int totalCount) {

    }

    @Override
    public void onDeselected(int position, int totalCount) {

    }
}
