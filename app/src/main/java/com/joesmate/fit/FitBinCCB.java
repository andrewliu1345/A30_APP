package com.joesmate.fit;

import android.content.Intent;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.bin.AddFileData;
import com.joesmate.bin.AddWebFileData;
import com.joesmate.bin.AppraiseCommonData;
import com.joesmate.bin.BaseData;
import com.joesmate.bin.ClearScreentData;
import com.joesmate.bin.DeleteFileData;
import com.joesmate.bin.DevRebootData;
import com.joesmate.bin.DeviceIdData;
import com.joesmate.bin.ExistFileData;
import com.joesmate.bin.GenerateKeyPw;
import com.joesmate.bin.GetPwCrc;
import com.joesmate.bin.GetSignPDFStateData;
import com.joesmate.bin.InteractiveMsgData;
import com.joesmate.bin.OptionPlayData;
import com.joesmate.bin.QueryFileData;
import com.joesmate.bin.QuestionData;
import com.joesmate.bin.SecretKeyData;
import com.joesmate.bin.SetPwMkey;
import com.joesmate.bin.SetPwWkey;
import com.joesmate.bin.SettingsData;
import com.joesmate.bin.UpdateApkData;
import com.joesmate.bin.icbc.ICBCDeviceInfData;
import com.joesmate.bin.icbc.ICBCDeviceStatusData;
import com.joesmate.bin.icbc.ICBCReadVoiceData;
import com.joesmate.bin.icbc.ICBCSetVolumeData;
import com.joesmate.bin.icbc.ICBCSignData;
import com.joesmate.bin.icbc.ResposeICBCSignatureData;
import com.joesmate.bin.icbc.SetPosterTypeData;
import com.joesmate.bin.icbc.SetStatusAreaStyleData;
import com.joesmate.bin.icbc.ShowStatusAreaData;
import com.joesmate.bin.sdcs.ActiveSignQryInfoData;
import com.joesmate.bin.sdcs.GetUserOperateRstHtmlData;
import com.joesmate.bin.sdcs.OperateQuestionData;
import com.joesmate.bin.sdcs.ReadDeviceIdData;
import com.joesmate.bin.sdcs.SDCSActiveWKeyData;
import com.joesmate.bin.sdcs.SDCSConfirmPDF;
import com.joesmate.bin.sdcs.SDCSDownLoadWKeyData;
import com.joesmate.bin.sdcs.SDCSInputCharacterData;
import com.joesmate.bin.sdcs.SDCSPlayVoiceData;
import com.joesmate.bin.sdcs.SDCSReadPinData;
import com.joesmate.bin.sdcs.SDCSResetPinData;
import com.joesmate.bin.sdcs.SDCSStartElectronicCardData;
import com.joesmate.bin.sdcs.SDCSStartEvaluateData;
import com.joesmate.bin.sdcs.SDCSUpdateMKeyData;
import com.joesmate.bin.sdcs.StartInfoHtmlData;
import com.joesmate.listener.OnIoListener;

import java.util.Arrays;

public class FitBinCCB extends BaseFitBin {
    public static final String TAG = "FitBinCCB";

    public FitBinCCB(OnIoListener onIoListener) {
        super(onIoListener);
    }

    public void setData(byte[] buffer, int length) {

        if (buffer == null) {
            Log.e(TAG, "package illegal");
            return;
        }
        Log.d(TAG, "<==========extractBuffer===========>");

        byte[] cmd = {buffer[0], buffer[1]};
        Log.d(TAG, "bill cmd:" + (char) buffer[0] + (char) buffer[1]);
        BaseData baseData = getBaseData(cmd);
        if (baseData != null) {
            baseData.setBackListener(this);
            try {
                baseData.setData(buffer, cmd);
            } catch (Exception e) {
                Log.e(TAG, "setData Exception：" + e.getMessage());
                //发生错误时要清屏
                Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
                intent.putExtra(AppAction.KEY_BROADCAST_CMD, Cmds.CMD_CS.getBytes());
                App.getInstance().sendBroadcast(intent);
            }
        }
    }

