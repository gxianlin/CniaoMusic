package com.ilynn.base.indicators;

import android.content.Context;
import android.view.View;

import com.ilynn.base.IPagerIndicator;
import com.ilynn.base.PositionData;

import java.util.List;

/**
 * 描述：TODO
 * 作者：gong.xl
 * 创建日期：2017/7/7 下午5:28
 * 修改日期: 2017/7/7
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class WrapPageIndicator extends View implements IPagerIndicator{
    public WrapPageIndicator(Context context) {
        super(context);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {

    }
}
