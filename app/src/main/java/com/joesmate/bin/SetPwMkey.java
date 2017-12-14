package com.joesmate.bin;

import android.util.Log;

import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;
import jniapi.device.signpad.SecureMod;

import java.util.Arrays;

public class SetPwMkey extends BaseData{
	private static final String TAG = "SetPwMkey";
	private static SetPwMkey setPwMkey;
	public static SetPwMkey getInstance(){
		if(setPwMkey == null){
			setPwMkey = new SetPwMkey();
		}
		return setPwMkey;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
	
		if(buffer.length < 5)
		{			
			returnConfirmCode(Cmds.CMD_MK,BackCode.CODE_01);
			return;
		}
		byte[] keyType = new byte[1];
		keyType[0] = buffer[2];
		
		byte[] mainKeyData = new byte[buffer.length-5+1];
		mainKeyData[0] = (byte) 0x04; //SM4		
		System.arraycopy(buffer, 5, mainKeyData, 1, buffer.length-5);
		printHexString(mainKeyData);
		
		if(Arrays.equals(keyType, CMD.KEY_SCREEN))
		{
			//byte[] setMKeyData = new byte[buffer.length-5];
			//System.arraycopy(buffer, 5, setMKeyData, 0, buffer.length-5);
			//printHexString(setMKeyData);			
			setScreenMKey(mainKeyData);
			return ;
		}		
		
		final byte [] wbyte = new SerialRequestFrame().sendSerialCmd(CMD.SERIAL_COMMAND_MASTERK, mainKeyData);
		printHexString(mainKeyData);
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
							
							returnSetMKeyResult(rbyte);							
					}
				}
			).start();
	}

    public static void printHexString(byte[] b)
    {
        String hexStr = "";
        for (int i = 0; i < b.length; i++)
        {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            hexStr = hexStr +hex.toLowerCase();
        }
        Log.d("SetPwMkey","hex: " + hexStr);
    }

    private void setScreenMKey(byte[] cipherPrmKey)
	{
		
    	int ret = SecureMod.setPrimKeyFromJNI(cipherPrmKey); //
		if(ret == SecureMod.RESULT_OK) {
			byte rbyte[] = new byte[2];
			System.arraycopy(Cmds.BACK_OK.getBytes(), 0, rbyte, 0, 2); //copy state OK
			returnSetMKeyResult(rbyte);			
		} else {
			returnConfirmCode(Cmds.CMD_MK,BackCode.CODE_01);
		}
	}
    
    private void returnSetMKeyResult(byte[] rbyte){
    	byte[] arrayData = new byte[4];
		int pos = 0;
		if(rbyte == null)
		{
			returnConfirmCode(Cmds.CMD_WK,BackCode.CODE_01);
			return ;
		}
		int dataLen = Cmds.CMD_MK.getBytes().length;
		System.arraycopy(Cmds.CMD_MK.getBytes(), 0, arrayData, pos, dataLen); //copy cmd MK
		
		pos += 2;
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
