package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;

import java.io.File;

public class DeleteFileData extends BaseData{

	private static DeleteFileData deleteFileData;

	private int fileType;
	public static DeleteFileData getInstance(){
		if(deleteFileData == null){
			deleteFileData = new DeleteFileData();
		}
		return deleteFileData;
	}
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		
		int pos = 2;
		fileType = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1]});
		Log.d(TAG, "delete fileType:"+fileType);
		
		pos = pos + 2;
		int receiveLen  = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
		Log.d(TAG, "receiveLen:"+receiveLen);

		pos = pos + 4;
		byte[] arraysFiles = new byte[receiveLen];
		System.arraycopy(buffer, pos, arraysFiles, 0, arraysFiles.length);
		String deleteFiles = AssitTool.getString(arraysFiles, AssitTool.UTF_8);
		Log.d(TAG, "deleteFiles:"+deleteFiles);
		File[] files = AssitTool.getDeleteFiles(fileType, deleteFiles);
		backQueryData();
		if(files != null && files.length != 0){
			new AssitTool.DeleteFile(files);
		}
		
	}
	
	private void backQueryData() {
		String backData = Cmds.CMD_DF + BackCode.CODE_00;
		backData(backData.getBytes());
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}
	
	
}
