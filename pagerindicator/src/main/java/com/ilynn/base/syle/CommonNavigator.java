package com.ilynn.base.syle;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.ilynn.base.CommonNavigatorAdapter;
import com.ilynn.base.title.IMeasureablePageTitleView;
import com.ilynn.base.title.IPageTitleView;
import com.ilynn.base.IPagerIndicator;
import com.ilynn.base.NavigatorHelper;
import com.ilynn.base.PositionData;
import com.ilynn.base.R;
import com.ilynn.base.abs.IOnNavigatorScrollListener;
import com.ilynn.base.abs.IPageNavigator;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：通用viewpager指示器
 * <p>
 * 作者：gong.xl
 * 创建日期：2017/7/6 下午5:18
 * 修改日期: 2017/7/6
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class CommonNavigator extends FrameLayout implements IPageNavigator, IOnNavigatorScrollListener {

    private HorizontalScrollView mScrollView;
    private LinearLayout mTitleContainer;
    private LinearLayout mIndicatorContainer;
    private IPagerIndicator mIndicator;

    private IPageTitleView mTitleView;

    private CommonNavigatorAdapter mAdapter;
    private NavigatorHelper mNavigatorHelper;

    private List<PositionData> mPositionDatas = new ArrayList<>();

    private boolean mAdjustMode;
    private boolean mEnablePivotScroll;
    private float mScrollPivotX = 0.5f;
    private boolean mSmoothScroll = true;
    private boolean mFollowTouch = true;
    private boolean mIndicatorOnTop;
    private boolean mSkimOver;
    private boolean mReselectWhenLayout = true;
    private int mLeftPadding;
    private int mRightPadding;


    private DataSetObserver mObserver = new DataSetObserver() {
        @Override
        public void onChanged() {
            mNavigatorHelper.setTotalCount(mAdapter.getCount());
            init();
        }

        @Override
        public void onInvalidated() {

        }
    };


    private void init() {
        View root;
        if (mAdjustMode) {
            root = LayoutInflater.from(getContext()).inflate(R.layout.page_navigator_layout_no_scroll, this);
        } else {
            root = LayoutInflater.from(getContext()).inflate(R.layout.page_navigator_layout, this);
        }

        mScrollView = (HorizontalScrollView) root.findViewById(R.id.scroll_view);

        mTitleContainer = (LinearLayout) root.findViewById(R.id.title_container);
        mTitleContainer.setPadding(mLeftPadding, 0, mRightPadding, 0);
        mIndicatorContainer = (LinearLayout) root.findViewById(R.id.indicator_container);
        if (mIndicatorOnTop) {
            mIndicatorContainer.getParent().bringChildToFront(mIndicatorContainer);
        }

        initTitlesAndIndicator();
    }

    /**
     * 初始化标题栏中标题和指示样式
     */
    private void initTitlesAndIndicator() {
        for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
            IPageTitleView titleView = mAdapter.getTitleView(getContext(), i);
            if (titleView instanceof View) {
                View view = (View) titleView;
                LinearLayout.LayoutParams lp;
                if (mAdjustMode) {
                    lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
                    lp.weight = mAdapter.getTitleWeight(getContext(), i);
                } else {
                    lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams
                            .MATCH_PARENT);
                }
                mTitleContainer.addView(view, lp);
            }
        }

        if (mAdapter != null) {
            mIndicator = mAdapter.getIndicator(getContext());
            if (mIndicator instanceof View) {
                LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup
                        .LayoutParams.MATCH_PARENT);
                mIndicatorContainer.addView((View) mIndicator, layoutParams);
            }
        }
    }


    public CommonNavigator(@NonNull Context context) {
        super(context);
        mNavigatorHelper = new NavigatorHelper();
        mNavigatorHelper.setNavigatorScrollListener(this);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mAdapter != null) {
            preparePositionData();
            if (mIndicator != null) {
                mIndicator.onPositionDataProvide(mPositionDatas);
            }
            if (mReselectWhenLayout) {
                onPageSelected(mNavigatorHelper.getCurrentIndex());
                onPageScrolled(mNavigatorHelper.getCurrentIndex(), 0.0f, 0);
            }
        }
    }

    /**
     * 获取title的位置信息,创建不同的指示器样式
     */
    private void preparePositionData() {
        mPositionDatas.clear();
        for (int i = 0, j = mNavigatorHelper.getTotalCount(); i < j; i++) {
            PositionData data = new PositionData();
            View v = mTitleContainer.getChildAt(i);
            if (v != null) {
                data.mLeft = v.getLeft();
                data.mTop = v.getTop();
                data.mRight = v.getRight();
                data.mBottom = v.getBottom();
                if (v instanceof IMeasureablePageTitleView) {
                    IMeasureablePageTitleView view = (IMeasureablePageTitleView) v;
                    data.mContentLeft = view.getContentLeft();
                    data.mContentTop = view.getContentTop();
                    data.mContentRight = view.getContentRight();
                    data.mContentBottom = view.getContentBottom();
                } else {
                    data.mContentLeft = data.mLeft;
                    data.mContentTop = data.mTop;
                    data.mContentRight = data.mRight;
                    data.mContentBottom = data.mBottom;
                }
            }
            mPositionDatas.add(data);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (mAdapter != null) {
            mNavigatorHelper.onPageScrolled(position, positionOffset, positionOffsetPixels);
            if (mIndicator != null) {
                mIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }


            //手动跟随滚动
            if (mScrollView != null && mPositionDatas.size() > 0 && position >= 0 && position < mPositionDatas.size()) {
                if (mFollowTouch) {
                    int currentPosition = Math.min(mPositionDatas.size() - 1, position);
                    int nextPosition = Math.min(mPositionDatas.size() - 1, position + 1);
                    PositionData current = mPositionDatas.get(currentPosition);
                    PositionData next = mPositionDatas.get(nextPosition);
                    float scrollTo = current.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                    float nextScrollTo = next.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;

                    mScrollView.scrollTo((int) (scrollTo + (nextScrollTo - scrollTo) * positionOffset), 0);
                } else {
                    //TODO 实现待选中项完全显示出来
                }
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageSelected(position);
            if (mIndicator != null) {
                mIndicator.onPageSelected(position);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (mAdapter != null) {
            mNavigatorHelper.onPageScrollStateChanged(state);
            if (mIndicator != null) {
                mIndicator.onPageScrollStateChanged(state);
            }
        }
    }

    @Override
    public void onAttachToIndicator() {
        init();
    }

    @Override
    public void onDetachFromIndicator() {

    }

    @Override
    public void notifyDataSetChanged() {
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEnter(int index, int totalCount, float enterPercent, boolean leftToRight) {
        if (mTitleContainer == null) {
            return;
        }

        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPageTitleView) {
            ((IPageTitleView) v).onEnter(index, totalCount, enterPercent, leftToRight);
        }
    }

    @Override
    public void onLeave(int index, int totalCount, float leavePercent, boolean leftToRight) {
        if (mTitleContainer == null) {
            return;
        }

        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPageTitleView) {
            ((IPageTitleView) v).onLeave(index, totalCount, leavePercent, leftToRight);
        }
    }

    @Override
    public void onSelected(int index, int totalCount) {
        if (mTitleContainer == null) {
            return;
        }

        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPageTitleView) {
            ((IPageTitleView) v).onSelected(index, totalCount);
        }

        if (!mAdjustMode && !mFollowTouch && mScrollView != null && mPositionDatas.size() > 0) {
            int currentIndex = Math.min(mPositionDatas.size() - 1, index);
            PositionData current = mPositionDatas.get(currentIndex);
            if (mEnablePivotScroll) {
                float scrollTo = current.horizontalCenter() - mScrollView.getWidth() * mScrollPivotX;
                if (mSmoothScroll) {
                    mScrollView.smoothScrollTo((int) scrollTo, 0);
                } else {
                    mScrollView.scrollTo((int) scrollTo, 0);
                }
            } else {
                if (mScrollView.getScaleX() > current.mLeft) {
                    if (mSmoothScroll) {
                        mScrollView.smoothScrollTo(current.mLeft, 0);
                    } else {
                        mScrollView.scrollTo(current.mLeft, 0);
                    }
                } else if (mScrollView.getScaleX() + getWidth() < current.mRight) {
                    if (mSmoothScroll) {
                        mScrollView.smoothScrollTo(current.mRight - getWidth(), 0);
                    } else {
                        mScrollView.scrollTo(current.mRight - getWidth(), 0);
                    }
                }
            }
        }
    }

    @Override
    public void onDeselected(int index, int totalCount) {
        if (mTitleContainer == null) {
            return;
        }

        View v = mTitleContainer.getChildAt(index);
        if (v instanceof IPageTitleView) {
            ((IPageTitleView) v).onDeselected(index, totalCount);
        }
    }

    public CommonNavigatorAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(CommonNavigatorAdapter adapter) {

        if (mAdapter == adapter) {
            return;
        }
        if (mAdapter == null) {
            mAdapter.registerDataSetObserver(mObserver);
            mNavigatorHelper.setTotalCount(mAdapter.getCount());
            if (mTitleContainer != null) {
                mAdapter.notifyDataSetChanged();
            }
        } else {
            mNavigatorHelper.setTotalCount(0);
            init();
        }
    }

    public boolean isIndicatorOnTop() {
        return mIndicatorOnTop;
    }

    public void setIndicatorOnTop(boolean indicatorOnTop) {
        mIndicatorOnTop = indicatorOnTop;
    }

    public boolean isReselectWhenLayout() {
        return mReselectWhenLayout;
    }

    public void setReselectWhenLayout(boolean reselectWhenLayout) {
        mReselectWhenLayout = reselectWhenLayout;
    }

    public int getLeftPadding() {
        return mLeftPadding;
    }

    public void setLeftPadding(int leftPadding) {
        mLeftPadding = leftPadding;
    }

    public int getRightPadding() {
        return mRightPadding;
    }

    public void setRightPadding(int rightPadding) {
        mRightPadding = rightPadding;
    }

    public boolean isEnablePivotScroll() {
        return mEnablePivotScroll;
    }

    public void setEnablePivotScroll(boolean enablePivotScroll) {
        mEnablePivotScroll = enablePivotScroll;
    }

    public boolean isSmoothScroll() {

        return mSmoothScroll;
    }

    public void setSmoothScroll(boolean smoothScroll) {
        mSmoothScroll = smoothScroll;
    }

    public boolean isFollowTouch() {
        return mFollowTouch;
    }

    public void setFollowTouch(boolean followTouch) {
        mFollowTouch = followTouch;
    }

    public boolean isSkimOver() {
        return mSkimOver;
    }

    public void setSkimOver(boolean skimOver) {
        mSkimOver = skimOver;
    }

    public LinearLayout getTitleContainer() {
        return mTitleContainer;
    }

    public IPagerIndicator getPageIndicator() {
        return mIndicator;
    }

    public boolean isAdjustMode() {
        return mAdjustMode;
    }

    public void setAdjustMode(boolean adjustMode) {
        mAdjustMode = adjustMode;
    }

    public float getScrollPivotX() {
        return mScrollPivotX;
    }

    public void setScrollPivotX(float scrollPivotX) {
        mScrollPivotX = scrollPivotX;
    }

    public IPageTitleView getPageTitleVIew(int index) {
        if (mTitleContainer == null) {
            return null;
        }
        return (IPageTitleView) mTitleContainer.getChildAt(index);
    }
}
