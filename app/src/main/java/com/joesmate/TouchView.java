package com.joesmate;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;

public class TouchView {
	public static final String TAG = "TouchView";
	public static final int TIME_MIN = 18*100;//1.8秒内
	public static final int SIZE = 100;
	private int x,y,widht,height;
	private String action;
	private long currentTimeMillis ;
	private int times;
	public TouchView(int x,int y,int width,int height,String action){
		this.x = x;
		this.y = y;
		this.widht = width;
	    this.height = height;
	    this.action = action;
	}
	private void launchIntent(String action){
		Intent intent = new Intent(action);
		App.getInstance().sendBroadcast(intent);
	}
	public void onTouch(MotionEvent event) {
		if(event.getAction() == MotionEvent.ACTION_UP){
			int tx = (int) event.getX();
			int ty = (int) event.getY();
			Log.d(TAG, "tx："+tx);
			Log.d(TAG, "ty："+ty);
			if(tx >= x && tx <= (x + widht) &&
					ty >= y && ty <= (y + height)){
				
				if(times == 0){
					currentTimeMillis = System.currentTimeMillis();
				}else{
					if(Math.abs(System.currentTimeMillis() - currentTimeMillis) <= TIME_MIN){
						if(times >= 10){
							times = 0;
							launchIntent(this.action);
						}
					}else{
						currentTimeMillis = System.currentTimeMillis();
						times = 0;
					}
				}
				times++;
			}
		}
	}
}
