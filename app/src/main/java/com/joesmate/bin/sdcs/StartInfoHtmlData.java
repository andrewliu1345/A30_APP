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
public class StartInfoHtmlData extends BaseData {


    private static StartInfoHtmlData startInfoHtmlData;
    private int curIndex, totalNum;

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    private String voiceText ;

    public String getHtmlData() {
        return htmlData;
    }

    public void setHtmlData(String htmlData) {
        this.htmlData = htmlData;
    }

    private String htmlData;
    private int voiceTextLen ;
    private int htmlDataLen ;
    private int curdataLen ;


    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }

    private int displayType ;

    private  byte[] arrayvoiceText;
    private  byte[] arrayHtmlData;
    private byte[] dataBuffer;

    public static StartInfoHtmlData getInstance() {
        if (startInfoHtmlData == null) {
            startInfoHtmlData = new StartInfoHtmlData();
        }
        return startInfoHtmlData;
    }


    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_SI.getBytes(), cmd)) {

            pos = 2;

            int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
            setTimeOut(timeOut);
            Log.d(TAG, "timeOut:" + timeOut);
            pos = pos + 3 ;


            displayType = (int)buffer[pos];
            Log.d(TAG, "displayType:" + displayType);
            pos = pos + 1 ;

            voiceTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "voiceTextLen:" + voiceTextLen);
            pos = pos + 4 ;

            htmlDataLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "htmlDataLen:" + htmlDataLen);
            pos = pos + 4 ;

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

            if (curIndex == 1) {
                Log.d(TAG, "curIndex:" + curIndex);
                dataBuffer = getTotalFileData(fileDatas);
                Log.d(TAG,"dataBuffer len:" +  dataBuffer.length);
                fileDatas.clear();

                int pos1 = 0 ;
                arrayvoiceText = new byte[voiceTextLen];
                System.arraycopy(dataBuffer,pos1,arrayvoiceText,0,voiceTextLen);
                voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
                Log.d(TAG, "voiceText:" + voiceText);

                pos1 = pos1 +  voiceTextLen ;
                arrayHtmlData = new byte[htmlDataLen];
                System.arraycopy(dataBuffer,pos1,arrayHtmlData,0,htmlDataLen);
                htmlData = AssitTool.getString(arrayHtmlData,AssitTool.UTF_8);
                Log.d(TAG, "htmlData:" + htmlData);
                legalData();

            }
            sendConfirmCode(Cmds.CMD_SI);

        }
    }

    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode");
        String backCode = backCmd + BackCode.CODE_00;
        backData(backCode.getBytes());
    }

    public void sendConfirmResult(String result) {
        String backCode = Cmds.CMD_SI + BackCode.CODE_20 + result;
        Log.d(TAG, "sendConfirmResult:"+backCode );
        backData(backCode.getBytes());
    }
}
