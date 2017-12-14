package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;

import java.util.Arrays;

import jniapi.device.signpad.SecureMod;

public class GetPwCrc extends BaseData{
	private static final String TAG = "GetPwCrc";
	private static GetPwCrc getPwCrc;
	public static GetPwCrc getInstance(){
		if(getPwCrc == null){
			getPwCrc = new GetPwCrc();
		}
		return getPwCrc;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);

		if(buffer.length != 4)
		{			
			returnConfirmCode(Cmds.CMD_CK,BackCode.CODE_01);
			return;
		}
		byte[] keyType = new byte[1];
		keyType[0] = buffer[2];
		if(Arrays.equals(keyType, CMD.KEY_SCREEN))
		{
			getScreenCRC(buffer);
			return ;
		}
		
		byte[] keyCrcData = new byte[1];
		System.arraycopy(buffer, 3, keyCrcData, 0, 1);		
		final byte [] wbyte = new SerialRequestFrame().sendSerialCmd(CMD.SERIAL_COMMAND_GETECC, keyCrcData);
		printHexString(wbyte);
				
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
							returnGetCRCResult(rbyte);
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
        Log.d(TAG,"hex: " + hexStr);
    }

    //private void getScreenCRC(byte[] keyType)
    public void getScreenCRC(byte[] keyType)
	{
    	int ret = 0;
		byte[] verifiedData = new byte[8];
		if(keyType[3] == CMD.KEY_PRIMARYPW[0])
		{
			ret = SecureMod.verifyKeyFromJNI(SecureMod.PRIMARY_KEY_TYPE, verifiedData);
		}
		else if(keyType[3] == CMD.KEY_WORKPW[0])
		{
			ret = SecureMod.verifyKeyFromJNI(SecureMod.WORKING_KEY_TYPE, verifiedData);
		}

		if(ret == SecureMod.RESULT_OK) {
			byte rbyte[] = new byte[2 + verifiedData.length];
			System.arraycopy(Cmds.BACK_OK.getBytes(), 0, rbyte, 0, 2); //copy state OK
			System.arraycopy(verifiedData, 0, rbyte, 2, verifiedData.length); //copy state OK
			returnGetCRCResult(rbyte);				
		}
		else {
			returnConfirmCode(Cmds.CMD_CK,BackCode.CODE_01);
		}
		
	}
    
    private void returnGetCRCResult(byte[] rbyte)
    {
		byte[] arrayData = new byte[rbyte.length+4];
		int pos = 0;
		int dataLen = Cmds.CMD_CK.getBytes().length;
		System.arraycopy(Cmds.CMD_CK.getBytes(), 0, arrayData, pos, dataLen); //copy cmd CK

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
		//if(isOK.toString().equalsIgnoreCase(Cmds.BACK_OK) )
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
