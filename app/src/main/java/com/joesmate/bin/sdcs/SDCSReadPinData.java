package com.joesmate.bin.sdcs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.KeyBordProtocol;
import com.joesmate.bin.BaseData;
import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;
import com.joesmate.page.PlayActivity;
import com.joesmate.util.ANSIFormat;
import com.joesmate.util.ANSIFormat_SM;
import com.joesmate.util.LogMg;
import com.joesmate.util.Util;

import java.util.Arrays;

/**
 * Created by bill on 2016/5/18.
 */
public class SDCSReadPinData extends BaseData {

    private static SDCSReadPinData sDCSReadPinData;

    public static SDCSReadPinData getInstance() {
        if (sDCSReadPinData == null) {
            sDCSReadPinData = new SDCSReadPinData();
        }
        return sDCSReadPinData;
    }

    public int getiEncryType() {
        return iEncryType;
    }

    public void setiEncryType(int iEncryType) {
        this.iEncryType = iEncryType;
    }

    private int iEncryType;

    public int getiInputPasswordNumber() {
        return iInputPasswordNumber;
    }

    public void setiInputPasswordNumber(int iInputPasswordNumber) {
        this.iInputPasswordNumber = iInputPasswordNumber;
    }

    private int iInputPasswordNumber;

    public int getiInputPassworfLength() {
        return iInputPassworfLength;
    }

    public void setiInputPassworfLength(int iInputPassworfLength) {
        this.iInputPassworfLength = iInputPassworfLength;
    }

    private int iInputPassworfLength;
    private int voiceTextLen;
    private byte[] arrayvoiceText;

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    private String voiceText;


    private int displayContentLen;
    private byte[] arrayDisplayContent;

    public String getDisplayContent() {
        return displayContent;
    }

    public void setDisplayContent(String displayContent) {
        this.displayContent = displayContent;
    }

    private String displayContent;

    public int getPasswordEndType() {
        return passwordEndType;
    }

    public void setPasswordEndType(int passwordEndType) {
        this.passwordEndType = passwordEndType;
    }

    private int passwordEndType;

    private byte EndType;

    private int iAccNoLen;

    private String iAccNo = "000000000000";

    @Override
    public void setData(byte[] buffer, byte[] cmd) {

        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_RP.getBytes(), cmd)) {
            pos = 2;


            iEncryType = AssitTool.getArrayCount(new byte[]{buffer[pos]});
            Log.d(TAG, "iEncryType:" + iEncryType);
            pos = pos + 1;
            if (iEncryType == 0) {
                closeInputChar();
                SerialUtil.getInstance().setStop(true);
                sendConfirmCode(BackCode.CODE_00);
                return;
            }

            iInputPasswordNumber = AssitTool.getArrayCount(new byte[]{buffer[pos]});
            Log.d(TAG, "iInputPasswordNumber:" + iInputPasswordNumber);
            pos = pos + 1;


            iInputPassworfLength = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1]});
            Log.d(TAG, "iInputPassworfLength:" + iInputPassworfLength);
            pos = pos + 2;


            int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2]});
            setTimeOut(timeOut);
            Log.d(TAG, "timeOut:" + timeOut);
            pos = pos + 3;


            voiceTextLen = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d(TAG, "voiceTextLen:" + voiceTextLen);
            pos = pos + 4;

            arrayvoiceText = new byte[voiceTextLen];
            System.arraycopy(buffer, pos, arrayvoiceText, 0, voiceTextLen);
            voiceText = AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
            Log.d(TAG, "voiceText:" + voiceText);
            pos = pos + voiceTextLen;


            displayContentLen = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d(TAG, "displayContentLen:" + displayContentLen);
            pos = pos + 4;

            arrayDisplayContent = new byte[displayContentLen];
            System.arraycopy(buffer, pos, arrayDisplayContent, 0, displayContentLen);
            displayContent = AssitTool.getString(arrayDisplayContent, AssitTool.UTF_8);
            Log.d(TAG, "displayContent:" + displayContent);
            pos = pos + displayContentLen;


            passwordEndType = AssitTool.getArrayCount(new byte[]{buffer[pos++]});
            Log.d(TAG, "passwordEndType:" + passwordEndType);
            if (passwordEndType == 0)
                EndType = '0';
            if (passwordEndType == 1)
                EndType = '1';

            iAccNoLen = AssitTool.getArrayCount(new byte[]{buffer[pos++], buffer[pos++]});
            LogMg.d(TAG, "iAccNoLen=%d", iAccNoLen);
            if (iAccNoLen == 12) {
                byte[] arrayAccNo = new byte[iAccNoLen];
                System.arraycopy(buffer, pos, arrayAccNo, 0, iAccNoLen);
                iAccNo = AssitTool.getString(arrayAccNo, AssitTool.UTF_8);
                pos += iAccNoLen;
            }

            sendConfirmCode(BackCode.CODE_00);

            App.isThirdpartySerial = false;
            inputChar();
            legalData();
