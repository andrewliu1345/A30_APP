package com.joesmate.bin.keyBoard;

import com.joesmate.bin.BaseData;

public class OtherCmdData extends BaseData{

	private static OtherCmdData cmdData;
	public static OtherCmdData getInstance(){
		if(cmdData == null){
			cmdData = new OtherCmdData();
		}
		return cmdData;
	}
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		// TODO Auto-generated method stub
		
	}

}
