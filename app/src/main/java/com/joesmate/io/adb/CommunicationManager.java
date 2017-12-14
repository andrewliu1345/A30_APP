package com.joesmate.io.adb;

import android.content.Intent;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.Cmds;
import com.joesmate.bin.InputCharacterData;
import com.joesmate.bin.InteractiveMsgData;
import com.joesmate.bin.QuestionData;
import com.joesmate.io.adb.SocketService.OnSocketListener;
import com.joesmate.listener.OnCallBackListenner;

public class CommunicationManager{
	public static final String TAG = "CommunicationManager";
	SocketService socketService;

	public CommunicationManager() {
		Log.d(TAG, "onCreate =================================");
		socketService = new SocketService(App.SOCKE_PORT, onSocketListener);
	}

	public void write(byte[] buffer) {
		socketService.write(buffer);
	}
	
	private OnSocketListener onSocketListener = new OnSocketListener() {
		
		@Override
		public void readBuffer(byte[] buffer, int lenght) {

			byte[] temBuf = new byte[lenght];
			System.arraycopy(buffer, 0, temBuf, 0, lenght);
/*			byte [] dataBuf = CheckData.getDataPackage(temBuf);
			
			if(dataBuf == null){
				return;
			}
			
			String cmd = CheckData.getCmd(dataBuf);
			Log.d(TAG, "CMD: "+cmd+" ==================================");
			if(cmd == null){
				return;
			}
			
			if(Cmds.CMD_JH.equals(cmd)){
				sendCmd(cmd);
				InteractiveMsgData.getInstance().setData(buffer, cmd,backListener);
				InteractiveMsgData.getInstance().setBack(BackCode.CODE_00, BackCode.CODE_00);
			}*/
			
			
			testInputMessage(temBuf,lenght);
			
		
		}
		
		@Override
		public void iOException() {
			
		}
	};
	private void sendCmd(String cmd){
		Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
		intent.putExtra(AppAction.KEY_BROADCAST_CMD, cmd);
		App.getInstance().sendBroadcast(intent);
	}
	public String testInputMessage(byte[] buffer, int numReadedBytes) {
		String msg = "";
		try {
			msg = new String(buffer, 0, numReadedBytes, "utf-8");
		} catch (Exception e) {
			Log.v(TAG, Thread.currentThread().getName() + "---->"
					+ "readFromSocket error");
			e.printStackTrace();
		}
		Log.v(TAG, "msg=" + msg);
		if(Cmds.CMD_JH.equals(msg)){
			InteractiveMsgData.getInstance().setBackListener(backListener);
		}else if(Cmds.CMD_PJ.equals(msg)){
			
		}else if(Cmds.CMD_IN.equals(msg)){
			InputCharacterData.getInstance().setBackListener(backListener);
		}else if(Cmds.CMD_QS.equals(msg)){
			QuestionData.getInstance().setBackListener(backListener);
		}
		sendCmd(msg);
		return msg;
	}

	private OnCallBackListenner backListener = new OnCallBackListenner() {
		
		@Override
		public void isLegal(byte[] cmd) {
			if(Cmds.CMD_JH.equals(cmd)){
				
			}
		}
		
		@Override
		public void backData(byte[] buffer) {
			Log.d(TAG, "backData");
			write(buffer);
		}
	};
	

}
