package com.cainiao.music.cniaomusic.common.utils;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.cainiao.music.cniaomusic.data.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * @description: 本地音乐库
 */

public class LocalMusicLibrary {

    /***
     * 获取本地所有对歌曲
     * @param context
     * @return
     */
    public static List<Song> getAllSongs(Context context) {
        ArrayList<Song> songs = new ArrayList<>();
        String selectionStatement = "is_music=1 AND title !=''";
        Cursor cursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{"_id", "title", "artist", "album", "duration", "track", "artist_id", "album_id",
                        "_data"}, selectionStatement,
                null, MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if ((cursor != null) && cursor.moveToFirst())
            do {
                Song song = getSongFromCursor(cursor);
                if (song.isStatus()) {
                    songs.add(song);
                }

            }
            while (cursor.moveToNext());
        if (cursor != null) {
            cursor.close();
        }
        return songs;
    }

    public static Song getSongFromCursor(Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE));
        String artist = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
        String album = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
        int duration = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
        int trackNumber = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TRACK));
        long artistId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST_ID));
        long albumId = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));
        String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
        Song song = new Song();
        song.setId(-id);
        song.setTitle(title);
        song.setAlbumName(album);
        song.setArtistName(artist);
        song.setDuration(duration);
        song.setTrackNumber(trackNumber);
        song.setArtistId(artistId);
        song.setAlbumId(albumId);
        String cover = getAlbumArtUri(albumId).toString();
        song.setCoverUrl(cover);
        song.setPath(url);
        song.setUrl(url);
        if (FileUtils.existFile(url)) {
            song.setStatus(true);
        }
        return song;
    }

    private static Uri getAlbumArtUri(long param) {
        return ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), param);
    }


}
