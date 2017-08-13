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

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.LocalMusicViewHolder>{

    private Context context;
    private List<Song> songs;
    private OnItemClickListener<Song> mItemClickListener;

    public LocalMusicListAdapter(Context context){
        this.context = context;
        songs = new ArrayList<>();
    }

    @Override
    public LocalMusicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycler_localmusic_listitem, parent, false);
        return new LocalMusicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(LocalMusicViewHolder holder, int position) {
        final Song song = songs.get(position);
        holder.title.setText(Html.fromHtml(song.getTitle()));
        if(TextUtils.isEmpty(song.getArtistName())){
            holder.detail.setText(R.string.music_unknown);
        }else{
            holder.detail.setText(song.getArtistName());
        }
        Glide.with(context)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.cover)
                .into(holder.cover);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    /***
     * 传递歌曲列表
     * @param songs
     */
    public void setData(List<Song> songs){
        this.songs = songs;
        notifyDataSetChanged();
    }

    public class LocalMusicViewHolder extends RecyclerView.ViewHolder {

        public View musicLayout;
        public TextView title,detail;
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
                    Song song = songs.get(getAdapterPosition());
                    if(mItemClickListener != null && song.isStatus()){
                        mItemClickListener.onItemSettingClick(setting,song,getAdapterPosition());
                    }
                }
            });

            musicLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Song song = songs.get(getAdapterPosition());
                    if(mItemClickListener != null && song.isStatus()){
                        mItemClickListener.onItemClick(song,getAdapterPosition());
                    }
                }
            });

        }
    }
    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener;
    }

    public OnItemClickListener getItemClickListener(){
        return mItemClickListener;
    }
}
