package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


/**
 * 所有activity的基类
 */
public class BaseAvtivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

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
