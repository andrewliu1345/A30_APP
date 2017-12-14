package com.joesmate.pageItem;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.joesmate.AssitTool;
import com.joesmate.FileInf;
import com.joesmate.FileInf.FileType;
import com.joesmate.SharedpreferencesData;
import com.joesmate.listener.OnPlayListener;
import com.joesmate.page.PlayActivity.OnResUpdateListener;
import com.joesmate.widget.MediaImages;
import com.joesmate.widget.MediaVideo;

import java.io.File;
import java.util.ArrayList;

/**
 * ICBC 播放(视频与图片)
 *
 * @author Administrator
 */
public class ICBCPlayView extends FrameLayout implements OnPlayListener {

    public static final String TAG = "ICBCPlayView";

    //MediaBitmap mediaBitmap;
    MediaVideo mediaVideo;
    MediaImages mediaImages;
    ArrayList<File> fileUrls_IMG;
    ArrayList<File> fileUrls_VIDEO;
    File[] fileUrls;
    int curIndex_img;
    int curIndex_video;
    int curIndex;
    boolean isStartPlay;
    OnResUpdateListener onResUpdateListener;
    Context m_context;

    ViewGroup.LayoutParams mparams;

    public ICBCPlayView(Context context, OnResUpdateListener onResUpdateListener) {
        super(context);
        Log.d(TAG, "<====>onCreate<====>");
        setBackgroundColor(Color.BLACK);
        this.onResUpdateListener = onResUpdateListener;
        m_context = context;
        /*mediaBitmap = new MediaBitmap(context,this);
        mediaBitmap.setVisibility(View.INVISIBLE);
		addView(mediaBitmap, getLayoutParams());*/


    }

    public ICBCPlayView(Context context, OnResUpdateListener onResUpdateListener, ViewGroup.LayoutParams params) {
        super(context);
        Log.d(TAG, "<====>onCreate<====>");
        this.onResUpdateListener = onResUpdateListener;
        m_context = context;
        mparams = params;
        //mediaBitmap = new MediaBitmap(context,this);
        //mediaBitmap.setVisibility(View.INVISIBLE);
        //addView(mediaBitmap, params);

/*		mediaVideo = new MediaVideo(context, this);
        mediaVideo.setVisibility(View.INVISIBLE);
		addView(mediaVideo, params);

		mediaImages = new MediaImages(context,this);
		mediaImages.setVisibility(VISIBLE);
		addView(mediaImages, params);*/
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isStartPlay = true;
        //play();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        isStartPlay = false;
    }


    public boolean optionRes(int resName, int playSecond) {
        String name = "" + resName;
        if (!AssitTool.isPlay()) {
            Log.w(TAG, "res nothing");
            if (onResUpdateListener != null) {
                onResUpdateListener.nothing();
            }
            return false;
        }
        fileUrls = AssitTool.getFileUrls(FileInf.RES);
        for (int i = 0; i < fileUrls.length; i++) {
            if (fileUrls[i].getName().contains(name)) {
                curIndex = i;
                mediaVideo.setVisibility(View.INVISIBLE);
                //mediaBitmap.setVisibility(View.INVISIBLE);
                //mediaBitmap.isFinish = false;
                play(playSecond);
                return true;
            }
        }
        return false;

    }

    private int m_PlaySecond = 10;

