package com.ilynn.base.indicators;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.ilynn.base.ArgbEvaluatorHolder;
import com.ilynn.base.FragmentContainerHelper;
import com.ilynn.base.IPagerIndicator;
import com.ilynn.base.PositionData;
import com.ilynn.base.UIUtil;

import java.util.Arrays;
import java.util.List;

/**
 * 描述：viewpager指示器,带颜色渐变的贝塞尔曲线
 * 作者：gong.xl
 * 创建日期：2017/7/7 下午2:53
 * 修改日期: 2017/7/7
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class BezierPageIndicator extends View implements IPagerIndicator {
    private List<PositionData> mPositionDatas;

    private float mLeftCircleRadius;
    private float mLeftCircleX;
    private float mRightCircleRadius;
    private float mRightCircleX;


    private float mYOffset;
    private float mMaxCircleRadius;
    private float mMinCircleRadius;

    private Paint mPaint;
    private Path mPath = new Path();

    private List<Integer> mClors;
    private Interpolator mStartInterpolator = new AccelerateInterpolator();
    private Interpolator mEndInterpolator = new DecelerateInterpolator();


    public BezierPageIndicator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mMaxCircleRadius = UIUtil.dip2px(context, 3.5);
        mMinCircleRadius = UIUtil.dip2px(context, 2);
        mYOffset = UIUtil.dip2px(context, 1.5);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mLeftCircleX, getHeight() - mYOffset - mMaxCircleRadius, mLeftCircleRadius, mPaint);
        canvas.drawCircle(mRightCircleX, getHeight() - mYOffset - mMaxCircleRadius, mRightCircleRadius, mPaint);
        drawBezierCurve(canvas);
    }

    /**
     * 绘制贝塞尔曲线
     *
     * @param canvas
     */
    private void drawBezierCurve(Canvas canvas) {
        mPath.reset();
        float y = getHeight() - mYOffset - mMaxCircleRadius;
        mPath.moveTo(mRightCircleX, y);
        mPath.lineTo(mRightCircleX, y - mRightCircleRadius);
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, y, mLeftCircleX, y - mLeftCircleRadius);
        mPath.lineTo(mLeftCircleX, y + mLeftCircleRadius);
        mPath.quadTo(mRightCircleX + (mLeftCircleX - mRightCircleX) / 2.0f, y, mRightCircleX, y + mRightCircleRadius);
        mPath.close();
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mPositionDatas == null || mPositionDatas.isEmpty()) {
            return;
        }

        //获取绘制的颜色
        if (mClors != null && mClors.size() > 0) {
            int currentColor = mClors.get(Math.abs(position) % mClors.size());
            int nextColor = mClors.get(Math.abs(position + 1) % mClors.size());
            int color = ArgbEvaluatorHolder.eval(positionOffset, currentColor, nextColor);
            mPaint.setColor(color);
        }

        //计算点的位置
        PositionData current = FragmentContainerHelper.getImitativePositionData(mPositionDatas, position);
        PositionData next = FragmentContainerHelper.getImitativePositionData(mPositionDatas, position + 1);

        float leftX = current.mLeft + (current.mRight - current.mLeft) / 2;
        float rightX = next.mRight + (next.mRight - next.mLeft) / 2;

        mLeftCircleX = leftX + (rightX - leftX) * mStartInterpolator.getInterpolation(positionOffset);
        mRightCircleX = leftX + (rightX - leftX) * mEndInterpolator.getInterpolation(positionOffset);
        mLeftCircleRadius = mMaxCircleRadius + (mMinCircleRadius - mMaxCircleRadius) * mStartInterpolator
                .getInterpolation(positionOffset);
        mRightCircleRadius = mMinCircleRadius + (mMaxCircleRadius - mMinCircleRadius) * mStartInterpolator
                .getInterpolation(positionOffset);

        invalidate();

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPositionDataProvide(List<PositionData> dataList) {
        mPositionDatas = dataList;
    }

    public float getYOffset() {
        return mYOffset;
    }

    public void setYOffset(float YOffset) {
        mYOffset = YOffset;
    }

    public float getMaxCircleRadius() {
        return mMaxCircleRadius;
    }

    public void setMaxCircleRadius(float maxCircleRadius) {
        mMaxCircleRadius = maxCircleRadius;
    }

    public float getMinCircleRadius() {
        return mMinCircleRadius;
    }

    public void setMinCircleRadius(float minCircleRadius) {
        mMinCircleRadius = minCircleRadius;
    }

    public void setColors(Integer... colors) {
        mClors = Arrays.asList(colors);
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new AccelerateInterpolator();
        }
    }

    public void setEndInterpolator(Interpolator endInterpolator) {
        mEndInterpolator = endInterpolator;

        if (mEndInterpolator == null) {
            mEndInterpolator = new AccelerateInterpolator();
        }
    }
}
