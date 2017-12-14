package com.joesmate.audio;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.joesmate.FileInf;

import java.io.File;
import java.io.IOException;
/**
 * 
 * @author yc.zhang
 *
 */
public class AudioPlayer {
	static final String  TAG = "AudioPlayer";
	private static AudioPlayer customMediaPlayer;
	private  MediaPlayer mediaPlayer;
	private Context context;
	
	public AudioPlayer(Context context){
		this.context = context;
	}
	public static AudioPlayer getInstance(Context context){
		if(customMediaPlayer ==  null){
			customMediaPlayer = new AudioPlayer(context);
		}
		return customMediaPlayer;
	}

	public  void playDi()
	{
		try {
			if(mediaPlayer!= null){
				mediaPlayer.stop();
				mediaPlayer.release();
				mediaPlayer = null ;
			}
			mediaPlayer = new MediaPlayer();
			AssetFileDescriptor afd = context.getResources().getAssets().openFd("di.wav");
			mediaPlayer.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());

		} catch (Exception e) {
			Log.d(TAG, "MediaPlayer create :"+e.getMessage());
		}
		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			//mediaPlayer.stop();
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
	}


	/**
	 * 注意无语音255判断
	 * @param audioType 
	 */
	public boolean play(String audioType){
		if(audioType == null){
			return false;
		}
		Uri uri = getUri(audioType);
		if(uri == null){
			Log.e(TAG, "not source to play");
			return false;
		}
		Log.d(TAG, "uri:"+uri.getPath());
		try {
			mediaPlayer = MediaPlayer.create(context, uri);
		} catch (Exception e) {
			Log.d(TAG, "MediaPlayer create :"+e.getMessage());
		}
		try {
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mediaPlayer.start();
		return true;
	}
	private Uri getUri(String type){
		File dir = new File(FileInf.AUDIO);
		if(dir == null || !dir.exists()){
			Log.e(TAG, FileInf.AUDIO+" not exists");
			return null;
		}
		File[] files = dir.listFiles();
		if(files == null || files.length == 0){
			Log.e(TAG, FileInf.AUDIO+"not have file");
			return null;
		}
		for(File file : files){
			if(file == null || !file.exists()){
				Log.e(TAG, FileInf.AUDIO+"continue file");
				continue;
			}
			String filepath = file.getAbsolutePath();
			Log.d(TAG, "audio filepath："+filepath);
			if(filepath == null){
				continue;
			}
			
			if(filepath.contains(type)){
				Log.i(TAG, "play this audioFile："+filepath);
				return Uri.fromFile(file);
			}
		}
		return null;
	}

}
