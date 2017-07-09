package com.cainiao.music.cniaomusic.ui.adapter;

import android.view.View;

/**
 * @desciption: 列表点击接口
 */
public interface OnItemClickListener<T> {

    void onItemClick(T item, int position);

    void onItemSettingClick(View v, T item, int position);
}
