package com.joesmate.bin;

import android.util.Log;

import com.joesmate.R;

public class InputCharacterData extends BaseData {

	public static final int TYPE_KEYBOARD_00 = 0x00; // 不控制
	public static final int TYPE_KEYBOARD_01 = 0x01; // 输入数字串，不带小数点，可以以0开头（输入账户密码）
	public static final int TYPE_KEYBOARD_02 = 0x02; // 非负数字，可带小数点（输入金额）

	public static final int TYPE_DIAPLAY_COMMON = 0; // 直接回显
	public static final int TYPE_DIAPLAY_PASSWORD = 1;// "*"代替

	private String hitMsg;
	private int displayType, charLen, keyBoardType;
	private static InputCharacterData characterData;

	public static InputCharacterData getInstance() {
		if (characterData == null) {
			characterData = new InputCharacterData();
		}
		return characterData;
	}

	public String getHitMsg() {
		return hitMsg;
	}

	public void setHitMsg(String hitMsg) {
		this.hitMsg = hitMsg;
	}

	public int getKeyBoardType() {
		return keyBoardType;
	}

	public void setKeyBoardType(int keyBoardType) {
		this.keyBoardType = keyBoardType;
	}

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
	}

	public int getCharLen() {
		return charLen;
	}

	public void setCharLen(int charLen) {
		this.charLen = charLen;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {

		if (buffer == null || buffer.length != 7) {
			return;
		}
		setCmd(cmd);
		int audioType = buffer[2];
		setAudioType(audioType);
		/*
		 * 0AH：语音，“请输入验证码” 0BH：语音，“请输入金额” 0CH：语音，“请输入账号” 0DH：语音，“请输入” FFH：无声
		 */
		if(audioType == 0x0A){
			setTitle(R.string.page_item_title_input_validation);
		}else if(audioType == 0x0B){
			setTitle(R.string.page_item_title_input_money);
		}else if(audioType == 0x0C){
			setTitle(R.string.page_item_title_input_account);
		}else if(audioType == 0x0D){
			setTitle(R.string.page_item_title_input);
		}else{
			setTitle(R.string.page_item_title_input);
		}
		Log.d(TAG, "audioType:"+audioType);

		int timeOut = buffer[3];
		setTimeOut(timeOut);

		int displayType = buffer[4];
		setDisplayType(displayType);
		Log.d(TAG, "displayType:"+displayType);

		int charLen = buffer[5];
		setCharLen(charLen);
		Log.d(TAG, "charLen:"+charLen);

		int keyType = buffer[6];
		setKeyBoardType(keyType);
		Log.d(TAG, "keyType:"+keyType);

		legalData();

	}
	public void backTimeOut(){
		byte[] bs = {0x10, 0x61,(byte) 0x80};
		backData(bs);
	}
	public void backResult(String result){
		if(result == null || result.length() == 0){
			backTimeOut();
			return;
		}
		byte[] bs = new byte[2 + result.length()];
		bs[0] =0;
		bs[1] =0;
		byte[] arrayResult = result.getBytes();
		System.arraycopy(arrayResult, 0, bs, 2, arrayResult.length);
		backData(bs);
	}

}
