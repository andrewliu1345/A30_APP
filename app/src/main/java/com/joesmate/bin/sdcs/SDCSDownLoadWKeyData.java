package com.joesmate.bin.sdcs;

import android.content.SharedPreferences;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.KeyBordProtocol;
import com.joesmate.bin.BaseData;
import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;
import com.joesmate.crypto.SM4;
import com.joesmate.crypto.SM4Utils;
import com.joesmate.util.LogMg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bill on 2016/5/18.
 */
public class SDCSDownLoadWKeyData extends BaseData {

    private static SDCSDownLoadWKeyData sDCSReadPinData;
    //初始密码
    private static final String Default_8 = "3838383838383838";
    private static final String Default_16 = Default_8 + Default_8;
    private static final String Default_24 = Default_16 + Default_16;

    private static final Map<Integer, String> DefaultKey = new HashMap<Integer, String>() {
        {
            put(Default_8.length(), Default_8);
            put(Default_16.length(), Default_16);
            put(Default_24.length(), Default_24);
        }
    };

    public int getZMKindex() {
        return ZMKindex;
    }

    public void setZMKindex(int ZMKindex) {
        this.ZMKindex = ZMKindex;
    }

    private int ZMKindex;


    public int getWpinIndex() {
        return wpinIndex;
    }

    public void setWpinIndex(int wpinIndex) {
        this.wpinIndex = wpinIndex;
    }

    private int wpinIndex;


    public int getwPinLengthType() {
        return wPinLengthType;
    }

    public void setwPinLengthType(int wPinLengthType) {
        this.wPinLengthType = wPinLengthType;
    }

    private int wPinLengthType;


    public int getwPinStrLength() {
        return wPinStrLength;
    }

    public void setwPinStrLength(int wPinStrLength) {
        this.wPinStrLength = wPinStrLength;
    }

    private int wPinStrLength;


    public String getwPinString() {
        return wPinString;
    }

    public void setwPinString(String wPinString) {
        this.wPinString = wPinString;
    }

    private String wPinString;

    public int getPinKeyStrLength() {
        return pinKeyStrLength;
    }

    public void setPinKeyStrLength(int pinKeyStrLength) {
        this.pinKeyStrLength = pinKeyStrLength;
    }

    public static SDCSDownLoadWKeyData getsDCSReadPinData() {
        return sDCSReadPinData;
    }

    public static void setsDCSReadPinData(SDCSDownLoadWKeyData sDCSReadPinData) {
        SDCSDownLoadWKeyData.sDCSReadPinData = sDCSReadPinData;
    }

    private int pinKeyStrLength;

    public String getPinKeyString() {
        return pinKeyString;
    }

    public void setPinKeyString(String pinKeyString) {
        this.pinKeyString = pinKeyString;
    }

    private String pinKeyString;

    private byte[] pinKeyStringArray;


    byte[] CheckValue1;
    byte[] CheckValue2;

