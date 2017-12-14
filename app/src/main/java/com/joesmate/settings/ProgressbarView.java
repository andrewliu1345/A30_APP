package com.joesmate.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.R;
import com.joesmate.widget.CustomText;


public class ProgressbarView extends LinearLayout{
	public static final String TAG = "ProgressbarView";
	private ProgressBar firstProgressBar ;
	private CustomText progressbarHint ;
	Context context;
	public ProgressbarView(Context context) {
		super(context);
		this.context = context;
		if(App.APP_TYPE == 1) {
			inflate(context, R.layout.progressbar_view1, this);
		}
		if(App.APP_TYPE == 2)
		{
			inflate(context, R.layout.progressbar_view2, this);
		}
		firstProgressBar = (ProgressBar)findViewById(R.id.progressbar_down); 
		progressbarHint = (CustomText)findViewById(R.id.progressbar_hint);
	}
	public void init(){
		firstProgressBar.setProgress(0);
		if(App.APP_TYPE == 1 ) {
			progressbarHint.setText(getResources().getString(R.string.transmit_str));
		}
		if(App.APP_TYPE == 2 )
		{
			progressbarHint.setText(getResources().getString(R.string.transmit_str1));
		}
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppAction.ACTION_BROADCAST_UPDATE_PROGRESSBAR);		
		context.registerReceiver(broadcastReceiver, filter);

	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		context.unregisterReceiver(broadcastReceiver);
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(AppAction.ACTION_BROADCAST_UPDATE_PROGRESSBAR.equals(intent.getAction())){
				String curProgressMsg = intent.getStringExtra(AppAction.KEY_BROADCAST_PROGRESS);
				String[] curProgress = curProgressMsg.split("%");
				int curPercent = 0;
				try {
					curPercent = Integer.parseInt(curProgress[0]);
				} catch (NumberFormatException e) {
				    e.printStackTrace();
				}
				firstProgressBar.setProgress(curPercent);
				if(App.APP_TYPE == 1 ) {
					progressbarHint.setText(getResources().getString(R.string.transmit_str) + "     " + curProgressMsg);
				}
				if(App.APP_TYPE == 2 )
				{
					progressbarHint.setText(getResources().getString(R.string.transmit_str1) + "     " + curProgressMsg);
				}
			}
			
		}
	};

}
