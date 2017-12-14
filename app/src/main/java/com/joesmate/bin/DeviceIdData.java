package com.joesmate.bin;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;

import java.util.Arrays;

/**
 * Created by andre on 2017/12/9 .
 */

public class DeviceIdData extends BaseData {

    private static DeviceIdData sDeviceIdData;
    public String DeviceId = "DeviceId";

    public static DeviceIdData getInstance() {
        if (sDeviceIdData == null) {
            sDeviceIdData = new DeviceIdData();
        }
        return sDeviceIdData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        SharedPreferences preferences = App.getInstance().preferences;
        setCmd(cmd);
        if (Arrays.equals(CMD.ID, cmd)) ;
        {
            int iType = buffer[2] & 0xFF;
            switch (iType) {
                case 0: {//读
                    String id = preferences.getString(DeviceId, Build.SERIAL);
                    String data = Cmds.CMD_ID + BackCode.CODE_00;
                    Log.d("bill", "no:" + id);
                    int idLength = id.getBytes().length;
                    byte[] arrayData = new byte[4 + 4 + idLength];
                    int pos = 0;
                    System.arraycopy(data.getBytes(), 0, arrayData, pos, 4);
                    pos += 4;
                    System.arraycopy(AssitTool.getCount4N(idLength), 0, arrayData, pos, 4);
                    pos += 4;
                    System.arraycopy(id.getBytes(), 0, arrayData, pos, idLength);
                    legalData();
                    backData(arrayData);
                    return;
                }


                case 1: {//写
                    int len = AssitTool.getArrayCount(new byte[]{buffer[3], buffer[4]});
                    String id = new String(buffer, 5, len);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString(DeviceId, id);
                    editor.commit();
                    String data = Cmds.CMD_ID + BackCode.CODE_00;
                    byte[] arrayData = new byte[4 + 4];
                    int pos = 0;
                    System.arraycopy(data.getBytes(), 0, arrayData, pos, 4);
                    legalData();
                    backData(arrayData);
                    return;
                }

                default:
                    break;
            }
        }
        String senddatastr = Cmds.CMD_ID + BackCode.CODE_01;
        byte[] sendData = new byte[4 + 4];
        int pos = 0;
        System.arraycopy(senddatastr.getBytes(), 0, sendData, pos, 4);
        backData(sendData);
    }

}
