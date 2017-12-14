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
public class SDCSUpdateMKeyData extends BaseData {

    private static SDCSUpdateMKeyData sDCSReadPinData;


    public int getZMKindex() {
        return ZMKindex;
    }

    public void setZMKindex(int ZMKindex) {
        this.ZMKindex = ZMKindex;
    }

    private  int ZMKindex ;

    public int getZMKLengthType() {
        return ZMKLengthType;
    }

    public void setZMKLengthType(int ZMKLengthType) {
        this.ZMKLengthType = ZMKLengthType;
    }

    private  int ZMKLengthType ;

    public int getZMKStrLength() {
        return ZMKStrLength;
    }

    public void setZMKStrLength(int ZMKStrLength) {
        this.ZMKStrLength = ZMKStrLength;
    }

    private  int ZMKStrLength ;

    public String getZMKString() {
        return ZMKString;
    }

    public void setZMKString(String ZMKString) {
        this.ZMKString = ZMKString;
    }

    private  String ZMKString ;

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

    private int pinKeyStrLength ;

    public String getPinKeyString() {
        return pinKeyString;
    }

    public void setPinKeyString(String pinKeyString) {
        this.pinKeyString = pinKeyString;
    }

    private String pinKeyString ;

    private  byte[]  pinKeyStringArray;

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
            ZMKindex =  buffer[pos];
            Log.d(TAG, "ZMKindex:" + ZMKindex);
            pos = pos + 1 ;

            //ZMKLengthType = AssitTool.getArrayCount(new byte[] { buffer[pos]});
            ZMKLengthType = buffer[pos];
            Log.d(TAG, "ZMKLengthType:" + ZMKLengthType);
            pos = pos + 1 ;

            ZMKStrLength = AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1]});
            Log.d(TAG, "ZMKStrLength:" + ZMKStrLength);
            pos = pos + 2 ;

            byte[]  ZMKStringArray = new byte[ZMKStrLength];
            System.arraycopy(buffer,pos,ZMKStringArray,0,ZMKStrLength);
            ZMKString =  AssitTool.getString(ZMKStringArray, AssitTool.UTF_8);
            Log.d(TAG, "ZMKString:" + ZMKString);
            pos = pos + ZMKStrLength ;

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
        String backCode = Cmds.CMD_UM + backCmd;
        backData(backCode.getBytes());
    }

    public void OperationSerial() {

        byte[] data = AssitTool.StringToBytes(ZMKString);

        int pos = 0 ;
        byte[] writeData = new byte[2+1+1+data.length];
        System.arraycopy(CMD.SD_CMD_UPDATE_MAIN_KEY,0,writeData,pos,2);
        pos = pos + 2 ;

        System.arraycopy(new byte[]{(byte)ZMKindex},0,writeData,pos,1);
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
                            if (Arrays.equals(new byte[]{rbyte[0],rbyte[1],rbyte[2],rbyte[3],rbyte[4],rbyte[5]}, new byte[]{0x00, 0x14, (byte)0xb0, 0x07, 0x00, 0x00})) {
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
