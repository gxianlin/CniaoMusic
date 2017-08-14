package com.cainiao.music.cniaomusic.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.data.Album;
import com.cainiao.music.cniaomusic.data.Artist;
import com.cainiao.music.cniaomusic.data.Song;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述：本地歌曲 recyclerview 适配器
 * 作者：gong.xl
 * 创建日期：2017/7/8 0008 0:45
 * 修改日期: 2017/7/8 0008
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.LocalMusicViewHolder> {

    private int type;
    private Context context;
    private List<Song> songs;
    private List<Artist> mArtists;
    private List<Album> mAlbumList;
    private OnItemClickListener<Song> mItemClickListener;

    public LocalMusicListAdapter(Context context) {
        this.context = context;
        songs = new ArrayList<>();
        mArtists = new ArrayList<>();
        mAlbumList = new ArrayList<>();
    }

    public LocalMusicListAdapter(Context context,int type ,List<Artist> mArtists) {
        this.context = context;
        this.type = type;
        this.mArtists=mArtists;
        songs = new ArrayList<>();
        mAlbumList = new ArrayList<>();
    }

    @Override
    public LocalMusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_localmusic_listitem, parent, false);
        return new LocalMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocalMusicViewHolder holder, int position) {
        switch (type) {
            case 0:
                setSingleSong(holder, position);
                break;
            case 1:
                setArtist(holder, position);
                break;
            case 2:
                setSpecial(holder, position);
                break;
            default:
                break;
        }
    }

    /**
     * 设置专辑页面
     *
     * @param holder
     * @param position
     */
    private void setSpecial(LocalMusicViewHolder holder, int position) {
        if (mAlbumList == null || mAlbumList.isEmpty()) {
            return;
        }
        Album album = mAlbumList.get(position);
        holder.title.setText(album.title);
        holder.detail.setText(album.songCount + "首  " + album.artistName);

        Glide.with(context)
                .load(album.cover)
                .placeholder(R.drawable.cover)
                .into(holder.cover);
    }

    /**
     * 设置歌手页面
     *
     * @param holder
     * @param position
     */
    private void setArtist(LocalMusicViewHolder holder, int position) {
        if (mArtists == null || mArtists.isEmpty()) {
            return;
        }
        Artist artist = mArtists.get(position);
        holder.title.setText(artist.name);
        holder.detail.setText(artist.songCount+"首");
        Glide.with(context)
                .load(artist.iconUrl)
                .placeholder(R.drawable.cover)
                .into(holder.cover);
    }

    /**
     * 单曲页面显示
     *
     * @param holder
     * @param position
     */
    private void setSingleSong(LocalMusicViewHolder holder, int position) {
        if (songs == null || songs.isEmpty()) {
            return;
        }

        final Song song = songs.get(position);
        holder.title.setText(Html.fromHtml(song.getTitle()));
        if (TextUtils.isEmpty(song.getArtistName())) {
            holder.detail.setText(R.string.music_unknown);
        } else {
            holder.detail.setText(song.getArtistName());
        }
        Glide.with(context)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.cover)
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        switch (type) {
            case 0:
                return songs.size();
            case 1:
                return mArtists.size();
            case 2:
                return mAlbumList.size();
            default:
                return 0;
        }

    }


    /***
     * 设置歌曲列表
     * @param songs
     */
    public void setSongs(List<Song> songs) {
        this.songs = songs;
        this.type = 0;
        notifyDataSetChanged();
    }

    /***
     * 设置歌手列标普
     * @param artists
     */
    public void setArtists(List<Artist> artists) {
        this.mArtists = artists;
        this.type = 1;
        notifyDataSetChanged();
    }

    /***
     * 设置专辑列表
     * @param alba
     */
    public void setAlbums(List<Album> alba) {
        this.mAlbumList = alba;
        this.type = 2;
        notifyDataSetChanged();
    }

    public class LocalMusicViewHolder extends RecyclerView.ViewHolder {

        public View musicLayout;
        public TextView title, detail;
        public ImageView cover;
        public AppCompatImageView setting;

        public LocalMusicViewHolder(View itemView) {
            super(itemView);
            musicLayout = itemView.findViewById(R.id.local_song_item);
            title = (TextView) itemView.findViewById(R.id.local_song_title);
            detail = (TextView) itemView.findViewById(R.id.local_song_detail);
            cover = (ImageView) itemView.findViewById(R.id.local_song_cover);
            setting = (AppCompatImageView) itemView.findViewById(R.id.local_song_setting);

            setting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mItemClickListener != null ) {
                        if (type==0) {
                            Song song = songs.get(getAdapterPosition());
                            if (song.isStatus()) {
                                mItemClickListener.onItemSettingClick(setting, song, getAdapterPosition());
                            }
                        }
                    }
                }
            });

            musicLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Song song = songs.get(getAdapterPosition());
                    if(mItemClickListener != null && song.isStatus()){
                        mItemClickListener.onItemClick(song,getAdapterPosition());
                    if (mItemClickListener != null ) {
                        if (type==0) {
                            Song song = songs.get(getAdapterPosition());
                            if (song.isStatus()) {
                                mItemClickListener.onItemSettingClick(setting, song, getAdapterPosition());
                            }
                        }
                    }
                }
            });

        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    public OnItemClickListener getItemClickListener() {
        return mItemClickListener;
    }
}
