package com.joesmate.bin.sdcs;

import android.os.Build;
import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

public class ReadDeviceIdData extends BaseData {

	private static ReadDeviceIdData deviceInfData;

	public static ReadDeviceIdData getInstance() {
		if (deviceInfData == null) {
			deviceInfData = new ReadDeviceIdData();
		}
		return deviceInfData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		if (Arrays.equals(Cmds.CMD_RD.getBytes(), cmd)) {
			String data  = Cmds.CMD_RD+ BackCode.CODE_00;
			String id = Build.SERIAL;
			Log.d("bill","no:"+id);
			int idLength = id.getBytes().length;
			byte[] arrayData = new byte[4+4+idLength];
			int pos = 0 ;
			System.arraycopy(data.getBytes(),0,arrayData,pos,4);
			pos = pos + 4 ;
			System.arraycopy(AssitTool.getCount4N(idLength),0,arrayData,pos,4);
			pos = pos + 4;
			System.arraycopy(id.getBytes(),0,arrayData,pos,idLength);
			backData(arrayData);
		}
	}
}
