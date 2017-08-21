package com.cainiao.music.cniaomusic.ui.radio;

import android.view.View;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.base.BaseFragment;
import com.cainiao.music.cniaomusic.ui.cnmusic.LocalMusicActivity;
import com.cainiao.music.cniaomusic.ui.cnmusic.RecentPlayListActivity;

import butterknife.OnClick;

/**
 * 描述：播放记录及本地音乐页面
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午22:20
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gxianlin@126.com
 */
public class RadioFragment extends BaseFragment {

    public RadioFragment() {
        // Required empty public constructor
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_radio;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setListener() {

    }

    @Override
    public void initData() {

    }

    @OnClick({R.id.local_music, R.id.recent_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.local_music:
                startToActivity(LocalMusicActivity.class);
                break;
            case R.id.recent_play:
                startToActivity(RecentPlayListActivity.class);
                break;
        }
    }
}
