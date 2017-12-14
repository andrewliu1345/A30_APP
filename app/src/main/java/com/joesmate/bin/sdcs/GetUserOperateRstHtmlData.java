package com.joesmate.bin.sdcs;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

/**
 * Created by bill on 2016/5/14.
 */
public class GetUserOperateRstHtmlData extends BaseData {


    private static GetUserOperateRstHtmlData getUserOperateRstHtmlData;

    private int curIndex, totalNum;
    private int curdataLen ;
    private byte[] dataBuffer;


    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    private String voiceText ;


    private int voiceTextLen ;

    private  byte[] arrayvoiceText;


    private  int displayContentLen ;
    private  byte[] arrayDisplayContent;

    public String getDisplayContent() {
        return displayContent;
    }

    public void setDisplayContent(String displayContent) {
        this.displayContent = displayContent;
    }

    private  String displayContent ;

    public static GetUserOperateRstHtmlData getInstance() {
        if (getUserOperateRstHtmlData == null) {
            getUserOperateRstHtmlData = new GetUserOperateRstHtmlData();
        }
        return getUserOperateRstHtmlData;
    }


    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_OR.getBytes(), cmd)) {

            pos = 2;
            voiceTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "voiceTextLen:" + voiceTextLen);
            pos = pos + 4 ;

      /*      arrayvoiceText = new byte[voiceTextLen];
            System.arraycopy(buffer,pos,arrayvoiceText,0,voiceTextLen);
            voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
            Log.d(TAG, "voiceText:" + voiceText);
            pos = pos + voiceTextLen;*/


            displayContentLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3],buffer[pos+4]});
            Log.d(TAG, "displayContentLen:" + displayContentLen);
            pos = pos + 5 ;

       /*     arrayDisplayContent = new byte[displayContentLen];
            System.arraycopy(buffer,pos,arrayDisplayContent,0,displayContentLen);
            displayContent =  AssitTool.getString(arrayDisplayContent, AssitTool.UTF_8);
            Log.d(TAG, "displayContent:" + displayContent);
            pos = pos + displayContentLen ;*/

            int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
            setTimeOut(timeOut);
            Log.d(TAG, "timeOut:" + timeOut);
            pos = pos + 3 ;


            curIndex = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "curIndex:" + curIndex);
            pos = pos + 4 ;

            totalNum = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "totalNum:" + totalNum);
            pos = pos + 4 ;

            curdataLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "curdataLen:" + curdataLen);
            pos = pos + 4 ;


            byte[] receiveBuf = new byte[curdataLen];
            System.arraycopy(buffer, pos, receiveBuf, 0, receiveBuf.length);

            if (totalNum == curIndex) {
                // 包序号采用倒序,序号与总包个数相同则是新包
                fileDatas.clear();
            }

            FileData fileData = new FileData(receiveBuf.length);
            fileData.setBuffer(receiveBuf);
            fileDatas.add(fileData);


            if (curIndex == 1)
            {
                Log.d(TAG, "curIndex:" + curIndex);
                dataBuffer = getTotalFileData(fileDatas);
                Log.d(TAG,"dataBuffer len:" +  dataBuffer.length);
                fileDatas.clear();

                int pos1 = 0 ;

                byte[] arrayvoiceText = new byte[voiceTextLen];
                System.arraycopy(dataBuffer,pos1,arrayvoiceText,0,voiceTextLen);
                voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
                Log.d(TAG, "voiceText:" + voiceText);
                pos1 = pos1  + voiceTextLen;


                arrayDisplayContent = new byte[displayContentLen];
                System.arraycopy(dataBuffer,pos1,arrayDisplayContent,0,displayContentLen);
                displayContent =  AssitTool.getString(arrayDisplayContent, AssitTool.UTF_8);
                Log.d(TAG, "displayContent:" + displayContent);
                //pos1 = pos1 + displayContentLen ;


                legalData();

            }
            sendConfirmCode(Cmds.CMD_OR);
        }
    }



    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode");
        String backCode = backCmd + BackCode.CODE_00;
        backData(backCode.getBytes());
    }

    public void sendConfirmResult(String result,String data) {
        String backCode = Cmds.CMD_OR + BackCode.CODE_20 + result;
        Log.d(TAG, "sendConfirmResult result:"+ result + " data:" + data );


        byte[] bs = new byte[2 + 2 + 1 + 4 + data.length()];
        int pos = 0 ;
        System.arraycopy(backCode.getBytes(),0,bs,pos,4);
        pos = pos + 4 ;

        System.arraycopy(result.getBytes(),0,bs,pos,1);
        pos = pos + 1 ;

        System.arraycopy(AssitTool.getCount4N(data.length()),0,bs,pos,4);
        pos = pos + 4 ;

        System.arraycopy(data.getBytes(), 0, bs, pos, data.getBytes().length);
        backData(bs);




    }
}
