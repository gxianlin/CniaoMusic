package com.cainiao.music.cniaomusic.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * 描述：服务帮助类
 * 作者：gong.xl
 * 创建日期：2017/8/12 0012 13:00
 * 修改日期: 2017/8/12 0012
 * 修改备注：
 * 邮箱：gong.xl@wonhigh.cn
 */

public class MusicServiceHelper {
    private Context mContext;

    private static MusicServiceHelper sMusicServiceHelper = new MusicServiceHelper();

    public static MusicServiceHelper get(Context context) {
        sMusicServiceHelper.mContext = context;
        return sMusicServiceHelper;
    }

    public MusicService mMusicService;


    /**
     * 服务初始化
     */
    public void initService() {
        if (mMusicService == null) {
            Intent intent = new Intent(mContext, MusicService.class);

            ServiceConnection conn = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MusicService.MyBinder binder = (MusicService.MyBinder) service;
                    mMusicService = binder.getMusicService();
                    mMusicService.setUp();
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {

                }
            };

            mContext.startService(intent);
            mContext.bindService(intent,conn,Context.BIND_AUTO_CREATE);
        }
    }

    public  MusicService getService() {

        return mMusicService;
    }
}
