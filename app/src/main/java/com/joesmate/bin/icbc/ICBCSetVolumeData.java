package com.joesmate.bin.icbc;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.Cmds;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.BaseData;

import java.util.Arrays;

/**
 * Created by bill on 2016/1/10.
 */
public class ICBCSetVolumeData extends BaseData {

    public    static  final int Volume_TYPE_Business = 0 ;
    public    static  final int Volume_TYPE_Advertisement = 1 ;


    private static ICBCSetVolumeData iCBCSetVolumeData;
    public static ICBCSetVolumeData getInstance(){
        if(iCBCSetVolumeData == null){
            iCBCSetVolumeData = new ICBCSetVolumeData();
        }
        return iCBCSetVolumeData;
    }

    public int getVolumeType() {
        return VolumeType;
    }

    public void setVolumeType(int volumeType) {
        VolumeType = volumeType;
    }

    public int getVolumeCount() {
        return VolumeCount;
    }

    public void setVolumeCount(int volumeCount) {
        VolumeCount = volumeCount;
    }

    private int  VolumeType ;
    private int VolumeCount;



    @Override
    public void setData(byte[] buffer, byte[] cmd) {
        setCmd(cmd);
        if(Arrays.equals(Cmds.CMD_SV.getBytes(), cmd)){
            int pos = 2;
            VolumeType =(int)buffer[pos] ;
            Log.d(TAG, "VolumeType:" + VolumeType);

            pos = pos + 1;
            VolumeCount = (int)buffer[pos];
            Log.d(TAG, "VolumeCount:" + VolumeCount);
            int returncode = 0 ;

            if(VolumeCount != 0 && VolumeCount != 1 && VolumeCount !=2 && VolumeCount != 3 )
            {
                if(VolumeType == Volume_TYPE_Advertisement)
                {
                    returncode = SharedpreferencesData.getInstance().getAdvertisement_volume();
                    Log.d("volume","request system volume Advertisement :"+returncode);
                }
                if(VolumeType == Volume_TYPE_Business)
                {
                    returncode = SharedpreferencesData.getInstance().getBusiness_volume();
                    Log.d("volume","request system volume Business :"+returncode);
                }
                String backCode = Cmds.CMD_SV + BackCode.CODE_00 + returncode;
                backData(backCode.getBytes());

            }
            else {
                String backCode = Cmds.CMD_SV + BackCode.CODE_00;
                backData(backCode.getBytes());
                Log.d("volume", "return ok");
            }

            setVolume();
            //legalData();
        }
    }


    private void setVolume()
    {
        if(VolumeType == Volume_TYPE_Advertisement)
        {
            SharedpreferencesData.getInstance().setAdvertisement_volume(VolumeCount);
            if(SharedpreferencesData.getInstance().getCurrent_volume_type() == Volume_TYPE_Advertisement )
            {
                AssitTool.SetSystemVolume(AssitTool.GetVolumeByLevel(VolumeCount));
            }
        }
        if(VolumeType == Volume_TYPE_Business)
        {
            SharedpreferencesData.getInstance().setBusiness_volume(VolumeCount);
            if(SharedpreferencesData.getInstance().getCurrent_volume_type() == Volume_TYPE_Business )
            {
                AssitTool.SetSystemVolume(AssitTool.GetVolumeByLevel(VolumeCount));
            }
        }



    }

}
