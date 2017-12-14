package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.Cmds;
import com.joesmate.FileInf;
import com.joesmate.FileInf.FileType;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class QueryFileData extends BaseData{

	private static QueryFileData queryFileData;
	private int fileType;
	public static QueryFileData getInstance(){
		if(queryFileData == null){
			queryFileData = new QueryFileData();
		}
		return queryFileData;
	}
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		
		if(Arrays.equals(Cmds.CMD_QF.getBytes(), cmd)){
			int pos = 2;
			fileType = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1]});
			Log.d(TAG, "query fileType:"+fileType);
			backQueryData();
		}
	}
	private void backQueryData(){
		String filePath = FileInf.getFilePath(fileType);
		FileType fileType1 = FileInf.getFileType(fileType);
		String queryFile = AssitTool.getQueryFile(filePath,fileType1);
		Log.d(TAG, "queryFile："+queryFile);
		String backData = Cmds.CMD_QF ;
		int queryFileLen = 0 ;
		if(queryFile != null){
			backData += queryFile;// AssitTool.getString(queryFile, AssitTool.UTF_8);
            try {
                queryFileLen = queryFile.getBytes(AssitTool.UTF_8).length;
            }catch (UnsupportedEncodingException e)
            {
                e.printStackTrace();
            }
		}
		byte[]  sendData = new byte[queryFileLen + 2 +  4 ] ;
		int pos  = 0 ;
		System.arraycopy(Cmds.CMD_QF.getBytes(),0, sendData, pos, 2);


		pos = pos + 2 ;
		//byte[] arrayQueryFileLen = AssitTool.getCount4N(queryFileLen);
		byte[] arrayQueryFileLen = AssitTool.getCount4N(queryFileLen);
		System.arraycopy(arrayQueryFileLen, 0, sendData, pos, arrayQueryFileLen.length);

		pos = pos + 4 ;
		try {
			if(queryFile != null) {
				System.arraycopy(queryFile.getBytes(AssitTool.UTF_8), 0, sendData, pos, queryFileLen);
			}
			backData(sendData);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
	public int getFileType() {
		return fileType;
	}
	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	
}
