package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.content.Context;
import android.util.Log;

import com.cainiao.music.cniaomusic.common.utils.LocalMusicLibrary;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.ui.model.LocalIview;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 描述：TODO
 * 作者：gong.xl
 * 创建日期：2017/7/8 0008 0:33
 * 修改日期: 2017/7/8 0008
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class LocalLibraryPresenter {

    private LocalIview.LocalMusic mLocalMusic;
    private Context mContext;
    public LocalLibraryPresenter(Context context,LocalIview.LocalMusic localMusic) {
        this.mContext = context;
        this.mLocalMusic = localMusic;
    }

    /***
     * 请求获取本地歌曲
     */
    public void requestMusic(){
        Observable.create(
                new Observable.OnSubscribe<List<Song>>() {
                    @Override
                    public void call(Subscriber<? super List<Song>> subscriber) {
                        List<Song> songs = LocalMusicLibrary.getAllSongs(mContext);
                        subscriber.onNext(songs);
                    }
                }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Song>>() {
                    @Override
                    public void call(List<Song> songs) {
                        if (mLocalMusic != null)
                            mLocalMusic.getLocalMusic(songs);

                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
//                        MoeLogger.e(throwable.toString());
                        Log.e("call: ", "call: "+throwable.toString());
                    }
                });
    }
}
