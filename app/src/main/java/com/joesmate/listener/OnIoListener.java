package com.joesmate.listener;

public interface OnIoListener {

	public void readBuffer(byte[] buffer, int length);
	public void writeBuffer(byte[] buffer,int length);
}
