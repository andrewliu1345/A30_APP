package com.joesmate.bin;

import android.util.Log;

import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;


public class InputPwChar{
	private static final String TAG = "InputPwChar";
	private static InputPwChar inputPwChar;
	public static InputPwChar getInstance(){
		if(inputPwChar == null){
			inputPwChar = new InputPwChar();
		}
		return inputPwChar;
	}

	public void inputChar() {
				
		byte[] inputCharCmd =new byte[]{(byte)0x81};
		final SerialResponseFrame serialResponse = new SerialResponseFrame();

		SerialUtil.getInstance().getSerialPort().write(inputCharCmd, inputCharCmd.length);

		new Thread(
				new Runnable(){
					@Override
					public void run() {
							SerialUtil.getInstance().setStop(false);							
							SerialUtil.getInstance().getmReadThread().run();				
							SerialResponseFrame.lock.lock();
							serialResponse.getRbyte();							
							SerialUtil.getInstance().setStop(true);
							SerialResponseFrame.lock.unlock();
							Log.d(TAG,"exit!");
					}
				}
			).start();
		
	}
   
}