    public void play(int playSecond) {
        m_PlaySecond = playSecond;
	/*	if(!AssitTool.isPlay()){
			Log.w(TAG, "res nothing");
			if(onResUpdateListener != null){
				onResUpdateListener.nothing();
			}
			return;
		}*/
        //更新播放资源列表


        //fileUrls = AssitTool.getFileUrls(FileInf.RES);

        //图片广告
        if (SharedpreferencesData.getInstance().getPoster_type() == 0) {

            if (!AssitTool.isImgCanPlay()) {
                Log.w(TAG, "res img  nothing");
                if (onResUpdateListener != null) {
                    onResUpdateListener.nothing();
                }
                return;
            }
            //更新播放资源列表
            //fileUrls_IMG = AssitTool.getFileUrls_IMG(FileInf.RES);

			/*if (curIndex_img >= fileUrls_IMG.size()) {
				curIndex_img = 0;
			}
			File curFile = fileUrls_IMG.get(curIndex_img);
			if(curFile == null || !curFile.exists()){
				if(curFile != null){
					Log.w(TAG, "curfile:" + curFile.getAbsolutePath()+"--- UnExists!");
				}else{
					Log.w(TAG, "curfile is null");
				}
				onPlayFinish();
			}else{
				FileType fileType = AssitTool.fileType(curFile.getAbsolutePath());
			    if (fileType == FileType.IMG){
					mediaBitmap.setSource(curFile.getAbsolutePath(),playSecond);
					mediaBitmap.setVisibility(View.VISIBLE);
				}
			}*/


            mediaImages = new MediaImages(m_context, this);
            mediaImages.setVisibility(VISIBLE);
            addView(mediaImages, mparams);

            mediaImages.setSource(AssitTool.getFileUrls_IMG(FileInf.IMAGE), playSecond);
            mediaImages.setVisibility(View.VISIBLE);

        }

        // 视频广告
        if (SharedpreferencesData.getInstance().getPoster_type() == 1) {
            if (!AssitTool.isVideoCanPlay()) {
                Log.w(TAG, "res video  nothing");
                if (onResUpdateListener != null) {
                    onResUpdateListener.nothing();
                }
                return;
            }
            //更新播放资源列表
            fileUrls_VIDEO = AssitTool.getFileUrls_VIDEO(FileInf.VIDEO);

            if (curIndex_video >= fileUrls_VIDEO.size()) {
                curIndex_video = 0;
            }
            File curFile = fileUrls_VIDEO.get(curIndex_video);
            if (curFile == null || !curFile.exists()) {
                if (curFile != null) {
                    Log.w(TAG, "curfile:" + curFile.getAbsolutePath() + "--- UnExists!");
                } else {
                    Log.w(TAG, "curfile is null");
                }
                onPlayFinish();
            } else {

                FileType fileType = AssitTool.fileType(curFile.getAbsolutePath());
                if (fileType == FileType.VIDEO) {
                    mediaVideo = new MediaVideo(m_context, this);
                    mediaVideo.setVisibility(View.INVISIBLE);
                    addView(mediaVideo, mparams);

                    mediaVideo.setDataSource(curFile.getAbsolutePath(), playSecond);
                    mediaVideo.setVisibility(View.VISIBLE);
                    //mediaVideo.setStart();
                } else {
                    onPlayFinish();
                }
            }
        }
        if (SharedpreferencesData.getInstance().getPoster_type() == 2) {

        }

    }


    @Override
    public void onPlayFinish() {
        Log.d(TAG,"onPlayFinish");
        if (mediaVideo != null) {
            mediaVideo.setVisibility(View.INVISIBLE);
        }
        //mediaBitmap.setVisibility(View.INVISIBLE);
        if (mediaImages != null) {
            mediaImages.setVisibility(INVISIBLE);
        }
        this.removeAllViews();
        if (SharedpreferencesData.getInstance().getPoster_type() == 0) {
/*			curIndex_img++;
			if (curIndex_img >= fileUrls_IMG.size()) {
				curIndex_img = 0;
			}*/
        }
        if (SharedpreferencesData.getInstance().getPoster_type() == 1) {
            curIndex_video++;
            Log.d(TAG, "---------------curIndex_video:" + curIndex_video);
            if (curIndex_video >= fileUrls_VIDEO.size()) {
                curIndex_video = 0;
            }
        }
        if (SharedpreferencesData.getInstance().getPoster_type() == 2) {

        }

        play(SharedpreferencesData.getInstance().getShowTime() * 1000);

    }
}

