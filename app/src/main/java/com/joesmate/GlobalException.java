package com.joesmate;

import java.lang.Thread.UncaughtExceptionHandler;

import android.os.Handler;
import android.os.Message;

public class GlobalException implements UncaughtExceptionHandler{
	private static GlobalException exception;
	public static GlobalException getException(){
		if(exception == null){
			exception = new GlobalException();
		}
		return exception;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handler.sendEmptyMessage(0);
		
	}
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
		}
		
	};

}
