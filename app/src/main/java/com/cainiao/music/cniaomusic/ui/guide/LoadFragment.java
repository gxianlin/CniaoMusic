package com.cainiao.music.cniaomusic.ui.guide;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 功能：fragment的预加载问题处理
 *
 * 1.可以懒加载的fragment
 *
 * 作者:gong.xl
 * 邮箱:gxianlin@126.com
 * 创建时间:2017/7/3
 */
public abstract class LoadFragment extends Fragment {

    /**
     * 1.判断试图是否已经初始化
     */
    protected boolean isInitView = false;

    /**
     * 2.是否可以加载数据
     */
    protected boolean isLoadData = false;


    private View view;
    private boolean canLoadData;

    public LoadFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(setContentView(), container, false);

        /**
         * s视图是否已经初始化
         */
        isInitView = true;

        isCanLoadData();
        return view;
    }

    /**
     * 绑定视图
     *
     * @return
     */
    protected abstract int setContentView();

    public void isCanLoadData() {
        if (!isInitView){
            return;
        }
        /**
         * 视图用户可见
         */
        if (getUserVisibleHint()){
            lazyLoadData();
            isLoadData = true;
        }else {
            if (isLoadData){
                stopLoad();
            }
        }
    }

    /**
     * 系统方法，告诉fragment的UI可见
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isCanLoadData();
    }

    /**
     * 停止加载数据
     */
    protected void stopLoad(){

    }

    /**
     * 预加载数据，子类实现
     */
    protected abstract void lazyLoadData();

    /**
     * 子类传递过来的View视图
     * @return
     */
    protected  View getContentView(){
        return view;
    }

    protected <T extends View> T findView(int id){
        return (T) getContentView().findViewById(id);
    }
}
