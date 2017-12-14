package com.joesmate.bin.keyBoard;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.frames.BaseSerialFrames;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by zwzhu on 2016/1/15.
 * 解析密码键盘返回的数据
 */
public class SerialResponseFrame extends BaseSerialFrames implements SerialUtil.OnDataReceiveListener {

    private static byte[] rbyte;
    public static ReentrantLock lock = new ReentrantLock();

    public SerialResponseFrame() {
        SerialUtil.getInstance().setOnDataReceiveListener(this);
    }

    @Override
     public byte[] makePackage() {
        return new byte[0];
    }

    @Override
    public byte[] extract() {
        return new byte[0];
    }

    /**
     * 1.获得密码键盘的数据
     *
     * @param buffer
     * @param size
     */
    @Override
    public void onDataReceive(byte[] buffer, int size) {

//        Log.d("SerialResponseFrame", "length is:" + size + ",data is:" + new String(buffer, 0, size));
        Log.d("SerialResponseFrame", "onDataReceive,"+size);
        rbyte = new byte[size];
        System.arraycopy(buffer, 0,rbyte , 0,size);
    }

    /**
     *rbyte 去掉0x02头，0x03尾，
     *
     * @return ok/er+data(去掉0x30)
     */
    public  byte[] getRbyte() {
        byte[] stateByte = new byte[2];
        if(rbyte == null){
        	rbyte = new byte[2];
        }
        if(rbyte.length < 5) return rbyte;
        byte[] retByte = new byte[rbyte.length - 4];//2 = 0x02头，0x03尾 ,ok/er
        Log.d("response 1 base", "" + Arrays.toString(rbyte));
        System.arraycopy(rbyte, 1, stateByte, 0, 2);
        System.arraycopy(rbyte, 3, retByte, 0, rbyte.length - 4);
        retByte = AssitTool.merge(retByte, 48);
        Log.d("response 2 base", "" + Arrays.toString(retByte));
        printHexString(retByte);
        rbyte = new byte[stateByte.length+retByte.length];
        System.arraycopy(stateByte, 0, rbyte, 0, 2);
        System.arraycopy(retByte, 0, rbyte, 2, retByte.length);
        Log.d("response 3 base", "" + Arrays.toString(rbyte));
        return rbyte;
    }


    /**
     *rbyte 去掉头，尾，crc
     *
     * @return ok/er+data(去掉0x30)
     */
    public  byte[] get_SD_Rbyte() {
        final int SPLIT = 0x30;
        if(rbyte != null) {
            if (rbyte.length >= 10) {
                //head 2  crc 2  tail 2
                byte[] returnByte = new byte[rbyte.length - 6];
                System.arraycopy(rbyte, 2, returnByte, 0, returnByte.length);
                byte[] returnData = AssitTool.merge(returnByte, SPLIT);
                printHexString(returnData);
                return returnData;
            } else {
                return rbyte;
            }
        }else
        {
            return  new byte[1];
        }

    }
    
    public static void printHexString(byte[] b)
    {
        String hexStr = "";
        for (int i = 0; i < b.length; i++)
        {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            hexStr = hexStr +hex.toLowerCase();
            if(i == 31)
            	hexStr = hexStr + " : ";
            //System.out.print(hex.toUpperCase() + " ");
            
        }
        Log.d("SerialR","hex: " + hexStr);
    }


}
