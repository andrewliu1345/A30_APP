package com.joesmate.bin.sdcs;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.R;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

public class SDCSInputCharacterData extends BaseData {

	public static final int TYPE_KEYBOARD_00 = 0x00; // 不控制
	public static final int TYPE_KEYBOARD_01 = 0x01; // 输入数字串，不带小数点，可以以0开头（输入账户密码）
	public static final int TYPE_KEYBOARD_02 = 0x02; // 非负数字，可带小数点（输入金额）


	private int charLen, keyBoardType,voiceTextLen;
	private  byte[] arrayvoiceText;

	public String getVoiceText() {
		return voiceText;
	}

	public void setVoiceText(String voiceText) {
		this.voiceText = voiceText;
	}

	private String voiceText ;
	private static SDCSInputCharacterData characterData;

	public static SDCSInputCharacterData getInstance() {
		if (characterData == null) {
			characterData = new SDCSInputCharacterData();
		}
		return characterData;
	}


	public int getKeyBoardType() {
		return keyBoardType;
	}

	public void setKeyBoardType(int keyBoardType) {
		this.keyBoardType = keyBoardType;
	}


	public int getCharLen() {
		return charLen;
	}

	public void setCharLen(int charLen) {
		this.charLen = charLen;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {

		setCmd(cmd);
		int pos = 0;
		if (Arrays.equals(Cmds.CMD_IN.getBytes(), cmd)) {
			pos = 2 ;


			charLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1]});
			Log.d(TAG, "charLen:" + charLen);
			pos = pos + 2 ;


			keyBoardType = AssitTool.getArrayCount(new byte[]{buffer[pos]});
			Log.d(TAG, "keyBoardType:" + keyBoardType);
			pos = pos + 1 ;


			int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
			setTimeOut(timeOut);
			Log.d(TAG, "timeOut:" + timeOut);
			pos = pos + 3 ;


			voiceTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "voiceTextLen:" + voiceTextLen);
			pos = pos + 4 ;


			arrayvoiceText = new byte[voiceTextLen];
			System.arraycopy(buffer,pos,arrayvoiceText,0,voiceTextLen);
			voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
			Log.d(TAG, "voiceText:" + voiceText);


             if(keyBoardType == TYPE_KEYBOARD_00)
			 {
				 setTitle(R.string.page_item_title_input);
			 }
			else if(keyBoardType == TYPE_KEYBOARD_01)
			 {
				 setTitle(R.string.page_item_title_input);
			 }
			else if(keyBoardType == TYPE_KEYBOARD_02)
			 {
				 setTitle(R.string.page_item_title_input_money);
			 }
             sendConfirmCode(BackCode.CODE_00);
			 legalData();

		}



	}


	// 00 ok  // 10 超时   //20 返回结果
	public void sendConfirmCode(String backCmd) {
		Log.d(TAG, "sendConfirmCode");
		String backCode = Cmds.CMD_IN + backCmd;
		backData(backCode.getBytes());
	}

	public void sendConfirmResult(String result) {
		String backCode = Cmds.CMD_IN + BackCode.CODE_20 ;
		Log.d(TAG, "sendConfirmResult:"+backCode );
		byte[] bs = new byte[2 + 2 + 2 + result.length()];
		int pos = 0 ;
		System.arraycopy(backCode.getBytes(),0,bs,pos,4);
		pos = pos + 4 ;

		System.arraycopy(AssitTool.getCount42(charLen),0,bs,pos,2);
		pos = pos + 2 ;

		byte[] arrayResult = result.getBytes();
		System.arraycopy(arrayResult, 0, bs, pos, arrayResult.length);
		backData(bs);
	}


}
