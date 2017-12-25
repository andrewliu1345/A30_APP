package com.joesmate.bin.icbc;

import android.util.Log;

import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

/**
 * Created by bill on 2016/1/10.
 */
public class SetPosterTypeData extends BaseData {

    private static SetPosterTypeData setPosterTypeData;

    public static SetPosterTypeData getInstance() {
        if (setPosterTypeData == null) {
            setPosterTypeData = new SetPosterTypeData();
        }
        return setPosterTypeData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        if (Arrays.equals(Cmds.CMD_PT.getBytes(), cmd)) {
            SharedpreferencesData.getInstance().setPoster_type(buffer[2]);
            if (SharedpreferencesData.getInstance().getPoster_type() == 0)
                SharedpreferencesData.getInstance().setShowTime(buffer[3] & 0xffff);
            Log.v(TAG, "Poster_type: " + buffer[2]);
            Log.v(TAG, "ShowTime: " + buffer[3]);
            backPTData();
            legalData();
        }

    }

    private void backPTData() {
        String backCode = Cmds.CMD_PT + BackCode.CODE_00;
        backData(backCode.getBytes());
    }
}
