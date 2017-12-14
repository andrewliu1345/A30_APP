package com.joesmate.bin.icbc;

import android.content.Intent;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

/**
 * Created by bill on 2016/1/9.
 */
public class SetStatusAreaStyleData  extends BaseData {


    private static SetStatusAreaStyleData  setStatusAreaStyleData;
    public static SetStatusAreaStyleData getInstance(){
        if(setStatusAreaStyleData == null){
            setStatusAreaStyleData = new SetStatusAreaStyleData();
        }
        return setStatusAreaStyleData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        if(Arrays.equals(Cmds.CMD_SA.getBytes(), cmd)){
            SharedpreferencesData.getInstance().setTop_font_family(buffer[2]);
            SharedpreferencesData.getInstance().setTop_font_size(buffer[3]);
            SharedpreferencesData.getInstance().setTop_font_weight(buffer[4]);
            SharedpreferencesData.getInstance().setTop_font_color(buffer[5]);
            SharedpreferencesData.getInstance().setTop_bgcolor(buffer[6]);
            int top_height =   AssitTool.getArrayCount(new byte[]{buffer[7],
                    buffer[8],buffer[9]}) ;
            if(top_height > 800)
            {
                Log.v(TAG,"top_height > 800 ,set default to 80 ");
                top_height = 80;
            }
            SharedpreferencesData.getInstance().setTop_height(top_height);
            Log.v(TAG, "family: " + buffer[2] + "  fontsize:" + buffer[3] + " weight:" + buffer[4] + "  color:" + buffer[5]
                    + "  bgcolor:" + buffer[6] + " height:" + AssitTool.getArrayCount(new byte[]{buffer[7],
                    buffer[8],buffer[9]}));
            backSAData();
            App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));
        }

    }

    private void backSAData(){
        String backCode = Cmds.CMD_SA + BackCode.CODE_00;
        backData(backCode.getBytes());
    }
}

