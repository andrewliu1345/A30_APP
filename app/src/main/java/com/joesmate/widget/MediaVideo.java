package com.joesmate.widget;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.joesmate.AssitTool;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.icbc.ICBCSetVolumeData;
import com.joesmate.listener.OnPlayListener;

import java.io.IOException;

public class MediaVideo extends SurfaceView implements SurfaceHolder.Callback, OnCompletionListener,OnPreparedListener
{

	public static final String TAG = "MediaVideo";
	private SurfaceHolder surfaceHolder;
	private String url;
	MediaPlayer mediaPlayer = null;
	OnPlayListener onPlayListener;
	int playSecond;

	public MediaVideo(Context context,OnPlayListener onPlayListener) {
		super(context);
		this.onPlayListener = onPlayListener;
		surfaceHolder = getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		Log.e("VideoView", "=== VideoView ");

		SharedpreferencesData.getInstance().setCurrent_volume_type(ICBCSetVolumeData.Volume_TYPE_Advertisement);
		AssitTool.SetSystemVolume(AssitTool.GetVolumeByLevel(SharedpreferencesData.getInstance().getAdvertisement_volume()));
		Log.d("volume","set current type to Advertisement") ;
	}

	public void setDataSource(String url,int playSecond) {
		this.url = url;
		this.playSecond = playSecond;
	}

	public void setStart() {
		boolean isException = false;
		Log.d(TAG, "setStart");
		/*if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}*/
		
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                AssitTool.deleteFile(url);
                release();
                if(onPlayListener != null){
                    onPlayListener.onPlayFinish();
                }
                return true;
            }
        });
		
		mediaPlayer.setOnPreparedListener(this);
		try {
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			
		} catch (IllegalArgumentException e) {
			isException = true;
			Log.w("MediaVideo", "start error: " + e.toString());
		} catch (SecurityException e) {
			isException = true;
			Log.w("MediaVideo", "start error: " + e.toString());
		} catch (IllegalStateException e) {
			isException = true;
			Log.w("MediaVideo", "start error: " + e.toString());
		} catch (IOException e) {
			//http://www.cnblogs.com/frydsh/p/3443064.html
			isException = true;
			Log.w("MediaVideo", "start error: " + e.toString());
		}finally{
			if(isException){
				if(onPlayListener != null){
					onPlayListener.onPlayFinish();
				}
			}
		}
	}


	public void release() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.reset();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		Log.d(TAG, "surfaceCreated");
		setStart();
		mediaPlayer.setDisplay(surfaceHolder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		Log.d(TAG, "surfaceChanged");
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		release();
		Log.d(TAG, "surfaceDestroyed");
		SharedpreferencesData.getInstance().setCurrent_volume_type(ICBCSetVolumeData.Volume_TYPE_Business);
		AssitTool.SetSystemVolume(AssitTool.GetVolumeByLevel(SharedpreferencesData.getInstance().getBusiness_volume()));
		Log.d("volume","set current type to Business") ;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		release();
		if(onPlayListener != null){
			onPlayListener.onPlayFinish();
		}
	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub

        Log.d(TAG, "----video width:" + mediaPlayer.getVideoWidth() + "   ----video height: " + mediaPlayer.getVideoHeight());
		Log.d(TAG, "-----width:" + getLayoutParams().width + " -------height:" + getLayoutParams().height);
		int width = getLayoutParams().width;
		int height = getLayoutParams().height;

		int vwidth = mediaPlayer.getVideoWidth();
		int vheight =mediaPlayer.getVideoHeight();

		float wRatio = (float) vwidth/(float)width;
		float hRatio = (float) vheight/(float)height;
		float ratio = Math.max(wRatio, hRatio);

		vwidth = (int)Math.ceil((float)vwidth/ratio);
		vheight = (int)Math.ceil((float)vheight/ratio);

		//setLayoutParams( new FrameLayout.LayoutParams(vwidth,vheight));
		mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
		mediaPlayer.start();
	}

	
}