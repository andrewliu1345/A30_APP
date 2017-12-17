package com.joesmate.bin.keyBoard;

import android.content.SharedPreferences;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.KeyBordProtocol;
import com.joesmate.bin.BaseData;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by andre on 2017/12/15 .
 */

public class CheckKeyData extends BaseData {

    int KeyIndex = 0;
    int KeyType = 1;
    int KeyLength = 0;
    int TimeOut = 20;
    String CheckValueString = "";
    byte[] CheckValueArray;
    //    //初始密码
//    private static final String Default_8 = "3838383838383838";
//    private static final String Default_16 = Default_8 + Default_8;
//    private static final String Default_24 = Default_16 + Default_16;
//
//    private static final Map<Integer, String> DefaultKey = new HashMap<Integer, String>() {
//        {
//            put(Default_8.length(), Default_8);
//            put(Default_16.length(), Default_16);
//            put(Default_24.length(), Default_24);
//        }
//    };
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
            KeyIndex = buffer[pos++];
            Log.d(TAG, "ZMKindex:" + KeyIndex);


            //ZMKLengthType = AssitTool.getArrayCount(new byte[] { buffer[pos]});
            KeyType = buffer[pos++];
            Log.d(TAG, "ZMKLengthType:" + KeyType);

            KeyLength = buffer[pos++];
            Log.d(TAG, "KeyLength:" + KeyLength);
            //          KeyLength = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1]});
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
        SharedPreferences preferences = App.getInstance().preferences;
        //String DefKey = DefaultKey.get(KeyLength * 2);//最初默认密钥
        String iKey = "";
        if (KeyType == 0) {
            iKey = preferences.getString(Cmds.LOAD_MASTER_KEY, "");

//            writeData = new byte[1 + 1];
//            System.arraycopy(CMD.SD_CMD_CHECK_MK, 0, writeData, pos++, 1);
//            writeData[pos++] = (byte) 1;

        } else {
            iKey = preferences.getString(Cmds.WORK_KEY, "");

//            writeData = new byte[1 + 1 + 1];
//            System.arraycopy(CMD.SD_CMD_CHECK_WK, 0, writeData, pos++, 1);
//            writeData[pos++] = (byte) 1;
//            writeData[pos++] = (byte) KeyIndex;
        }
        if (iKey.length() > 0) {
            byte[] arrykey = new byte[iKey.length() / 2];
            arrykey = AssitTool.HexStringToBytes(iKey);
            CheckValueArray = KeyBordProtocol.getInstance().getCheckValues(arrykey);
            CheckValueString = AssitTool.BytesToHexString(CheckValueArray);
            int iIndex = 0;
            byte[] send = new byte[2 + BackCode.CODE_00.length() + 1 + CheckValueString.length()];
            send[iIndex++] = 'U';
            send[iIndex++] = 'W';
            System.arraycopy(BackCode.CODE_00.getBytes(), 0, send, iIndex, 2);
            iIndex += 2;
            send[iIndex++] = (byte) CheckValueString.length();
            System.arraycopy(CheckValueString.getBytes(), 0, send, iIndex, 8);
            backData(send);
        } else {
            sendConfirmCode(BackCode.CODE_01);
        }
//        final byte[] wbyte = new SerialRequestFrame().make_kb_Package(writeData);
//        final SerialResponseFrame serialResponse = new SerialResponseFrame();
//        SerialUtil.getInstance().getSerialPort().write(wbyte, wbyte.length);
//
//        new Thread(
//                new Runnable() {
//                    @Override
//                    public void run() {
//                        SerialUtil.getInstance().setStop(false);
//                        SerialUtil.getInstance().getmSReadThread().run();
//                        SerialResponseFrame.lock.lock();
//                        byte[] rbyte = serialResponse.get_SD_Rbyte();
//                        SerialUtil.getInstance().setStop(true);
//                        SerialResponseFrame.lock.unlock();
//                        if (rbyte.length == 22) {
////                            if (Arrays.equals(new byte[]{rbyte[0], rbyte[1], rbyte[2], rbyte[3], rbyte[4], rbyte[5]}, new byte[]{0x00, 0x14, (byte) 0xa0, 0x08, 0x00, 0x00})) {
////                                byte[] data = new byte[16];
////                                System.arraycopy(rbyte, 6, data, 0, 16);
////                                if (Arrays.equals(data, CheckValueArray)) {
////                                    sendConfirmCode(BackCode.CODE_00);
////                                } else {
////                                    sendConfirmCode(BackCode.CODE_01);
////                                }
////                            } else {
////                                sendConfirmCode(BackCode.CODE_01);
////                            }
//                        } else {
//                            sendConfirmCode(BackCode.CODE_02);
//                        }
//
//                    }
//                }
//        ).start();
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode:" + backCmd);
        String backCode = Cmds.CMD_CY + backCmd;
        backData(backCode.getBytes());
    }
}