    public static SDCSDownLoadWKeyData getInstance() {
        if (sDCSReadPinData == null) {
            sDCSReadPinData = new SDCSDownLoadWKeyData();
        }
        return sDCSReadPinData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {

        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_UW.getBytes(), cmd)) {
            pos = 2;

            ZMKindex = buffer[pos];
            Log.d(TAG, "ZMKindex:" + ZMKindex);
            pos = pos + 1;


            wpinIndex = buffer[pos];
            Log.d(TAG, "wpinIndex:" + wpinIndex);
            pos = pos + 1;


            wPinLengthType = buffer[pos];
            Log.d(TAG, "wPinLengthType:" + wPinLengthType);
            pos = pos + 1;


            wPinStrLength = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1]});
            Log.d(TAG, "wPinStrLength:" + wPinStrLength);
            pos = pos + 2;

            byte[] wPinStringArray = new byte[wPinStrLength];
            System.arraycopy(buffer, pos, wPinStringArray, 0, wPinStrLength);
            wPinString = AssitTool.getString(wPinStringArray, AssitTool.UTF_8);
            Log.d(TAG, "wPinString:" + wPinString);
            pos = pos + wPinStrLength;

            pinKeyStrLength = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1]});
            Log.d(TAG, "pinKeyStrLength:" + pinKeyStrLength);
            pos = pos + 2;

            ////CheckValue
            pinKeyStringArray = new byte[pinKeyStrLength];
            System.arraycopy(buffer, pos, pinKeyStringArray, 0, pinKeyStrLength);
            pinKeyString = AssitTool.getString(pinKeyStringArray, AssitTool.UTF_8);
            Log.d(TAG, "pinKeyString:" + pinKeyString);

            OperationSerial();


        }
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode:" + backCmd);
        String backCode = Cmds.CMD_UW + backCmd;
        backData(backCode.getBytes());
    }

    public void OperationSerial() {
//        final SharedPreferences preferences = App.getInstance().preferences;
//        String DefKey = DefaultKey.get(wPinLengthType * 2);//最初默认密钥
//        String LoadMasterKey;
        final byte WorkKey[] = AssitTool.HexStringToBytes(wPinString);
        CheckValue1 = AssitTool.HexStringToBytes(pinKeyString.substring(0, 16));
//        LoadMasterKey = preferences.getString(Cmds.LOAD_MASTER_KEY, DefKey);//明文密钥
//        byte[] loadMasterKey = AssitTool.HexStringToBytes(LoadMasterKey);
//        byte[] tmp = KeyBordProtocol.getInstance().SM4Dcrypt(WorkKey, loadMasterKey);
//        CheckValue2 = KeyBordProtocol.getInstance().getCheckValues(tmp);
//        if (Arrays.equals(CheckValue1, CheckValue2))
//            sendConfirmCode(BackCode.CODE_01);
        final byte[] checkvaluel = CheckValue1;
        int pos = 0;
        byte[] writeData = new byte[2 + 1 + 1 + 1 + WorkKey.length];
        System.arraycopy(CMD.SD_CMD_UPDATE_WORK_KEY, 0, writeData, pos, 2);
        pos = pos + 2;

        System.arraycopy(new byte[]{(byte) 1}, 0, writeData, pos, 1);
        pos = pos + 1;

        System.arraycopy(new byte[]{(byte) wpinIndex}, 0, writeData, pos, 1);
        pos = pos + 1;

        System.arraycopy(new byte[]{(byte) WorkKey.length}, 0, writeData, pos, 1);
        pos = pos + 1;

        System.arraycopy(WorkKey, 0, writeData, pos, WorkKey.length);

        final byte[] wbyte = new SerialRequestFrame().make_SD_Package(writeData);
        final SerialResponseFrame serialResponse = new SerialResponseFrame();

        SerialUtil.getInstance().getSerialPort().write(wbyte, wbyte.length);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences = App.getInstance().preferences;
                        String LoadMastkey = preferences.getString(Cmds.MASTER_KEY, preferences.getString(Cmds.LOAD_MASTER_KEY, ""));
                        LogMg.d(TAG, "Mastkey:%s", LoadMastkey);
                        if (LoadMastkey.length() <= 0) {
                            sendConfirmCode(BackCode.CODE_01);
                        }
                        byte[] arryLoadMastKey = AssitTool.HexStringToBytes(LoadMastkey);
                        SerialUtil.getInstance().setStop(false);
                        SerialUtil.getInstance().getmSReadThread().run();
                        SerialResponseFrame.lock.lock();
                        byte[] rbyte = serialResponse.get_SD_Rbyte();
                        SerialUtil.getInstance().setStop(true);
                        SerialResponseFrame.lock.unlock();
                        if (rbyte.length == 22) {
                            if (Arrays.equals(new byte[]{rbyte[0], rbyte[1], rbyte[2], rbyte[3], rbyte[4], rbyte[5]}, new byte[]{0x00, 0x14, (byte) 0xa0, 0x08, 0x00, 0x00})) {
                                int iIndex = 0;
                                byte[] data = new byte[8];

                                System.arraycopy(rbyte, 6, data, 0, 8);
                                String CheckValueStr = AssitTool.BytesToHexString(data);
                                LogMg.d(TAG, "CheckValueStr=%s\n",CheckValueStr );
                                if (Arrays.equals(checkvaluel, data)) {
                                    byte[] arrworkkey = SM4Utils.SM4_ECB(WorkKey, arryLoadMastKey, SM4.SM4_DECRYPT);//KeyBordProtocol.getInstance().SM4Dcrypt(WorkKey, arryLoadMastKey);
                                    String workkeystr = AssitTool.BytesToHexString(arrworkkey);
                                    LogMg.d(TAG, "workkeystr:%s", workkeystr);
                                    SharedPreferences.Editor editor = App.getInstance().preferences.edit();
                                    editor.putString(Cmds.WORK_KEY, workkeystr);
                                    editor.commit();
                                    byte[] send = new byte[2 + BackCode.CODE_00.length() + 1 + CheckValueStr.length()];
                                    send[iIndex++] = 'U';
                                    send[iIndex++] = 'W';
                                    System.arraycopy(BackCode.CODE_00.getBytes(), 0, send, iIndex, 2);
                                    iIndex += 2;
                                    send[iIndex++] = (byte) CheckValueStr.length();
                                    System.arraycopy(CheckValueStr.getBytes(), 0, send, iIndex, 8);
                                    backData(send);
                                } else {
                                    byte[] send = new byte[2 + BackCode.CODE_15.length() + 1 + CheckValueStr.length()];
                                    send[iIndex++] = 'U';
                                    send[iIndex++] = 'W';
                                    System.arraycopy(BackCode.CODE_15.getBytes(), 0, send, iIndex, 3);
                                    iIndex += 3;
                                    send[iIndex++] = (byte) CheckValueStr.length();
                                    System.arraycopy(CheckValueStr.getBytes(), 0, send, iIndex, CheckValueStr.length());
                                    backData(send);
                                }
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
