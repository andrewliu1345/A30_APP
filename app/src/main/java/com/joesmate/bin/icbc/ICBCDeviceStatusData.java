package com.joesmate.bin.icbc;

import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;


public class ICBCDeviceStatusData extends BaseData {

	private static ICBCDeviceStatusData iCBCDeviceStatusData;
	
	public static ICBCDeviceStatusData getInstance() {
		if (iCBCDeviceStatusData == null) {
			iCBCDeviceStatusData = new ICBCDeviceStatusData();
		}
		return iCBCDeviceStatusData;
	}
	
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		
		byte[] arrayData = new byte[2 + 1];

		int pos = 0;
		System.arraycopy(Cmds.CMD_GS.getBytes(), 0, arrayData, pos,2);

		backData(arrayData);
		
	}
}
