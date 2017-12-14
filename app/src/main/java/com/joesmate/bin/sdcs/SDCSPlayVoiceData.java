package com.joesmate.bin.sdcs;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

public class SDCSPlayVoiceData extends BaseData {




	private int voiceTextLen;
	private  byte[] arrayvoiceText;

	public String getVoiceText() {
		//return "123456";
		return voiceText;
	}

	public void setVoiceText(String voiceText) {
		this.voiceText = voiceText;
	}

	private String voiceText ;
	private static SDCSPlayVoiceData sDCSPlayVoice;

	public static SDCSPlayVoiceData getInstance() {
		if (sDCSPlayVoice == null) {
			sDCSPlayVoice = new SDCSPlayVoiceData();
		}
		return sDCSPlayVoice;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {

		setCmd(cmd);
		int pos = 0;
		if (Arrays.equals(Cmds.CMD_PV.getBytes(), cmd)) {
			pos = 2 ;

			voiceTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "voiceTextLen:" + voiceTextLen);
			pos = pos + 4 ;

			arrayvoiceText = new byte[voiceTextLen];
			System.arraycopy(buffer,pos,arrayvoiceText,0,voiceTextLen);
			voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
			Log.d(TAG, "voiceText:" + voiceText);

			sendConfirmCode(BackCode.CODE_00);
			legalData();

		}



	}


	// 00 ok  // 10 超时   //20 返回结果
	public void sendConfirmCode(String backCmd) {
		Log.d(TAG, "sendConfirmCode");
		String backCode = Cmds.CMD_PV + backCmd;
		backData(backCode.getBytes());
	}




}
