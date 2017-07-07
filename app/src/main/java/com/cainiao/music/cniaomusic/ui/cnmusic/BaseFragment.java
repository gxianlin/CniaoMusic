package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;

/**
 * 描述：所有fragment的基类
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午2:20
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gxianlin@126.com
 */
public abstract class BaseFragment extends Fragment implements View.OnClickListener {
    protected final String TAG = BaseFragment.class.getSimpleName();

    private boolean isVisible = false;
    private boolean isInitView = false;
    private boolean isFirstLoad = true;
    protected View contentView;
    private SparseArray<View> mViews;
    /**
     * 绑定布局文件
     * @return
     */
    public abstract int getLayoutId();


    /**
     * 初始化视图控件
     */
    public abstract void initViews();

    /**
     * 设置监听事件
     */
    public abstract void setListener();

    /**
     * 初始化数据
     */
    public abstract void initData();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mViews = new SparseArray<>();
        if (contentView == null) {
            contentView = inflater.inflate(getLayoutId(), container, false);
        }
        ButterKnife.inject(this,contentView);
        initViews();
        isInitView = true;
        lazyLoad();
        return contentView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isVisible = true;
            lazyLoad();
        } else {
            isVisible = false;
        }
    }

    /**
     * 用于接收参数
     */
    protected void receiveData() {

    }

    //懒加载
    protected void lazyLoad() {

        if (!isFirstLoad || !isVisible || !isInitView) {
            //如果不是第一次加载、不是可见、不是初始化view，则不加载数据
            return;
        }
        //加载数据
        setListener();
        initData();
        //设置已经不是第一次加载
        isFirstLoad = false;
    }

    /**
     * 通过id找到view
     *
     * @param viewId
     * @param <E>
     * @return
     */
    public <E extends View> E findView(int viewId) {
        E view = (E) mViews.get(viewId);
        if (view == null) {
            view = (E) contentView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    /**
     * 设置view的点击事件
     *
     * @param view
     * @param <E>
     */
    public <E extends View> void setOnClick(E view) {
        view.setOnClickListener(this);
    }

    /**
     * 跳转页面
     *
     * @param zClass activity
     */
    public void gotoActivity(Class zClass) {
        startActivity(new Intent(getContext(), zClass));
    }
}
