package com.cainiao.music.cniaomusic.ui.cnmusic;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.data.Song;
import com.cainiao.music.cniaomusic.music.MusicPlaylist;
import com.cainiao.music.cniaomusic.service.MusicPlayerManager;
import com.cainiao.music.cniaomusic.service.OnSongchangeListener;
import com.cainiao.music.cniaomusic.ui.adapter.OnItemClickListener;
import com.cainiao.music.cniaomusic.ui.adapter.PlayListAdapter;
import com.lb.materialdesigndialog.base.DialogBase;
import com.lb.materialdesigndialog.base.DialogWithTitle;
import com.lb.materialdesigndialog.impl.MaterialDialogNormal;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlayQueueFragment extends DialogFragment implements OnSongchangeListener {

    @InjectView(R.id.playlist_addto)
    TextView mPlaylistAddto;
    @InjectView(R.id.play_list_number)
    TextView mPlayListNumber;
    @InjectView(R.id.playlist_clear_all)
    TextView mPlaylistClearAll;
    @InjectView(R.id.play_list)
    RecyclerView mRecyclerView;

    private Context mContext;
    private LinearLayoutManager layoutManager;
    private PlayListAdapter playListAdapter;

    private MusicPlayerManager mPlayerManager;
    private Handler mHandler;
    private MusicPlaylist mPlaylist;
    private ArrayList<Song> playlist;
    private DividerItemDecoration itemDecoration;

    public PlayQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置样式
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.CustomDatePickerDialog);
        mContext = getContext();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //设置无标题
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        //设置从底部弹出
        WindowManager.LayoutParams params = getDialog().getWindow()
                .getAttributes();
        params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);

        View view = inflater.inflate(R.layout.fragment_queue, container);
        ButterKnife.inject(this, view);

        MusicPlayerManager.getInstance().registerListener(this);
        initRecyclerView(view);
        return view;
    }

    private void initRecyclerView(View view) {
        playListAdapter = new PlayListAdapter(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.play_list);
        layoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(playListAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (MusicPlayerManager.getInstance().getMusicPlaylist() != null) {
            playListAdapter.setData(MusicPlayerManager.getInstance().getMusicPlaylist().getQueue());
            Log.e("initRecyclerView: ", "initRecyclerView: " + MusicPlayerManager.getInstance().getMusicPlaylist()
                    .getQueue().size());
        }

        playListAdapter.setSongClickListener(new OnItemClickListener<Song>() {
            @Override
            public void onItemClick(Song song, int position) {
                MusicPlayerManager.getInstance().playQueueItem(position);
            }

            @Override
            public void onItemSettingClick(View v, Song song, int position) {
                //                showPopupMenu(v, song, position);
            }
        });

    }


    @Override
    public void onStart() {
        super.onStart();
        //设置fragment高度 、宽度
        int dialogHeight = (int) (mContext.getResources().getDisplayMetrics().heightPixels * 0.6);
        getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, dialogHeight);
        getDialog().setCanceledOnTouchOutside(true);

    }


    @OnClick({R.id.playlist_addto, R.id.playlist_clear_all})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.playlist_addto:

                break;
            case R.id.playlist_clear_all:
                showBasicDialog();
                break;
        }
    }


    @Override
    public void onSongChanged(Song song) {
        if (MusicPlayerManager.getInstance().getMusicPlaylist() != null) {
            playListAdapter.setData(MusicPlayerManager.getInstance().getMusicPlaylist().getQueue());
        }
    }

    @Override
    public void onPlayBackStateChanged(PlaybackStateCompat state) {

    }


    private void showBasicDialog() {
        MaterialDialogNormal materialDialogNormal = new MaterialDialogNormal(getActivity());
        materialDialogNormal.setMessage("确定要清空列表吗?");
        materialDialogNormal.setNegativeButton("取消", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                dialog.dismiss();
            }
        });
        materialDialogNormal.setPositiveButton("确定", new DialogWithTitle.OnClickListener() {
            @Override
            public void click(DialogBase dialog, View view) {
                playListAdapter.clearAll();
                MusicPlayerManager.getInstance().clearPlayer();
                dismiss();
            }
        });
        materialDialogNormal.setTitle("清空列表");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MusicPlayerManager.getInstance().unregisterListener(this);
        ButterKnife.reset(this);

    }

//    @OnClick({R.id.playlist_addto, R.id.playlist_clear_all})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.playlist_addto:
//                break;
//            case R.id.playlist_clear_all:
//                break;
//        }
//    }
}
