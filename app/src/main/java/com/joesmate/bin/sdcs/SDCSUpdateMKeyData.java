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

import java.security.Key;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bill on 2016/5/18.
 */
public class SDCSUpdateMKeyData extends BaseData {

    private static SDCSUpdateMKeyData sDCSReadPinData;

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
    byte[] CheckValue1;
    byte[] CheckValue2;
    byte[] ZMKStringArray;

    public int getZMKindex() {
        return ZMKindex;
    }

    public void setZMKindex(int ZMKindex) {
        this.ZMKindex = ZMKindex;
    }

    private int ZMKindex;

    public int getZMKLengthType() {
        return ZMKLengthType;
    }

    public void setZMKLengthType(int ZMKLengthType) {
        this.ZMKLengthType = ZMKLengthType;
    }

    private int ZMKLengthType;

    public int getZMKStrLength() {
        return ZMKStrLength;
    }

    public void setZMKStrLength(int ZMKStrLength) {
        this.ZMKStrLength = ZMKStrLength;
    }

    private int ZMKStrLength;

    public String getZMKString() {
        return ZMKString;
    }

    public void setZMKString(String ZMKString) {
        this.ZMKString = ZMKString;
    }

    private String ZMKString;

    public int getPinKeyStrLength() {
        return pinKeyStrLength;
    }

    public void setPinKeyStrLength(int pinKeyStrLength) {
        this.pinKeyStrLength = pinKeyStrLength;
    }

    public static SDCSUpdateMKeyData getsDCSReadPinData() {
        return sDCSReadPinData;
    }

