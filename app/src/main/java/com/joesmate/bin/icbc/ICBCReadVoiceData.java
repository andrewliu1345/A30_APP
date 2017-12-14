package com.joesmate.bin.icbc;

import android.media.MediaPlayer;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.FileInf;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

/**
 * Created by bill on 2016/1/10.
 */
public class ICBCReadVoiceData extends BaseData {

    private   static  final int AUDIO_TYPE_TEXT = 0 ;
    private   static  final int AUDIO_TYPE_FILE = 1 ;

    private static ICBCReadVoiceData iCBCReadVoiceData;
    public static ICBCReadVoiceData getInstance(){
        if(iCBCReadVoiceData == null){
            iCBCReadVoiceData = new ICBCReadVoiceData();
        }
        return iCBCReadVoiceData;
    }

    private int  voiceType ;
    private String audioContent ;


    public int  getVoiceType() {
        return voiceType;
    }

    public void setAudioType(int _voiceType) {
        this.voiceType = _voiceType;
    }

    public String getAudioContent() {
        return audioContent;
    }

    public void setAudioContent(String audioContent) {
        this.audioContent = audioContent;
    }




    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        if(Arrays.equals(Cmds.CMD_RV.getBytes(), cmd)){
            //SharedpreferencesData.getInstance().setPoster_type(buffer[2]);
            Log.v(TAG, "receive cmd_rv: " + buffer[2]);


            int pos = 2;
            voiceType = (int)buffer[pos];
            Log.d(TAG, "voiceType:"+voiceType);

            pos = pos + 1;
            int receiveLen  = AssitTool.getArrayCount(new byte[]{buffer[pos],buffer[pos+1],buffer[pos+2],buffer[pos+3]});
            Log.d(TAG, "receiveLen:"+receiveLen);

            pos = pos + 4;
            byte[] arraysAudioContent = new byte[receiveLen];
            System.arraycopy(buffer, pos, arraysAudioContent, 0, arraysAudioContent.length);
            audioContent = AssitTool.getString(arraysAudioContent, AssitTool.UTF_8);
            Log.d(TAG, "audioContent:" + audioContent);
            backRVData();
            if(voiceType == AUDIO_TYPE_TEXT) {
                App.getInstance().tts.Read(audioContent, 1);
            }
            if(voiceType == AUDIO_TYPE_FILE) {
                MediaPlayer player = new MediaPlayer();

                String filepath = FileInf.AUDIO+"/"+audioContent;
                Log.d(TAG,"filepath:"+ filepath);
                try{
                player.setDataSource(filepath);
                player.prepare();
                player.start();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
            //legalData();
        }
    }

    private void backRVData(){
        String backCode = Cmds.CMD_RV + BackCode.CODE_00;
        backData(backCode.getBytes());
    }
}
