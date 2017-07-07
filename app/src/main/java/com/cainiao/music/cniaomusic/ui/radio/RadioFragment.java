package com.cainiao.music.cniaomusic.ui.radio;

import android.view.View;
import android.widget.TextView;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.cnmusic.BaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 描述：唱片
 * 作者：gong.xl
 * 创建日期：2017/7/4 下午22:20
 * 修改日期: 2017/7/4
 * 修改备注：
 * 邮箱：gxianlin@126.com
 */
public class RadioFragment extends BaseFragment {

    @InjectView(R.id.textview)
    TextView mTextview;

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

    @Override
    public void onClick(View v) {

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
