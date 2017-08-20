package com.cainiao.music.cniaomusic.ui.base;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;
import android.widget.Toast;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.service.MusicPlayerManager;
import com.cainiao.music.cniaomusic.service.MusicServiceHelper;
import com.cainiao.music.cniaomusic.ui.play.PlayActivity;

import butterknife.ButterKnife;


/**
 * 描述：所有activity的基类
 * 作者：gong.xl
 * 创建日期：2017/7/3 下午10:43
 * 修改日期: 2017/7/3
 * 修改备注：
 * 邮箱：gxianlin@126.com
 */
public abstract class BaseAvtivity extends AppCompatActivity implements View.OnClickListener {
    protected final String TAG = BaseAvtivity.class.getSimpleName();
    private SparseArray<View> mViews;

    /**
     * 绑定布局文件
     *
     * @return
     */
    public abstract int getLayoutId();

    /**
     * 初始化视图控件
     */
    public abstract void initViews();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 设置控件监听事件
     */
    public abstract void setListener();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mViews = new SparseArray<>();
        //开启服务
        MusicServiceHelper.get(getApplicationContext()).initService();
        setContentView(getLayoutId());
        ButterKnife.inject(this);
        receiveData();
        initViews();
        setListener();
        initData();
    }

    /**
     * 用于接收参数
     */
    protected void receiveData() {

    }

    /**
     * 跳转到音乐播放界面
     * @return
     */
    public boolean gotoSongPlayerActivity() {
        if (MusicPlayerManager.getInstance().getPlayingSong() == null) {
            showToast(R.string.music_playing_none);
            return false;
        }
        PlayActivity.open(this);
        return true;
    }

    /**
     * @param outState 取消保存状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //super.onSaveInstanceState(outState);
    }

    /**
     * @param savedInstanceState 取消保存状态
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 显示Snackbar
     *
     * @param view
     * @param text
     */
    public void showSnackBar(View view, @Nullable String text) {
        Snackbar.make(view, text, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBar(View view, int resID) {
        Snackbar.make(view, resID, Snackbar.LENGTH_SHORT).show();
    }

    /***
     * 跳转页面
     */
    public void startToActivity(Class activity) {
        Intent intent = new Intent();
        intent.setClass(this, activity);
        startActivity(intent);
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
            view = (E) findViewById(viewId);
            mViews.put(viewId, view);
        }
        return view;
    }

    public boolean goToSongPlayActivity(){
        if (MusicPlayerManager.getInstance().getPlayingSong() == null){
            showToast(R.string.music_playing_none);
            return false;
        }

        PlayActivity.open(this);
        return true;
    }

    public void showToast(int toastRes){
        Toast.makeText(this, getString(toastRes), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService();
        ButterKnife.reset(this);
    }

    private void unbindService() {

    }

}
