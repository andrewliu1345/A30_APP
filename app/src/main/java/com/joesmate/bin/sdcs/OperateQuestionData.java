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
public class OperateQuestionData extends BaseData {

    public static final int TYPE_OPTION_SINGLE = 1;
    public static final int TYPE_OPTION_DOUBLE = 2;

    private static OperateQuestionData operateQuestionData;
    private int curIndex, totalNum;
    private int curdataLen ;

    public String[] getOptionItems() {
        return optionItems;
    }

    public void setOptionItems(String[] optionItems) {
        this.optionItems = optionItems;
    }

    private String[] optionItems;

    public int getQuestionCurIndex() {
        return questionCurIndex;
    }

    public void setQuestionCurIndex(int questionCurIndex) {
        this.questionCurIndex = questionCurIndex;
    }

    private int questionCurIndex;

    public int getQuestiontotalNum() {
        return questiontotalNum;
    }

    public void setQuestiontotalNum(int questiontotalNum) {
        this.questiontotalNum = questiontotalNum;
    }

    private int questiontotalNum ;


    public static OperateQuestionData getInstance() {
        if (operateQuestionData == null) {
            operateQuestionData = new OperateQuestionData();
        }
        return operateQuestionData;
    }

    int QuestionDataLen ;

    public String getQuestionData() {
        return QuestionData;
    }

    public void setQuestionData(String questionData) {
        QuestionData = questionData;
    }

    String QuestionData ;
    int  OptionsLen ;

    public String getOptions() {
        return Options;
    }

    public void setOptions(String options) {
        Options = options;
    }

    String  Options ;

    public int getQuestionClass() {
        return QuestionClass;
    }

    public void setQuestionClass(int questionClass) {
        QuestionClass = questionClass;
    }

    int  QuestionClass ;
    int QuestionNumberLen ;

    public String getQuestionNumber() {
        return QuestionNumber;
    }



    public void setQuestionNumber(String questionNumber) {
        QuestionNumber = questionNumber;
    }

    String QuestionNumber;
    int voiceTextLen ;

    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    String voiceText ;

    private byte[] dataBuffer;




    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        int pos = 0;
        if (Arrays.equals(Cmds.CMD_OQ.getBytes(), cmd)) {

            pos = 2;

            QuestionDataLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "QuestionDataLen:" + QuestionDataLen);
            pos = pos + 4 ;


            OptionsLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "OptionsLen:" + OptionsLen);
            pos = pos + 4 ;


            QuestionClass =  AssitTool.getArrayCount(new byte[]{buffer[pos]});
            Log.d(TAG, "QuestionClass:" + QuestionClass);
            pos = pos + 1 ;



            QuestionNumberLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "QuestionNumberLen:" + QuestionNumberLen);
            pos = pos + 4 ;



            voiceTextLen = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos + 1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "voiceTextLen:" + voiceTextLen);
            pos = pos + 4 ;




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


                 byte[] arrayQuestionData  = new byte[QuestionDataLen];
                  System.arraycopy(dataBuffer,pos1,arrayQuestionData,0,QuestionDataLen);
                  QuestionData =  AssitTool.getString(arrayQuestionData, AssitTool.UTF_8);
                  Log.d(TAG, "QuestionData:" + QuestionData);
                  pos1 = pos1 + QuestionDataLen;


                 byte[] arrayOptions  = new byte[OptionsLen];
                 System.arraycopy(dataBuffer,pos1,arrayOptions,0,OptionsLen);
                 Options =  AssitTool.getString(arrayOptions, AssitTool.UTF_8);
                 Log.d(TAG, "Options:" + Options);
                 setOptionItems(AssitTool.getSplit(Options,AssitTool.SPLIT_LINE_NEW));
                 Log.d(TAG, "OptionItems:" + optionItems.toString());
                 pos1 = pos1 + OptionsLen;



                 byte[] arrayQuestionNumber  = new byte[QuestionNumberLen];
                 System.arraycopy(dataBuffer,pos1,arrayQuestionNumber,0,QuestionNumberLen);
                 QuestionNumber =  AssitTool.getString(arrayQuestionNumber, AssitTool.UTF_8);
                 Log.d(TAG, "QuestionNumber:" + QuestionNumber);
                 pos1 = pos1 + QuestionNumberLen;

                 String[] QuestionNumberData = QuestionNumber.split("/");
                if(QuestionNumberData.length == 2) {
                    questionCurIndex =  Integer.parseInt(QuestionNumberData[0]);
                    Log.d(TAG, "questionCurIndex:" + questionCurIndex);
                    questiontotalNum =  Integer.parseInt(QuestionNumberData[1]);
                    Log.d(TAG, "questiontotalNum:" + questiontotalNum);
                }

                  byte[] arrayvoiceText = new byte[voiceTextLen];
                  System.arraycopy(dataBuffer,pos1,arrayvoiceText,0,voiceTextLen);
                  voiceText =  AssitTool.getString(arrayvoiceText, AssitTool.UTF_8);
                  Log.d(TAG, "voiceText:" + voiceText);
                  pos1 = pos1  + voiceTextLen;


                  legalData();

            }
            sendConfirmCode(Cmds.CMD_OQ);


        }
    }


    public void sendConfirmCode(String backCmd) {
        Log.d(TAG, "sendConfirmCode");
        String backCode = backCmd + BackCode.CODE_00;
        backData(backCode.getBytes());
    }

    public void sendConfirmResult(String result,String data) {
        String backCode = Cmds.CMD_OQ + BackCode.CODE_20 + result;
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

