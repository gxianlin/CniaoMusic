package com.ilynn.base;

import java.util.List;

/**
 * 描述：TODO
 * 作者：gong.xl
 * 创建日期：2017/7/6 下午5:21
 * 修改日期: 2017/7/6
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public interface IPagerIndicator {

    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);

    void onPositionDataProvide(List<PositionData> dataList);
}
