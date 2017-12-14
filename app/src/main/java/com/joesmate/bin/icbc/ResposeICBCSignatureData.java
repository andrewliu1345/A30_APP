package com.joesmate.bin.icbc;

import android.graphics.Bitmap;
import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ResposeICBCSignatureData extends BaseData {

    private static ResposeICBCSignatureData resposeICBCSignatureData;
    public static final String TYPE_IMG = "1";
    public static final String TYPE_TRACK = "2";
    static final float MAX_LEN = 4000f;
    private byte[] resposeData;

    private byte[] signData;
    private byte[] imgData;
    private byte[] signpdfData;
    //private static int curIndex;
    private static int totalNum;
    private static int packIndex;

    public int getSignState() {
        return signState;
    }

    public void setSignState(int signState) {
        this.signState = signState;
    }

    private int signState;
    String backCode = null;

    public static ResposeICBCSignatureData getInstance() {
        if (resposeICBCSignatureData == null) {
            resposeICBCSignatureData = new ResposeICBCSignatureData();
        }
        return resposeICBCSignatureData;
    }

    public void setResposeData(byte[] data, int length) {

    }

    public void setResposeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        imgData = baos.toByteArray();

        File file = new File(ICBCSignData.getInstance().getSignImagePath());
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            out.write(baos.toByteArray());
            out.flush();
            out.close();
            baos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //setData(CMD.SR, CMD.SR);
    }

    public void setSignData(String signdata) {
        signData = signdata.getBytes();
    }

    public void setSignPdfData() {
        File file = new File(ICBCSignData.getInstance().getPDFSignPath());
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
        resposeData = new byte[6 * 3 + signData.length + imgData.length + signpdfData.length];
        int pos = 0;
        System.arraycopy(AssitTool.getCount6N(signData.length), 0, resposeData, pos, 6);
        pos += 6;
        System.arraycopy(AssitTool.getCount6N(imgData.length), 0, resposeData, pos, 6);
        pos += 6;
        System.arraycopy(AssitTool.getCount6N(signpdfData.length), 0, resposeData, pos, 6);
        pos += 6;
        System.arraycopy(signData, 0, resposeData, pos, signData.length);
        pos += signData.length;
        System.arraycopy(imgData, 0, resposeData, pos, imgData.length);
        pos += imgData.length;
        System.arraycopy(signpdfData, 0, resposeData, pos, signpdfData.length);
        Log.d("myitm", "resposeData :" + resposeData.length);

    }

    @Override
    public void setData(byte[] buffer, byte[] cmd) {

        //MakeResposeData() ;
        int current_index = -1;
        if (buffer == null) {
            Log.e(TAG, "buffer is null");
            return;
        }
        Log.d("myitm", " enter    totalNum:" + totalNum + "  packIndex:" + packIndex);
        if (Arrays.equals(cmd, Cmds.CMD_RG.getBytes())) {
            backCode = Cmds.CMD_RG;
            if (resposeData == null) {
                return;
            }
            float resposeDataLen = resposeData.length;
            totalNum = (int) Math.ceil(resposeDataLen / MAX_LEN);
            Log.d("myitm", " --------------------------------------------cmd rg totalNum：" + totalNum);
            current_index = totalNum;
            packIndex = 0;

        } else if (Arrays.equals(cmd, Cmds.BACK_RR.getBytes())) {
            //TODO 如果返回码是不合
            int pos = 2;
            current_index = AssitTool.getArrayCount(new byte[]{buffer[pos], buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]});
            Log.d("myitm", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!cmd rr respose  index ：" + current_index);
            packIndex += 4000;
            //current_index = index ;

        }


/*
        String erroCode = null;
		if(resposeData == null){
			erroCode = BackCode.CODE_12;
			backData((backCode + backCode).getBytes());
			return;
		}else{
			erroCode = BackCode.CODE_00;
		}*/

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


}
