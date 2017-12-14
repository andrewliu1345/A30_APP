package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.R;

public class InteractiveMsgData extends BaseData{

	public static final String TAG = "InteractiveMsgData";
	public static final int MIN_LEN = 10;
	private static InteractiveMsgData msgData;
	private String message;
	
	public InteractiveMsgData(){
		setTitle(R.string.page_item_title_jh);
	}
	public static InteractiveMsgData getInstance(){
		if(msgData == null){
			msgData = new InteractiveMsgData();
		}
		return msgData;
	}

	public void setData(byte[] buffer,byte[] cmd){
		setCmd(cmd);
		int audioType = buffer[2];
		Log.d(TAG, "audioType："+audioType);
		
		setAudioType(audioType);
		
		int timeOut = buffer[3];
		setTimeOut(timeOut);
		Log.d(TAG, "timeOut："+timeOut);
		
		
		int index = buffer[4];
		Log.d(TAG, "index："+index);
		
		
		byte[] msgs = new byte[buffer.length -5];
		System.arraycopy(buffer, 5, msgs, 0, msgs.length);
		String msg = AssitTool.arrayToString(msgs, AssitTool.UTF_8);
		setMessage(msg);
		Log.d(TAG, "msg："+msg);
		legalData();
		sendConfirmCode();
	}
	
	private void sendConfirmCode(){
		backData(new byte[]{0,0});
	}
	public void operation(byte code){
		byte[] buffer = new byte[3];
		buffer[0] = 0;
		buffer[1] = 0;
		buffer[2] = code;
		backData(buffer);
	}
	
	public void setBack(String erroCode,String result){
		String backMsg = Cmds.BACK_JI;
		if(erroCode.equals(CMD.CODE_00H)){
			backMsg += erroCode + result;
		}else{
			backMsg +=erroCode;
		}
		backData(backMsg.getBytes());
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
}
