package com.ilynn.base.title;

/**
 * 描述：可测量内容区域的指示器标题
 * 作者：gong.xl
 * 创建日期：2017/7/7 上午11:14
 * 修改日期: 2017/7/7
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public interface IMeasureablePageTitleView extends IPageTitleView {
    int getContentLeft();
    int getContentTop();
    int getContentRight();
    int getContentBottom();
}
