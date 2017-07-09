package com.cainiao.music.cniaomusic.ui.model;

import com.cainiao.music.cniaomusic.data.Album;
import com.cainiao.music.cniaomusic.data.Song;

import java.util.List;

/**
 * 描述：TODO
 * 作者：gong.xl
 * 创建日期：2017/7/8 0008 0:35
 * 修改日期: 2017/7/8 0008
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public interface LocalIview {
    interface LocalMusic{
        /**
         * 获取本地歌曲
         * @param musics
         */
        void getLocalMusic(List<Song> musics);
    }

    interface LocalAlbum{
        void getLocalAlbum(List<Album> albums);
    }
}
