package com.ilynn.base;

/**
 * 描述：保存指示器标题的坐标
 * <p>
 * 作者：gong.xl
 * 创建日期：2017/7/6 下午5:23
 * 修改日期: 2017/7/6
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class PositionData {

    public int mLeft;
    public int mTop;
    public int mRight;
    public int mBottom;
    public int mContentLeft;
    public int mContentTop;
    public int mContentRight;
    public int mContentBottom;


    public int width() {
        return mRight - mLeft;
    }

    public int height() {
        return mBottom - mTop;
    }


    public int contentWidth() {
        return mContentRight - mContentLeft;
    }

    public int contentHeight() {
        return mContentBottom - mContentTop;
    }

    public int horizontalCenter() {
        return mLeft + width() / 2;
    }

    public int verticalCenter() {
        return mTop + height() / 2;
    }
}
