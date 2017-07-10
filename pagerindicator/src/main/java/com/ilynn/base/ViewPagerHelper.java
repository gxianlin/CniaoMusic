package com.ilynn.base;

import android.support.v4.view.ViewPager;

/**
 * 描述：viewpager辅助类,简化了与viewpager的绑定
 * <p>
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午5:49
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class ViewPagerHelper {
    /**
     * 绑定viewpager与标题
     * @param pageIndicator
     * @param viewPager
     */
    public static void bind(final PageIndicator pageIndicator, ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                pageIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                pageIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                pageIndicator.onPageScrollStateChanged(state);
            }
        });
    }
}
