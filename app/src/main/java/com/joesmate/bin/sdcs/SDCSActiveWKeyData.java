package com.joesmate.bin.sdcs;

import android.util.Log;

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
public class SDCSActiveWKeyData extends BaseData {

    private static SDCSActiveWKeyData sDCSReadPinData;

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

    public static SDCSActiveWKeyData getInstance() {
        if (sDCSReadPinData == null) {
            sDCSReadPinData = new SDCSActiveWKeyData();
        }
        return sDCSReadPinData;
    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {

        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_AW.getBytes(), cmd)) {
            pos = 2;

            ZMKindex =  buffer[pos];
            Log.d(TAG, "ZMKindex:" + ZMKindex);
            pos = pos + 1 ;


            wpinIndex = buffer[pos];
            Log.d(TAG, "wpinIndex:" + wpinIndex);

            OperationSerial();



        }
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode:"+backCmd);
        String backCode = Cmds.CMD_AW + backCmd;
        backData(backCode.getBytes());
    };

    public void OperationSerial() {
        int pos = 0 ;
        byte[] writeData = new byte[2+1+1];
        System.arraycopy(CMD.SD_CMD_ACTIVATE_WORK_KEY,0,writeData,pos,2);
        pos = pos + 2 ;

        System.arraycopy(new byte[]{(byte)ZMKindex},0,writeData,pos,1);
        pos = pos + 1 ;

        System.arraycopy(new byte[]{(byte)wpinIndex},0,writeData,pos,1);

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
                        if(rbyte.length == 6 ) {
                            if (Arrays.equals(new byte[]{rbyte[0],rbyte[1],rbyte[2],rbyte[3],rbyte[4],rbyte[5]}, new byte[]{0x00, 0x04, (byte)0xa0, 0x09, 0x00, 0x00})) {
                                sendConfirmCode(BackCode.CODE_00);
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
