package com.joesmate.bin.sdcs;

import android.content.SharedPreferences;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;
import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;

import java.util.Arrays;

/**
 * Created by bill on 2016/5/18.
 */
public class SDCSResetPinData extends BaseData {

    private static SDCSResetPinData sDCSReadPinData;

    public static SDCSResetPinData getInstance() {
        if (sDCSReadPinData == null) {
            sDCSReadPinData = new SDCSResetPinData();
        }
        return sDCSReadPinData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {

        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_IP.getBytes(), cmd)) {
            ResetPinPad();
            SharedPreferences.Editor editor = App.getInstance().preferences.edit();
            editor.remove(Cmds.LOAD_MASTER_KEY);//主密钥
            editor.remove(Cmds.WORK_KEY);//工作密钥
            editor.commit();
            //sendConfirmCode(BackCode.CODE_00);
            //legalData();

        }
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode:" + backCmd);
        String backCode = Cmds.CMD_RP + backCmd;
        backData(backCode.getBytes());
    }

    public void ResetPinPad() {

        byte[] data = {(byte) 0xa0, 0x10};
        final byte[] wbyte = new SerialRequestFrame().make_SD_Package(data);
        final SerialResponseFrame serialResponse = new SerialResponseFrame();

        SerialUtil.getInstance().getSerialPort().write(wbyte, wbyte.length);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        SerialUtil.getInstance().setStop(false);
                        SerialUtil.getInstance().getmSReadThread().run();
                        SerialResponseFrame.lock.lock();
                        byte[] rbyte = serialResponse.get_SD_Rbyte();
                        SerialUtil.getInstance().setStop(true);
                        SerialResponseFrame.lock.unlock();
                        if (rbyte.length == 6) {
                            if (Arrays.equals(rbyte, new byte[]{0x00, 0x04, (byte) 0xa0, 0x10, 0x00, 0x00})) {
                                sendConfirmCode(BackCode.CODE_00);
                            } else {
                                sendConfirmCode(BackCode.CODE_01);
                            }
                        } else {
                            sendConfirmCode(BackCode.CODE_02);
                        }

                    }
                }
        ).start();

    }
}
