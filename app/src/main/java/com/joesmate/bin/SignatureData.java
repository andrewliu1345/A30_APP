package com.joesmate.bin;

import java.util.Arrays;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.R;

public class SignatureData extends BaseData{

	private int fileType;
	private String fileUrl;
	private int curIndex,totalNum;
	public static final int MIN_LEN = 15;
	private byte[] dataBuffer;
	private byte[] arrayHtml;
	private static SignatureData signatureData;
	public SignatureData(){
		setTitle(R.string.page_item_title_signature);
	}
	public static SignatureData getInstance(){
		if(signatureData == null){
			signatureData = new SignatureData();
		}
		return signatureData;
	}
	
	
	public byte[] getArrayHtml() {
		return arrayHtml;
	}


	public void setArrayHtml(byte[] arrayHtml) {
		this.arrayHtml = arrayHtml;
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

	public byte[] getDataBuffer() {
		return dataBuffer;
	}

	public void setDataBuffer(byte[] dataBuffer) {
		this.dataBuffer = dataBuffer;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		
		if(buffer == null){
			return;
		}
		setCmd(cmd);
		if(Arrays.equals(CMD.DB, cmd)){
			int bufLen = buffer.length;
			if(bufLen < MIN_LEN){
				sendConfirmCode(BackCode.CODE_12);
				return;
			}
			fileType = AssitTool.getArrayCount(new byte[]{buffer[2]});
			curIndex = AssitTool.getArrayCount(new byte[]{buffer[3],buffer[4],buffer[5],buffer[6]});
			totalNum = AssitTool.getArrayCount(new byte[]{buffer[7],buffer[8],buffer[9],buffer[10]});
			Log.d(TAG, "fileType："+fileType);
			Log.d(TAG, "curIndex："+curIndex);
			Log.d(TAG, "totalNum："+totalNum);
			int dataLen = AssitTool.getArrayCount(new byte[]{buffer[11],buffer[12],buffer[13],buffer[14]});
			if(dataLen + MIN_LEN != bufLen ){
				sendConfirmCode(BackCode.CODE_12);
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
				
			}
			sendConfirmCode(BackCode.CODE_00);
		}else if(Arrays.equals(CMD.SG, cmd)){
			int pos = 2;
			int audioType = AssitTool.getArrayCount(new byte[] {buffer[pos],buffer[pos+1]});
			setAudioType(audioType);
			
			pos = 4;
			int timeOut = AssitTool.getArrayCount(new byte[] {buffer[pos],buffer[pos+1]});
			setTimeOut(timeOut);
			
			pos = 6;
			fileType = AssitTool.getArrayCount(new byte[] {buffer[pos]});;
			Log.d(TAG, "fileType："+fileType);
			if(fileType == 3){
				pos = 7;
				int htmlLen = AssitTool.getArrayCount(new byte[] {buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
				pos = 11;
				arrayHtml = new byte[htmlLen];
				System.arraycopy(buffer, pos, arrayHtml, 0, htmlLen);
			}
			legalData();
		}
		
	}
	
	private void sendConfirmCode(String code){
		Log.d(TAG, "sendConfirmCode");
		String backCode = Cmds.BACK_DB+code;
		backData(backCode.getBytes());
	}

}
