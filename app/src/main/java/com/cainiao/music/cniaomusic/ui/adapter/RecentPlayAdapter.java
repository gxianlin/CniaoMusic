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

/**
 * 描述：TODO
 * 作者：gong.xl
 * 创建日期：2017/8/19 0019 10:08
 * 修改日期: 2017/8/19 0019
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class RecentPlayAdapter extends RecyclerView.Adapter<RecentPlayAdapter.RecentViewHolder> {
    private Context mContext;
    private ArrayList<Song> mSongs;
    private OnItemClickListener songClickListener;

    public RecentPlayAdapter(Context context) {
        mContext = context;
    }

    /**
     * 设置歌曲数据
     *
     * @param songs
     */
    public void setData(ArrayList<Song> songs) {
        mSongs = songs;
        notifyDataSetChanged();
    }

    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(mContext).inflate(R.layout.recycler_recently_listitem, parent, false);
        return new RecentViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(final RecentViewHolder holder, int position) {
        final Song song = mSongs.get(position);
        holder.title.setText(Html.fromHtml(song.getTitle()));
        if (TextUtils.isEmpty(song.getArtistName())) {
            holder.datail.setText(R.string.music_unknown);
        } else {
            holder.datail.setText(song.getArtistName());
        }

        Glide.with(mContext)
                .load(song.getCoverUrl())
                .placeholder(R.drawable.cover)
                .into(holder.cover);

        holder.setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songClickListener != null && song.isStatus()) {
                    songClickListener.onItemSettingClick(holder.setting, song, holder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs == null ? 0 : mSongs.size();
    }

    public OnItemClickListener getSongClickListener() {
        return songClickListener;
    }

    public void setSongClickListener(OnItemClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }


    public class RecentViewHolder extends RecyclerView.ViewHolder {

        public View songItem;
        public TextView title;
        public TextView datail;
        public ImageView cover;
        public AppCompatImageView setting;

        public RecentViewHolder(View itemView) {
            super(itemView);
            songItem = itemView.findViewById(R.id.song_item);
            title = (TextView) itemView.findViewById(R.id.song_title);
            datail = (TextView) itemView.findViewById(R.id.song_detail);
            cover = (ImageView) itemView.findViewById(R.id.song_cover);
            setting = (AppCompatImageView) itemView.findViewById(R.id.song_setting);
        }
    }
}
