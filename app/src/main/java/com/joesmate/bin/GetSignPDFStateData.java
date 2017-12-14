package com.joesmate.bin;

import android.content.Intent;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.icbc.ResposeICBCSignatureData;

import java.util.Arrays;

public class GetSignPDFStateData extends BaseData {

	private static GetSignPDFStateData getSignPDFStateData;

	

	public static GetSignPDFStateData getInstance() {
		if (getSignPDFStateData == null) {
			getSignPDFStateData = new GetSignPDFStateData();
		}
		return getSignPDFStateData;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		if (Arrays.equals(Cmds.CMD_ES.getBytes(), cmd)) {

			Log.d("myitm1", "CMD_ES");
			//backData();
			Log.d(TAG, "CMD_ES:" + ResposeICBCSignatureData.getInstance().getSignState());

	/*		try {
				Thread.sleep(3000);
			}catch (Exception e)
			{

			}*/
			if(ResposeICBCSignatureData.getInstance().getSignState() == 2 )
			{
				Log.d("myitm1", "send data  2 ");
				App.getInstance().fitManagerCCB.getBaseFitBin().setData(Cmds.CMD_RG.getBytes(),
						Cmds.CMD_RG.getBytes().length);
			}
		    else if(ResposeICBCSignatureData.getInstance().getSignState() == 1)
			{
				Log.d("myitm1", "send data  1 ");
               App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_MAKE_SIGN_DATA));
			}
			else
			{
				Log.d("myitm1", "send data  other ");
				SendBackCode(ResposeICBCSignatureData.getInstance().getSignState());
				//ResposeICBCSignatureData.getInstance().SendBackCode(ResposeICBCSignatureData.getInstance().getSignState());
			}
		}
		
	}

	private void backData(){
		String backCode = Cmds.CMD_ES + BackCode.CODE_00;
		backData(backCode.getBytes());
	}

	public void SendBackCode( int state)
	{
		byte[] arrayData = new byte[3];
		System.arraycopy(Cmds.CMD_RG.getBytes(), 0, arrayData, 0, 2);
		byte[] arrayState = AssitTool.getCount41(state);
		System.arraycopy(arrayState,0,arrayData,2,1);
		Log.d(TAG, "arrayData:" + AssitTool.getString(arrayData,AssitTool.UTF_8));
		backData(arrayData);
	}
}
