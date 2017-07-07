package com.ilynn.base.title;

import android.content.Context;

/**
 * 描述：带颜色渐变和标题缩放的指示器标题
 * 作者：gong.xl
 * 创建日期：2017/7/7 下午2:31
 * 修改日期: 2017/7/7
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class ScaleTranstionPageTitleView extends ColorTransitionPageTitleView {
    private float mMinScal = 0.75f;

    public ScaleTranstionPageTitleView(Context context) {
        super(context);
    }

    @Override
    public void onEnter(int position, int totalCount, float enterPercent, boolean leftToRigh) {
        super.onEnter(position, totalCount, enterPercent, leftToRigh);
        setScaleX(mMinScal + (1.0f - mMinScal) * enterPercent);
        setScaleY(mMinScal + (1.0f - mMinScal) * enterPercent);
    }

    @Override
    public void onLeave(int position, int totalCount, float leavePercent, boolean leftToRight) {
        super.onLeave(position, totalCount, leavePercent, leftToRight);
        setScaleX(1.0f + (mMinScal - 1.0f) * leavePercent);
        setScaleY(1.0f + (mMinScal - 1.0f) * leavePercent);
    }

    public float getMinScal() {
        return mMinScal;
    }

    public void setMinScal(float minScal) {
        mMinScal = minScal;
    }
}
