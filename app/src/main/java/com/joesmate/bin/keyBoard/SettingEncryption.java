package com.joesmate.bin.keyBoard;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

/**
 * Created by andre on 2017/12/15 .
 */

public class SettingEncryption extends BaseData {

    private static SettingEncryption mInstance;
    private int iEncryType;

    public int getiEncryType() {
        return iEncryType;
    }

    public void setiEncryType(int iEncryType) {
        this.iEncryType = iEncryType;
    }


    public static SettingEncryption getInstance() {
        if (mInstance == null) {
            mInstance = new SettingEncryption();
        }
        return mInstance;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        if (Arrays.equals(Cmds.CMD_ST.getBytes(), cmd)) {
            int pos = 2;
            iEncryType = AssitTool.getArrayCount(new byte[]{buffer[pos++]});
            Log.d(TAG, "iEncryType:" + iEncryType);
            OperationSerial();
        }
    }

    public void OperationSerial() {
        int pos = 0;
        byte[] writeData = new byte[2 + 1];
        System.arraycopy(CMD.SD_CMD_TYPE, 0, writeData, pos, 2);
        pos = pos + 2;
        System.arraycopy(new byte[]{(byte) iEncryType}, 0, writeData, pos, 1);
        pos = pos + 1;
        final byte[] wbyte = new SerialRequestFrame().make_SD_Package(writeData);
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
                            if (Arrays.equals(new byte[]{rbyte[0], rbyte[1], rbyte[2], rbyte[3], rbyte[4], rbyte[5]}, new byte[]{0x00, 0x04, (byte) 0xa0, 0x05, 0x00, 0x00})) {
                                //设置加密方式成功
                                sendConfirmCode(BackCode.CODE_00);
                            }
                            {
                                sendConfirmCode(BackCode.CODE_01);
                            }
                        }
                    }
                }).start();
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode111");
        String backCode = Cmds.CMD_ST + backCmd;
        backData(backCode.getBytes());
    }
}