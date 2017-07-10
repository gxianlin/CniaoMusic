package com.ilynn.base;

import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.ilynn.base.abs.IOnNavigatorScrollListener;

/**
 * 描述：用于拓展IPageNavigator的帮助类,将viewpager的三个回调方法
 * 进行转换,方便拓展
 * <p>
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午5:38
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */
public class NavigatorHelper {
    private SparseBooleanArray mDeselectedItems = new SparseBooleanArray();
    private SparseArray<Float> mLeavedPercents  = new SparseArray<>();

    private int   mTotalCount;
    private int   mCurrentIndex;
    private int   mLastIndex;
    private float mLastPositionOffsetSum;
    private int   mScrollState;

    private boolean                                  mSkimOver;
    private IOnNavigatorScrollListener mNavigatorScrollListener;

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        float   currentPositionOffsetSum = position + positionOffset;
        boolean leftToRight              = false;
        if (mLastPositionOffsetSum <= currentPositionOffsetSum) {
            leftToRight = true;
        }
        if (mScrollState != ScrollState.SCROLL_STATE_IDLE) {
            if (currentPositionOffsetSum == mLastPositionOffsetSum) {
                return;
            }
            int     nextPosition   = position + 1;
            boolean normalDispatch = true;
            if (positionOffset == 0.0f) {
                if (leftToRight) {
                    nextPosition = position - 1;
                    normalDispatch = false;
                }
            }
            for (int i = 0; i < mTotalCount; i++) {
                if (i == position || i == nextPosition) {
                    continue;
                }
                Float leavedPercent = mLeavedPercents.get(i, 0.0f);
                if (leavedPercent != 1.0f) {
                    dispatchOnLeave(i, 1.0f, leftToRight, true);
                }
            }
            if (normalDispatch) {
                if (leftToRight) {
                    dispatchOnLeave(position, positionOffset, true, false);
                    dispatchOnEnter(nextPosition, positionOffset, true, false);
                } else {
                    dispatchOnLeave(nextPosition, 1.0f - positionOffset, false, false);
                    dispatchOnEnter(position, 1.0f - positionOffset, false, false);
                }
            } else {
                dispatchOnLeave(nextPosition, 1.0f - positionOffset, true, false);
                dispatchOnEnter(position, 1.0f - positionOffset, true, false);
            }
        } else {
            for (int i = 0; i < mTotalCount; i++) {
                if (i == mCurrentIndex) {
                    continue;
                }
                boolean deselected = mDeselectedItems.get(i);
                if (!deselected) {
                    dispatchOnDeselected(i);
                }
                Float leavedPercent = mLeavedPercents.get(i, 0.0f);
                if (leavedPercent != 1.0f) {
                    dispatchOnLeave(i, 1.0f, false, true);
                }
            }
            dispatchOnEnter(mCurrentIndex, 1.0f, false, true);
            dispatchOnSelected(mCurrentIndex);
        }
        mLastPositionOffsetSum = currentPositionOffsetSum;
    }

    private void dispatchOnEnter(int index, float enterPercent, boolean leftToRight, boolean force) {
        if (mSkimOver || index == mCurrentIndex || mScrollState == ScrollState.SCROLL_STATE_DRAGGING || force) {
            if (mNavigatorScrollListener != null) {
                mNavigatorScrollListener.onEnter(index, mTotalCount, enterPercent, leftToRight);
            }
            mLeavedPercents.put(index, 1.0f - enterPercent);
        }
    }

    private void dispatchOnLeave(int index, float leavePercent, boolean leftToRight, boolean force) {
        if (mSkimOver || index == mLastIndex || mScrollState == ScrollState.SCROLL_STATE_DRAGGING ||
                ((index == mCurrentIndex - 1 || index == mCurrentIndex + 1) &&
                        mLeavedPercents.get(index, 0.0f) != 1.0f) || force) {

            if (mNavigatorScrollListener != null) {
                mNavigatorScrollListener.onLeave(index, mTotalCount, leavePercent, leftToRight);
            }
            mLeavedPercents.put(index, leavePercent);
        }
    }

    private void dispatchOnSelected(int index) {
        if (mNavigatorScrollListener != null) {
            mNavigatorScrollListener.onSelected(index, mTotalCount);
        }
        mDeselectedItems.put(index, false);
    }

    private void dispatchOnDeselected(int index) {
        if (mNavigatorScrollListener != null) {
            mNavigatorScrollListener.onDeselected(index, mTotalCount);
        }
        mDeselectedItems.put(index, true);
    }

    public void onPageSelected(int position) {
        mLastIndex = mCurrentIndex;
        mCurrentIndex = position;
        dispatchOnSelected(mCurrentIndex);
        for (int i = 0; i < mTotalCount; i++) {
            if (i == mCurrentIndex) {
                continue;
            }
            boolean deselected = mDeselectedItems.get(i);
            if (!deselected) {
                dispatchOnDeselected(i);
            }
        }
    }

    public void onPageScrollStateChanged(int state) {
        mScrollState = state;
    }

    public void setNavigatorScrollListener(IOnNavigatorScrollListener navigatorScrollListener) {
        mNavigatorScrollListener = navigatorScrollListener;
    }

    public void setSkimOver(boolean skimOver) {
        mSkimOver = skimOver;
    }

    public int getTotalCount() {
        return mTotalCount;
    }

    public void setTotalCount(int totalCount) {
        mTotalCount = totalCount;
        mDeselectedItems.clear();
        mLeavedPercents.clear();
    }

    public int getCurrentIndex() {
        return mCurrentIndex;
    }

    public int getScrollState() {
        return mScrollState;
    }
}
