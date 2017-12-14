package com.joesmate;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.media.AudioManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

public class DeviceSettings {

	public static final String TAG = "DeviceSettings";

	public static void setVolume(int streamType,int value) {
		AudioManager audioManager = (AudioManager) App.getInstance()
				.getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		int tem = (int) Math.rint(value / 100.0 * 15);
		audioManager.setStreamVolume(streamType, tem, AudioManager.FLAG_PLAY_SOUND);
	}
	
	public static int getVolume(int streamType){
		AudioManager audioManager = (AudioManager) App.getInstance()
				.getApplicationContext()
				.getSystemService(Context.AUDIO_SERVICE);
		return (int) Math.rint(audioManager.getStreamVolume(streamType) / 15.0 * 100);
	}
	
	/**
	 * 获取系统当前亮度
	 * 
	 * @return 系统亮度是0-255,转化成0-100
	 */
	public static int getSystemBrightness() {
		int screenBrightness = 255;
		try {
			screenBrightness = Settings.System.getInt(App.getInstance()
					.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
			screenBrightness = (int) Math.rint(screenBrightness / 255.0 * 100);
		} catch (Exception localException) {
			Log.d(TAG, "getSystemBrightness Exception");
		}
		return screenBrightness;
	}

	/**
	 * 保存系统亮度
	 * 
	 * @param brightness
	 *            0 - 100
	 */
	public static void saveSystemBrightness(int brightness) {
		// 将百分比转化成0-255范围
		brightness = (int) Math.rint(brightness / 100.0 * 255);
		try {
			Settings.System.putInt(App.getInstance().getContentResolver(),
					Settings.System.SCREEN_BRIGHTNESS, brightness);
		} catch (Exception localException) {
			localException.printStackTrace();
		}
	}

	/**
	 * 设置APP的亮度,对系统亮度无效
	 * 
	 * @param brightness
	 *            0-100
	 */
	public static void setAppWindowBrightness(Context context,float brightness) {
		// App屏幕亮度是0-1
		brightness = brightness / 100.0F;
		Window localWindow = ((Activity) context).getWindow();
		WindowManager.LayoutParams localLayoutParams = localWindow
				.getAttributes();
		localLayoutParams.screenBrightness = brightness;
		localWindow.setAttributes(localLayoutParams);
	}
	public static void reboot() {
		Log.d(TAG, "reboot");
/*		PowerManager manager = (PowerManager) App.getInstance().getSystemService(Context.POWER_SERVICE);
		manager.reboot("reboot");*/
		/*try {
			execRootCommand("reboot");
		}
		catch (Exception e)
		{
			Log.d(TAG,e.getMessage());
		}*/
		String cmd = "su -c reboot";
		try {
		          Runtime.getRuntime().exec(cmd);
		  } catch (IOException e) {
		         // TODO Auto-generated catch block
		         Log.d(TAG,"reboot fail");
		  }		
	}
	
	public static Process execRootCommand(String command) throws IOException {
		if (command == null) {
			Log.w(TAG, "command is null");
			return null;
		}

		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec("su");

		OutputStream os = proc.getOutputStream();
		String cmd = command + "\n";
		os.write(cmd.getBytes());
		os.flush();

		return proc;
	}
	public static void deleteSystemLog(){
		File dir = new File(FileInf.SYSTEM_LOG);
		if(!dir.exists()){
			return;
		}
		File file = new File(dir, FileInf.LOG_NAME);
		if(!file.exists()){
			return;
		}else{
			file.delete();
		}
		
	}
	
	public static void createSystemLog(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				File dir = new File(FileInf.SYSTEM_LOG);
				if(!dir.exists()){
					dir.mkdirs();
				}
				File file = new File(dir, FileInf.LOG_NAME);
				try {
					execRootCommand("adb logcat -f "+file.getAbsolutePath());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static  String getVersionName(Context context){
		String versionName = null;
		PackageManager manager = context.getPackageManager();
		try {
			PackageInfo packageInfo = manager.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			versionName = null;
		}
		return versionName;
	}
}
