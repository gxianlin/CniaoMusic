package com.cainiao.music.cniaomusic.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.service.MusicPlayerManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import magicasakura.widgets.TintImageView;

/**
 * @description: 播放列表适配器
 */
public class PlayListAdapter extends RecyclerView.Adapter<PlayListAdapter.PlayListHolder> implements
        ItemTouchHelperAdapter {

    private Context context;
    private List<Song> songs;
    private OnItemClickListener songClickListener;
    private int currentlyPlayingPosition;

    public PlayListAdapter(Context context) {
        this.context = context;
        songs = new ArrayList<>();
    }

    public void setData(List<Song> songs) {
        this.songs = songs;
        notifyDataSetChanged();
    }

    public void clearAll() {
        int itemCount = getItemCount();
        songs.clear();
        notifyItemRangeRemoved(0, itemCount);
    }

    @Override
    public PlayListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_playqueue_item, parent, false);
        return new PlayListHolder(view);
    }

    @Override
    public void onBindViewHolder(final PlayListHolder holder, int position) {
        final Song song = songs.get(position);
        holder.MusicName.setText(Html.fromHtml(song.getTitle()));
        Log.e("onBindViewHolder: ", "onBindViewHolder: " + Html.fromHtml(song.getTitle()));
        if (TextUtils.isEmpty(song.getArtistName())) {
            holder.Artist.setVisibility(View.GONE);
        } else {
            holder.Artist.setVisibility(View.VISIBLE);
            holder.Artist.setText(song.getArtistName());
        }
        if (MusicPlayerManager.getInstance().getPlayingSong() != null && song.getId() == MusicPlayerManager
                .getInstance().getPlayingSong().getId()) {
            holder.MusicName.setTextColor(context.getResources().getColor(R.color.theme_color_primary));
        } else {
            holder.MusicName.setTextColor(context.getResources().getColor(R.color.black_normal));
        }
        if (MusicPlayerManager.getInstance().getPlayingSong().getId() == song.getId()) {
            holder.playstate.setVisibility(View.VISIBLE);
            holder.playstate.setImageResource(R.drawable.song_play_icon);
            holder.playstate.setImageTintList(R.color.theme_color_primary);
            currentlyPlayingPosition = position;
        } else {
            holder.playstate.setVisibility(View.GONE);
        }
        holder.musiclayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songClickListener != null && song.isStatus()) {
                    songClickListener.onItemClick(song, holder.getAdapterPosition());
                }
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (songClickListener != null && song.isStatus()) {
                    songClickListener.onItemSettingClick(holder.delete, song, holder.getAdapterPosition());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public OnItemClickListener getSongClickListener() {
        return songClickListener;
    }

    public void setSongClickListener(OnItemClickListener songClickListener) {
        this.songClickListener = songClickListener;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(songs, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        songs.remove(position);
        notifyItemRemoved(position);
    }

    public class PlayListHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {
        ImageView delete;
        TextView MusicName, Artist;
        TintImageView playstate;
        RelativeLayout musiclayout;

        public PlayListHolder(View itemView) {
            super(itemView);
            this.playstate = (TintImageView) itemView.findViewById(R.id.play_state);
            this.delete = (ImageView) itemView.findViewById(R.id.play_list_delete);
            this.MusicName = (TextView) itemView.findViewById(R.id.play_list_musicname);
            this.Artist = (TextView) itemView.findViewById(R.id.play_list_artist);
            this.musiclayout = (RelativeLayout) itemView.findViewById(R.id.musiclayout);
            this.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });


        }

        @Override
        public void onItemSelected() {
        }

        @Override
        public void onItemClear() {
        }
    }
}
