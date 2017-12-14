package com.joesmate.bin;

import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.DeviceSettings;

public class DevRebootData extends BaseData{
	private static DevRebootData data;
	public static DevRebootData getInstance(){
		if(data == null){
			data = new DevRebootData();
		}
		return data;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		backData((Cmds.CMD_RS+BackCode.CODE_00).getBytes());
		DeviceSettings.reboot();
	}

}
