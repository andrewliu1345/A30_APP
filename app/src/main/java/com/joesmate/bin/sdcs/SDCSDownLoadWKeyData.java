package com.joesmate.bin.sdcs;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;
import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;

import java.util.Arrays;

/**
 * Created by bill on 2016/5/18.
 */
public class SDCSDownLoadWKeyData extends BaseData {

    private static SDCSDownLoadWKeyData sDCSReadPinData;


    public int getZMKindex() {
        return ZMKindex;
    }

    public void setZMKindex(int ZMKindex) {
        this.ZMKindex = ZMKindex;
    }

    private  int ZMKindex ;


    public int getWpinIndex() {
        return wpinIndex;
    }

    public void setWpinIndex(int wpinIndex) {
        this.wpinIndex = wpinIndex;
    }

    private  int wpinIndex ;


    public int getwPinLengthType() {
        return wPinLengthType;
    }

    public void setwPinLengthType(int wPinLengthType) {
        this.wPinLengthType = wPinLengthType;
    }

    private  int wPinLengthType ;


    public int getwPinStrLength() {
        return wPinStrLength;
    }

    public void setwPinStrLength(int wPinStrLength) {
        this.wPinStrLength = wPinStrLength;
    }

    private  int wPinStrLength ;


    public String getwPinString() {
        return wPinString;
    }

    public void setwPinString(String wPinString) {
        this.wPinString = wPinString;
    }

    private  String wPinString ;

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

    private int pinKeyStrLength ;

    public String getPinKeyString() {
        return pinKeyString;
    }

    public void setPinKeyString(String pinKeyString) {
        this.pinKeyString = pinKeyString;
    }

    private String pinKeyString ;

    private  byte[]  pinKeyStringArray;

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

            ZMKindex =  buffer[pos];
            Log.d(TAG, "ZMKindex:" + ZMKindex);
            pos = pos + 1 ;


            wpinIndex = buffer[pos];
            Log.d(TAG, "wpinIndex:" + wpinIndex);
            pos = pos + 1 ;


            wPinLengthType = buffer[pos];
            Log.d(TAG, "wPinLengthType:" + wPinLengthType);
            pos = pos + 1 ;


            wPinStrLength = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1]});
            Log.d(TAG, "wPinStrLength:" + wPinStrLength);
            pos = pos + 2 ;

            byte[]  wPinStringArray = new byte[wPinStrLength];
            System.arraycopy(buffer,pos,wPinStringArray,0,wPinStrLength);
            wPinString =  AssitTool.getString(wPinStringArray, AssitTool.UTF_8);
            Log.d(TAG, "wPinString:" + wPinString);
            pos = pos + wPinStrLength ;

            pinKeyStrLength = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1]});
            Log.d(TAG, "pinKeyStrLength:" + pinKeyStrLength);
            pos = pos + 2 ;

            pinKeyStringArray = new byte[pinKeyStrLength];
            System.arraycopy(buffer,pos,pinKeyStringArray,0,pinKeyStrLength);
            pinKeyString =  AssitTool.getString(pinKeyStringArray, AssitTool.UTF_8);
            Log.d(TAG, "pinKeyString:" + pinKeyString);

            OperationSerial();



        }
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode:"+backCmd);
        String backCode = Cmds.CMD_UW + backCmd;
        backData(backCode.getBytes());
    }

    public void OperationSerial() {

        byte[] data = AssitTool.StringToBytes(wPinString);

        int pos = 0 ;
        byte[] writeData = new byte[2+1+1+1+data.length];
        System.arraycopy(CMD.SD_CMD_UPDATE_WORK_KEY,0,writeData,pos,2);
        pos = pos + 2 ;

        System.arraycopy(new byte[]{(byte)ZMKindex},0,writeData,pos,1);
        pos = pos + 1 ;

        System.arraycopy(new byte[]{(byte)wpinIndex},0,writeData,pos,1);
        pos = pos + 1 ;

        System.arraycopy(new byte[]{(byte)data.length},0,writeData,pos,1);
        pos = pos + 1 ;

        System.arraycopy(data,0,writeData,pos,data.length);

        final byte [] wbyte = new SerialRequestFrame().make_SD_Package(writeData);
        final SerialResponseFrame serialResponse = new SerialResponseFrame();

        SerialUtil.getInstance().getSerialPort().write(wbyte, wbyte.length);

        new Thread(
                new Runnable(){
                    @Override
                    public void run() {
                        SerialUtil.getInstance().setStop(false);
                        SerialUtil.getInstance().getmSReadThread().run();
                        SerialResponseFrame.lock.lock();
                        byte[] rbyte = serialResponse.get_SD_Rbyte();
                        SerialUtil.getInstance().setStop(true);
                        SerialResponseFrame.lock.unlock();
                        if(rbyte.length == 22 ) {
                            if (Arrays.equals(new byte[]{rbyte[0],rbyte[1],rbyte[2],rbyte[3],rbyte[4],rbyte[5]}, new byte[]{0x00, 0x14, (byte)0xa0, 0x08, 0x00, 0x00})) {
                                byte[] data = new byte[16] ;
                                System.arraycopy(rbyte,6,data,0,16);
                                if(Arrays.equals(data,pinKeyStringArray))
                                {
                                    sendConfirmCode(BackCode.CODE_00);
                                }
                                else {
                                    sendConfirmCode(BackCode.CODE_01);
                                }
                            } else {
                                sendConfirmCode(BackCode.CODE_01);
                            }
                        }
                        else
                        {
                            sendConfirmCode(BackCode.CODE_02);
                        }

                    }
                }
        ).start();

    }
}
