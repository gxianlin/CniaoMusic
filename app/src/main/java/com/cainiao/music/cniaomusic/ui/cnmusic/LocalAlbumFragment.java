package com.cainiao.music.cniaomusic.ui.cnmusic;

import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cainiao.music.cniaomusic.R;
import com.cainiao.music.cniaomusic.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


public class LocalAlbumFragment extends BaseFragment {

    @InjectView(R.id.viewpager)
    ViewPager mViewpager;

    private int index;

    private List<ImageView> mImageViews;
    private int[] images = {R.drawable.image_1, R.drawable.image_2, R.drawable.image_3, R.drawable.image_4, R
            .drawable.image_5};

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            index++;
            mViewpager.setCurrentItem(index);
            mHandler.postDelayed(mRunnable, 2000);
        }
    };
    private MyAdapter mAdapter;


    public static LocalAlbumFragment newInstance() {
        return new LocalAlbumFragment();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_local_album;
    }

    @Override
    public void initViews() {

    }

    @Override
    public void setListener() {
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void initData() {
        mImageViews = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            mImageViews.add(iv);
            iv.setImageResource(images[i]);
        }
        mAdapter = new MyAdapter();
        mViewpager.setAdapter(mAdapter);

        mHandler.removeCallbacks(mRunnable);
        mHandler.post(mRunnable);
    }

    @Override
    public void onClick(View v) {

    }

    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
//            return mImageViews == null ? 0 : mImageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mImageViews.get(position % mImageViews.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(mImageViews.get(position % mImageViews.size()));
            return mImageViews.get(position % mImageViews.size());
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(mRunnable);
    }
}
