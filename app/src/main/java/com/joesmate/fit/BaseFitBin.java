package com.joesmate.fit;

import android.content.Intent;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.listener.OnCallBackListenner;
import com.joesmate.listener.OnIoListener;

public abstract class BaseFitBin implements OnCallBackListenner{

	private OnIoListener onIoListener;
	public BaseFitBin(OnIoListener onIoListener){
		this.onIoListener = onIoListener;
	}
	
	public abstract void setData(byte[] buffer, int length);
	
	public void setOnIoListenner(OnIoListener onIoListener){
		this.onIoListener = onIoListener;
	}
	public void displayPage(byte[] cmd) {
		Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
		intent.putExtra(AppAction.KEY_BROADCAST_CMD, cmd);
		App.getInstance().sendBroadcast(intent);
	}

	@Override
	public void backData(byte[] buffer) {
		if(onIoListener != null){
			onIoListener.writeBuffer(buffer, buffer.length);
		}
	}

	@Override
	public void isLegal(byte[] cmd) {
		displayPage(cmd);
	}
	
}
