package com.joesmate.bin;

import android.util.Log;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.listener.OnCallBackListenner;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseData {

	public static final String TAG = "BaseData";
	private String audioType;
	private int timeOut;
	private String title;
	private byte[] cmd;
	public List<FileData> fileDatas = new ArrayList<BaseData.FileData>();
	private OnCallBackListenner backListener;

	
	public OnCallBackListenner getBackListener() {
		return backListener;
	}
	public void setBackListener(OnCallBackListenner backListener) {
		this.backListener = backListener;
	}
	public String getAudioType() {
		return audioType;
	}
	public void setAudioType(int audioType) {
		this.audioType = audioType < 10 ? "0"+audioType : ""+audioType;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setTitle(int resid) {
		this.title = App.getInstance().getResources().getString(resid);
	}
	public int getTimeOut() {
		return timeOut;
	}
	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	} 
	
	public byte[] getCmd() {
		return cmd;
	}
	public void setCmd(byte[] cmd) {
		this.cmd = cmd;
	}
	public void backData(byte[] buffer){
		if(this.backListener != null){
			backListener.backData(buffer);
		}
	}

	/**
	 * 数据不合法
	 */
	public void illegalData(){
		Log.d(TAG, AssitTool.getString(cmd, AssitTool.UTF_8)+":illegal");
		String msg = cmd + BackCode.CODE_12;
		backData(msg.getBytes());
	}
	
	public void legalData(){
		if(this.backListener != null){
			Log.d(TAG, cmd+"：legal");
			backListener.isLegal(cmd);
		}
	}
	
	public abstract void setData(byte[] buffer,byte[] cmd) ;
	
	public byte[] getTotalFileData(List<FileData> fileDatas){
		if(fileDatas == null || fileDatas.isEmpty()){
			return null;
		}
		int arrayLen = 0;
		for(int i = 0; i < fileDatas.size(); i++){
			arrayLen += fileDatas.get(i).getDataLen();
		}
		byte[] buffer = new byte[arrayLen];
		int temLen = 0;
		for(int i = 0; i < fileDatas.size(); i++){
			FileData fileData = fileDatas.get(i);
			System.arraycopy(fileData.getBuffer(), 0, buffer, temLen, fileData.dataLen);
			temLen += fileData.getDataLen();
		}
		return buffer;
	}
	public static class FileData{
		private byte[] buffer;
		private int dataLen;
		public FileData(int dataLen){
			this.dataLen = dataLen;
			buffer = new byte[dataLen];
		}
		public byte[] getBuffer() {
			return buffer;
		}
		public void setBuffer(byte[] buffer) {
			System.arraycopy(buffer, 0, this.buffer, 0, dataLen);
		}
		public int getDataLen() {
			return dataLen;
		}
		public void setDataLen(int dataLen) {
			this.dataLen = dataLen;
		}
		
		
	}
}
