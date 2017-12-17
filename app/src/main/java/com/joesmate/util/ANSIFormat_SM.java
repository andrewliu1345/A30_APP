package com.joesmate.util;

import com.joesmate.AssitTool;

import java.util.Arrays;

/**
 * Created by andre on 2017/12/17 .
 */

public class ANSIFormat_SM {
    private static final String TAG = ANSIFormat.class.getName();
    private String pin;
    private String accno;

    public ANSIFormat_SM(String pin, String accno) {
        this.pin = pin;
        this.accno = accno;
    }

    public static byte[] process(String pin, String accno) {
        byte arrPin[] = getHPin(pin);
        byte arrAccno[] = getHAccno(accno);
        byte arrRet[] = new byte[16];
        //PIN BLOCK 格式等于 PIN 按位异或 主帐号;
        for (int i = 0; i <16; i++) {
            arrRet[i] = (byte) (arrPin[i] ^ arrAccno[i]);
        }
        LogMg.d(TAG, "encode :%s", Util.Bytes2HexString(arrRet));
        return arrRet;
    }

    private static byte[] getHPin(String pin) {

        byte encode[] = new byte[16];
        Arrays.fill(encode, (byte) 0xff);


        byte arrpin[] = AssitTool.HexStringToBytes(pin);
        encode[0] = (byte) pin.length();
        for (int i = 1, j = 0; j < arrpin.length && i < encode.length; i++, j++) {
            encode[i] = arrpin[j];
        }

        LogMg.d(TAG, "encode :%s", AssitTool.BytesToHexString(encode));
        return encode;
    }

    private static byte[] getHAccno(String accno) {
        //取出主帐号；

        byte arrAccno[] = AssitTool.HexStringToBytes(accno);
        byte encode[] = new byte[16];
        int accnolen = arrAccno.length;
        Arrays.fill(encode, (byte) 0x00);
        for (int i = 15, j = accnolen - 1; i >= 0 && j >= 0; i--, j--) {
            encode[i] = arrAccno[j];
        }
        LogMg.d(TAG, "encode :%s", AssitTool.BytesToHexString(encode));
        return encode;
    }
}
