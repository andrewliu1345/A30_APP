package com.joesmate.bin;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.Cmds;
import com.joesmate.DeviceSettings;

import java.io.UnsupportedEncodingException;

public class DeviceInfData extends BaseData {

	private static DeviceInfData deviceInfData;
	String devType = "TLC";
	

	public static DeviceInfData getInstance() {
		if (deviceInfData == null) {
			deviceInfData = new DeviceInfData();
		}
		return deviceInfData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {

		String version =  DeviceSettings.getVersionName(App.getInstance().getApplicationContext());
		int devTypeLen = devType.length();
		int versionLen = version.length();
		byte[] arrayData = new byte[6 + devTypeLen + 2 + versionLen + 1];

		int pos = 0;
		System.arraycopy(Cmds.BACK_GS.getBytes(), 0, arrayData, pos,2);
		
		pos = 2;
		System.arraycopy(new byte[]{0x30, 0x30}, 0, arrayData, pos, 2);
		
		pos = 4;
		byte[] arrayDevType = AssitTool.integerToArray(devTypeLen);
		System.arraycopy(arrayDevType, 0, arrayData, pos, 2);
		
		pos = 6;
		try {
			System.arraycopy(devType.getBytes(AssitTool.UTF_8), 0, arrayData, pos, devTypeLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pos += devTypeLen;
		byte[] arrayVersion = AssitTool.integerToArray(versionLen);
		System.arraycopy(arrayVersion, 0, arrayData, pos, 2);
		
		pos += 2;
		try {
			System.arraycopy(version.getBytes(AssitTool.UTF_8), 0, arrayData, pos, versionLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		pos += versionLen;
		System.arraycopy(new byte[]{0x31}, 0, arrayData, pos, 1);
		
		backData(arrayData);
		
	}
}