//            if (iEncryType == 1) {
//                inputChar();
//                legalData();
//            }
//            if (iEncryType == 5) {
//                OperationSerial();
//            }


        }
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode111");
        String backCode = Cmds.CMD_RP + backCmd;
        backData(backCode.getBytes());
    }

    public void sendConfirmResult(String result) {
        // closeInputChar();
        String backCode = Cmds.CMD_RP + BackCode.CODE_20;

        if (iEncryType == 1) {
            int resultSize = result.length();
            byte[] arrayData = new byte[2 + 2 + 2 + resultSize];
            int pos = 0;
            System.arraycopy(backCode.getBytes(), 0, arrayData, pos, 4);
            pos = pos + 4;

            System.arraycopy(AssitTool.getCount42(resultSize), 0, arrayData, pos, 2);
            pos = pos + 2;
            System.arraycopy(result.getBytes(), 0, arrayData, pos, resultSize);
            LogMg.d(TAG, "返回密码明文%s", arrayData);

            backData(arrayData);
        } else if (iEncryType == 5) {
            SharedPreferences preferences = App.getInstance().preferences;
            String WorkKey = preferences.getString(Cmds.WORK_KEY, "");
            byte[] iPinBlock = ANSIFormat_SM.process(result, iAccNo);

            if (WorkKey.length() > 0) {
                byte[] arrayWorkKey = AssitTool.HexStringToBytes(WorkKey);
                byte[] cipher = KeyBordProtocol.getInstance().SM4Encrypt(iPinBlock, arrayWorkKey);
                String cipherstr = AssitTool.BytesToHexString(cipher);
                if (cipher == null) {
                    sendConfirmCode(BackCode.CODE_01);
                    return;
                }
                int cipherlen = cipherstr.length();
                byte[] arrayData = new byte[2 + 2 + 2 + cipherlen];
                int pos = 0;
                System.arraycopy(backCode.getBytes(), 0, arrayData, pos, 4);
                pos = pos + 4;
                System.arraycopy(AssitTool.getCount42(cipherlen), 0, arrayData, pos, 2);
                pos = pos + 2;
                System.arraycopy(cipherstr.getBytes(), 0, arrayData, pos, cipherlen);
                pos += cipherlen;
                LogMg.d(TAG, "返回密码明文%s", arrayData);
                backData(arrayData);
            } else {
                sendConfirmCode(BackCode.CODE_01);
            }
        }
    }


    public void sendConfirmResult(byte[] result) {
        //closeInputChar();
        String backCode = Cmds.CMD_RP + BackCode.CODE_20;
        int resultSize = result.length;
        byte[] arrayData = new byte[2 + 2 + 2 + resultSize];
        int pos = 0;
        System.arraycopy(backCode.getBytes(), 0, arrayData, pos, 4);
        pos = pos + 4;

        System.arraycopy(AssitTool.getCount42(resultSize), 0, arrayData, pos, 2);
        pos = pos + 2;
        System.arraycopy(result, 0, arrayData, pos, resultSize);
        Log.d(TAG, "sendConfirmData:" + arrayData);
        backData(arrayData);
    }

    //再次输入密码
    public void inputCharAgain() {

        //0x82 是请输入密码，打开密码键盘
        Log.d("myitm1", "open input pw  again ----");
        byte[] inputCharCmd = new byte[]{(byte) 0x81};
        final SerialResponseFrame serialResponse = new SerialResponseFrame();

        SerialUtil.getInstance().getSerialPort().write(inputCharCmd, inputCharCmd.length);


        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        SerialUtil.getInstance().setStop(false);
                        SerialUtil.getInstance().getmMReadThread().run();
                        /*SerialResponseFrame.lock.lock();
                        serialResponse.getRbyte();*/
                        SerialUtil.getInstance().setStop(true);
                       /* SerialResponseFrame.lock.unlock();*/
                        Log.d(TAG, "exit!");
                    }
                }
        ).start();

    }


    //关闭
    public void closeInputChar() {
        Log.d("myitm1", "close input pw ----");
        //0x82 是请输入密码，打开密码键盘
        byte[] inputCharCmd = new byte[]{(byte) 0x83};
        final SerialResponseFrame serialResponse = new SerialResponseFrame();

        SerialUtil.getInstance().getSerialPort().write(inputCharCmd, inputCharCmd.length);
    }

    public void inputChar() {

        //0x82 是请输入密码，打开密码键盘
        Log.d("myitm1", "open input pw   ----");
        byte[] inputCharCmd = new byte[]{(byte) 0x82};
        final SerialResponseFrame serialResponse = new SerialResponseFrame();

        SerialUtil.getInstance().getSerialPort().write(inputCharCmd, inputCharCmd.length);

        new Thread(
                new Runnable() {
                    @Override
                    public void run() {
                        SerialUtil.getInstance().setStop(false);
                        SerialUtil.getInstance().getmMReadThread().run();
                       /* SerialResponseFrame.lock.lock();
                        serialResponse.getRbyte();*/
                        SerialUtil.getInstance().setStop(true);
                        /*SerialResponseFrame.lock.unlock();*/
                        Log.d(TAG, "exit!");
                    }
                }
        ).start();

    }


    public void OperationSerial() {
        int pos = 0;
        byte[] writeData = new byte[2 + 1];
        System.arraycopy(CMD.SD_CMD_TYPE, 0, writeData, pos, 2);
        pos = pos + 2;
        System.arraycopy(new byte[]{0x03}, 0, writeData, pos, 1);
        pos = pos + 1;
        final byte[] wbyte = new SerialRequestFrame().make_SD_Package(writeData);
        final SerialResponseFrame serialResponse = new SerialResponseFrame();
        final String AccNo = this.iAccNo;
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
                                //设置sm4加密方式成功


                                int pos1 = 0;
                                byte[] writeData1 = new byte[2 + 1];
                                System.arraycopy(CMD.SD_CMD_LENGTH, 0, writeData1, pos1, 2);
                                pos1 = pos1 + 2;
                                System.arraycopy(new byte[]{(byte) iInputPassworfLength}, 0, writeData1, pos1, 1);
                                pos1 = pos1 + 1;
                                final byte[] wbyte1 = new SerialRequestFrame().make_SD_Package(writeData1);
                                final SerialResponseFrame serialResponse1 = new SerialResponseFrame();

                                SerialUtil.getInstance().getSerialPort().write(wbyte1, wbyte1.length);

                                SerialUtil.getInstance().setStop(false);
                                SerialUtil.getInstance().getmSReadThread().run();
                                SerialResponseFrame.lock.lock();
                                byte[] rbyte1 = serialResponse1.get_SD_Rbyte();
                                SerialUtil.getInstance().setStop(true);
                                SerialResponseFrame.lock.unlock();

                                if (rbyte1.length == 6) {
                                    if (Arrays.equals(new byte[]{rbyte1[0], rbyte1[1], rbyte1[2], rbyte1[3], rbyte1[4], rbyte1[5]}, new byte[]{0x00, 0x04, (byte) 0xa0, 0x06, 0x00, 0x00})) {
                                        //设置密码长度成功

                                        int pos2 = 0;
                                        byte[] writeData2 = new byte[2 + 1 + 2 + AccNo.length()];
                                        System.arraycopy(CMD.SD_CMD_READ_ACCNO_PIN, 0, writeData2, pos2, 2);
                                        pos2 = pos2 + 2;
//                                        System.arraycopy(new byte[]{0x01}, 0, writeData2, pos2, 1);
//                                        pos2++;
                                        System.arraycopy(new byte[]{0x02}, 0, writeData2, pos2, 1);
                                        pos2++;
                                        System.arraycopy(new byte[]{0x30}, 0, writeData2, pos2, 1);
                                        pos2++;
                                        System.arraycopy(new byte[]{EndType}, 0, writeData2, pos2, 1);
                                        pos2++;
                                        System.arraycopy(AccNo.getBytes(), 0, writeData2, pos2, AccNo.length());
                                        final byte[] wbyte2 = new SerialRequestFrame().make_SD_Package(writeData2);
                                        final SerialResponseFrame serialResponse2 = new SerialResponseFrame();

                                        SerialUtil.getInstance().getSerialPort().write(wbyte2, wbyte2.length);

                                        legalData();
                                        SerialUtil.getInstance().setStop(false);
                                        SerialUtil.getInstance().getmSReadThread().run();
                                        SerialResponseFrame.lock.lock();
                                        byte[] rbyte2 = serialResponse2.get_SD_Rbyte();
                                        if (rbyte2.length > 6) {
                                            if (Arrays.equals(new byte[]{rbyte2[2], rbyte2[3], rbyte2[4], rbyte2[5]}, new byte[]{(byte) 0xb0, 0x0a, 0x00, 0x00})) {
                                                Log.d(TAG, "4444444");
                                                byte[] returnData = new byte[rbyte2.length - 6];
                                                //AssitTool.splitBuffer(returnData,)
                                                System.arraycopy(rbyte2, 6, returnData, 0, returnData.length);

                                                String hexStr = "";
                                                for (int i = 0; i < returnData.length; i++) {
                                                    String hex = Integer.toHexString(returnData[i] & 0xFF);
                                                    if (hex.length() == 1) {
                                                        hex = '0' + hex;
                                                    }
                                                    hexStr = hexStr + hex.toLowerCase();

                                                }
                                                //AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                                SerialUtil.getInstance().setStop(true);
                                                SerialResponseFrame.lock.unlock();
                                                sendConfirmResult(hexStr);
                                                //closeInputChar();
                                                Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
                                                intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
                                                App.getInstance().sendBroadcast(intent1);
                                                //sendConfirmResult(returnData);
                                            } else {
                                                Log.d(TAG, "555555");
                                                SerialUtil.getInstance().setStop(true);
                                                SerialResponseFrame.lock.unlock();
                                                sendConfirmCode(BackCode.CODE_01);
                                            }
                                        } else {
                                            Log.d(TAG, "777777");
                                            SerialUtil.getInstance().setStop(true);
                                            SerialResponseFrame.lock.unlock();
                                            sendConfirmCode(BackCode.CODE_01);

                                        }


                                        Log.d(TAG, "bbdbdbdbdb");

                                    } else {
                                        Log.d(TAG, "8888888");
                                        sendConfirmCode(BackCode.CODE_01);

                                    }
                                } else {
                                    Log.d(TAG, "999999");
                                    sendConfirmCode(BackCode.CODE_01);

                                }


                            } else {
                                Log.d(TAG, "1010101");
                                sendConfirmCode(BackCode.CODE_01);
                            }
                        } else {
                            Log.d(TAG, "12121212");
                            sendConfirmCode(BackCode.CODE_01);
                        }

                    }
                }
        ).start();

    }
}
