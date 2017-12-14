package com.joesmate.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.Cmds;
import com.joesmate.R;
import com.joesmate.bin.keyBoard.SettingsM3Data;
import com.joesmate.widget.OptionItemLyout;
import com.joesmate.widget.SettingsItemScroll;
import com.joesmate.widget.WeightItem;

public class KeyBoradSettingsView extends LinearLayout{

	public static final String TAG = "KeyBoradSettingsView";
	int[] itemsId = {R.id.keyboard_settings_item_1,R.id.keyboard_settings_item_2,R.id.keyboard_settings_item_3,
			R.id.keyboard_settings_item_4,R.id.keyboard_settings_item_5,R.id.keyboard_settings_item_6};
	int[] optionArrayId ={R.array.keyboard_settings_options1,R.array.keyboard_settings_options2,
			R.array.keyboard_settings_options3,R.array.keyboard_settings_options4,
			R.array.keyboard_settings_options5,R.array.keyboard_settings_options6};
 	SettingsItemScroll[] itemScrolls = new SettingsItemScroll[itemsId.length];
	OptionItemLyout[] itemLyouts = new OptionItemLyout[itemsId.length];
	WeightItem[] weightItems = new WeightItem[itemsId.length];
	
	SettingsM3Data settingsM3Data;
	Context context;
	public KeyBoradSettingsView(Context context) {
		super(context);
		this.context = context;
		
		inflate(context, R.layout.keyboard_settings_view, this);
		
		settingsM3Data = SettingsM3Data.getInstance();
		Resources resources = getResources();
		String[] itemsTitle = resources.getStringArray(R.array.keyboard_settings_title);
		
		for(int i = 0 ; i < itemScrolls.length ;i++){
			itemScrolls[i] = (SettingsItemScroll) findViewById(itemsId[i]);
			
			itemLyouts[i] = new OptionItemLyout(context);
			itemLyouts[i].setText(itemsTitle[i], optionArrayId[i],1);
			itemScrolls[i].setView(itemLyouts[i], itemScrollListener);
			
			weightItems[i] = new WeightItem(context);
			weightItems[i].setItems(optionArrayId[i], 1, weightItemListener);
			itemScrolls[i].setView(weightItems[i],itemScrollListener);
			
		}
		
	}
	
	@Override
	protected void onAttachedToWindow() {
		// TODO Auto-generated method stub
		super.onAttachedToWindow();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppAction.ACTION_BROADCAST_CMD);
		context.registerReceiver(broadcastReceiver, filter);
	}
	public void init(){
		App.getInstance().fitManagerTeller.getBaseFitBin().setData(
				Cmds.CMD_RC.getBytes(), 2);
		
	}
	
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final String action = intent.getAction();
			Log.d(TAG, "receive action：" + action);

			if (AppAction.ACTION_BROADCAST_CMD.equals(action)) {

				final byte[] cmd = intent
						.getByteArrayExtra(AppAction.KEY_BROADCAST_CMD);
				String strCmd = new String(cmd);
				Log.d(TAG, "receive cmd：" + strCmd);
				sendMessage(strCmd);
			}

		}

	};
	
	private void sendMessage(String cmd) {
		Message message = new Message();
		message.obj = cmd;
		handler.sendMessage(message);
	}
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String cmd = (String) msg.obj;
			if (Cmds.BACK_RC.equals(cmd)) {
				byte[] re = settingsM3Data.arrayParam;
				for(int i =0 ; i < re.length; i ++){
					Log.d(TAG, "re["+i+"]:"+re[i]);
				}
				itemLyouts[0].setOption(re[0]);
				itemLyouts[1].setOption(re[1]);
				itemLyouts[2].setOption(re[2] -1);
				itemLyouts[3].setOption(re[3] -1);
				itemLyouts[4].setOption(re[4]);
				itemLyouts[5].setOption(re[5] -1);
				
				weightItems[0].setItem(re[0]);
				weightItems[1].setItem(re[1]);
				weightItems[2].setItem(re[2] -1);
				weightItems[3].setItem(re[3] -1);
				weightItems[4].setItem(re[4]);
				weightItems[5].setItem(re[5] -1);
			}
		}
	};
	WeightItem.OnWeightItemListener weightItemListener = new WeightItem.OnWeightItemListener() {
		
		@Override
		public void itemClick(WeightItem weightItem,int index) {
			for(int i = 0 ; i < weightItems.length ;i++){
				if(weightItem == weightItems[i]){
					itemLyouts[i].setOption(index);
					itemScrolls[i].setCurrentItem();
					//TODO 
				}
			}
			StringBuffer sb = new StringBuffer();
			sb.append(itemLyouts[0].getOption());
			sb.append(itemLyouts[1].getOption());
			sb.append(itemLyouts[2].getOption()+1);
			sb.append(itemLyouts[3].getOption()+1);
			sb.append(itemLyouts[4].getOption());
			sb.append(itemLyouts[5].getOption()+1);
			settingsM3Data.setSetParam(sb.toString());
			App.getInstance().fitManagerTeller.getBaseFitBin().setData(
					Cmds.CMD_WC.getBytes(), 2);
		}
	};
	private SettingsItemScroll.OnSettingsItemScrollListener itemScrollListener = new SettingsItemScroll.OnSettingsItemScrollListener() {
		
		@Override
		public void onPageSelected(SettingsItemScroll settingsItemScroll, int arg0) {
			if(arg0 == 1)
			for(int i = 0 ; i < itemScrolls.length ;i++){
				if(settingsItemScroll != itemScrolls[i]){
					itemScrolls[i].toFirstPage();
				}
			}
			
		}
	};
}
