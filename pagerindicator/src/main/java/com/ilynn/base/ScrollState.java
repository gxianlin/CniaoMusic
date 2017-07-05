package com.ilynn.base;

/**
 * 描述：自定义滚动状态
 * 作者：gong.xl
 * 创建日期：2017/7/5 上午11:53
 * 修改日期: 2017/7/5
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public interface ScrollState {
    int SCROLL_STATE_IDLE = 0;
    int SCROLL_STATE_DRAGGING = 1;
    int SCROLL_STATE_SETTLING = 2;
}
