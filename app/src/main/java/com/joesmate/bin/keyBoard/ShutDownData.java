package com.joesmate.bin.keyBoard;

import java.util.Arrays;

import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

public class ShutDownData extends BaseData{

	private static ShutDownData data;
	public static ShutDownData getInstance(){
		if( data == null){
			data = new ShutDownData();
		}
		return data;
	}
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		if(Arrays.equals(Cmds.CMD_GJ.getBytes(), cmd)){
			confirmCode(true);
			String backCode = Cmds.BACK_GJ+BackCode.CODE_00;
			backData(backCode.getBytes());
			legalData();
		}
	}
	private void confirmCode(boolean isConfirm){
		byte[] arrayCode = new byte[1];
		if(isConfirm){
			arrayCode[0] = BackCode.CODE_ACK;
		}else {
			arrayCode[0] = BackCode.CODE_NAK;
		}
		backData(arrayCode);
	}
}
