package com.cainiao.music.cniaomusic.ui.guide;

import android.net.Uri;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.widget.CustomView;


/**
 * 功能：视频引导页
 * 作者:gong.xl
 * 邮箱:gxianlin@126.com
 * 创建时间:2017/7/3
 */
public class GuideFragment extends LoadFragment {

    //构造出一个自定义播放控件
    private CustomView customView;


    @Override
    protected int setContentView() {
        return R.layout.fragment_guide;
    }

    @Override
    protected void stopLoad() {
        super.stopLoad();
        if (customView != null) {
            customView.pause();
        }
    }

    @Override
    protected void lazyLoadData() {
        if (customView == null) {
            customView = findView(R.id.cv);
            int index = getArguments().getInt("index");
            Uri uri;
            if (index == 1) {
                //获取视频路径
                uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_1);
            } else if (index == 2) {
                uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_2);
            } else {
                uri = Uri.parse("android.resource://" + getActivity().getPackageName() + "/" + R.raw.guide_3);
            }
            customView.playVideo(uri);
        }else {
            customView.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (customView != null){
            customView.stopPlayback();
        }
    }
}
