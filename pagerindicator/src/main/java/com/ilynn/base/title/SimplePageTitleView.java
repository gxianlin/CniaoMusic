package com.ilynn.base.title;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.TextView;

import com.ilynn.base.UIUtil;

/**
 * 描述：带文本的指示器标题
 * 作者：gong.xl
 * 创建日期：2017/7/7 上午11:54
 * 修改日期: 2017/7/7
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class SimplePageTitleView extends TextView implements IMeasureablePageTitleView {
    protected int mSelectedColor;
    protected int mNormaColor;

    public SimplePageTitleView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        setGravity(Gravity.CENTER);
        int padding = UIUtil.dip2px(context, 10);
        setPadding(padding, 0, padding, 0);
        setSingleLine();
        setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public int getContentLeft() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 - contentWidth / 2;
    }

    @Override
    public int getContentTop() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 - contentHeight / 2);
    }

    @Override
    public int getContentRight() {
        Rect bound = new Rect();
        getPaint().getTextBounds(getText().toString(), 0, getText().length(), bound);
        int contentWidth = bound.width();
        return getLeft() + getWidth() / 2 + contentWidth / 2;
    }

    @Override
    public int getContentBottom() {
        Paint.FontMetrics metrics = getPaint().getFontMetrics();
        float contentHeight = metrics.bottom - metrics.top;
        return (int) (getHeight() / 2 + contentHeight / 2);
    }

    @Override
    public void onSelected(int position, int totalCount) {
        setTextColor(mSelectedColor);
    }

    @Override
    public void onDeselected(int position, int totalCount) {
        setTextColor(mNormaColor);
    }

    @Override
    public void onLeave(int position, int totalCount, float leavePercent, boolean leftToRight) {

    }

    @Override
    public void onEnter(int position, int totalCount, float enterPercent, boolean leftToRigh) {

    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setSelectedColor(int selectedColor) {
        mSelectedColor = selectedColor;
    }

    public int getNormaColor() {
        return mNormaColor;
    }

    public void setNormaColor(int normaColor) {
        mNormaColor = normaColor;
    }
}
