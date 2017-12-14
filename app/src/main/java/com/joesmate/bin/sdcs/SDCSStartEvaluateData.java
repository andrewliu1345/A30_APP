package com.joesmate.bin.sdcs;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

public class SDCSStartEvaluateData extends BaseData {




	private int voiceTextLen;
	private  byte[] arrayvoiceText;




	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	private  String name;

	public String getJobNumber() {
		return jobNumber;
	}

	public void setJobNumber(String jobNumber) {
		this.jobNumber = jobNumber;
	}

	private String jobNumber;

	public int getRate() {
		return rate;
	}

	public void setRate(int rate) {
		this.rate = rate;
	}

	private int rate;

	public String getPhotoName() {
		return photoName;
	}

	public void setPhotoName(String photoName) {
		this.photoName = photoName;
	}

	private String photoName;


	public String getBeforeMsg() {
		return beforeMsg;
	}

	public void setBeforeMsg(String beforeMsg) {
		this.beforeMsg = beforeMsg;
	}

	private String beforeMsg;

	public String getAfterMsg() {
		return afterMsg;
	}

	public void setAfterMsg(String afterMsg) {
		this.afterMsg = afterMsg;
	}

	private String afterMsg;


	private String voiceText;

	public void setVoiceText(String voiceText) {
		this.voiceText = voiceText;
	}

	public String getVoiceText() {
		return voiceText;
	}


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	private String result ;


	private int nameLen, jobNumberLen,photoNameLen,beforeMsglen,afterMsgLen ;

	private byte[]  nameArray ;
	private byte[]  jobNumberArray;
	private byte[]  photoNameArray;
	private byte[]  beforeMsgArray;
	private byte[]  afterMsgArray;


	public int getAfterTimeout() {
		return afterTimeout;
	}

	public void setAfterTimeout(int afterTimeout) {
		this.afterTimeout = afterTimeout;
	}

	private int afterTimeout ;


	private String jobNumbervoiceText ;
	private static SDCSStartEvaluateData sDCSStartEvaluateData;

	public static SDCSStartEvaluateData getInstance() {
		if (sDCSStartEvaluateData == null) {
			sDCSStartEvaluateData = new SDCSStartEvaluateData();
		}
		return sDCSStartEvaluateData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {

		setCmd(cmd);
		int pos = 0;
		if (Arrays.equals(Cmds.CMD_SE.getBytes(), cmd)) {
			pos = 2 ;

			jobNumberLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1]});
			Log.d(TAG, "jobNumberLen:" + jobNumberLen);
			pos = pos + 2 ;

			jobNumberArray = new byte[jobNumberLen];
			System.arraycopy(buffer,pos,jobNumberArray,0,jobNumberLen);
			jobNumber =  AssitTool.getString(jobNumberArray, AssitTool.UTF_8);
			Log.d(TAG, "jobNumber:" + jobNumber);
			pos = pos + jobNumberLen ;



			photoNameLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2]});
			Log.d(TAG, "photoNameLen:" + photoNameLen);
			pos = pos + 3 ;


			photoNameArray = new byte[photoNameLen];
			System.arraycopy(buffer,pos,photoNameArray,0,photoNameLen);
			photoName =  AssitTool.getString(photoNameArray, AssitTool.UTF_8);
			Log.d(TAG, "photoName:" + photoName);
			pos = pos + photoNameLen;



			nameLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1]});
			Log.d(TAG, "nameLen:" + nameLen);
			pos = pos + 2 ;


			nameArray = new byte[nameLen];
			System.arraycopy(buffer,pos,nameArray,0,nameLen);
			name =  AssitTool.getString(nameArray, AssitTool.UTF_8);
			Log.d(TAG, "name:" + name);
			pos = pos + nameLen;


			beforeMsglen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2]});
			Log.d(TAG, "beforeMsglen:" + beforeMsglen);
			pos = pos + 3 ;


			beforeMsgArray = new byte[beforeMsglen];
			System.arraycopy(buffer,pos,beforeMsgArray,0,beforeMsglen);
			beforeMsg =  AssitTool.getString(beforeMsgArray, AssitTool.UTF_8);
			Log.d(TAG, "beforeMsg:" + beforeMsg);
			pos = pos + beforeMsglen;



			afterMsgLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2]});
			Log.d(TAG, "afterMsgLen:" + afterMsgLen);
			pos = pos + 3 ;


			afterMsgArray = new byte[afterMsgLen];
			System.arraycopy(buffer,pos,afterMsgArray,0,afterMsgLen);
			afterMsg =  AssitTool.getString(afterMsgArray, AssitTool.UTF_8);
			Log.d(TAG, "afterMsg:" + afterMsg);
			pos = pos + afterMsgLen;


			voiceTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2]});
			Log.d(TAG, "voiceTextLen:" + voiceTextLen);
			pos = pos + 3 ;


			arrayvoiceText = new byte[voiceTextLen];
			System.arraycopy(buffer,pos,arrayvoiceText,0,voiceTextLen);
			voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
			Log.d(TAG, "voiceText:" + voiceText);
			pos = pos + voiceTextLen;



			afterTimeout =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2]});
			Log.d(TAG, "afterTimeout:" + afterTimeout);
			pos = pos + 3 ;


			int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
			setTimeOut(timeOut);
			Log.d(TAG, "timeOut:" + timeOut);



			sendConfirmCode(BackCode.CODE_00);
			legalData();

		}



	}


	public void sendConfirmCode(String backCmd) {
		Log.d(TAG, "sendConfirmCode");
		String backCode = Cmds.CMD_SE + backCmd;
		backData(backCode.getBytes());
	}

	public void sendConfirmResult(String result) {
		String backCode = Cmds.CMD_SE + BackCode.CODE_20 + result;
		Log.d(TAG, "sendConfirmResult:"+backCode );
		backData(backCode.getBytes());
	}




}
