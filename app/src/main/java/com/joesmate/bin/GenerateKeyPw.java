package com.joesmate.bin;

import java.util.Arrays;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;
import jniapi.device.signpad.SecureMod;

public class GenerateKeyPw extends BaseData{
	private static final String TAG = "GenerateKeyPw"; 
	byte [] rbyte;
	private static GenerateKeyPw generateKeyPw;
	public static GenerateKeyPw getInstance(){
		if(generateKeyPw == null){
			generateKeyPw = new GenerateKeyPw();
		}
		return generateKeyPw;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		byte[] keyType = new byte[1];
		keyType[0] = buffer[2];
		if(Arrays.equals(keyType, CMD.KEY_SCREEN))
		{
			getScreenKey();
			return ;
		}		
		final byte [] wbyte = new SerialRequestFrame().sendSingleSerialCmd(CMD.SERIAL_COMMAND_SM2PK);
		final SerialResponseFrame serialResponse = new SerialResponseFrame();

		SerialUtil.getInstance().getSerialPort().write(wbyte, wbyte.length);

		new Thread(
				new Runnable(){
					@Override
					public void run() {
							SerialUtil.getInstance().setStop(false);							
							SerialUtil.getInstance().getmReadThread().run();
							SerialResponseFrame.lock.lock();
							byte[] rbyte = serialResponse.getRbyte();							
							SerialUtil.getInstance().setStop(true);
							SerialResponseFrame.lock.unlock();
							
							returnKeyData(rbyte);
					}
				}
			).start();
		
	}

	//private void getScreenKey()
	public void getScreenKey()
	{
		int ret = SecureMod.genSM2PairFromJNI(); 
		if(ret == SecureMod.RESULT_OK) {
			Log.d(TAG, "Generate SM2 Pair OK!");			
		} else {
			Log.d(TAG, "Generate SM2 Pair Fail, ret = " + ret);
			returnConfirmCode(Cmds.CMD_GK,BackCode.CODE_01);
			return ;
		}

		byte[] baSm2Pub = new byte[64];
		ret = SecureMod.getSM2PubFromJNI(baSm2Pub); 
		if(ret == SecureMod.RESULT_OK) {	
			byte rbyte[] = new byte[2 + baSm2Pub.length];
			System.arraycopy(Cmds.BACK_OK.getBytes(), 0, rbyte, 0, 2); //copy state OK
			System.arraycopy(baSm2Pub, 0, rbyte, 2, baSm2Pub.length); //copy state OK
			returnKeyData(rbyte);			
		} else {
			Log.d(TAG, "Get SM2 Public Fail, ret = " + ret);
			returnConfirmCode(Cmds.CMD_GK,BackCode.CODE_01);
			return ;
		}
	}
	
	private void returnKeyData(byte[] rbyte){
		if(rbyte.length < 3)
		{
			returnConfirmCode(Cmds.CMD_GK,BackCode.CODE_01);
			return;
		}
		byte[] arrayData = new byte[rbyte.length+4];
		int pos = 0;
		int dataLen = Cmds.CMD_GK.getBytes().length;
		System.arraycopy(Cmds.CMD_GK.getBytes(), 0, arrayData, pos, dataLen); //copy cmd GK
		
		pos += dataLen;
		int pwKeyLen = rbyte.length-2;
		byte[] arraypwKey_Len = AssitTool.integerToArray(pwKeyLen); //pwKey len
		System.arraycopy(arraypwKey_Len, 0, arrayData, pos, 2);
		
		pos += 2;
		dataLen = rbyte.length-2;
		System.arraycopy(rbyte, 2, arrayData, pos, dataLen);
		
		pos += dataLen;
		dataLen = 2;
		byte[] isOK = new byte[2];
		System.arraycopy(rbyte, 0, isOK, 0, 2);
		if(Arrays.equals(isOK,Cmds.BACK_OK.getBytes()))
			System.arraycopy(CMD.CODE_OK, 0, arrayData, pos, dataLen);
		else
			System.arraycopy(CMD.CODE_ER, 0, arrayData, pos, dataLen);
		Log.d(TAG, "" + Arrays.toString(arrayData));
		backData(arrayData);		
	}
	
	private void returnConfirmCode(String cmd,String code){
		String backCode = cmd+code;
		backData(backCode.getBytes());
	}
}
