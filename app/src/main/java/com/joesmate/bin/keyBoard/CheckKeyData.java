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

public class CheckKeyData extends BaseData {

    int KeyIndex = 0;
    int KeyType = 1;
    int KeyLength = 0;
    int TimeOut = 20;
    String CheckValueString = "";
    byte []CheckValueArray;
    private static CheckKeyData sCheckKeyData;

    public static CheckKeyData getInstance() {
        if (sCheckKeyData == null) {
            sCheckKeyData = new CheckKeyData();
        }
        return sCheckKeyData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_CY.getBytes(), cmd)) {
            pos = 2;

            //ZMKindex =  AssitTool.getArrayCount(new byte[] { buffer[pos]});
            KeyIndex = buffer[pos];
            Log.d(TAG, "ZMKindex:" + KeyIndex);
            pos = pos + 1;

            //ZMKLengthType = AssitTool.getArrayCount(new byte[] { buffer[pos]});
            KeyType = buffer[pos];
            Log.d(TAG, "ZMKLengthType:" + KeyType);
            pos = pos + 1;

//            KeyLength = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1]});
//            Log.d(TAG, "KeyLength:" + KeyLength);
//            pos = pos + 2;
//
//            CheckValueArray = new byte[KeyLength];
//            System.arraycopy(buffer, pos, CheckValueArray, 0, KeyLength);
//            CheckValueString = AssitTool.getString(CheckValueArray, AssitTool.UTF_8);
//            Log.d(TAG, "CheckValueString:" + CheckValueString);
//            pos = pos + KeyLength;

            TimeOut = buffer[pos++];
            setTimeOut(TimeOut);
            OperationSerial();
        }
    }

    public void OperationSerial() {
        int pos = 0;
        byte[] writeData;
        if (KeyType == 0) {
            writeData = new byte[1 + 1];
            System.arraycopy(CMD.SD_CMD_CHECK_MK, 0, writeData, pos++, 1);
            writeData[pos++] = (byte) KeyIndex;

        } else {
            writeData = new byte[1 + 1 + 1];
            System.arraycopy(CMD.SD_CMD_CHECK_WK, 0, writeData, pos++, 1);
            writeData[pos++] = (byte) 2;
            writeData[pos++] = (byte) KeyIndex;
        }
        final byte[] wbyte = new SerialRequestFrame().make_kb_Package(writeData);
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
                        if (rbyte.length == 22) {
//                            if (Arrays.equals(new byte[]{rbyte[0], rbyte[1], rbyte[2], rbyte[3], rbyte[4], rbyte[5]}, new byte[]{0x00, 0x14, (byte) 0xa0, 0x08, 0x00, 0x00})) {
//                                byte[] data = new byte[16];
//                                System.arraycopy(rbyte, 6, data, 0, 16);
//                                if (Arrays.equals(data, CheckValueArray)) {
//                                    sendConfirmCode(BackCode.CODE_00);
//                                } else {
//                                    sendConfirmCode(BackCode.CODE_01);
//                                }
//                            } else {
//                                sendConfirmCode(BackCode.CODE_01);
//                            }
                        } else {
                            sendConfirmCode(BackCode.CODE_02);
                        }

                    }
                }
        ).start();
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode:" + backCmd);
        String backCode = Cmds.CMD_CY + backCmd;
        backData(backCode.getBytes());
    }
}
