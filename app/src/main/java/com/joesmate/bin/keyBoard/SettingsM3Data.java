package com.joesmate.bin.keyBoard;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

public class SettingsM3Data extends BaseData{
	private String setParam;
	private static SettingsM3Data m3Data;
	public static SettingsM3Data getInstance(){
		if(m3Data == null){
			m3Data = new SettingsM3Data();
		}
		return m3Data;
	}

	public byte[] arrayParam = new byte[6];
 	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		// TODO Auto-generated method stub
		setCmd(cmd);
		if(Arrays.equals(Cmds.CMD_WC.getBytes(), cmd)){
			String send = Cmds.CMD_WC + setParam;
			backData(send.getBytes());
		}else if(Arrays.equals(Cmds.CMD_RC.getBytes(), cmd)){
			backData(Cmds.CMD_RC.getBytes());
		}else if(Arrays.equals(Cmds.BACK_RC.getBytes(), cmd)){
			int pos = 2;
			String erro = AssitTool.getString(new byte[]{buffer[pos], buffer[pos+1]},AssitTool.UTF_8);
			Log.d(TAG, "erro："+erro);
			pos = 4;
			System.arraycopy(buffer, pos, arrayParam, 0, arrayParam.length);
			for(int i = 0 ; i < arrayParam.length;i++){
				arrayParam[i] -= 48;
				Log.d(TAG, "arrayParam:["+i+"]"+arrayParam[i]);
			}
			legalData();
			if(BackCode.CODE_00.equals(erro)){
			}
		}
	}

	public String getSetParam() {
		return setParam;
	}

	public void setSetParam(String setParam) {
		this.setParam = setParam;
	}
	

}
