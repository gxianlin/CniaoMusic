package com.cainiao.music.cniaomusic.music;

import android.util.Log;

import com.cainiao.music.cniaomusic.MusicApplication;
import com.cainiao.music.cniaomusic.common.utils.ACache;
import com.cainiao.music.cniaomusic.data.Song;

import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 描述：最近播放列表
 * 作者：gong.xl
 * 创建日期：2017/8/19 0019 9:33
 * 修改日期: 2017/8/19 0019
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class MusicRecentPlayList {
    private final String PLAY_QUEUE = "song_queue";
    private final int RECENT_COUNT = 20;
    private static MusicRecentPlayList mInstance;

    private ArrayList<Song> queue;

    private MusicRecentPlayList() {
        queue = readQueueFromCache();
    }

    public static MusicRecentPlayList getInstance() {
        if (mInstance == null) {
            mInstance = new MusicRecentPlayList();
        }
        return mInstance;
    }

    //歌曲的添加
    public void addPlaySong(Song song) {
        queue.add(0,song);
        //
        for (int i=1;i<queue.size();i++){
            //歌曲不能重复
            if (song.getId() == queue.get(i).getId()) {
                queue.remove(i);
                break;
            }
            //列表最大数(数量限制)
            if (i > RECENT_COUNT) {
                queue.remove(i);
                continue;
            }
        }

        //添加缓存
        Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {
                addQueueToFileCache();
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    public ArrayList<Song> getQueue() {
        Log.i("tag","获取历史歌曲列表");
        return queue;
    }

    /**
     * 将播放列表缓存到文件中,以便下次读取
     */
    private void addQueueToFileCache() {
        Log.i("tag","添加歌曲缓存");
        ACache.get(MusicApplication.getInstance(), PLAY_QUEUE).put(PLAY_QUEUE, queue);
    }


    /**
     * 歌曲的读取
     *
     * @return
     */
    private ArrayList<Song> readQueueFromCache() {
        Object object = ACache.get(MusicApplication.getInstance(), PLAY_QUEUE).getAsObject(PLAY_QUEUE);
        if (object != null && object instanceof ArrayList) {
            return (ArrayList<Song>) object;
        }
        return new ArrayList<>();
    }


    public void clearRecentPlayList(){
        queue.clear();
    }
}
