package com.joesmate.pageItem;

import java.io.File;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.FileInf;
import com.joesmate.FileInf.FileType;
import com.joesmate.SharedpreferencesData;
import com.joesmate.listener.OnPlayListener;
import com.joesmate.page.PlayActivity.OnResUpdateListener;
import com.joesmate.widget.MediaBitmap;
import com.joesmate.widget.MediaVideo;

/**
 * 播放(视频与图片)
 * 
 * @author Administrator
 * 
 */
public class PlayView extends FrameLayout implements OnPlayListener {

	public static final String TAG = "PlayView";

	MediaBitmap mediaBitmap;
	MediaVideo mediaVideo;
	File[] fileUrls;
	int curIndex;
	boolean isStartPlay;
	OnResUpdateListener onResUpdateListener;
	public PlayView(Context context,OnResUpdateListener onResUpdateListener) {
		super(context);
		Log.d(TAG, "<====>onCreate<====>");
		this.onResUpdateListener = onResUpdateListener;
		mediaBitmap = new MediaBitmap(context,this);
		mediaBitmap.setVisibility(View.INVISIBLE);
		addView(mediaBitmap,getLayoutParams());

		mediaVideo = new MediaVideo(context, this);
		mediaVideo.setVisibility(View.INVISIBLE);
		addView(mediaVideo, getLayoutParams());
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
	
	public ViewGroup.LayoutParams getLayoutParams(){
		return AssitTool.getLinearParams(App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
	}
	
	public boolean optionRes(int resName,int playSecond){
		String name = ""+resName;
		if(!AssitTool.isPlay()){
			Log.w(TAG, "res nothing");
			if(onResUpdateListener != null){
				onResUpdateListener.nothing();
			}
			return false;
		}
		fileUrls = AssitTool.getFileUrls(FileInf.RES);
		for(int i=0; i < fileUrls.length; i ++){
			if(fileUrls[i].getName().contains(name)){
				curIndex = i;
				mediaVideo.setVisibility(View.INVISIBLE);
				mediaBitmap.setVisibility(View.INVISIBLE);
				mediaBitmap.isFinish = false;
				play(playSecond);
				return true;
			}
		}
		return false;
		
	}


	public void play(int playSecond) {
		if(!AssitTool.isPlay()){
			Log.w(TAG, "res nothing");
			if(onResUpdateListener != null){
				onResUpdateListener.nothing();
			}
			return;
		}
		//更新播放资源列表
		fileUrls = AssitTool.getFileUrls(FileInf.RES);
		if (curIndex >= fileUrls.length) {
			curIndex = 0;
		}
		File curFile = fileUrls[curIndex];
		if(curFile == null || !curFile.exists()){
			if(curFile != null){
				Log.w(TAG, "curfile:" + curFile.getAbsolutePath()+"--- UnExists!");
			}else{
				Log.w(TAG, "curfile is null");
			}
			onPlayFinish();
		}else{
			
			FileType fileType = AssitTool.fileType(curFile.getAbsolutePath());
			
			if (fileType == FileType.VIDEO) {
				mediaVideo.setDataSource(curFile.getAbsolutePath(),playSecond);
				mediaVideo.setVisibility(View.VISIBLE);
				//mediaVideo.setStart();
			} else if (fileType == FileType.IMG){
				mediaBitmap.setSource(curFile.getAbsolutePath(),playSecond);
				mediaBitmap.setVisibility(View.VISIBLE);
			}else{
				onPlayFinish();
			}
		}
	}


	@Override
	public void onPlayFinish() {
	
		mediaVideo.setVisibility(View.INVISIBLE);
		mediaBitmap.setVisibility(View.INVISIBLE);
		curIndex++;
		if (curIndex >= fileUrls.length) {
			curIndex = 0;
		}
		play(SharedpreferencesData.getInstance().getShowTime());
	}
}

