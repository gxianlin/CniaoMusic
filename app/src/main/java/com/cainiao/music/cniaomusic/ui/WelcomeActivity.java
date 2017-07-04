package com.cainiao.music.cniaomusic.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.cnmusic.BaseAvtivity;
import com.cainiao.music.cniaomusic.ui.guide.GuideActivity;

/**
 * 功能：应用欢迎页面
 * 作者:gong.xl
 * 邮箱:gxianlin@126.com
 * 创建时间:2017/7/3
 */
public class WelcomeActivity extends BaseAvtivity {

    private ImageView logo;

    @Override
    public int getLayoutId() {
        return R.layout.activity_welcome;
    }

    @Override
    public void initViews() {
        logo = (ImageView) findViewById(R.id.welcome);
    }

    @Override
    public void setListener() {
    }

    @Override
    public void initData() {
        startAniamation();
    }


    /**
     * 启动页动画
     */
    private void startAniamation() {
        //创建动画并播放
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0f, 1f);
        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(alphaAnimator).with(scaleX).with(scaleY);
        animatorSet.setDuration(1500);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //动画播放结束
                //做出相关操作：用户登陆...
                //页面跳转
                startToActivity(GuideActivity.class);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
    }
}
