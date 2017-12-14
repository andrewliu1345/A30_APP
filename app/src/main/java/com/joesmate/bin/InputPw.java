package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;

import java.util.Arrays;

public class InputPw extends BaseData{
	private static InputPw inputPw;
	SerialResponseFrame serialResponse;
	private InputPwThread inputPwThread; 
	private boolean bInputPwOk = false;
	public static InputPw getInstance(){
		if(inputPw == null){
			inputPw = new InputPw();
		}
		return inputPw;
	}


	public String getStatusContent() {
		return statusContent;
	}

	public void setStatusContent(String statusContent) {
		this.statusContent = statusContent;
	}

	public String getMainContent() {
		return mainContent;
	}

	public void setMainContent(String mainContent) {
		this.mainContent = mainContent;
	}

	public String getVoiceText() {
		return voiceText;
	}

	public void setVoiceText(String voiceText) {
		this.voiceText = voiceText;
	}

	private String statusContent ;
	private String mainContent ;
	private String voiceText ;

	public int getPasswordLen() {
		return passwordLen;
	}

	public void setPasswordLen(int passwordLen) {
		this.passwordLen = passwordLen;
	}

	private int passwordLen ;





	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		//int PwType = buffer[2];

		//byte[] inputKeyData = new byte[buffer.length-5];
		//System.arraycopy(buffer, 5, inputKeyData, 0, buffer.length-5);
		//for test


		/*Intent intent;
		intent = new Intent(AppAction.ACTION_BROADCAST_START_INPUT_PW);
		App.getInstance().sendBroadcast(intent);
		Log.d("InputPw","send broadcast: input pw");
		*/
		int pos = 2 ;
		int timeout = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
		setTimeOut(timeout);
		Log.d(TAG, "timeout: " + timeout);

		pos = pos + 4 ;
        int statusContentlen = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
		Log.d(TAG, "statusContentlen: " + statusContentlen);

		pos = pos + 4 ;
		byte[] arraystatusContentData = new byte[statusContentlen] ;
		System.arraycopy(buffer, pos, arraystatusContentData, 0, statusContentlen);
		statusContent =  AssitTool.getString(arraystatusContentData, AssitTool.UTF_8);
		Log.d(TAG, "statusContent:" + statusContent);

		pos = pos +  statusContentlen ;
		int mainContentlen = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
		Log.d(TAG, "mainContentlen: " + mainContentlen);

		pos = pos + 4 ;
		byte[] arraymainContentData = new byte[mainContentlen] ;
		System.arraycopy(buffer, pos, arraymainContentData, 0, mainContentlen);
		mainContent =  AssitTool.getString(arraymainContentData, AssitTool.UTF_8);
		Log.d(TAG, "mainContent:" + mainContent);

		pos = pos + mainContentlen ;
		int voiceTextlen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
		Log.d(TAG, "voiceTextlen: " + voiceTextlen);


		pos = pos + 4 ;
		byte[] arrayvoiceTextData = new byte[voiceTextlen] ;
		System.arraycopy(buffer,pos,arrayvoiceTextData,0,voiceTextlen);
		voiceText =  AssitTool.getString(arrayvoiceTextData, AssitTool.UTF_8);
		Log.d(TAG, "voiceText:" + voiceText);

		pos = pos + voiceTextlen ;
		passwordLen = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1]});
		Log.d(TAG, "passwordLen:" + passwordLen);



		byte[] inputKeyData = new byte[]{
				0x01,0x00
		};
		final byte [] wbyte = new SerialRequestFrame().sendSerialCmd(CMD.SERIAL_COMMAND_INPUTPW,inputKeyData);
		serialResponse = new SerialResponseFrame();

		SerialUtil.getInstance().getSerialPort().write(wbyte, wbyte.length);

		legalData();
		
		inputPwThread = new InputPwThread();
		inputPwThread.start();
		try {
			inputPwThread.join();
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if(true == bInputPwOk)
		{  //input pw again		
/*			Log.d("InputPw","input again");
			Intent intent1;
			intent1 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_AGAIN);
			App.getInstance().sendBroadcast(intent1);
			Log.d("InputPw","send broadcast: input pw again");
			
			byte[] inputAgainKeyData = new byte[]{
					0x02,0x00
				};
				
				final byte [] wbyte1 = new SerialRequestFrame().sendSerialCmd(CMD.SERIAL_COMMAND_INPUTPW,inputAgainKeyData);
				//serialResponse = new SerialResponseFrame();
				try {
					SerialUtil.getInstance().getSerialPort().getOutputStream().write(wbyte1);
				} catch (IOException e) {
					e.printStackTrace();
				}
				InputPwThread inputPwThread1 = new InputPwThread();
				inputPwThread1.start();
*/

            //App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_CLEAR_SCREEN));

		}
	}

	public class InputPwThread extends Thread {

        @Override
        public void run() {
			SerialUtil.getInstance().setStop(false);
			byte[] rbyte = null;
			SerialUtil.getInstance().getmReadThread().run();
			SerialResponseFrame.lock.lock();
			rbyte = serialResponse.getRbyte();
			SerialUtil.getInstance().setStop(true);
			SerialResponseFrame.lock.unlock();

			SerialUtil.getInstance().setStop(false);
			//byte[] rbyte = null;
			SerialUtil.getInstance().getmReadThread().run();
			SerialResponseFrame.lock.lock();
			rbyte = serialResponse.getRbyte();
			SerialUtil.getInstance().setStop(true);
			SerialResponseFrame.lock.unlock();

			byte[] arrayData = new byte[rbyte.length+4];
			int pos = 0;
			//int dataLen = Cmds.CMD_IP.getBytes().length;
			System.arraycopy(Cmds.CMD_IP.getBytes(), 0, arrayData, pos, 2); //copy cmd IK

			pos += 2;
			int pwKeyLen = rbyte.length - 2;
			byte[] arraypwKey_Len = AssitTool.getCount42(pwKeyLen); //pwKey len
			System.arraycopy(arraypwKey_Len, 0, arrayData, pos, 2);

			Log.d("Input Pw", "" + Arrays.toString(rbyte));
			pos += 2;
			int dataLen = rbyte.length-2;
			System.arraycopy(rbyte, 2, arrayData, pos, dataLen);
			Log.d("Input Pw22", "" + Arrays.toString(arrayData));

			byte[] isOK = new byte[2];
			bInputPwOk = false;
			pos += dataLen;
			Log.d("InputPw","rbyte lenth "+ rbyte.length);
			for(int i = 0;i<rbyte.length-1;i++) //search "OK"
			{
				System.arraycopy(rbyte, i, isOK, 0, 2);
				if(Arrays.equals(isOK,Cmds.BACK_OK.getBytes()))
				{
					bInputPwOk = true;
					Log.d("InputPw","Ok i " + i);
					break;
				}
			}
			if(true == bInputPwOk)
			{
				System.arraycopy(CMD.CODE_OK, 0, arrayData, pos, 2);
				Log.d("InputPw", "Ok pos" + pos);
			}
			else
			{
				System.arraycopy(CMD.CODE_ER, 0, arrayData, pos, 2);
				Log.d("InputPw", "err pos" + pos);
			}
			Log.d("InputPw", "" + Arrays.toString(arrayData));
			backData(arrayData);        	
        }
	}
}
