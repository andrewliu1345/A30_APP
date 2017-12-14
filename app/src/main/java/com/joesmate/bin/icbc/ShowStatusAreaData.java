package com.joesmate.bin.icbc;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

/**
 * Created by bill on 2016/1/10.
 */
public class ShowStatusAreaData extends BaseData {

    private static ShowStatusAreaData showStatusAreaData;
    public static ShowStatusAreaData getInstance(){
        if(showStatusAreaData == null){
            showStatusAreaData = new ShowStatusAreaData();
        }
        return showStatusAreaData;
    }


    public String getNewToptitle() {
        return newToptitle;
    }

    public void setNewToptitle(String newToptitle) {
        this.newToptitle = newToptitle;
    }

    private String newToptitle ;


    public String getVoiceText() {
        return voiceText;
    }

    public void setVoiceText(String voiceText) {
        this.voiceText = voiceText;
    }

    private String voiceText ;

    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        if(Arrays.equals(Cmds.CMD_SS.getBytes(), cmd)){
            //SharedpreferencesData.getInstance().setPoster_type(buffer[2]);
            int pos = 2 ;
            int newTopTitleContentLength   = AssitTool.getArrayCount(new byte[]{buffer[pos],
                    buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]}) ;
            Log.d(TAG,"newTopTitleContentLength:"+ newTopTitleContentLength);
            pos = pos + 4 ;
            int voiceTextLength = AssitTool.getArrayCount(new byte[]{buffer[pos],
                    buffer[pos + 1], buffer[pos + 2], buffer[pos + 3]}) ;
            Log.d(TAG,"voiceTextLength:"+ voiceTextLength);

            pos = pos + 4 ;

            byte[] arrayTopTitleContentData = new byte[newTopTitleContentLength] ;
            System.arraycopy(buffer,pos,arrayTopTitleContentData,0,newTopTitleContentLength);
            newToptitle =  AssitTool.getString(arrayTopTitleContentData, AssitTool.UTF_8);
            Log.d(TAG, "newToptitle:" + newToptitle);


            pos = pos + newTopTitleContentLength ;
            byte[] arrayvoiceTextData = new byte[voiceTextLength] ;
            System.arraycopy(buffer,pos,arrayvoiceTextData,0,voiceTextLength);
            voiceText =  AssitTool.getString(arrayvoiceTextData, AssitTool.UTF_8);
            Log.d(TAG, "voiceText:" + voiceText);
            backSSData();
            legalData();
        }

    }

    private void backSSData(){
        String backCode = Cmds.CMD_SS + BackCode.CODE_00;
        backData(backCode.getBytes());
    }
}
