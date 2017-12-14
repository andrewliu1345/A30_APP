package com.joesmate.widget;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.icbc.ICBCSetVolumeData;

import java.io.IOException;

public class MediaVideoGif extends SurfaceView implements SurfaceHolder.Callback, OnCompletionListener,OnPreparedListener
{

	public static final String TAG = "MediaVideo";
	private SurfaceHolder surfaceHolder;
	private String url;
	MediaPlayer mediaPlayer ;
	Context mcontext ;

	public MediaVideoGif(Context context) {
		super(context);
		mcontext = context ;
		surfaceHolder = getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		Log.e("VideoView", "=== VideoView ");

		SharedpreferencesData.getInstance().setCurrent_volume_type(ICBCSetVolumeData.Volume_TYPE_Advertisement);
		AssitTool.SetSystemVolume(SharedpreferencesData.getInstance().getBusiness_volume());
		Log.d("volume","set current type to Advertisement") ;
	}

	public MediaVideoGif(Context context,AttributeSet attrs) {
		super(context);
		mcontext = context ;
		surfaceHolder = getHolder();
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		surfaceHolder.addCallback(this);
		Log.e("VideoView", "=== VideoView ");

		SharedpreferencesData.getInstance().setCurrent_volume_type(ICBCSetVolumeData.Volume_TYPE_Advertisement);
		AssitTool.SetSystemVolume(SharedpreferencesData.getInstance().getBusiness_volume());
		Log.d("volume","set current type to Advertisement") ;
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

		boolean isException = false;

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnCompletionListener(this);
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

		mediaPlayer.setOnPreparedListener(this);
		try {
			if(App.APP_TYPE == 1 ) {

                AssetFileDescriptor afd = getResources().getAssets().openFd("inputpassword1.avi");
                mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                mediaPlayer.prepare();
			}

			if(App.APP_TYPE == 2 )
			{
                AssetFileDescriptor afd = getResources().getAssets().openFd("inputpassword2.avi");
                mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                mediaPlayer.prepare();
			}


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

			}
		}

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
		AssitTool.SetSystemVolume(SharedpreferencesData.getInstance().getBusiness_volume());
		Log.d("volume","set current type to Business") ;
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		//release();
		Log.d(TAG, "---------nCompletion---------");
		//setStart();
		try {
			 mediaPlayer.reset();
			if(App.APP_TYPE == 1 )
			{
                AssetFileDescriptor afd = getResources().getAssets().openFd("inputpassword1.avi");
                mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                mediaPlayer.prepare();
			}
			if(App.APP_TYPE == 2 )
			{
                AssetFileDescriptor afd = getResources().getAssets().openFd("inputpassword2.avi");
                mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
                mediaPlayer.prepare();
			}

		}catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		mediaPlayer.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
		mediaPlayer.start();


	}

	
}