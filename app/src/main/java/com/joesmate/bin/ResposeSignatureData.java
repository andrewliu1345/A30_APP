package com.joesmate.bin;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

import android.graphics.Bitmap;
import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;

public class ResposeSignatureData extends BaseData{

	private static ResposeSignatureData resposeSignatureData;
	public static final String TYPE_IMG = "1";
	public static final String TYPE_TRACK = "2";
	static final float MAX_LEN = 4000f;
	private byte[] resposeData;
	private static int curIndex;
	private static int totalNum;
	private static int packIndex;
	String backCode = null;
	public static ResposeSignatureData getInstance(){
		if(resposeSignatureData == null){
			resposeSignatureData = new ResposeSignatureData();
		}
		return resposeSignatureData;
	}
	public void setResposeData(byte[] data,int length){
		
	}
	public void setResposeBitmap(Bitmap bitmap){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		resposeData = baos.toByteArray();
		
		//setData(CMD.SR, CMD.SR);
	}
	
	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		if(buffer == null){
			Log.e(TAG, "buffer is null");
			return;
		}
		
		if(Arrays.equals(cmd, CMD.SR) || Arrays.equals(cmd, CMD.SF)){
			if(Arrays.equals(cmd, CMD.SR)){
				backCode = Cmds.CMD_SR;
			}else if(Arrays.equals(cmd, CMD.SF)){
				backCode = Cmds.BACK_SF;
			}
			if(resposeData == null){
				return;
			}
			float resposeDataLen = resposeData.length;
			totalNum = (int) Math.ceil(resposeDataLen/MAX_LEN);
			Log.d(TAG, "totalNum："+totalNum);
			curIndex = totalNum;
			packIndex= 0;
			
		}else if(Arrays.equals(cmd, CMD.RR)){
			//TODO 如果返回码是不合法
			String  _backCode = new String(new byte[]{buffer[2],buffer[3]});
			Log.d(TAG, "RR respone："+_backCode);
			packIndex += 4000;
			curIndex -=1;
			Log.d(TAG, "curIndex:"+curIndex);
		}
		String erroCode = null;
		if(resposeData == null){
			erroCode = BackCode.CODE_12;
			backData((backCode + backCode).getBytes());
			return;
		}else{
			erroCode = BackCode.CODE_00;
		}
		
		if(curIndex > 0){
			int arraySendLen = resposeData.length - packIndex;
			arraySendLen = (int) (arraySendLen >= MAX_LEN ? MAX_LEN : arraySendLen);
			byte[] arraySend = new byte[arraySendLen];
			System.arraycopy(resposeData, packIndex, arraySend, 0, arraySendLen);
			
			byte[] arrayPack = new byte[backCode.length() + erroCode.length()+1+12+arraySend.length];
			int post = 0;
			System.arraycopy(backCode.getBytes(), 0, arrayPack, post, backCode.length());
			
			post = backCode.length();
			System.arraycopy(erroCode.getBytes(), 0, arrayPack, post, erroCode.length());
			
			post += erroCode.length();
			System.arraycopy(TYPE_IMG.getBytes(), 0, arrayPack, post, TYPE_IMG.length());
			
			post += TYPE_IMG.length();
			byte[] arrayCurIndex = AssitTool.getCount4N(curIndex);
			System.arraycopy(arrayCurIndex, 0, arrayPack, post, arrayCurIndex.length);
			
			post += arrayCurIndex.length;
			byte[] arrayTotal = AssitTool.getCount4N(totalNum);
			System.arraycopy(arrayTotal, 0, arrayPack, post, arrayTotal.length);
			
			post += arrayTotal.length;
			byte[] arrayDataLen = AssitTool.getCount4N(arraySend.length);
			System.arraycopy(arrayDataLen, 0, arrayPack, post, arrayDataLen.length);
			
			post += arrayDataLen.length;
			System.arraycopy(arraySend, 0, arrayPack, post, arraySend.length);
			backData(arrayPack);
		}else{
			
		}
		
	}


}
