package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.AssitTool.AddFile.OnAddFileListener;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.FileInf;

import java.util.Arrays;

public class AddWebFileData extends BaseData {

	private static AddWebFileData addWebFileData;
	private int curIndex, totalNum;
	private byte[] dataBuffer;
	private String fileName;
	private int homePage;
	private String homeFile;

	public static AddWebFileData getInstance() {
		if (addWebFileData == null) {
			addWebFileData = new AddWebFileData();
		}
		return addWebFileData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		// TODO Auto-generated method stub
		setCmd(cmd);
		int pos = 0;
		if (Arrays.equals(Cmds.CMD_DH.getBytes(), cmd)) {
			//web应用下载
			pos = 2;

			int fileNum = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1] });
			Log.d(TAG, "fileNum:" + fileNum);

			pos = 4;
			int fileIndex = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1] });
			Log.d(TAG, "fileIndex:" + fileIndex);

			pos = 6;
			homePage = AssitTool.getArrayCount(new byte[] { buffer[pos] });
			Log.d(TAG, "homePage:" + homePage);

			pos = 7;
			int fileNameLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2] });
			Log.d(TAG, "fileNameLen:" + fileNameLen);

			pos = 10;
			byte[] arrayFileName = new byte[fileNameLen];
			System.arraycopy(buffer, pos, arrayFileName, 0, fileNameLen);
			fileName = AssitTool.getString(arrayFileName, AssitTool.UTF_8);
			Log.d(TAG, "fileName:" + fileName);
			
			if(homePage == 1){
				homeFile = FileInf.HTML+"/"+fileName;
				Log.d(TAG, "homeFile:" + homeFile);
			}

			pos += fileNameLen;
			curIndex = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });
			Log.d(TAG, "curIndex：" + curIndex);

			pos += 4;
			totalNum = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });
			Log.d(TAG, "totalNum：" + totalNum);

			pos += 4;
			int dataLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });

			pos += 4;
			byte[] receiveBuf = new byte[dataLen];
			System.arraycopy(buffer, pos, receiveBuf, 0, receiveBuf.length);
			if (totalNum == curIndex) {
				// 包序号采用倒序,序号与总包个数相同则是新包
				fileDatas.clear();
			}
			FileData fileData = new FileData(receiveBuf.length);
			fileData.setBuffer(receiveBuf);
			fileDatas.add(fileData);
			if (curIndex == 1) {
				dataBuffer = getTotalFileData(fileDatas);
				fileDatas.clear();
				Log.d(TAG, "to save " + fileName);
				AssitTool.AddFile addFile = new AssitTool.AddFile(FileInf.HTML+"/"+fileName,
						dataBuffer);
				addFile.setOnOperationFileListener(addFileListener);
			}else{
				sendConfirmCode(Cmds.BACK_DH);
			}

		}else if(Arrays.equals(Cmds.CMD_HH.getBytes(), cmd)){
			//交互
			pos = 2;
			//语音
			
			pos = 4;
			int timeOut = AssitTool.getArrayCount(new byte[] {buffer[pos],buffer[pos+1]});
			setTimeOut(timeOut);
			legalData();
			
		}else if(Arrays.equals(Cmds.CMD_DT.getBytes(), cmd)){
			//html片段下载
			pos = 2;
			
			curIndex = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });
			Log.d(TAG, "curIndex：" + curIndex);

			pos += 4;
			totalNum = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });
			Log.d(TAG, "totalNum：" + totalNum);

			pos += 4;
			int dataLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });

			pos += 4;
			byte[] receiveBuf = new byte[dataLen];
			System.arraycopy(buffer, pos, receiveBuf, 0, receiveBuf.length);
			if (totalNum == curIndex) {
				// 包序号采用倒序,序号与总包个数相同则是新包
				fileDatas.clear();
			}
			FileData fileData = new FileData(receiveBuf.length);
			fileData.setBuffer(receiveBuf);
			fileDatas.add(fileData);
			if (curIndex == 1) {
				dataBuffer = getTotalFileData(fileDatas);
				fileDatas.clear();
				Log.d(TAG, "to save "+FileInf.HTML_HOME_PAGE);
				AssitTool.AddFile addFile = new AssitTool.AddFile(FileInf.HTML,FileInf.HTML_HOME_PAGE,
						dataBuffer);
				addFile.setOnOperationFileListener(addFileListener);
			}else{
				sendConfirmCode(Cmds.BACK_DT);
			}
			
		}else if(Arrays.equals(Cmds.CMD_MM.getBytes(), cmd)){
			//交互
			pos = 2;
			//语音
			
			pos = 4;
			int timeOut = AssitTool.getArrayCount(new byte[] {buffer[pos],buffer[pos+1]});
			setTimeOut(timeOut);
			legalData();
			
		}

	}
	OnAddFileListener addFileListener = new OnAddFileListener() {
		
		@Override
		public void complete() {
			if(Arrays.equals(Cmds.CMD_DT.getBytes(), getCmd())){
				homeFile = FileInf.HTML+"/"+FileInf.HTML_HOME_PAGE;
				sendConfirmCode(Cmds.BACK_DT);
			}else if(Arrays.equals(Cmds.CMD_DH.getBytes(), getCmd())){
				sendConfirmCode(Cmds.BACK_DH);
			}
		}
	};

	private void sendConfirmCode(String backCmd) {
		Log.d(TAG, "sendConfirmCode");
		String backCode = backCmd + BackCode.CODE_00;
		backData(backCode.getBytes());
	}
	
	public void sendJhData(String erro,String json){
		String backCode =null;
		if(Arrays.equals(Cmds.CMD_HH.getBytes(), getCmd())){
			backCode = Cmds.BACK_HH;
		}else if(Arrays.equals(Cmds.CMD_MM.getBytes(), getCmd())){
			backCode = Cmds.BACK_MM;
		}	
		backCode += erro;
		if(BackCode.CODE_00.equals(erro)){
			backCode += json;
			Log.d(TAG, "json："+json);
		}
		backData(backCode.getBytes());
	}

	public String getHomeFile() {
		return homeFile;
	}

	public void setHomeFile(String homeFile) {
		this.homeFile = homeFile;
	}

	
}
