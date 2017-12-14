package com.joesmate.settings;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.LinearLayout;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.Cmds;
import com.joesmate.DeviceSettings;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.keyBoard.SettingsM3Data;
import com.joesmate.widget.CustomText;
import com.joesmate.widget.ItemSeekBar;
import com.joesmate.widget.OptionItemLyout;
import com.joesmate.widget.SettingsItemScroll;
import com.joesmate.widget.WeightItem;

public class DevSettingsView extends LinearLayout{
	public static final String TAG = "DevSettingsView";
	private ItemSeekBar seekBarLoudSpeaker,seekBarHeadSet,
	seekBarBrightness,seekBarTextSize,seekBarShowTime,seekBarPen;
	SettingsItemScroll scrollColor,scrollBaud;
	OptionItemLyout optionItemLyoutColor,optionItemLyoutBaud;
	WeightItem weightItemColor,weightItemBaud;
	Context context;
	CustomText tvVersion;
	public DevSettingsView(Context context) {
		super(context);
		this.context = context;
		inflate(context, R.layout.dev_settings_view, this);
		tvVersion = (CustomText) findViewById(id.app_version);
		tvVersion.setText("软件版本:"+DeviceSettings.getVersionName(context));
		
		seekBarLoudSpeaker = (ItemSeekBar) findViewById(R.id.dev_settings_item_loudspeaker);
		seekBarHeadSet = (ItemSeekBar) findViewById(R.id.dev_settings_item_headset);
		seekBarBrightness = (ItemSeekBar) findViewById(R.id.dev_settings_item_brigthness);
		seekBarShowTime = (ItemSeekBar) findViewById(R.id.dev_settings_item_showTime);
		seekBarTextSize = (ItemSeekBar) findViewById(R.id.dev_settings_item_jhTextSize);
		seekBarPen = (ItemSeekBar) findViewById(R.id.dev_settings_item_pen);
		scrollColor = (SettingsItemScroll) findViewById(R.id.dev_settings_item_color);
		scrollBaud = (SettingsItemScroll) findViewById(R.id.dev_settings_item_baud);
		
		optionItemLyoutColor = new OptionItemLyout(context);
		optionItemLyoutColor.setText("画笔颜色", R.array.dev_settings_item_colors,SharedpreferencesData.getInstance().getPenColor());
		
		weightItemColor = new WeightItem(context);
		weightItemColor.setItems(R.array.dev_settings_item_colors, SharedpreferencesData.getInstance().getPenColor(), weightItemListener);
		
		scrollColor.setView(optionItemLyoutColor, null);
		scrollColor.setView(weightItemColor, null);
		
		
		optionItemLyoutBaud = new OptionItemLyout(context);
		optionItemLyoutBaud.setText("波特率", R.array.dev_settings_item_baud,SharedpreferencesData.getInstance().getBaud() -1);
		
		weightItemBaud = new WeightItem(context);
		weightItemBaud.setItems(R.array.dev_settings_item_baud, SharedpreferencesData.getInstance().getBaud() -1, weightItemListener);
		
		scrollBaud.setView(optionItemLyoutBaud, null);
		scrollBaud.setView(weightItemBaud, null);
		
		
		seekBarLoudSpeaker.init("扬声器", DeviceSettings.getVolume(AudioManager.STREAM_MUSIC), 100);
		seekBarHeadSet.init("耳机", DeviceSettings.getVolume(AudioManager.STREAM_SYSTEM), 100);
		seekBarBrightness.init("屏幕亮度", DeviceSettings.getSystemBrightness(), 100);
		seekBarShowTime.init("展示时间", SharedpreferencesData.getInstance().getShowTime(), 100);
		seekBarPen.init("画笔大小", SharedpreferencesData.getInstance().getPenWidth(), 10);
		seekBarTextSize.init("字体大小", SharedpreferencesData.getInstance().getJhFontSize(), 72);
		
		seekBarShowTime.setOnItemSeekBarListener(barListener);
		seekBarTextSize.setOnItemSeekBarListener(barListener);
		seekBarBrightness.setOnItemSeekBarListener(barListener);
		seekBarHeadSet.setOnItemSeekBarListener(barListener);
		seekBarLoudSpeaker.setOnItemSeekBarListener(barListener);
		seekBarPen.setOnItemSeekBarListener(barListener);
	}
	public void init(){
		seekBarLoudSpeaker.setProgress(DeviceSettings.getVolume(AudioManager.STREAM_MUSIC));
		seekBarHeadSet.setProgress(DeviceSettings.getVolume(AudioManager.STREAM_SYSTEM));
		seekBarBrightness.setProgress(DeviceSettings.getSystemBrightness());
		seekBarShowTime.setProgress( SharedpreferencesData.getInstance().getShowTime());
		seekBarTextSize.setProgress( SharedpreferencesData.getInstance().getJhFontSize());
		seekBarPen.setProgress( SharedpreferencesData.getInstance().getPenWidth());
		optionItemLyoutColor.setOption(SharedpreferencesData.getInstance().getPenColor());
		weightItemColor.setItem(SharedpreferencesData.getInstance().getPenColor());
		optionItemLyoutBaud.setOption(SharedpreferencesData.getInstance().getBaud() -1);
		weightItemBaud.setItem(SharedpreferencesData.getInstance().getBaud() -1);
		
		App.getInstance().fitManagerTeller.getBaseFitBin().setData(
				Cmds.CMD_RC.getBytes(), 2);
	}
	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppAction.ACTION_BROADCAST_SETTINGS_UPDATE);
		context.registerReceiver(broadcastReceiver, filter);
		
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(AppAction.ACTION_BROADCAST_CMD);
		context.registerReceiver(broadcastReceiver2, filter2);
	}
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		context.unregisterReceiver(broadcastReceiver);
		context.unregisterReceiver(broadcastReceiver2);
	}
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(AppAction.ACTION_BROADCAST_SETTINGS_UPDATE.equals(intent.getAction())){
				int setType = intent.getIntExtra(AppAction.KEY_BROADCAST_SETTINGS_TYPE, 0);
				int value = intent.getIntExtra(AppAction.KEY_BROADCAST_SETTINGS, 0);
				if(setType ==1){
					seekBarLoudSpeaker.setProgress(value);
				}else if(setType ==2){
					seekBarHeadSet.setProgress(value);
				}else if(setType ==3){
					seekBarBrightness.setProgress(value);
				}else if(setType ==4){
					seekBarShowTime.setProgress(value);
				}else if(setType == 5){
					optionItemLyoutBaud.setOption(value -1);
					weightItemBaud.setItem(value -1);
				}
			}
			
		}
	};
	
	private ItemSeekBar.OnItemSeekBarListener barListener = new ItemSeekBar.OnItemSeekBarListener() {
		
		@Override
		public void onItemChanged(ItemSeekBar itemSeekBar, int progress) {
			if(itemSeekBar == seekBarLoudSpeaker){
				DeviceSettings.setVolume(AudioManager.STREAM_MUSIC, progress);
			}else if(itemSeekBar == seekBarHeadSet){
				DeviceSettings.setVolume(AudioManager.STREAM_SYSTEM, progress);
			}else if(itemSeekBar == seekBarBrightness){
				DeviceSettings.setAppWindowBrightness(context, progress);
				DeviceSettings.saveSystemBrightness(progress);
			}else if(itemSeekBar == seekBarShowTime){
				SharedpreferencesData.getInstance().setShowTime(progress);
			}else if(itemSeekBar == seekBarPen){
				SharedpreferencesData.getInstance().setPenWidth(progress);
			}else if(itemSeekBar == seekBarTextSize){
				SharedpreferencesData.getInstance().setJhFontSize(progress);
			}
		}
	};
	
	WeightItem.OnWeightItemListener weightItemListener = new WeightItem.OnWeightItemListener() {

		@Override
		public void itemClick(WeightItem weightItem, int index) {
			if(weightItem == weightItemColor){
				SharedpreferencesData.getInstance().setPenColor(index);
				optionItemLyoutColor.setOption(index);
				scrollColor.setCurrentItem();
			}else if(weightItem == weightItemBaud){
				SharedpreferencesData.getInstance().setBaud(index + 1);
				optionItemLyoutBaud.setOption(index);
				scrollBaud.setCurrentItem();
				
				if(re != null && re.length == 6){
					if(index == 0){
						//1200
						re[3] = 2;
					}else if(index == 1){
						//9600
						re[3] = 1;
					}else if(index == 2){
						//115200
						re[3] = 3;
					}
					StringBuffer sb = new StringBuffer();
					for(int i = 0 ; i < re.length; i++){
						sb.append(re[i]);
					}
					SettingsM3Data.getInstance().setSetParam(sb.toString());
					App.getInstance().fitManagerTeller.getBaseFitBin().setData(
							Cmds.CMD_WC.getBytes(), 2);
				}
			}
		}
		
	};
	
	private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {

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
	byte[] re;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String cmd = (String) msg.obj;
			if (Cmds.BACK_RC.equals(cmd)) {
				re = SettingsM3Data.getInstance().arrayParam;
				for(int i =0 ; i < re.length; i ++){
					Log.d(TAG, "re["+i+"]:"+re[i]);
				}
			}
		}
	};

}
