package com.joesmate.bin;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.Cmds;

import java.io.File;
import java.io.FileInputStream;
import java.util.Arrays;

/**
 * Created by andre on 2017/12/28 .
 */

public class ResposeFingerSignerData extends BaseData {
    private byte[] signpdfData;
    private byte[] resposeData;
    private static int totalNum;
    private static int packIndex;
    static final float MAX_LEN = 4000f;

    private static ResposeFingerSignerData mInstance;

    public static ResposeFingerSignerData getInstance() {
        if (mInstance == null) {
            mInstance = new ResposeFingerSignerData();
        }
        return mInstance;
    }

    public int getSignState() {
        return signState;
    }

    public void setSignState(int signState) {
        this.signState = signState;
    }

    private int signState;
    String backCode = null;
    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        //MakeResposeData() ;
        int current_index = -1;
        if (buffer == null) {
            Log.e(TAG, "buffer is null");
            return;
        }
        Log.d("myitm", " enter    totalNum:" + totalNum + "  packIndex:" + packIndex);
       if (Arrays.equals(cmd, Cmds.CMD_RF.getBytes())) {
            //TODO 如果返回码是不合
           backCode = Cmds.CMD_RF;
           if (resposeData == null) {
               return;
           }
           float resposeDataLen = resposeData.length;
           totalNum = (int) Math.ceil(resposeDataLen / MAX_LEN);
            int pos = 2;
            current_index = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d("myitm", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!cmd rr respose  index ：" + current_index);
            packIndex += 4000;
            //current_index = index ;

        }


        if (current_index > 0) {
            int arraySendLen = resposeData.length - packIndex;
            arraySendLen = (int) (arraySendLen >= MAX_LEN ? MAX_LEN : arraySendLen);
            byte[] arraySend = new byte[arraySendLen];
            System.arraycopy(resposeData, packIndex, arraySend, 0, arraySendLen);

            byte[] arrayPack = new byte[backCode.length() + 1 + 12 + arraySend.length];
            int post = 0;
            System.arraycopy(backCode.getBytes(), 0, arrayPack, post, backCode.length());


            post = backCode.length();


            Log.d("myitm", "signState：" + signState);
            //当前signState
            byte[] arraySignState = AssitTool.getCount41(signState);
            System.arraycopy(arraySignState, 0, arrayPack, post, arraySignState.length);


            post = post + 1;
            Log.d("myitm", "current_index：" + current_index);
            //当前index
            byte[] arrayCurIndex = AssitTool.getCount4N(current_index);
            System.arraycopy(arrayCurIndex, 0, arrayPack, post, arrayCurIndex.length);


            post += arrayCurIndex.length;
            // Log.d("myitm", "totalNum："+totalNum);
            //总数量
            byte[] arrayTotal = AssitTool.getCount4N(totalNum);
            System.arraycopy(arrayTotal, 0, arrayPack, post, arrayTotal.length);


            post += arrayTotal.length;
            Log.d("myitm", "arraySend.length：" + arraySend.length);
            //当前数据长度
            byte[] arrayDataLen = AssitTool.getCount4N(arraySend.length);
            System.arraycopy(arrayDataLen, 0, arrayPack, post, arrayDataLen.length);


            post += arrayDataLen.length;
            System.arraycopy(arraySend, 0, arrayPack, post, arraySend.length);
            backData(arrayPack);

            //ResposeICBCSignatureData.getInstance().setSignState(0);


        }

    }
    public void setSignPdfData() {
        File file = new File(FingerSignerData.getInstance().getPDFSignPath());
        if (file.exists()) {
            try {
                FileInputStream out = new FileInputStream(file);
                int file_size = out.available();
                signpdfData = new byte[file_size];
                out.read(signpdfData);
                out.close();
            } catch (Exception ex) {

            }
        }

    }

    public void MakeResposeData() {
        resposeData = new byte[6 * 1 + signpdfData.length];
        int pos = 0;
        System.arraycopy(AssitTool.getCount6N(signpdfData.length), 0, resposeData, pos, 6);
        pos += 6;
        System.arraycopy(signpdfData, 0, resposeData, pos, signpdfData.length);
        pos += signpdfData.length;
        Log.d("myitm", "resposeData :" + resposeData.length);

    }
}
