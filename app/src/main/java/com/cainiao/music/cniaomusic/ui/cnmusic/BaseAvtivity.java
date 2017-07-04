package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * 描述：所有activity的基类
 * 作者：gong.xl
 * 创建日期：2017/7/3 下午10:43
 * 修改日期: 2017/7/3
 * 修改备注：
 * 邮箱：gxianlin@126.com
 */
public class BaseAvtivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * 显示Snackbar
     *
     * @param view
     * @param text
     */
    public void showSnackBar(View view,@Nullable String text){
        Snackbar.make(view,text, Snackbar.LENGTH_SHORT).show();
    }

    public void showSnackBar(View view,int resID){
        Snackbar.make(view,resID,Snackbar.LENGTH_SHORT).show();
    }

    /***
     * 跳转页面
     */
    public void startToActivity(Class activity){
        Intent intent = new Intent();
        intent.setClass(this,activity);
        startActivity(intent);
    }
}
