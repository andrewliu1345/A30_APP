package com.joesmate.bin;

import java.util.Arrays;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.R;

public class InteractiveFileData extends BaseData{
	private int fileType;
	private String fileUrl;
	private int curIndex,totalNum;
	private static InteractiveFileData fileData;
	public static final int MIN_LEN = 15;
	private byte[] dataBuffer;
	public InteractiveFileData(){
		setTitle(R.string.page_item_title_jh);
	}
	public static InteractiveFileData getInstance(){
		if(fileData == null){
			fileData = new InteractiveFileData();
		}
		return fileData;
	}
	
	
	public byte[] getDataBuffer() {
		return dataBuffer;
	}


	public int getFileType() {
		return fileType;
	}



	public void setFileType(int fileType) {
		this.fileType = fileType;
	}



	public String getFileUrl() {
		return fileUrl;
	}



	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	


	public int getCurIndex() {
		return curIndex;
	}


	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}


	public int getTotalNum() {
		return totalNum;
	}


	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	long f;
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		if(buffer == null){
			return;
		}
		if(Arrays.equals(CMD.DF, cmd)){
			int bufLen = buffer.length;
			if(bufLen < MIN_LEN){
				illegalData();
				return;
			}
			f = System.currentTimeMillis();
			fileType = AssitTool.getArrayCount(new byte[]{buffer[2]});
			curIndex = AssitTool.getArrayCount(new byte[]{buffer[3],buffer[4],buffer[5],buffer[6]});
			totalNum = AssitTool.getArrayCount(new byte[]{buffer[7],buffer[8],buffer[9],buffer[10]});
			Log.d(TAG, "fileType："+fileType);
			Log.d(TAG, "curIndex："+curIndex);
			Log.d(TAG, "totalNum："+totalNum);
			int dataLen = AssitTool.getArrayCount(new byte[]{buffer[11],buffer[12],buffer[13],buffer[14]});
			if(dataLen + MIN_LEN != bufLen ){
				illegalData();
				return;
			}
			
			byte[] receiveBuf = new byte[dataLen];
			System.arraycopy(buffer, 15, receiveBuf, 0, receiveBuf.length);
			if(totalNum == curIndex){
				//包序号采用倒序,序号与总包个数相同则是新包
				fileDatas.clear();
			}
			FileData fileData = new FileData(receiveBuf.length);
			fileData.setBuffer(receiveBuf);
			fileDatas.add(fileData);
			if(curIndex == 1){
				dataBuffer = getTotalFileData(fileDatas);
				fileDatas.clear();
				long e = System.currentTimeMillis();
				Log.d("TimeMillis", "TimeMillis："+(e -f));
			}
			sendConfirmCode();
		}else if(Arrays.equals(CMD.JF, cmd)){
			if(buffer.length != 6){
				illegalData();
				return;
			}
			byte[] arrayAudio = {buffer[2],buffer[3]};
			int audioType = AssitTool.getArrayCount(arrayAudio);
			setAudioType(audioType);
			byte[] arrayTime = {buffer[4],buffer[5]};
			int timeOut = AssitTool.getArrayCount(arrayTime);
			setTimeOut(timeOut);
			legalData();
		}
		
		
	}
	
	private void sendConfirmCode(){
		Log.d(TAG, "sendConfirmCode");
		String backCode = Cmds.BACK_DR+BackCode.CODE_00;
		backData(backCode.getBytes());
	}
	
	public void operation(String code,String result){
		String str = Cmds.BACK_JF +code+result;
		backData(str.getBytes());
	}
	
	

}
