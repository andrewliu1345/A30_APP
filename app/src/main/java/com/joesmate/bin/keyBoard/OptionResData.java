package com.joesmate.bin.keyBoard;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

public class OptionResData extends BaseData{

	private static OptionResData data;
	private int resName;
	public static OptionResData getInstance(){
		if(data == null){
			data = new OptionResData();
		}
		return data;
	}
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		int pos = 2;
		resName = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1]});
		confirmCode(true);
		legalData();
	}
	public void backCode(String erroCode){
		String backCode = Cmds.BACK_ZJ+erroCode;
		backData(backCode.getBytes());
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
	public int getResName() {
		return resName;
	}
	public void setResName(int resName) {
		this.resName = resName;
	}
	

}
