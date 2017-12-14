package com.joesmate;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.joesmate.fit.FitManager;
import com.joesmate.fit.FitManager.FrameType;
import com.joesmate.fit.FitManager.IoType;
import com.joesmate.io.adb.CommunicationManager;
import com.nantian.tts.TTS;

/**
 * 中国建行智能信息交互终端
 * <p>
 * 密码显示
 * <p>
 * 交易信息确认
 * <p>
 * 交易图片/PDF信息确认
 * <p>
 * 客户评价
 * <p>
 * 柜员评价
 * <p>
 * 数字输入
 * <p>
 * 电子签名
 * <p>
 * 广告播放
 * 
 * @author yc.zhang
 * 
 */
public class App extends Application {

	public  SharedPreferences preferences;//当返回的时候保存在文件
	private static final String ATTENDANCEREMIND = "Joesmate_device_info";//文件名称
	public static final String TAG = "App";
	public static final int SOCKE_PORT = 9100;
	public static int SCREEN_WIDTH = 1280;
	public static int SCREEN_HEIGHT = 800;
	public static int APP_TYPE = 1 ;  //(1  app1   2  app2 )
	
	public CommunicationManager communicationManager;
	public static FitManager fitManagerTeller;
	public static FitManager fitManagerCCB;
	public static FitManager fitManagerSerial;
	private static App app;
	public  static TTS tts;
	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "<====>onCreate<====>");
		app = this;
		preferences=getSharedPreferences(ATTENDANCEREMIND,MODE_PRIVATE);
		/*if(fitManagerTeller == null){
			fitManagerTeller = new FitManager(IoType.SERIAL, FrameType.TELLER);
		}*/
		if(fitManagerSerial == null){
			fitManagerSerial = new FitManager(IoType.SERIAL, FrameType.CCB);
		}
		if(fitManagerCCB == null){
			fitManagerCCB = new FitManager(IoType.HID, FrameType.CCB);
		}
		tts = new TTS(getApplicationContext());
	}
	Handler handlerLog = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			DeviceSettings.createSystemLog();
		}
		
	};
	
	public static App getInstance(){
		return app;
	}
	public void initScreenSize(){
		WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		DisplayMetrics displayMetrics = new DisplayMetrics();
		display.getMetrics(displayMetrics);
		
		SCREEN_WIDTH = displayMetrics.widthPixels;
		SCREEN_HEIGHT = displayMetrics.heightPixels+48;
		
		float density = displayMetrics.density;
		
		Log.d(TAG, "<====>initScreenSize<====>");
		Log.d(TAG, "SCREEN_WIDTH:"+SCREEN_WIDTH);
		Log.d(TAG, "SCREEN_HEIGHT:"+SCREEN_HEIGHT);
		Log.d(TAG, "density:"+density);
		
	}
	
	public void setNavigation(boolean isVisible){
		Intent intent = new Intent("rk.android.navigation.SHOW");
		intent.putExtra("show", isVisible);
		sendBroadcast(intent);
	}

	public static boolean isThirdpartySerial =false;



}
