package com.cainiao.music.cniaomusic.service;

import android.support.v4.media.session.PlaybackStateCompat;

import com.cainiao.music.cniaomusic.data.Song;

/**
 * 描述：TODO
 * 作者：gong.xl
 * 创建日期：2017/8/12 0012 13:10
 * 修改日期: 2017/8/12 0012
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public interface OnSongchangeListener {
    //歌曲改变的回调
    void onSongChanged(Song song);

    //歌曲后台改变的回调
    void onPlayBackStateChanged(PlaybackStateCompat state);
}
