package com.joesmate.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.joesmate.R;
import com.joesmate.SharedpreferencesData;
import com.joesmate.listener.OnPlayListener;
import com.joesmate.util.LogMg;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bill on 2016/1/13.
 */
public class MediaImages extends LinearLayout implements ViewPager.OnPageChangeListener {

    final static String TAG = MediaImages.class.getName();
    /**
     * ViewPager
     */
    private ViewPager viewPager;

    /**
     * 单张图片显示
     */
    private ImageView adImageView;


    private RelativeLayout dotGroup;

    /**
     * 装点点的ImageView数组
     */
    private ImageView[] tips;

    /**
     * 装ImageView数组
     */
    private ImageView[] mImageViews;

    /**
     * 图片资源id
     */


    ArrayList<File> fileImages;

    //ArrayList<Bitmap> bitmapArrayList ;

    ViewGroup group;

    private int[] imgIdArray;

    private Context mcontext;

    int playSecond;

    int currentIndex;


    OnPlayListener onPlayListener;

    public MediaImages(Context context, OnPlayListener onPlayListener) {
        super(context);
        mcontext = context;
        this.onPlayListener = onPlayListener;
        inflate(context, R.layout.mediaimages, this);
        onFinishInflate();

    }

    public MediaImages(Context context, OnPlayListener onPlayListener, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        this.onPlayListener = onPlayListener;
        inflate(context, R.layout.mediaimages, this);
        onFinishInflate();

    }


    public void setSource(ArrayList<File> files, int second) {
        if (files.size() == 2) {
            files.add(files.get(0));
            files.add(files.get(1));
        }

        if (files.size() > 1) {

            //大于等于2张图片

            adImageView.setVisibility(GONE);
            viewPager.setVisibility(VISIBLE);
            dotGroup.setVisibility(VISIBLE);

            //System.gc();
            fileImages = files;
            playSecond = second;
            //将点点加入到ViewGroup中
            this.removeAllViews();
            inflate(mcontext, R.layout.mediaimages, this);
            group = (ViewGroup) findViewById(R.id.viewGroup);
            viewPager = (ViewPager) findViewById(R.id.viewPager);
            tips = new ImageView[files.size()];
            for (int i = 0; i < tips.length; i++) {
                ImageView imageView = new ImageView(mcontext);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10, 10);
                params.setMargins(20, 20, 20, 20);
                imageView.setLayoutParams(params);
                tips[i] = imageView;
                if (i == 0) {
                    tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
                } else {
                    tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
                group.addView(imageView);
            }


            //bitmapArrayList = new ArrayList<Bitmap>(files.size());
            //将图片装载到数组中
            mImageViews = new ImageView[files.size()];
            for (int i = 0; i < mImageViews.length; i++) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inSampleSize = 1;
                Bitmap bmp = BitmapFactory.decodeFile(fileImages.get(i).getAbsolutePath(), options);
                //bitmapArrayList.add(bmp);
                ImageView imageView = new ImageView(mcontext);
                mImageViews[i] = imageView;
                imageView.setBackground((new BitmapDrawable(bmp)));
                //imageView.setImageBitmap(bmp);
                //imageView.setBackground()
                //imageView.setBackgroundResource(imgIdArray[i]);
            }

            //设置Adapter
            viewPager.setAdapter(new MyAdapter());
            //设置监听，主要是设置点点的背景
            viewPager.setOnPageChangeListener(this);
            //设置ViewPager的默认项, 设置为长度的100倍，这样子开始就能往左滑动
            //viewPager.setCurrentItem((mImageViews.length) * 100);
//            int n = Integer.MAX_VALUE / 2 % files.size();
//            int itemPosition = Integer.MAX_VALUE / 2 - n;
//            viewPager.setCurrentItem(itemPosition);

            handler.removeMessages(0);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int count = fileImages.size();
                    currentIndex = viewPager.getCurrentItem();
                    Log.e("bill", "------index:" + currentIndex);
                    viewPager.setCurrentItem(currentIndex + 1);
                    handler.postDelayed(this, playSecond * 1000);
                    if (SharedpreferencesData.getInstance().getPoster_type() == 2) {
                        LogMg.d(TAG, "getPoster_type :%d", SharedpreferencesData.getInstance().getPoster_type());
                        LogMg.d(TAG, "currentIndex :%d", currentIndex);
                        LogMg.d(TAG, "mImageViews.length :%d", mImageViews.length);
                        if (currentIndex == mImageViews.length - 1) {
                            handler1.sendEmptyMessage(0);
                        }
                    }
                }
            }, playSecond * 1000);

//            handler1.removeMessages(0);
//            handler1.sendEmptyMessageDelayed(0, 5 * 60 * 1000);
        } else {
            //只有一张图片
            adImageView.setVisibility(VISIBLE);
            viewPager.setVisibility(GONE);
            dotGroup.setVisibility(GONE);
            if (files.size() == 1) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.RGB_565;
                options.inPurgeable = true;
                options.inInputShareable = true;
                options.inSampleSize = 1;
                Bitmap bmp = BitmapFactory.decodeFile(files.get(0).getAbsolutePath(), options);
                adImageView.setBackground((new BitmapDrawable(bmp)));
            }
        }

    }

    public Handler handler = new Handler();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        group = (ViewGroup) findViewById(R.id.viewGroup);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        adImageView = (ImageView) findViewById(R.id.adImageView);
        dotGroup = (RelativeLayout) findViewById(R.id.dotgroup);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        //play();
    }

    @Override
    protected void onDetachedFromWindow() {
   /*     if(mImageViews != null) {
            for (int i = 0; i < mImageViews.length; ++i) {

            }
        }*/
        super.onDetachedFromWindow();
  /*      for(int i = 0 ; i < bitmapArrayList.size() ;++ i)
        {
            if(bitmapArrayList.get(i).isRecycled() == false)
            {
                bitmapArrayList.get(i).recycle();
            }
        }*/
        //recycleBitmap();
        //System.gc();

        handler.removeMessages(0);
        handler1.removeMessages(0);
    }


    private void recycleBitmap() {
        for (int i = 0; i < mImageViews.length; ++i) {
            Drawable drawable = mImageViews[i].getDrawable();
            if (drawable != null) {
                if (drawable instanceof BitmapDrawable) {
                    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                    Bitmap bitmap = bitmapDrawable.getBitmap();
                    if (bitmap != null)
                        bitmap.recycle();
                    bitmap = null;
                }
            }
        }

    }


    public Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Log.e("bill", "------check tick is coming  ");
            if (onPlayListener != null) {
                onPlayListener.onPlayFinish();
            }
        }

    };


    /**
     * @author xiaanming
     */
    public class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            // ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);

        }

        /**
         * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
         */
        @Override
        public Object instantiateItem(View container, int position) {
            try {
                ((ViewPager) container).addView(mImageViews[position % mImageViews.length], 0);
            } catch (Exception e) {
                //handler something
            }
            return mImageViews[position % mImageViews.length];
        }


    }


    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int arg0) {
        setImageBackground(arg0 % mImageViews.length);
    }

    /**
     * 设置选中的tip的背景
     *
     * @param selectItems
     */
    private void setImageBackground(int selectItems) {
        for (int i = 0; i < tips.length; i++) {
            if (i == selectItems) {
                tips[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                tips[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
        }
    }
}
