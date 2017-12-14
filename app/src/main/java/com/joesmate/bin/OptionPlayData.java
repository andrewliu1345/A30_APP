package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.Cmds;
import com.joesmate.SharedpreferencesData;

public class OptionPlayData extends BaseData {
	private static OptionPlayData data;
	private int resName;
	private int playType;
	private int playTime;

	public static OptionPlayData getInstance() {
		if (data == null) {
			data = new OptionPlayData();
		}
		return data;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(Cmds.CMD_ZJ_HID.getBytes());
		int pos = 2;
		playType = AssitTool.getArrayCount(new byte[] { buffer[pos],
				buffer[pos + 1] });
		pos += 2;
		resName = AssitTool.getArrayCount(new byte[] { buffer[pos],
				buffer[pos + 1] });
		
		pos += 2;
		playTime = AssitTool.getArrayCount(new byte[] { buffer[pos],
				buffer[pos + 1],buffer[pos + 2],buffer[pos + 3] });
		Log.d(TAG, "playTime:"+playTime);
		if(playTime <= 0){
			playTime = SharedpreferencesData.getInstance().getShowTime();
		}
		legalData();

	}

	public void backCode(String erroCode) {
		String backCode = Cmds.BACK_ZJ + erroCode;
		backData(backCode.getBytes());
	}

	public int getPlayTime() {
		return playTime;
	}

	public void setPlayTime(int playTime) {
		this.playTime = playTime;
	}

	public int getResName() {
		return resName;
	}

	public void setResName(int resName) {
		this.resName = resName;
	}

	public int getPlayType() {
		return playType;
	}

	public void setPlayType(int playType) {
		this.playType = playType;
	}

}
