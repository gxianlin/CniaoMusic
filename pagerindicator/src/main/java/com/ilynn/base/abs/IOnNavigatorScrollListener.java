package com.ilynn.base.abs;

/**
 * 描述：指示器滑动监听接口
 *
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午5:42
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public interface IOnNavigatorScrollListener {
    /**
     *
     * @param index
     * @param totalCount
     * @param enterPercent
     * @param leftToRight
     */
    void onEnter(int index,int totalCount,float enterPercent,boolean leftToRight);

    /**
     *
     * @param index
     * @param totalCount
     * @param leavePercent
     * @param leftToRight
     */
    void onLeave(int index,int totalCount,float leavePercent,boolean leftToRight);

    /**
     *
     * @param index
     * @param totalCount
     */
    void onSelected(int index,int totalCount);


    /**
     *
     * @param index
     * @param totalCount
     */
    void onDeselected(int index,int totalCount);
}
