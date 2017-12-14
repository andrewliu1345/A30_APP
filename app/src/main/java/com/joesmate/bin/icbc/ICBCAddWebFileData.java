package com.joesmate.bin.icbc;

import android.util.Log;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.AssitTool.AddFile.OnAddFileListener;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.FileInf;
import com.joesmate.ZIP;
import com.joesmate.bin.BaseData;

import java.io.File;
import java.util.Arrays;

public class ICBCAddWebFileData extends BaseData {

	private static ICBCAddWebFileData  icbcAddWebFileData;
	private int curIndex, totalNum;
	private byte[] dataBuffer;



	private String fileName;
	private String statusContent ;
	private int statusContentDataLen;
	private int displayType ;
	private String voiceText ;
	private int voiceTextLen ;
	private int FileTotalSize ;
	private int fileNameLen ;
	private int curdataLen ;

	byte[] arraystatusContentData;
	byte[] arrayfileName;
	byte[] arrayFileData;
	byte[] arrayvoiceText;


	public String getFileName() {
			return  fileName ;
	}

	public String getFilePath()
	{
			Log.d(TAG, "getFilePath:" + FileInf.HTML+"/"+ AssitTool.getFileName(fileName)+ "/index.html");
			return  FileInf.HTML+"/"+ AssitTool.getFileName(fileName)+ "/index.html" ;
	}

    public String getFileFolderPath()
    {
            Log.d(TAG, "getFileFolderPath:" + FileInf.HTML+"/"+ AssitTool.getFileName(fileName));
            return  FileInf.HTML+"/"+ AssitTool.getFileName(fileName) ;
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

	public int getDisplayType() {
		return displayType;
	}

	public void setDisplayType(int displayType) {
		this.displayType = displayType;
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

	public static ICBCAddWebFileData getInstance() {
		if (icbcAddWebFileData == null) {
			icbcAddWebFileData = new ICBCAddWebFileData();
		}
		return icbcAddWebFileData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		// TODO Auto-generated method stub
		setCmd(cmd);
		int pos = 0;
		if (Arrays.equals(Cmds.CMD_SI.getBytes(), cmd)) {
			//web应用下载
			pos = 2;

			displayType = (int)buffer[pos];
			Log.d(TAG, "displayType:" + displayType);

			pos = pos+1;
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
				Log.d(TAG,"dataBuffer len:" +  dataBuffer.length);
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
				if( displayType == 0 || displayType == 1 ||  displayType == 2 )
				{
					//如果是 传的html 文件夹存在，就删除该文件夹
                    Log.d(TAG,"delete folder :"+  getFileFolderPath());
					AssitTool.DeleteFolder(getFileFolderPath());
				}
				AssitTool.AddFile addFile = new AssitTool.AddFile(FileInf.HTML + "/" + fileName,
							arrayFileData);
				addFile.setOnOperationFileListener(addFileListener);
				sendConfirmCode(Cmds.CMD_SI);

			}else{
				sendConfirmCode(Cmds.CMD_SI);
			}
			//AssitTool.setProgressbarState(totalNum,curIndex);
		}

	}
	OnAddFileListener addFileListener = new OnAddFileListener() {
		
		@Override
		public void complete() {
			if(Arrays.equals(Cmds.CMD_SI.getBytes(), getCmd())){
				// 文件是html 的zip 包
				if( displayType == 0 || displayType == 1 || displayType == 2  )
				{
					File file = new File(FileInf.HTML+"/"+fileName);
					if(file.exists())
					{
					 new Thread( new Runnable() {
							@Override
							public void run() {
								try {
                                    File file = new File(getFileFolderPath());
                                    if (!file.exists())
                                    {
                                        Log.d(TAG," make  dir :"+ getFileFolderPath());
                                        file.mkdir();
                                    }
									ZIP.UnZipFolder(FileInf.HTML+"/"+fileName, getFileFolderPath());

								}
								catch (Exception e)
								{
									Log.d(TAG,"unzip have  a  error:"+ e.toString());
								}
								File homeindexFile = new File(FileInf.HTML+"/"+ AssitTool.getFileName(fileName));
								if(homeindexFile.exists())
								{
									Log.d(TAG,"------- home index.html  exist ");


									//对于要反馈数据的类型特殊处理。
									if(displayType == 2)
									{
										try {
											AssitTool.copyJqueryToSdcard(App.getInstance(), getFileFolderPath() + "/jquery.js");
											Log.d(TAG, "copy js  :" + getFileFolderPath() + "/jquery.js");

											AssitTool.addScripttoIndexHtml(getFileFolderPath()+"/index.html",AssitTool.JS_SCRIPT);
											Log.d(TAG, "add script");

										}
										catch (Exception e)
										{
											Log.d(TAG,"copy js have a error :"+ e.toString());
										}

									}


									legalData();
								}
								else
								{
									Log.d(TAG,"------- home index.html not exist ");
								}

							}
						}).start();
					}
				}

				Log.d(TAG,"save file complete ...............");
			}
				//sendConfirmCode(Cmds.BACK_DT);
		}
	};

	private void sendConfirmCode(String backCmd) {
		Log.d(TAG, "sendConfirmCode");
		String backCode = backCmd + BackCode.CODE_00;
		backData(backCode.getBytes());
	}
	
	public void sendJsData(String json){
		Log.d(TAG, "sendJsData");
		String backCode = Cmds.CMD_SI + BackCode.CODE_00;
		int jsonLen = json.getBytes().length;
		Log.d(TAG, "jsonLen:"+jsonLen);
		byte[] arrayData = new byte[2+2+4+jsonLen];
		System.arraycopy(backCode.getBytes(), 0, arrayData, 0, 4);
		int pos = 4 ;
		byte[] arrayjsonLen = AssitTool.getCount4N(jsonLen);
 		System.arraycopy(arrayjsonLen,0,arrayData,pos,4);
		pos = pos + 4 ;
		System.arraycopy(json.getBytes(),0,arrayData,pos,jsonLen);
		Log.d(TAG, "arrayData:" + AssitTool.getString(arrayData,AssitTool.UTF_8));
 		backData(arrayData);
	}

}
