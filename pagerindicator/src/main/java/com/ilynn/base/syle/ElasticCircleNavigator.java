package com.ilynn.base.syle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.ilynn.base.ArgbEvaluatorHolder;
import com.ilynn.base.NavigatorHelper;
import com.ilynn.base.UIUtil;
import com.ilynn.base.abs.IOnNavigatorScrollListener;
import com.ilynn.base.abs.IPageNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：指示器样式
 * 圆点可伸缩的样式
 * 作者：gong.xl
 * 创建日期：2017/7/5 下午12:29
 * 修改日期: 2017/7/5
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class ElasticCircleNavigator extends View implements IPageNavigator, IOnNavigatorScrollListener {
    private int mMinRadius;                     //小圆半径
    private int mMaxRadius;                     //大圆半径
    private int mNormaCircleColor = Color.LTGRAY;   //未选中的颜色
    private int mSelectCircleColor = Color.GRAY;    //选中的颜色
    private int mCircleSpacing;                     //圆点间的间隔距离
    private int mCircleCount;                       //圆点个数

    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private List<PointF> mCirclePoints = new ArrayList<>();
    private SparseArray<Float> mCircleRadiusArray = new SparseArray<>();

    //是否可接受点击
    private boolean mTouchable;
    private OnCircleClickListener mOnCircleClickListener;
    //一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。用这个距离来判断用户是否翻页
    private int mTouchSlop;
    private float mDownX;         //手指按下的两个坐标
    private float mDownY;

    private boolean mFoolowTouch = true;

    private NavigatorHelper mNavigatorHelper = new NavigatorHelper();
    private Interpolator mStartInterpolator = new LinearInterpolator();


    public ElasticCircleNavigator(Context context) {
        super(context);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mMinRadius = UIUtil.dip2px(context, 3);
        mMaxRadius = UIUtil.dip2px(context, 5);
        mCircleSpacing = UIUtil.dip2px(context, 8);
        mNavigatorHelper.setNavigatorScrollListener(this);
        mNavigatorHelper.setSkimOver(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(mesureWidth(widthMeasureSpec), mesureHeight(heightMeasureSpec));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        for (int i = 0, j = mCirclePoints.size(); i < j; i++) {
            PointF pointF = mCirclePoints.get(i);
            float radius = mCircleRadiusArray.get(i, (float) mMinRadius);
            mPaint.setColor(ArgbEvaluatorHolder.eval((radius - mMinRadius) / (mMaxRadius - mMinRadius),
                    mNormaCircleColor, mSelectCircleColor));
            canvas.drawCircle(pointF.x, getHeight() / 2.0f, radius, mPaint);
        }
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
                if (mOnCircleClickListener != null) {
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
                            mOnCircleClickListener.onClick(index);
                        }
                    }
                }
                break;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        prepareCirclePoints();
    }

    /**
     *
     */
    private void prepareCirclePoints() {
        mCirclePoints.clear();
        if (mCircleCount > 0) {
            int y = Math.round(getHeight() / 2.0f);
            int centerSpacing = mMinRadius * 2 + mCircleSpacing;
            int startX = mMaxRadius + getPaddingLeft();
            for (int i = 0; i < mCircleCount; i++) {
                PointF pointF = new PointF(startX, y);
                mCirclePoints.add(pointF);
                startX += centerSpacing;
            }
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
                result = mMaxRadius * 2 + getPaddingTop() + getPaddingBottom();
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
                result = (mCircleCount - 1) * mMinRadius * 2 + mMaxRadius * 2 + (mCircleCount - 1) * mCircleSpacing
                        + getPaddingLeft() + getPaddingRight();
                break;
        }
        return result;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
    }

    @Override
    public void onPageSelected(int position) {
        mNavigatorHelper.onPageSelected(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mNavigatorHelper.onPageScrollStateChanged(state);
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

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mFoolowTouch){
            float radius = mMinRadius + (mMaxRadius - mMinRadius) * mStartInterpolator.getInterpolation(enterPercent);
            mCircleRadiusArray.put(index,radius);
            invalidate();
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mFoolowTouch){
            float radius = mMaxRadius + (mMinRadius - mMaxRadius) * mStartInterpolator.getInterpolation(leavePercent);
            mCircleRadiusArray.put(index,radius);
            invalidate();
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
        if (!mFoolowTouch){
            mCircleRadiusArray.put(index, (float) mMaxRadius);
            invalidate();
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        if (!mFoolowTouch){
            mCircleRadiusArray.put(index, (float) mMinRadius);
            invalidate();
        }
    }

    public interface OnCircleClickListener {
        void onClick(int position);
    }

    /**
     * 设置指示器点击监听事件
     *
     * @param onCircleClickListener
     */
    public void setOnCircleClickListener(OnCircleClickListener onCircleClickListener) {
        if (!mTouchable) {
            mTouchable = true;
        }
        mOnCircleClickListener = onCircleClickListener;
    }

    /**
     * 设置未选中圆的半径
     * @param minRadius
     */
    public void setMinRadius(int minRadius){
        mMinRadius = minRadius;
        notifyDataSetChanged();
    }

    /**
     * 设置选中圆的半径
     */
    public void setMaxRadius(int maxRadius){
        mMaxRadius = maxRadius;
        notifyDataSetChanged();
    }

    /**
     * 设置未选中圆的颜色
     * @param normaCircleColor
     */
    public void setNormaCircleColor(int normaCircleColor){
        mNormaCircleColor = normaCircleColor;
        invalidate();
    }

    /**
     * 设置选中圆的颜色
     * @param selectCircleColor
     */
    public void setSelectCircleColor(int selectCircleColor){
        mSelectCircleColor = selectCircleColor;
        invalidate();
    }

    /**
     * 设置圆点间间距
     * @param circleSpacing
     */
    public void setCircleSpacing(int circleSpacing){
        mCircleSpacing = circleSpacing;
        notifyDataSetChanged();
    }

    public void setStartInterpolator(Interpolator startInterpolator){
        mStartInterpolator = startInterpolator;
        if (mStartInterpolator == null){
            mStartInterpolator = new LinearInterpolator();
        }
    }

    /**
     * 设置指示器个数
     * @param count
     */
    public void setCircleCount(int count){
        mCircleCount = count;
        mNavigatorHelper.setTotalCount(mCircleCount);
    }
}
