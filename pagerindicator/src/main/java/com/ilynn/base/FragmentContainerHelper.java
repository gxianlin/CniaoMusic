package com.ilynn.base;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：pagerIndicator 在fragment中使用的时候需要
 * 作者：gong.xl
 * 创建日期：2017/7/7 下午4:41
 * 修改日期: 2017/7/7
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FragmentContainerHelper {

    private List<IPagerIndicator> mPagerIndicators = new ArrayList<>();
    private ValueAnimator mScrollAnimator;
    private int mLastSelectedIndex;
    private int mDuration = 150;
    private Interpolator mInterpolator = new AccelerateDecelerateInterpolator();

    private Animator.AnimatorListener mAnimatorListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_IDLE);
            mScrollAnimator = null;
        }
    };

    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            float positionOffsetSum = (float) animation.getAnimatedValue();
            int position = (int) positionOffsetSum;
            float positionOffset = positionOffsetSum - position;
            if (positionOffsetSum < 0) {
                position = position - 1;
                positionOffset = 1.0f + positionOffset;
            }
            dispatchPageScrolled(position, positionOffset, 0);
        }
    };

    public FragmentContainerHelper() {
    }

    public FragmentContainerHelper(IPagerIndicator pagerIndicator) {
        mPagerIndicators.add(pagerIndicator);
    }

    /**
     * IPagrIndicator支持弹性效果的辅助方法
     *
     * @param positionDatas
     * @param index
     * @return
     */
    public static PositionData getImitativePositionData(List<PositionData> positionDatas, int index) {
        if (index >= 0 && index <= positionDatas.size() - 1) {
            return positionDatas.get(index);
        }else {
            PositionData result = new PositionData();
            PositionData referenceData;

            int offset;

            if (index <0){
                offset = index;
                referenceData = positionDatas.get(0);
            }else {
                offset = index - positionDatas.size() +1;
                referenceData = positionDatas.get(positionDatas.size() -1);
            }

            result.mLeft = referenceData.mLeft + offset * referenceData.width();
            result.mTop = referenceData.mTop;
            result.mRight = referenceData.mRight + offset * referenceData.width();
            result.mBottom = referenceData.mBottom;

            result.mContentLeft = referenceData.mContentLeft + offset * referenceData.width();
            result.mContentTop = referenceData.mContentTop;
            result.mContentRight = referenceData.mContentRight + offset * referenceData.width();
            result.mContentBottom = referenceData.mContentBottom;
            return result;
        }
    }

    public void handlePageSelected(int selectedIndex){
        handlePageSelected(selectedIndex,true);
    }

    private void handlePageSelected(int selectedIndex, boolean smooth) {
        if (mLastSelectedIndex == selectedIndex){
            return;
        }

        if (smooth){
            if (mScrollAnimator == null || !mScrollAnimator.isRunning()){
                dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_SETTLING);
            }

            dispatchPageSelected(selectedIndex);

            float currentPositionOffsetSum = mLastSelectedIndex;
            if (mScrollAnimator != null){
                currentPositionOffsetSum = (float) mScrollAnimator.getAnimatedValue();
                mScrollAnimator.cancel();
                mScrollAnimator = null;
            }

            mScrollAnimator = new ValueAnimator();
            mScrollAnimator.setFloatValues(currentPositionOffsetSum,selectedIndex);
            mScrollAnimator.addUpdateListener(mAnimatorUpdateListener);
            mScrollAnimator.addListener(mAnimatorListener);
            mScrollAnimator.setInterpolator(mInterpolator);
            mScrollAnimator.setDuration(mDuration);
            mScrollAnimator.start();
        }else {
            dispatchPageSelected(selectedIndex);
            if (mScrollAnimator !=null&& mScrollAnimator.isRunning()){
                dispatchPageScrolled(mLastSelectedIndex,0.0f,0);
            }
            dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_IDLE);
            dispatchPageScrolled(selectedIndex,0.0f,0);
        }

        mLastSelectedIndex = selectedIndex;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setInterpolator(Interpolator interpolator) {
        if (interpolator == null){
            mInterpolator = new AccelerateDecelerateInterpolator();
        }else {
            mInterpolator = interpolator;
        }
    }

    public void attchPageIndicator(IPagerIndicator pagerIndicator){
        mPagerIndicators.add(pagerIndicator);
    }

    private void dispatchPageSelected(int selectedIndex) {
        for (IPagerIndicator pagerIndicator : mPagerIndicators){
            pagerIndicator.onPageSelected(selectedIndex);
        }
    }

    private void dispatchPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (IPagerIndicator pagerIndicator : mPagerIndicators){
            pagerIndicator.onPageScrolled(position,positionOffset,positionOffsetPixels);
        }
    }

    private void dispatchPageScrollStateChanged(int state) {
        for (IPagerIndicator pagerIndicator : mPagerIndicators){
            pagerIndicator.onPageScrollStateChanged(state);
        }
    }

}
