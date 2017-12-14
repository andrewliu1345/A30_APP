package com.joesmate.bin.sdcs;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.AssitTool.AddFile.OnAddFileListener;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.FileInf;
import com.joesmate.bin.BaseData;

import java.io.File;
import java.util.Arrays;

public class SDCSConfirmPDF extends BaseData {

	private static SDCSConfirmPDF iCBCSignData;
	private int curIndex, totalNum;
	private byte[] dataBuffer;


	public int getIsUserConfirm() {
		return isUserConfirm;
	}

	public void setIsUserConfirm(int isUserConfirm) {
		this.isUserConfirm = isUserConfirm;
	}

	private  int isUserConfirm;
	private  int fileNameLen ;
	private  int FileTotalSize ;
	private int voiceTextLen ;
	private int leftTextLen;
	private int rightTextLen;
	private int curdataLen ;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private String fileName;

	public String getVoiceText() {
		return voiceText;
	}

	public void setVoiceText(String voiceText) {
		this.voiceText = voiceText;
	}

	private String voiceText ;

	public String getLeftText() {
		return leftText;
	}

	public void setLeftText(String leftText) {
		this.leftText = leftText;
	}

	private String leftText ;

	public String getRightText() {
		return rightText;
	}

	public void setRightText(String rightText) {
		this.rightText = rightText;
	}

	private String rightText;

	byte[] arrayfileName;

	public byte[] getArrayFileData() {
		return arrayFileData;
	}

	public void setArrayFileData(byte[] arrayFileData) {
		this.arrayFileData = arrayFileData;
	}

	byte[] arrayFileData;
	byte[] arrayvoiceText;
	byte[] arrayLeftText;
	byte[] arrayRightText;

	public String getPDFPath()
	{
			Log.d(TAG, "getPDFPath:" + FileInf.PDF+"/"+ fileName);
			return  FileInf.PDF+"/"+ fileName ;
	}


	public static SDCSConfirmPDF getInstance() {
		if (iCBCSignData == null) {
			iCBCSignData = new SDCSConfirmPDF();
		}
		return iCBCSignData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		// TODO Auto-generated method stub
		setCmd(cmd);
		int pos = 0;
		if (Arrays.equals(Cmds.CMD_CP.getBytes(), cmd)) {
			pos = 2;

			isUserConfirm = buffer[pos];
			Log.d(TAG, "isUserConfirm:" + isUserConfirm);
			pos = pos + 1 ;

			fileNameLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1],buffer[pos+2],buffer[pos+3] });
			Log.d(TAG, "fileNameLen:" + fileNameLen);
			pos = pos + 4;

			FileTotalSize = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3],buffer[pos+4],buffer[pos+5],buffer[pos+6],
			buffer[pos+7],buffer[pos+8],buffer[pos+9],buffer[pos+10],buffer[pos+11]});
			Log.d(TAG, "FileTotalSize:" + FileTotalSize);
			pos = pos + 12;

			voiceTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "voiceTextLen:" + voiceTextLen);
			pos = pos + 4;

			leftTextLen  =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG,"leftTextLen:" + leftTextLen);
			pos = pos + 4 ;

			rightTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG,"rightTextLen:"+ rightTextLen);
			pos = pos + 4 ;

			int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
			setTimeOut(timeOut);
			Log.d(TAG, "timeOut:" + timeOut);
			pos = pos + 3 ;

			curIndex =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "curIndex:" + curIndex);
			pos = pos + 4 ;

			totalNum = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "totalNum:" + totalNum);
			pos = pos + 4 ;

			curdataLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });
			Log.d(TAG, "curdataLen:" + curdataLen);
			pos = pos + 4 ;

			byte[] receiveBuf = new byte[curdataLen];
			System.arraycopy(buffer, pos, receiveBuf, 0, receiveBuf.length);

			if (totalNum == curIndex) {
				// 包序号采用倒序,序号与总包个数相同则是新包
				fileDatas.clear();
			}

			FileData fileData = new FileData(receiveBuf.length);
			fileData.setBuffer(receiveBuf);
			fileDatas.add(fileData);
			if (curIndex == 1) {
				Log.d(TAG, "curIndex:" + curIndex);
				dataBuffer = getTotalFileData(fileDatas);
				Log.d(TAG, "dataBuffer len:" + dataBuffer.length);
				fileDatas.clear();

				int pos1 = 0 ;
				arrayfileName = new byte[fileNameLen] ;
				System.arraycopy(dataBuffer,pos1,arrayfileName,0,fileNameLen);
				fileName =  AssitTool.getString(arrayfileName, AssitTool.UTF_8);
				Log.d(TAG, "fileName:" + fileName);
				pos1 = pos1 +  fileNameLen;


				arrayFileData = new byte[FileTotalSize];
				System.arraycopy(dataBuffer,pos1,arrayFileData,0,FileTotalSize);
				pos1 = pos1 + FileTotalSize;


				arrayvoiceText = new byte[voiceTextLen];
				System.arraycopy(dataBuffer,pos1,arrayvoiceText,0,voiceTextLen);
				voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
				Log.d(TAG, "arrayvoiceText:" + voiceText);
				pos1 = pos1 + voiceTextLen;


				arrayLeftText  = new byte[leftTextLen];
				System.arraycopy(dataBuffer,pos1,arrayLeftText,0,leftTextLen);
				leftText =  AssitTool.getString(arrayLeftText, AssitTool.UTF_8);
				Log.d(TAG, "leftText:" + leftText);
				pos1 = pos1 + leftTextLen;

				arrayRightText  = new byte[rightTextLen];
				System.arraycopy(dataBuffer,pos1,arrayRightText,0,rightTextLen);
				rightText =  AssitTool.getString(arrayRightText, AssitTool.UTF_8);
				Log.d(TAG, "rightText:" + rightText);
				pos1 = pos1 + rightTextLen;


				//如果是 传的pdf 和签名 文件存在，就删除该些文件
				/*AssitTool.deleteFile(getPDFPath());
				AssitTool.AddFile addFile = new AssitTool.AddFile(getPDFPath(),
							arrayFileData);
				addFile.setOnOperationFileListener(addFileListener);*/
				sendConfirmCode(Cmds.CMD_CP);
				legalData();

			}else{
				sendConfirmCode(Cmds.CMD_CP);
			}


		}

	}
	OnAddFileListener addFileListener = new OnAddFileListener() {
		
		@Override
		public void complete() {
			if(Arrays.equals(Cmds.CMD_CP.getBytes(), getCmd())){
				// 文件是 pdf 文档
					File file = new File(getPDFPath());
					if(file.exists())
					{
						Log.d(TAG,"save file complete ...............");
						legalData();
					}
			}
		}
	};

	private void sendConfirmCode(String backCmd) {
		Log.d(TAG, "sendConfirmCode");
		String backCode = backCmd + BackCode.CODE_80;
		backData(backCode.getBytes());
	}
	
	public void sendResult(int result){
		String backCode = Cmds.CMD_CP + BackCode.CODE_00;
		byte[] data = new byte[5];
		System.arraycopy(backCode.getBytes(),0,data,0,4);
		data[4] = (byte)result;
		backData(data);
	}

}
