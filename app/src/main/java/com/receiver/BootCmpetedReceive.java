package com.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.joesmate.page.PlayActivity;

public class BootCmpetedReceive extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		//Toast.makeText(context, "=======================", Toast.LENGTH_LONG).show();
		//DeviceSettings.deleteSystemLog();
		final Intent intentStartMain = new Intent(context, PlayActivity.class);
		String action = "android.intent.action.MAIN";
		String category = "android.intent.category.LAUNCHER";
		intentStartMain.addCategory(category);
		intentStartMain.setAction(action);
		intentStartMain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intentStartMain);
		
	}



}
