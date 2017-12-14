package com.joesmate.bin.icbc;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.AssitTool.AddFile.OnAddFileListener;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.FileInf;
import com.joesmate.bin.BaseData;

import java.io.File;
import java.util.Arrays;

public class ICBCSignData extends BaseData {

	private static ICBCSignData iCBCSignData;
	private int curIndex, totalNum;
	private byte[] dataBuffer;



	private String fileName;
	private String statusContent ;
	private int statusContentDataLen;
	private int signType ; //0  不需要电子签名  1  需要电子签名

	public int getSignSyncType() {
		return signSyncType;
	}

	public void setSignSyncType(int signSyncType) {
		this.signSyncType = signSyncType;
	}

	private int signSyncType ; // 0: 同步模式  1:异步模式

	public int getRemainTime() {
		return remainTime;
	}

	public void setRemainTime(int remainTime) {
		this.remainTime = remainTime;
	}

	private int  remainTime ;
	private String voiceText ;
	private int voiceTextLen ;
	private int FileTotalSize ;
	private int fileNameLen ;
	private int curdataLen ;

	private int pdfPageNumber ;
	private int signX ;
	private int signY ;
	private int signWidth ;
	private int signHeight ;

	public int getPicWidth() {
		return picWidth;
	}

	public void setPicWidth(int picWidth) {
		this.picWidth = picWidth;
	}

	private int picWidth ;

	public int getPicHeight() {
		return picHeight;
	}

	public void setPicHeight(int picHeight) {
		this.picHeight = picHeight;
	}

	private int picHeight ;

	public boolean isShowSignedPdf() {
		return showSignedPdf;
	}

	public void setShowSignedPdf(boolean showSignedPdf) {
		this.showSignedPdf = showSignedPdf;
	}

	private boolean showSignedPdf  = false;


	byte[] arraystatusContentData;
	byte[] arrayfileName;
	byte[] arrayFileData;
	byte[] arrayvoiceText;


	public String getFileName() {
			return  fileName ;
	}

	public String getPDFPath()
	{
			Log.d(TAG, "getPDFPath:" + FileInf.PDF+"/"+ fileName);
			return  FileInf.PDF+"/"+ fileName ;
	}


	public String getPDFSignPath()
	{
		Log.d(TAG, "getPDFSignPath:" + FileInf.PDF+"/"+ AssitTool.getFileName(fileName)+"_sign.pdf");
		return  FileInf.PDF+"/"+ AssitTool.getFileName(fileName)+"_sign.pdf" ;
	}

	public  String getSignImagePath()
	{
		Log.d(TAG, "getSignImagePath:" + FileInf.PDF+"/"+ AssitTool.getFileName(fileName)+"_sign.png");
		return FileInf.PDF+"/"+ AssitTool.getFileName(fileName)+"_sign.png" ;
	}
	public int getSignX() {
		return signX;
	}

	public void setSignX(int signX) {
		this.signX = signX;
	}

	public int getSignY() {
		return signY;
	}

	public void setSignY(int signY) {
		this.signY = signY;
	}

	public int getSignWidth() {
		return signWidth;
	}

	public void setSignWidth(int signWidth) {
		this.signWidth = signWidth;
	}

	public int getSignHeight() {
		return signHeight;
	}

	public void setSignHeight(int signHeight) {
		this.signHeight = signHeight;
	}

	public int getPdfPageNumber() {
        if(pdfPageNumber <= 0 )
            pdfPageNumber = 1 ;
		return pdfPageNumber;
	}

	public void setPdfPageNumber(int pdfPageNumber) {
		this.pdfPageNumber = pdfPageNumber;
	}

	public  String getTitleA()
	{
		 String[] titles =  statusContent.split("&");
		 if(titles.length >= 1)
		 {
			 return  titles[0];
		 }
		else
		 {
			 return  statusContent ;
		 }
	}

