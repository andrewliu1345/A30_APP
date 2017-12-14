package com.joesmate.io;

import com.joesmate.listener.OnIoListener;

public abstract class BaseIO {

	public static boolean isRunRead;
	public static boolean isRunWrite;
	public boolean isWrite;
	public byte[] readBuf = new byte[1024]; //串口或HID最大传输包 500bit
	public byte[] writeBuf;
	public OnIoListener onIoListener;
	public static final float HID_MAX = 64.0f;
	public BaseIO(OnIoListener onIoListener){
		this.onIoListener = onIoListener;
	}
	public void setOnIoListener(OnIoListener onIoListener){
		this.onIoListener = onIoListener;
	}
	public abstract void write(byte[] buffer,int length);
}
