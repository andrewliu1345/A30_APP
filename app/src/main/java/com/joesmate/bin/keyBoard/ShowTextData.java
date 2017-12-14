package com.joesmate.bin.keyBoard;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

public class ShowTextData extends BaseData {

	private int titleLen, msgMaxLen, msgLen;
	private String title, msg;
	private static ShowTextData showTextData;

	public ShowTextData(){
		setTitle("请输入密码");
	}
	public static ShowTextData getInstance() {
		if (showTextData == null) {
			showTextData = new ShowTextData();
		}
		return showTextData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);

		if (Arrays.equals(Cmds.CMD_DD.getBytes(), cmd)) {

			int pos = 2;
			int titleLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1] });
			Log.d(TAG, "titleLen：" + titleLen);

			pos = 4;
			byte[] arrayTitle = new byte[titleLen];
			System.arraycopy(buffer, pos, arrayTitle, 0, titleLen);
			String title = AssitTool.getString(arrayTitle, AssitTool.UTF_8);
			setTitle(title);
			Log.d(TAG, "title：" + title);

			pos += titleLen;
			int msgMaxLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1] });
			setMsgMaxLen(msgMaxLen);
			Log.d(TAG, "msgMaxLen：" + msgMaxLen);

			pos += 2;
			int msgLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1] });
			setMsgLen(msgLen);
			Log.d(TAG, "msgLen：" + msgLen);

			pos += 2;
			if (msgLen == 0) {
				setMsg(null);
			} else {
				byte[] arrayMsg = new byte[msgLen];
				System.arraycopy(buffer, pos, arrayMsg, 0, msgLen);
				String msg = AssitTool.getString(arrayMsg, AssitTool.UTF_8);
				setMsg(msg);
				Log.d(TAG, "msg：" + msg);
			}

			pos += msgLen;
			int audioType = buffer[pos];
			//setAudioType(audioType);
			Log.d(TAG, "audioType：" + audioType);
			confirmCode(true);
			backCode(Cmds.BACK_DD, BackCode.CODE_00);
			legalData();
		}else if (Arrays.equals(Cmds.CMD_CD.getBytes(), cmd)) {
			confirmCode(true);
			backCode(Cmds.BACK_CD, BackCode.CODE_00);
			legalData();
		}else if (Arrays.equals(Cmds.CMD_XS.getBytes(), cmd)) {
			
			int pos = 2;
			int msgLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1] });
			setMsgLen(msgLen);
			Log.d(TAG, "msgLen：" + msgLen);

			pos += 2;
			if (msgLen == 0) {
				setMsg(null);
			} else {
				byte[] arrayMsg = new byte[msgLen];
				System.arraycopy(buffer, pos, arrayMsg, 0, msgLen);
				String msg = AssitTool.getString(arrayMsg, AssitTool.UTF_8);
				setMsg(msg);
				Log.d(TAG, "msg：" + msg);
			}
			
			confirmCode(true);
			backCode(Cmds.BACK_XS, BackCode.CODE_00);
			legalData();
		}

	}
	
	private void confirmCode(boolean isConfirm){
		byte[] arrayCode = new byte[1];
		if(isConfirm){
			arrayCode[0] = BackCode.CODE_ACK;
		}else {
			arrayCode[0] = BackCode.CODE_NAK;
		}
		backData(arrayCode);
	}
	private void backCode(String cmd,String erroCode){
		String backStr = cmd + erroCode;
		backData(backStr.getBytes());
	}

	public int getTitleLen() {
		return titleLen;
	}

	public void setTitleLen(int titleLen) {
		this.titleLen = titleLen;
	}

	public int getMsgMaxLen() {
		return msgMaxLen;
	}

	public void setMsgMaxLen(int msgMaxLen) {
		this.msgMaxLen = msgMaxLen;
	}

	public int getMsgLen() {
		return msgLen;
	}

	public void setMsgLen(int msgLen) {
		this.msgLen = msgLen;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
