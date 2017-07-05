package com.ilynn.base.syle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ilynn.base.UIUtil;
import com.ilynn.base.abs.IPageNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：圆圈式指示器
 * 作者：gong.xl
 * 创建日期：2017/7/5 下午5:04
 * 修改日期: 2017/7/5
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class CircleNavigator extends View implements IPageNavigator {
    private int mRadius;            //圆圈半径
    private int mCircleColor;       //圆圈颜色
    private int mStrokeWidth;       //空心圆 圈的宽度
    private int mCircleSpacing;      //圆之间间距
    private int mTotalCount;        //圆点个数
    private int mCurrentIndex;

    private Interpolator mStartInterpolator = new LinearInterpolator();

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<PointF> mCirclePoints = new ArrayList<>();
    private float mIndicatorX;


    private boolean mTouchable;
    private float mDownX;
    private float mDownY;
    private int mTouchSlop;

    private boolean mFollowTouch = true;

    private OnCircleClickListener mCircleClickListener;


    public CircleNavigator(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mRadius = UIUtil.dip2px(context, 3);
        mCircleSpacing = UIUtil.dip2px(context, 8);
        mStrokeWidth = UIUtil.dip2px(context, 1);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mesureWidth(widthMeasureSpec), mesureHeight(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(mCircleColor);
        drawCircles(canvas);
        drawIndicator(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (mTouchable) {
                    mDownX = x;
                    mDownY = y;
                    return true;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (mCircleClickListener != null) {
                    if (Math.abs(x - mDownX) <= mTouchSlop && Math.abs(y - mDownY) <= mTouchSlop) {
                        float max = Float.MAX_VALUE;
                        int index = 0;
                        for (int i = 0; i < mCirclePoints.size(); i++) {
                            PointF pointF = mCirclePoints.get(i);
                            float offset = Math.abs(pointF.x - x);

                            if (offset < max) {
                                max = offset;
                                index = i;
                            }
                            mCircleClickListener.onClick(index);
                        }
                    }
                }
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     *
     */
    private void prepareCirclePoints() {
        mCirclePoints.clear();
        if (mTotalCount > 0) {
            int y = (int) (getHeight() / 2.0f + 0.5f);
            int centerSpacing = mRadius * 2 + mCircleSpacing;
            int startX = mRadius + (int) (mStrokeWidth / 2.0f + 0.5f) + getPaddingLeft();
            for (int i = 0; i < mTotalCount; i++) {
                PointF pointF = new PointF(startX, y);
                mCirclePoints.add(pointF);
                startX += centerSpacing;
            }
            mIndicatorX = mCirclePoints.get(mCurrentIndex).x;
        }
    }

    /**
     * 画空心圆
     *
     * @param canvas
     */
    private void drawCircles(Canvas canvas) {
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        for (int i = 0, j = mCirclePoints.size(); i < j; i++) {
            PointF pointF = mCirclePoints.get(i);
            canvas.drawCircle(pointF.x, pointF.y, mRadius, mPaint);
        }
    }

    /**
     * 画圆点
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        mPaint.setStyle(Paint.Style.FILL);
        if (mCirclePoints.size() > 0) {
            canvas.drawCircle(mIndicatorX, (int) (getHeight() / 2.0f + 0.5f), mRadius, mPaint);
        }
    }

    /**
     * 测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int mesureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = height;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mRadius * 2 + mStrokeWidth * 2 + getPaddingTop() + getPaddingBottom();
                break;
        }
        return result;
    }

    /**
     * 测量宽度
     *
     * @param widthMeasureSpec
     * @return
     */
    private int mesureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int result = 0;
        switch (mode) {
            case MeasureSpec.EXACTLY:
                result = width;
                break;
            case MeasureSpec.AT_MOST:
            case MeasureSpec.UNSPECIFIED:
                result = mTotalCount * mRadius * 2 + (mTotalCount - 1) * mCircleSpacing + mStrokeWidth * 2 +
                        getPaddingLeft() + getPaddingRight();
                break;
        }
        return result;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mFollowTouch) {
            if (mCirclePoints.isEmpty()) {
                return;
            }

            int currentPosition = Math.min(mCirclePoints.size() - 1, position);
            int nextPosition = Math.min(mCirclePoints.size() - 1, position + 1);
            PointF current = mCirclePoints.get(currentPosition);
            PointF next = mCirclePoints.get(nextPosition);

            mIndicatorX = current.x + (next.x - current.x) * mStartInterpolator.getInterpolation(positionOffset);
            invalidate();
        }
    }

    @Override
    public void onPageSelected(int position) {
        mCurrentIndex = position;
        if (!mFollowTouch) {
            mIndicatorX = mCirclePoints.get(position).x;
            invalidate();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onAttachToIndicator() {

    }

    @Override
    public void onDetachFromIndicator() {

    }

    @Override
    public void notifyDataSetChanged() {
        prepareCirclePoints();
        invalidate();
    }

    public int getRadius() {
        return mRadius;
    }

    public void setRadius(int radius) {
        mRadius = radius;
        notifyDataSetChanged();
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        invalidate();
    }

    public int getStrokeWidth() {
        return mStrokeWidth;
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        invalidate();
    }

    public int getCircleSpacing() {
        return mCircleSpacing;
    }

    public void setCircleSpacing(int circleSpacing) {
        mCircleSpacing = circleSpacing;
        notifyDataSetChanged();
    }

    public Interpolator getStartInterpolator() {
        return mStartInterpolator;
    }

    public void setStartInterpolator(Interpolator startInterpolator) {
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null) {
            mStartInterpolator = new LinearInterpolator();
        }
    }

    public int getCirclelCount() {
        return mTotalCount;
    }

    public void setCircleCount(int totalCount) {
        mTotalCount = totalCount;
    }

    public boolean isTouchable() {
        return mTouchable;
    }

    public void setTouchable(boolean touchable) {
        mTouchable = touchable;
    }

    public boolean isFollowTouch() {
        return mFollowTouch;
    }

    public void setFollowTouch(boolean followTouch) {
        mFollowTouch = followTouch;
    }


    public OnCircleClickListener getCircleClickListener() {
        return mCircleClickListener;
    }

    public void setCircleClickListener(OnCircleClickListener circleClickListener) {
        if (!mTouchable) {
            mTouchable = true;
        }
        mCircleClickListener = circleClickListener;
    }

    public interface OnCircleClickListener {
        void onClick(int position);
    }
}
