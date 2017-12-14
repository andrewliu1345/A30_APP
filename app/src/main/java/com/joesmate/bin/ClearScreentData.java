package com.joesmate.bin;

import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.icbc.ResposeICBCSignatureData;

public class ClearScreentData extends BaseData{
	private static ClearScreentData data;
	public static ClearScreentData getInstance(){
		if(data == null){
			data = new ClearScreentData();
		}
		return data;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		ResposeICBCSignatureData.getInstance().setSignState(0);
		backData((Cmds.CMD_CS+BackCode.CODE_00).getBytes());
		legalData();
		//DeviceSettings.reboot();
	}

}