	public String getTitleB()
	{
		String[] titles =  statusContent.split("&");
		if(titles.length >= 2)
		{
			return  titles[1];
		}
		else
		{
			return  null ;
		}
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getStatusContent() {
		return statusContent;
	}

	public void setStatusContent(String statusContent) {
		this.statusContent = statusContent;
	}

	public int getSignType() {

		if(showSignedPdf)
		{
			return  AssitTool.PAGE_PDF_SHOWSIGN ;
		}
		else {
			if (signHeight == 0 || signWidth == 0) {
				return AssitTool.PAGE_PDF_NOSIGN;
			} else {
				return AssitTool.PAGE_PDF_SIGN;
			}
		}
	}

	public void setSignType(int _signType) {
		this.signType = _signType;
	}

	public String getVoiceText() {
		return voiceText;
	}

	public void setVoiceText(String voiceText) {
		this.voiceText = voiceText;
	}

	public int getFileTotalSize() {
		return FileTotalSize;
	}

	public void setFileTotalSize(int fileTotalSize) {
		FileTotalSize = fileTotalSize;
	}

	public static ICBCSignData getInstance() {
		if (iCBCSignData == null) {
			iCBCSignData = new ICBCSignData();
		}
		return iCBCSignData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		// TODO Auto-generated method stub
		setCmd(cmd);
		int pos = 0;
		if (Arrays.equals(Cmds.CMD_SG.getBytes(), cmd)) {
			//web应用下载
			pos = 2;

			//signType = (int)buffer[pos];
			//Log.d(TAG, "signType:" + signType);

			//pos = pos+1;


			signSyncType = buffer[pos];
			Log.d(TAG, "signSyncType:" + signSyncType);


			pos = pos + 1 ;
			statusContentDataLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1],buffer[pos+2],buffer[pos+3] });
			Log.d(TAG, "statusContentDataLen:" + statusContentDataLen);

			pos = pos + 4;
			fileNameLen = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			//fileNameLen =8;
			Log.d(TAG, "fileNameLen:" + fileNameLen);


			pos = pos + 4;
			FileTotalSize = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3],buffer[pos+4],buffer[pos+5],
					buffer[pos+6],buffer[pos+7],buffer[pos+8],buffer[pos+9],buffer[pos+10],buffer[pos+11]});
			Log.d(TAG, "FileTotalSize:" + FileTotalSize);

            pos = pos + 12;
			voiceTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "voiceTextLen:" + voiceTextLen);

			pos = pos + 4 ;
			int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
			setTimeOut(timeOut);
			Log.d(TAG, "timeOut:" + timeOut);

			pos = pos + 3 ;
			pdfPageNumber =  AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "pdfPageNumber:" + pdfPageNumber);

			pos = pos + 4 ;
			signX = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "signX:" + signX);

			pos = pos + 4 ;
			signY = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "signY:" + signY);

			pos = pos + 4 ;
			signWidth = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "signWidth:" + signWidth);

			pos = pos + 4 ;
			signHeight = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
			Log.d(TAG, "signHeight:" + signHeight);

			pos = pos + 4 ;
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
				arraystatusContentData = new byte[statusContentDataLen] ;
				System.arraycopy(dataBuffer,pos1,arraystatusContentData,0,statusContentDataLen);
				statusContent =  AssitTool.getString(arraystatusContentData, AssitTool.UTF_8);
				Log.d(TAG, "statusContent:" + statusContent);

				pos1 = pos1 +  statusContentDataLen;
				arrayfileName = new byte[fileNameLen];
				System.arraycopy(dataBuffer,pos1,arrayfileName,0,fileNameLen);
				fileName =  AssitTool.getString(arrayfileName, AssitTool.UTF_8);
				Log.d(TAG, "fileName:" + fileName);

				pos1 = pos1 + fileNameLen ;
				arrayFileData = new byte[FileTotalSize];
				System.arraycopy(dataBuffer,pos1,arrayFileData,0,FileTotalSize);


				pos1 = pos1 + FileTotalSize ;
				arrayvoiceText = new byte[voiceTextLen];
				System.arraycopy(dataBuffer,pos1,arrayvoiceText,0,voiceTextLen);
				voiceText = AssitTool.getString(arrayvoiceText,AssitTool.UTF_8);
				Log.d(TAG, "voiceText:" + voiceText);

				Log.d(TAG, "to save " + fileName);

				//如果是 传的pdf 和签名 文件存在，就删除该些文件
				AssitTool.deleteFile(getPDFPath());
				AssitTool.deleteFile(getSignImagePath());
				AssitTool.AddFile addFile = new AssitTool.AddFile(getPDFPath(),
							arrayFileData);
				addFile.setOnOperationFileListener(addFileListener);
				sendConfirmCode(Cmds.CMD_SG);

			}else{
				sendConfirmCode(Cmds.CMD_SG);
			}

			//AssitTool.setProgressbarState(totalNum,curIndex);

		}

	}
	OnAddFileListener addFileListener = new OnAddFileListener() {
		
		@Override
		public void complete() {
			if(Arrays.equals(Cmds.CMD_SG.getBytes(), getCmd())){
				// 文件是 pdf 文档
					File file = new File(getPDFPath());
					if(file.exists())
					{
						Log.d(TAG,"save file complete ...............");
						legalData();
					}
			}
				//sendConfirmCode(Cmds.BACK_DT);
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

}
