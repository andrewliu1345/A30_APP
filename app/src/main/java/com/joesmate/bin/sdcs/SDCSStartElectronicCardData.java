package com.joesmate.bin.sdcs;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

public class SDCSStartElectronicCardData extends BaseData {


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
	private int nameLen, jobNumberLen,photoNameLen ;

	private static SDCSStartElectronicCardData sDCSStartElectronicCardData;

	public static SDCSStartElectronicCardData getInstance() {
		if (sDCSStartElectronicCardData == null) {
			sDCSStartElectronicCardData = new SDCSStartElectronicCardData();
		}
		return sDCSStartElectronicCardData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {

		setCmd(cmd);
		int pos = 0;
		if (Arrays.equals(Cmds.CMD_SC.getBytes(), cmd)) {
			pos = 2 ;

			nameLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1]});
			Log.d(TAG, "nameLen:" + nameLen);
			pos = pos + 2 ;


		    byte[]	arrayName = new byte[nameLen];
			System.arraycopy(buffer,pos,arrayName,0,nameLen);
			name =  AssitTool.getString(arrayName, AssitTool.UTF_8);
			Log.d(TAG, "arrayName:" + arrayName);
			pos = pos + nameLen ;

			jobNumberLen = AssitTool.getArrayCount(new byte[] {buffer[pos],buffer[pos+1]});
			Log.d(TAG, "jobNumberLen:" + jobNumberLen);
			pos = pos + 2 ;

			byte[]	arrayJobName = new byte[jobNumberLen];
			System.arraycopy(buffer,pos,arrayJobName,0,jobNumberLen);
			jobNumber =  AssitTool.getString(arrayJobName, AssitTool.UTF_8);
			Log.d(TAG, "jobNumber:" + jobNumber);
			pos = pos + jobNumberLen ;

            rate = AssitTool.getArrayCount(new byte[] {buffer[pos]});
			Log.d(TAG, "rate:" + rate);
			pos = pos + 1 ;


			photoNameLen = AssitTool.getArrayCount(new byte[] {buffer[pos],buffer[pos+1],buffer[pos+2]});
			Log.d(TAG, "photoNameLen:" + photoNameLen);
			pos = pos + 3 ;

			byte[]	arrayPhotoName = new byte[photoNameLen];
			System.arraycopy(buffer,pos,arrayPhotoName,0,photoNameLen);
			photoName =  AssitTool.getString(arrayPhotoName, AssitTool.UTF_8);
			Log.d(TAG, "photoName:" + photoName);
			pos = pos + photoNameLen;

			int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
			setTimeOut(timeOut);
			Log.d(TAG, "timeOut:" + timeOut);

			sendConfirmCode(BackCode.CODE_00);
			legalData();

		}



	}


	// 00 ok  // 10 超时   //20 返回结果
	public void sendConfirmCode(String backCmd) {
		Log.d(TAG, "sendConfirmCode");
		String backCode = Cmds.CMD_SC + backCmd;
		backData(backCode.getBytes());
	}




}
