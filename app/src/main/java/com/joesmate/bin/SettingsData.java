package com.joesmate.bin;

import java.util.Arrays;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.DeviceSettings;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.keyBoard.SettingsM3Data;

public class SettingsData extends BaseData {
	
	public static final int TYPE_SET_LOUDSPEAKER = 1;
	public static final int TYPE_SET_HEADSET = 2;
	public static final int TYPE_SET_BRIGTHNESS = 3;
	public static final int TYPE_SET_SHOWTIME = 4;
	
	private static SettingsData data;
	public SettingsData(){
		IntentFilter filter2 = new IntentFilter();
		filter2.addAction(AppAction.ACTION_BROADCAST_CMD);
		App.getInstance().registerReceiver(broadcastReceiver2, filter2);
	}
	public static SettingsData getInstance(){
		if(data == null){
			data = new SettingsData();
		}
		return data;
	}

	/*
	 * 01：扬声器音量(语音 STREAM_RING) 02：耳机音量(广告 STREAM_MUSIC) 03：屏幕亮度 04：广告展示时间 05波特率
	 */
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		int pos =0;
		if(Arrays.equals(Cmds.CMD_SO.getBytes(), cmd)){
			pos = 2;
			int setType = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1]});
			
			pos = 4;
			int value = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1],buffer[pos+2]});
			if(setType ==1){
				setLoudspeaker(value);
			}else if(setType ==2){
				setHeadset(value);
			}else if(setType ==3){
				//setBrigthness(value);
			}else if(setType ==4){
				setShowTime(value);
			}else if(setType == 5){
				SharedpreferencesData.getInstance().setBaud(value);
				App.getInstance().fitManagerTeller.getBaseFitBin().setData(
						Cmds.CMD_RC.getBytes(), 2);
			}
			if(setType >=1 && setType <=5){
				//同步页面数据变化
				Intent intent = new Intent(AppAction.ACTION_BROADCAST_SETTINGS_UPDATE);
				intent.putExtra(AppAction.KEY_BROADCAST_SETTINGS_TYPE, setType);
				intent.putExtra(AppAction.KEY_BROADCAST_SETTINGS, value);
				App.getInstance().sendBroadcast(intent);
			}
			Log.d(TAG, "setType：<"+setType+">  value<"+value+">");
			backSettings();
		}else if(Arrays.equals(Cmds.CMD_QO.getBytes(), cmd)){
			String backCode = Cmds.BACK_QO+BackCode.CODE_00+DeviceSettings.getVolume(AudioManager.STREAM_MUSIC)+AssitTool.SPLIT_LINE+
					getHeadset()+AssitTool.SPLIT_LINE+DeviceSettings.getSystemBrightness()+AssitTool.SPLIT_LINE+
					getShowTime()+AssitTool.SPLIT_LINE+SharedpreferencesData.getInstance().getBaud();
			Log.d(TAG, "backCode："+backCode);
			backData(backCode.getBytes());
		}

	}
	private void backSettings(){
		String backCode = Cmds.BACK_SO + BackCode.CODE_00;
		backData(backCode.getBytes());
	}
	public int getLoudspeaker() {
		return DeviceSettings.getVolume(AudioManager.STREAM_MUSIC);
	}
	public void setLoudspeaker(int loudspeaker) {
		DeviceSettings.setVolume(AudioManager.STREAM_MUSIC, loudspeaker);
	}
	public int getHeadset() {
		return DeviceSettings.getVolume(AudioManager.STREAM_SYSTEM);
	}
	public void setHeadset(int headset) {
		DeviceSettings.setVolume(AudioManager.STREAM_SYSTEM, headset);
	}
	public int getBrigthness() {
		return DeviceSettings.getSystemBrightness();
	}

	public void setShowTime(int showTime) {
		SharedpreferencesData.getInstance().setShowTime(showTime);
	}
	
	public int getShowTime(){
		return SharedpreferencesData.getInstance().getShowTime();
	}
	
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
				if (Cmds.BACK_RC.equals(cmd)) {
					re = SettingsM3Data.getInstance().arrayParam;
					for(int i =0 ; i < re.length; i ++){
						Log.d(TAG, "re["+i+"]:"+re[i]);
					}
					int index = SharedpreferencesData.getInstance().getBaud()-1;
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

		}

	};
	
	byte[] re;
	
}
