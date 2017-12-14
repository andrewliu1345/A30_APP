package com.joesmate.bin;

import android.util.Log;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.DeviceSettings;
import com.joesmate.FileInf;

import java.util.Arrays;

public class UpdateApkData extends BaseData {

	private static UpdateApkData updateApkData;
	private String version;
	private int curIndex, totalNum;
	private boolean boolAppend = false;
	private byte[] dataBuffer; 

	public static UpdateApkData getInstance() {
		if (updateApkData == null) {
			updateApkData = new UpdateApkData();
		}
		return updateApkData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		if (Arrays.equals(CMD.UV, cmd)) {
			setCmd(cmd);

			int pos = 2;
			int versionLen = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1]});
			pos = 4;
			byte[] arrayVersion = new byte[versionLen];
			System.arraycopy(buffer, pos, arrayVersion, 0, arrayVersion.length);
			String version = AssitTool
					.getString(arrayVersion, AssitTool.UTF_8);
			setVersion(version);
			Log.d(TAG, "version：" + version);

			pos += versionLen;
			curIndex = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });
			Log.d(TAG, "curIndex："+curIndex);
			pos += 4;
			totalNum = AssitTool.getArrayCount(new byte[] { buffer[pos],
					buffer[pos + 1], buffer[pos + 2], buffer[pos + 3] });
			Log.d(TAG, "totalNum："+totalNum);
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
			sendConfirmCode();
			//mReadProcessDia.setProgress((totalNum-curIndex)/totalNum);
			if (curIndex == 1 || (totalNum + 1 - curIndex)%1000 == 0) {
				dataBuffer = getTotalFileData(fileDatas);
				fileDatas.clear();
				Log.d(TAG, "to save apk");
				AssitTool.AddFile operationFile = new AssitTool.AddFile(
						FileInf.APK, FileInf.APK_NAME, dataBuffer,boolAppend);
				boolAppend = true;
				if(curIndex == 1)
				{
					operationFile.setOnOperationFileListener(operationFileListener);
					boolAppend = false;
				}
			}

			AssitTool.setProgressbarState(totalNum,curIndex);
		}
	}

	private AssitTool.AddFile.OnAddFileListener operationFileListener = new AssitTool.AddFile.OnAddFileListener() {

		@Override
		public void complete() {
			Log.d(TAG, "save apk over");
			legalData();
		}
	};

	private void sendConfirmCode() {
		Log.d(TAG, "sendConfirmCode");
		String backCode = Cmds.CMD_UV+BackCode.CODE_00;
		backData(backCode.getBytes());
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	public boolean isUpdateApk(){
		if(version == null){
			return false;
		}
		else if("NONE".equals(version)){
			return true;
		}else{
			String curVerison = DeviceSettings.getVersionName(App.getInstance());
			if(curVerison != null && curVerison.equals(version)){
				return false;
			}
		}
		return true;
	}

}
