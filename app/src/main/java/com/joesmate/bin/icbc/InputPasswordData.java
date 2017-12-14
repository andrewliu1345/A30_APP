package com.joesmate.bin.icbc;

import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

/**
 * Created by bill on 2016/1/10.
 */
public class InputPasswordData extends BaseData {

    private static InputPasswordData inputPasswordData;
    public static InputPasswordData getInstance(){
        if(inputPasswordData == null){
            inputPasswordData = new InputPasswordData();
        }
        return inputPasswordData;
    }
    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        if(Arrays.equals(Cmds.CMD_IP.getBytes(), cmd)){
            //SharedpreferencesData.getInstance().setPoster_type(buffer[2]);
           // Log.v(TAG, "receive cmd_ip: " + buffer[2]);
            backIPData();
            legalData();
        }

    }

    private void backIPData(){
        String backCode = Cmds.CMD_IP + BackCode.CODE_00;
        backData(backCode.getBytes());
    }
}
