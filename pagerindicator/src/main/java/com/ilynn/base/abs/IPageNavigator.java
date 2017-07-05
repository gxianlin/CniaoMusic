package com.ilynn.base.abs;

/**
 * 描述：抽象ViewPager指示器
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午5:17
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public interface IPageNavigator {

    void onPageScrolled(int position,float positionOffset,int positionOffsetPixels);

    void onPageSelected(int position);

    void onPageScrollStateChanged(int state);


    /**
     * 当IPageNavigator被添加到Indicatord上移除是调用
     */
    void onAttachToIndicator();

    /**
     * 当IPageNavigator从Indicatord上移除是调用
     */
    void onDetachFromIndicator();

    /**
     * ViewPager内容改变时需要先调用此方法,自定义的IPageNavigator都遵守此约定
     */
    void notifyDataSetChanged();

}
