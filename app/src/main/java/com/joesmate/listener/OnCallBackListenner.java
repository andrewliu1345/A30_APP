package com.joesmate.listener;

public interface OnCallBackListenner {

	public void backData(byte[] buffer);
	public void isLegal(byte[] cmd);
}
