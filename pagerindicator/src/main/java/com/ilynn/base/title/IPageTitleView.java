package com.ilynn.base.title;

/**
 * 描述：指示器标题
 * 作者：gong.xl
 * 创建日期：2017/7/6 下午5:34
 * 修改日期: 2017/7/6
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public interface IPageTitleView {
    /**
     * 选中
     * @param position
     * @param totalCount
     */
    void onSelected(int position,int totalCount);

    /**
     * 未选中
     * @param position
     * @param totalCount
     */
    void onDeselected(int position,int totalCount);

    /**
     * 离开
     * @param position
     * @param totalCount
     * @param leavePercent
     * @param leftToRight
     */
    void onLeave(int position,int totalCount,float leavePercent,boolean leftToRight);

    /**
     * 进入
     * @param position
     * @param totalCount
     * @param enterPercent
     * @param leftToRigh
     */
    void onEnter(int position,int totalCount,float enterPercent,boolean leftToRigh);
}