    private BaseData getBaseData(byte[] cmd) {
        BaseData baseData = null;
        if (Arrays.equals(CMD.JH, cmd)) {
            baseData = InteractiveMsgData.getInstance();
        } /*else if (Arrays.equals(CMD.DF, cmd) || Arrays.equals(CMD.JF, cmd)) {
            baseData = InteractiveFileData.getInstance();
		}*/ else if (Arrays.equals(CMD.QS, cmd)) {
            baseData = QuestionData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_GK.getBytes(), cmd)) {  //get device info,3.4.1
            baseData = GenerateKeyPw.getInstance();
        } else if (Arrays.equals(Cmds.CMD_MK.getBytes(), cmd)) {  //get device info,3.4.2
            baseData = SetPwMkey.getInstance();
        } else if (Arrays.equals(Cmds.CMD_WK.getBytes(), cmd)) {  //get device info,3.4.3
            baseData = SetPwWkey.getInstance();
        } else if (Arrays.equals(Cmds.CMD_CK.getBytes(), cmd)) {  //get device info,3.4.4
            baseData = GetPwCrc.getInstance();
        } else if (Arrays.equals(Cmds.CMD_IP.getBytes(), cmd)) {  //get device info,3.4.5
            //baseData = InputPw.getInstance();
            baseData = SDCSResetPinData.getInstance();
        } else if (Arrays.equals(CMD.PJ, cmd) || Arrays.equals(CMD.PH, cmd)) {
            baseData = AppraiseCommonData.getinstance();
        } else if (Arrays.equals(Cmds.CMD_IN.getBytes(), cmd)) {
            //baseData = InputCharacterData.getInstance();
            baseData = SDCSInputCharacterData.getInstance();
        } else if (Arrays.equals(CMD.SG, cmd)) {
            //baseData = SignatureData.getInstance();
            baseData = ICBCSignData.getInstance();
        } else if (Arrays.equals(CMD.AF, cmd)) {
            baseData = AddFileData.getInstance();
        } /*else if (Arrays.equals(CMD.RR, cmd) || Arrays.equals(CMD.SR, cmd)
				|| Arrays.equals(CMD.SF, cmd)) {
			baseData = ResposeSignatureData.getInstance();
		}*/ else if (Arrays.equals(Cmds.CMD_GI.getBytes(), cmd)) {
            baseData = ICBCDeviceInfData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_PV.getBytes(), cmd)) {
            baseData = SDCSPlayVoiceData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_RG.getBytes(), cmd) || Arrays.equals(Cmds.BACK_RR.getBytes(), cmd)) {
            baseData = ResposeICBCSignatureData.getInstance();
        } else if (Arrays.equals(CMD.UV, cmd)) {
            baseData = UpdateApkData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_QF.getBytes(), cmd)) {
            baseData = QueryFileData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_DF.getBytes(), cmd)) {
            baseData = DeleteFileData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_SO.getBytes(), cmd) || Arrays.equals(Cmds.CMD_QO.getBytes(), cmd)) {
            baseData = SettingsData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_DH.getBytes(), cmd) || Arrays.equals(Cmds.CMD_HH.getBytes(), cmd) ||
                Arrays.equals(Cmds.CMD_DT.getBytes(), cmd) || Arrays.equals(Cmds.CMD_MM.getBytes(), cmd)) {
            baseData = AddWebFileData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_RS.getBytes(), cmd)) {
            baseData = DevRebootData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_GS.getBytes(), cmd)) {
            //baseData = DeviceInfData.getInstance();
            baseData = ICBCDeviceStatusData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_ZJ.getBytes(), cmd)) {
            baseData = OptionPlayData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_SA.getBytes(), cmd))//SetStatusAreaStyleData
        {
            baseData = SetStatusAreaStyleData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_PT.getBytes(), cmd))//SetPosterTypeData
        {
            baseData = SetPosterTypeData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_SI.getBytes(), cmd)) {
            //baseData = ICBCAddWebFileData.getInstance();
            baseData = StartInfoHtmlData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_RP.getBytes(), cmd)) {
            baseData = SDCSReadPinData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_CP.getBytes(), cmd)) {
            baseData = SDCSConfirmPDF.getInstance();
        } else if (Arrays.equals(Cmds.CMD_UM.getBytes(), cmd)) {
            baseData = SDCSUpdateMKeyData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_UW.getBytes(), cmd)) {
            baseData = SDCSDownLoadWKeyData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_AW.getBytes(), cmd)) {
            baseData = SDCSActiveWKeyData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_OQ.getBytes(), cmd)) {
            baseData = OperateQuestionData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_OR.getBytes(), cmd)) {
            baseData = GetUserOperateRstHtmlData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_QI.getBytes(), cmd)) {
            baseData = ActiveSignQryInfoData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_SC.getBytes(), cmd)) {
            baseData = SDCSStartElectronicCardData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_SE.getBytes(), cmd)) {
            baseData = SDCSStartEvaluateData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_CS.getBytes(), cmd)) {
            baseData = ClearScreentData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_SS.getBytes(), cmd)) {
            baseData = ShowStatusAreaData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_RD.getBytes(), cmd)) {
            baseData = ReadDeviceIdData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_ES.getBytes(), cmd)) {
            baseData = GetSignPDFStateData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_RV.getBytes(), cmd)) {
            baseData = ICBCReadVoiceData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_SV.getBytes(), cmd)) {
            baseData = ICBCSetVolumeData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_KA.getBytes(), cmd)) {
            baseData = SecretKeyData.getInstance();
        } else if (Arrays.equals(Cmds.CMD_ID.getBytes(), cmd)) {
            baseData = DeviceIdData.getInstance();
        }else if(Arrays.equals(Cmds.CMD_EF.getBytes(),cmd))
        {
            baseData= ExistFileData.getInstance();
        }
        return baseData;
    }

}
