package com.joesmate.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.joesmate.listener.OnPlayListener;

public class MediaBitmap extends ImageView{

	public static final String TAG = "MediaBitmap";
	OnPlayListener onPlayListener;
	BitmapDrawable bitmapDrawable;
	boolean isfirst;
	public boolean isFinish;
	int playSecond;
	public MediaBitmap(Context context,OnPlayListener onPlayListener) {
		super(context);
		this.onPlayListener = onPlayListener;
	}

	public void setSource(String source,int playSecond){
		this.playSecond = playSecond;
		isfirst = true;
		isFinish = true;
		setBackground(new BitmapDrawable(source));
		handler.removeMessages(0);
		handler.sendEmptyMessageDelayed(0, playSecond*1000);
	}
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.d(TAG, "onDetachedFromWindow");
		handler.removeMessages(0);
		
	};
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if(isfirst){
			handler.removeMessages(0);
			handler.sendEmptyMessageDelayed(0, playSecond*1000);	
		}
		Log.d(TAG, "onAttachedToWindow");
	};
	public Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			if(onPlayListener != null && isFinish){
				onPlayListener.onPlayFinish();
			}
		}
		
	};
}
