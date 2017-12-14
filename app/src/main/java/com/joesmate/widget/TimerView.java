package com.joesmate.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;
/**
 * 计时器
 * @author yc.zhang
 *
 */
public class TimerView extends TextView {

	public static final String TAG = "TimerView";
	public static final long DELAY_MILLIS = 1000;
	public static final int MSG_UPDATE = 0;
	public static final int MSG_TIMEOUT = 1;
	private int time;
	OnTimerListener onTimerListener;
	public TimerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		Log.d(TAG, "create");
		/*Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZSong-RKXX.TTF");
		setTypeface(typeface);*/
	}
	
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		Log.d(TAG, "onAttachedToWindow");
		runnable.run();
	}
	
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.d(TAG, "onDetachedFromWindow");
		handler.removeCallbacks(runnable);
	}
	
	public void setOnTimerListener(OnTimerListener onTimerListener){
		this.onTimerListener = onTimerListener;
	}

	public  int getCurTime()
	{
		return  time ;
	}
	public void setTime(int time){
		this.time = time;
		setText("倒计时:"+time);
	}
	Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			if(time > 0){
				time --;
				int msg = MSG_UPDATE;
				if(time == 0){
					msg = MSG_TIMEOUT;
				}
			   handler.sendEmptyMessage(msg);
				
			}
			
			handler.postDelayed(runnable, DELAY_MILLIS);
		}
	};
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			setText("倒计时:"+time);
			if(msg.what == MSG_TIMEOUT){
				if(onTimerListener != null){
					onTimerListener.timeOut();
				}
			}
		}
		
	};
	
	public static interface OnTimerListener{
		public void timeOut();
	}

}
