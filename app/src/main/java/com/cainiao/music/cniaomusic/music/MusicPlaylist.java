package com.cainiao.music.cniaomusic.music;

import com.cainiao.music.cniaomusic.data.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：播放列表
 *       对歌曲的存放,查找
 * 作者：gong.xl
 * 创建日期：2017/7/8 0008 0:53
 * 修改日期: 2017/7/8 0008
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class MusicPlaylist {

    private List<Song> queue;
    private Song curSong;
    private long ablumID;
    private String title;
    private List<Song> mQueue;


    public MusicPlaylist() {
        queue = new ArrayList<>();
    }
    public MusicPlaylist(List<Song> queue) {
        setQueue(queue);
    }

    public void setQueue(List<Song> queue) {

    }

    /**
     * 添加歌曲
     * @param song
     * @param position
     */
    public void addSong(Song song,int position) {
        queue.add(position,song);
    }
    /**
     * 添加歌曲
     * @param song
     */
    public void addSong(Song song) {
        queue.add(song);
    }

    /**
     * 添加歌曲
     * @param queue
     * @param head
     */
    public void addQueue(List<Song> queue,boolean head) {
        if (!head) {
            queue.addAll(queue);
        }else {
            queue.addAll(0,queue);
        }

    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getAblumID() {
        return ablumID;
    }

    public void setAblumID(long ablumID) {
        this.ablumID = ablumID;
    }
}
