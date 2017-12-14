package com.joesmate.fit;

import java.util.Arrays;

import android.util.Log;

import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;
import com.joesmate.bin.keyBoard.OptionResData;
import com.joesmate.bin.keyBoard.SettingsM3Data;
import com.joesmate.bin.keyBoard.ShowTextData;
import com.joesmate.bin.keyBoard.ShutDownData;
import com.joesmate.listener.OnIoListener;

public class FitBinTeller extends BaseFitBin{
	public static final String TAG = "FitBinTeller";
	public FitBinTeller(OnIoListener onIoListener) {
		super(onIoListener);
	}
	@Override
	public void setData(byte[] buffer, int length) {
		
		if (buffer == null) {
			Log.e(TAG, "package illegal");
			return;
		}
		Log.d(TAG, "<==========extractBuffer===========>");
		if(length == 1){
			Log.d(TAG, "confirm code:"+buffer[0]);
			return;
		}
		byte[] arrayCmd = { buffer[0], buffer[1] };
		String cmd = new String(arrayCmd);
		Log.d(TAG, "cmd==>"+cmd);
		BaseData baseData = getBaseData(arrayCmd);
		if (baseData != null) {
			baseData.setBackListener(this);
			try {
				baseData.setData(buffer, arrayCmd);
			} catch (Exception e) {
				Log.e(TAG, "setData Exception："+e.getMessage());
			}
		}
	}
	
	private BaseData getBaseData(byte[] arrayCmd) {
		BaseData baseData = null;
		if (Arrays.equals(Cmds.CMD_DD.getBytes(), arrayCmd)||
				Arrays.equals(Cmds.CMD_CD.getBytes(), arrayCmd)||
				Arrays.equals(Cmds.CMD_XS.getBytes(), arrayCmd)) {
			baseData = ShowTextData.getInstance();
		}else if (Arrays.equals(Cmds.CMD_WC.getBytes(), arrayCmd)||
				Arrays.equals(Cmds.BACK_WC.getBytes(), arrayCmd)||
				Arrays.equals(Cmds.CMD_RC.getBytes(), arrayCmd)||
				Arrays.equals(Cmds.BACK_RC.getBytes(), arrayCmd)) {
			baseData = SettingsM3Data.getInstance();
		}else if (Arrays.equals(Cmds.CMD_GJ.getBytes(), arrayCmd)){
			baseData = ShutDownData.getInstance();
		}else if (Arrays.equals(Cmds.CMD_ZJ.getBytes(), arrayCmd)){
			baseData = OptionResData.getInstance();
		}
		return baseData;
	}

}