    public static void setsDCSReadPinData(SDCSUpdateMKeyData sDCSReadPinData) {
        SDCSUpdateMKeyData.sDCSReadPinData = sDCSReadPinData;
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

    public static SDCSUpdateMKeyData getInstance() {
        if (sDCSReadPinData == null) {
            sDCSReadPinData = new SDCSUpdateMKeyData();
        }
        return sDCSReadPinData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {

        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_UM.getBytes(), cmd)) {
            pos = 2;

            //ZMKindex =  AssitTool.getArrayCount(new byte[] { buffer[pos]});
            ZMKindex = buffer[pos++];
            Log.d(TAG, "ZMKindex:" + ZMKindex);


            //ZMKLengthType = AssitTool.getArrayCount(new byte[] { buffer[pos]});
            ZMKLengthType = buffer[pos++];
            Log.d(TAG, "ZMKLengthType:" + ZMKLengthType);


            ZMKStrLength = AssitTool.getArrayCount(new byte[]{buffer[pos++], buffer[pos++]});
            Log.d(TAG, "ZMKStrLength:" + ZMKStrLength);


            ZMKStringArray = new byte[ZMKStrLength];
            System.arraycopy(buffer, pos, ZMKStringArray, 0, ZMKStrLength);
            ZMKString = AssitTool.getString(ZMKStringArray, AssitTool.UTF_8);
            Log.d(TAG, "ZMKString:" + ZMKString);
            pos = pos + ZMKStrLength;

            pinKeyStrLength = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1]});
            Log.d(TAG, "pinKeyStrLength:" + pinKeyStrLength);
            pos = pos + 2;

            //CheckValue
            pinKeyStringArray = new byte[pinKeyStrLength];
            System.arraycopy(buffer, pos, pinKeyStringArray, 0, pinKeyStrLength);
            pinKeyString = AssitTool.getString(pinKeyStringArray, AssitTool.UTF_8);
            Log.d(TAG, "pinKeyString:" + pinKeyString);

            OperationSerial();


        }
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode:" + backCmd);
        String backCode = Cmds.CMD_UM + backCmd;
        backData(backCode.getBytes());
    }

    public void OperationSerial() {
        final SharedPreferences preferences = App.getInstance().preferences;
        String DefKey = DefaultKey.get(ZMKLengthType * 2);//最初默认密钥
        String LoadMasterKey;
        final byte MasterKey[] = AssitTool.HexStringToBytes(ZMKString);
        byte[] tmp = new byte[ZMKStrLength];
        byte[] out = new byte[ZMKStrLength];
        if (ZMKindex == 1) {
            CheckValue2 = SM4Utils.getCheckValues(MasterKey);//KeyBordProtocol.getInstance().getCheckValues(MasterKey);
            LoadMasterKey = preferences.getString(Cmds.LOAD_MASTER_KEY, DefKey);//明文密钥
            byte[] loadMasterKey = AssitTool.HexStringToBytes(LoadMasterKey);
            try {
                out = SM4Utils.SM4_ECB(MasterKey, loadMasterKey, SM4.SM4_ENCRYPT); //KeyBordProtocol.getInstance().SM4Encrypt(MasterKey, loadMasterKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // SMS4.getInstance().sms4(MasterKey, ZMKStrLength, LoadMasterKey.getBytes(), out, SMS4.CRYPT_FLAG_DECRY);


        } else if (ZMKindex == 2) {
            CheckValue1 = AssitTool.HexStringToBytes(pinKeyString);
            LoadMasterKey = preferences.getString(Cmds.LOAD_MASTER_KEY, DefKey);//明文密钥
            byte[] loadMasterKey = AssitTool.HexStringToBytes(LoadMasterKey);
            tmp = SM4Utils.SM4_ECB(MasterKey, loadMasterKey, SM4.SM4_DECRYPT); //KeyBordProtocol.getInstance().SM4Dcrypt(MasterKey, loadMasterKey);
//            CheckValue2 = KeyBordProtocol.getInstance().getCheckValues(tmp);
//            if (!Arrays.equals(CheckValue1, CheckValue2)) {
//                sendConfirmCode(BackCode.CODE_01);
//                return;
//            }
            CheckValue2 = AssitTool.HexStringToBytes(pinKeyString.substring(0, 16));
            out = MasterKey;

        }
        final byte MasterKey2[] = tmp;

        byte[] data = out;

        int pos = 0;
        byte[] writeData = new byte[2 + 1 + 1 + data.length];
        System.arraycopy(CMD.SD_CMD_UPDATE_MAIN_KEY, 0, writeData, pos, 2);
        pos = pos + 2;

        System.arraycopy(new byte[]{(byte) 1}, 0, writeData, pos, 1);
        pos = pos + 1;

        System.arraycopy(new byte[]{(byte) data.length}, 0, writeData, pos, 1);
        pos = pos + 1;

        System.arraycopy(data, 0, writeData, pos, data.length);

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
                        if (rbyte.length == 22) {
                            if (Arrays.equals(new byte[]{rbyte[0], rbyte[1], rbyte[2], rbyte[3], rbyte[4], rbyte[5]}, new byte[]{0x00, 0x14, (byte) 0xb0, 0x07, 0x00, 0x00})) {
                                byte[] data = new byte[8];

                                int iIndex = 0;
                                System.arraycopy(rbyte, 6, data, 0, 8);

                                LogMg.d(TAG, "CheckValue2=%s\n", AssitTool.BytesToHexString(data));
                                if (getZMKindex() == 1) {//明文
                                    String CheckValueStr = AssitTool.BytesToHexString(CheckValue2);
                                    byte[] send = new byte[2 + BackCode.CODE_00.length() + 1 + CheckValueStr.length()];
                                    SharedPreferences.Editor editor = preferences.edit();
                                    editor.putString(Cmds.LOAD_MASTER_KEY, ZMKString);
                                    editor.commit();
                                    send[iIndex++] = 'U';
                                    send[iIndex++] = 'M';
                                    System.arraycopy(BackCode.CODE_00.getBytes(), 0, send, iIndex, 2);
                                    iIndex += 2;
                                    send[iIndex++] = (byte) CheckValueStr.length();
                                    System.arraycopy(CheckValueStr.getBytes(), 0, send, iIndex, CheckValueStr.length());
                                    backData(send);

                                } else if (getZMKindex() == 2) {//密文
                                    String CheckValueStr = AssitTool.BytesToHexString(data);
                                    byte[] send = new byte[2 + 2 + 1 + CheckValueStr.length()];
                                    if (Arrays.equals(data, CheckValue2)) {
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString(Cmds.LOAD_MASTER_KEY, AssitTool.BytesToHexString(MasterKey2));
                                        editor.commit();
                                        send[iIndex++] = 'U';
                                        send[iIndex++] = 'M';
                                        System.arraycopy(BackCode.CODE_00.getBytes(), 0, send, iIndex, 2);
                                        iIndex += 2;
                                        send[iIndex++] = (byte) CheckValueStr.length();
                                        System.arraycopy(CheckValueStr.getBytes(), 0, send, iIndex, CheckValueStr.length());
                                        backData(send);
                                        return;
                                    }
                                    sendConfirmCode(BackCode.CODE_01);
                                } else
                                    sendConfirmCode(BackCode.CODE_01);
//                                if (Arrays.equals(data, pinKeyStringArray)) {
//                                    sendConfirmCode(BackCode.CODE_00);
//                                } else {
//                                    sendConfirmCode(BackCode.CODE_01);
//                                }
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
