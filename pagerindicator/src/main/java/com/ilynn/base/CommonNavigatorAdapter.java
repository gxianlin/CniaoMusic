package com.ilynn.base;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;

import com.ilynn.base.title.IPageTitleView;

/**
 * 描述：CommonNavigator适配器,用于切换不同样式
 * 作者：gong.xl
 * 创建日期：2017/7/6 下午5:28
 * 修改日期: 2017/7/6
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public abstract class CommonNavigatorAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public abstract int getCount();

    public abstract IPagerIndicator getIndicator(Context compat);

    public abstract IPageTitleView getTitleView(Context context, int position);

    public float getTitleWeight(Context context,int index){
        return 1;
    }

    public final void registerDataSetObserver(DataSetObserver observer){
        mDataSetObservable.registerObserver(observer);
    }

    public final void unregisterDataSetObserver(DataSetObserver observer){
        mDataSetObservable.unregisterObserver(observer);
    }


    public void notifyDataSetChanged(){
        mDataSetObservable.notifyChanged();
    }

    public final void notifyDataSetInvalidated(){
        mDataSetObservable.notifyInvalidated();
    }

}
