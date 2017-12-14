package com.joesmate.upgrade;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.joesmate.AppAction;
import com.joesmate.FileInf;

public class PeripheralReceiver extends BroadcastReceiver {

	public static final String TAG = "PeripheralReceiver";

	
	@Override
	public void onReceive(Context context, Intent intent) {

		final String action = intent.getAction();
		final String receiverPath = intent.getData().getPath();

		Log.d(TAG, "action:" + action);
		Log.d(TAG, "receiverPath:" + receiverPath);
		if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
			if(Tool.isExistsFile(receiverPath, "res")){
				startCopy(context,receiverPath);
			}else if(Tool.isExistsFile(receiverPath, FileInf.APK_NAME)){
				Tool.installApk(receiverPath+"/"+FileInf.APK_NAME);
			}
		} else if (Intent.ACTION_MEDIA_EJECT.equals(action)) {
			context.sendBroadcast(new Intent(AppAction.ACTION_CLOSE_DIALOG));
		}
	}
	
	private void startCopy(Context context,String src){
		Intent intent = new Intent(context, DialogActivity.class);
		intent.putExtra(AppAction.KEY_SRC, src);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}


}
