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
public class ActiveSignQryInfoData extends BaseData {


    private static ActiveSignQryInfoData activeSignQryInfoData;

    public static final int TYPE_OPTION_SINGLE = 1;
    public static final int TYPE_OPTION_DOUBLE = 2;

    public String getVoiceText() {
        return voiceText;
    }
    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }
    private String voiceText ;
    private int voiceTextLen ;
    private  byte[] arrayvoiceText;

    private int titleLen;

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    private String title;
    private int accountLen;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    private String account;
    private int tableCols ;
    private int leftButtonTextLen;

    public String getLeftButtonText() {
        return leftButtonText;
    }

    public void setLeftButtonText(String leftButtonText) {
        this.leftButtonText = leftButtonText;
    }

    private String leftButtonText;
    private int rightButtonTextLen;

    public String getRightButtonText() {
        return rightButtonText;
    }

    public void setRightButtonText(String rightButtonText) {
        this.rightButtonText = rightButtonText;
    }

    private String rightButtonText;
    private int tableTitleLen;

    public String getTableTitle() {
        return tableTitle;
    }

    public void setTableTitle(String tableTitle) {
        this.tableTitle = tableTitle;
    }

    private String tableTitle;
    private int tableContentLen;

    public String getTableContent() {
        return tableContent;
    }

    public void setTableContent(String tableContent) {
        this.tableContent = tableContent;
    }

    private String tableContent;

    public int getTableContentNumber() {
        return tableContentNumber;
    }

    public void setTableContentNumber(int tableContentNumber) {
        this.tableContentNumber = tableContentNumber;
    }

    private int tableContentNumber;
    private int colWidthAbsLen ;

    public String getColWidthAbs() {
        return colWidthAbs;
    }

    public void setColWidthAbs(String colWidthAbs) {
        this.colWidthAbs = colWidthAbs;
    }

    private String colWidthAbs;

    public int getQuestionClass() {
        return questionClass;
    }

    public void setQuestionClass(int questionClass) {
        this.questionClass = questionClass;
    }

    private int questionClass;


    private int curIndex, totalNum;
    private int curdataLen ;
    private byte[] dataBuffer;






    public static ActiveSignQryInfoData getInstance() {
        if (activeSignQryInfoData == null) {
            activeSignQryInfoData = new ActiveSignQryInfoData();
        }
        return activeSignQryInfoData;
    }


    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_QI.getBytes(), cmd)) {


            pos = 2;
            voiceTextLen =  AssitTool.getArrayCount(new byte[] { buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "voiceTextLen:" + voiceTextLen);
            pos = pos + 4 ;


            int timeOut = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
            setTimeOut(timeOut);
            Log.d(TAG, "timeOut:" + timeOut);
            pos = pos + 3;


            titleLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "titleLen:" + titleLen);
            pos = pos + 4 ;


            accountLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "accountLen:" + accountLen);
            pos = pos + 4 ;


            tableCols =  AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2]});
            Log.d(TAG, "tableCols:" + tableCols);
            pos = pos + 3;



            leftButtonTextLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "leftButtonTextLen:" + leftButtonTextLen);
            pos = pos + 4 ;


            rightButtonTextLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "rightButtonTextLen:" + rightButtonTextLen);
            pos = pos + 4 ;


            tableTitleLen =  AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "tableTitleLen:" + tableTitleLen);
            pos = pos + 4 ;


            tableContentLen =  AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "tableContentLen:" + tableContentLen);
            pos = pos + 4 ;


            tableContentNumber = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "tableContentNumber:" + tableContentNumber);
            pos = pos + 4 ;


            colWidthAbsLen =  AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "colWidthAbsLen:" + colWidthAbsLen);
            pos = pos + 4 ;


            questionClass =  AssitTool.getArrayCount(new byte[]{buffer[pos]});
            Log.d(TAG, "questionClass:" + questionClass);
            pos = pos + 1 ;



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
                Log.d(TAG, "dataBuffer len:" + dataBuffer.length);
                fileDatas.clear();

                int pos1 = 0;


                byte[] arrayvoiceText = new byte[voiceTextLen];
                System.arraycopy(dataBuffer,pos1,arrayvoiceText,0,voiceTextLen);
                voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
                Log.d(TAG, "voiceText:" + voiceText);
                pos1 = pos1  + voiceTextLen;



                byte[] arrayTitle  = new byte[titleLen];
                System.arraycopy(dataBuffer,pos1,arrayTitle,0,titleLen);
                title =  AssitTool.getString(arrayTitle, AssitTool.UTF_8);
                Log.d(TAG, "title:" + title);
                pos1 = pos1 + titleLen;



                byte[] arrayAccount  = new byte[accountLen];
                System.arraycopy(dataBuffer,pos1,arrayAccount,0,accountLen);
                account =  AssitTool.getString(arrayAccount, AssitTool.UTF_8);
                Log.d(TAG, "account:" + account);
                pos1 = pos1 + accountLen;



                byte[] arrayLeftButtonText  = new byte[leftButtonTextLen];
                System.arraycopy(dataBuffer,pos1,arrayLeftButtonText,0,leftButtonTextLen);
                leftButtonText =  AssitTool.getString(arrayLeftButtonText, AssitTool.UTF_8);
                Log.d(TAG, "leftButtonText:" + leftButtonText);
                pos1 = pos1 + leftButtonTextLen;


                byte[] arrayRightButtonText  = new byte[rightButtonTextLen];
                System.arraycopy(dataBuffer,pos1,arrayRightButtonText,0,rightButtonTextLen);
                rightButtonText =  AssitTool.getString(arrayRightButtonText, AssitTool.UTF_8);
                Log.d(TAG, "rightButtonText:" + rightButtonText);
                pos1 = pos1 + rightButtonTextLen;


                byte[] arrayTableTitle  = new byte[tableTitleLen];
                System.arraycopy(dataBuffer,pos1,arrayTableTitle,0,tableTitleLen);
                tableTitle =  AssitTool.getString(arrayTableTitle, AssitTool.UTF_8);
                Log.d(TAG, "tableTitle:" + tableTitle);
                pos1 = pos1 + tableTitleLen;


                byte[] arrayTableContent  = new byte[tableContentLen];
                System.arraycopy(dataBuffer,pos1,arrayTableContent,0,tableContentLen);
                tableContent =  AssitTool.getString(arrayTableContent, AssitTool.UTF_8);
                Log.d(TAG, "tableContent:" + tableContent);
                pos1 = pos1 + tableContentLen;



                byte[] arrayColWidthAbs = new byte[colWidthAbsLen];
                System.arraycopy(dataBuffer,pos1,arrayColWidthAbs,0,colWidthAbsLen);
                colWidthAbs =  AssitTool.getString(arrayColWidthAbs, AssitTool.UTF_8);
                Log.d(TAG, "colWidthAbs:" + colWidthAbs);
                pos1 = pos1 + colWidthAbsLen;


                legalData();

            }
            sendConfirmCode(Cmds.CMD_QI);

        }
    }







    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode");
        String backCode = backCmd + BackCode.CODE_00;
        backData(backCode.getBytes());
    }

    public void sendConfirmResult(String result,String data) {
        String backCode = Cmds.CMD_QI + BackCode.CODE_20 + result;
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
