package com.joesmate;

/**
 * 序号	指令名称	指令代码	备注
 * <P>1	非加密方式输入密码	PW	见1.1节
 * <P>2	加密方式输入密码	XS、JM	见1.2节
 * <P>3	交易显示确认	JH	见1.3节
 * <P>4	交易显示确认-文件	JF	见1.4节
 * <P>5	问卷调查	QS	见1.5节
 * <P>6	客户评价	PJ	见1.6节
 * <P>7	输入数字	IN	见1.7节
 * <P>8	获取版本号（GV）	GV	见1.8节
 * <P>9	获取设备状态（GS）	GS	见1.9节
 * <P>10	密钥灌注（签名设备）（KA）	KA	见1.10节
 * <P>11	密钥灌注（密码键盘）（KP）	KP	见1.11节
 * <P>12	软件版本升级（UV）	UV	见1.12节
 * <P>13	文件新增（AF）	AF	见1.13节
 * <P>14	文件删除（DF）	DF	见1.14节
 * <P>15	文件查询（QF）	QF	见1.15节
 * <P>16	参数设置（SO）	SO	见1.16节
 * <P>17	参数查询（QO）	QO	见1.17节
 * <P>18	设备重启（RS）	RS	见1.18节
 * <P>19	电子签名	SG	见1.19节
 *
 * @author yc.zhang
 */
public class Cmds {
    //建行==========================================================//命令
    public static final String CMD_JH = "JH";
    public static final String CMD_JF = "JF";
    public static final String CMD_QS = "QS";
    public static final String CMD_PJ = "PJ";
    public static final String CMD_IN = "IN";
    public static final String CMD_GV = "GV";
    //public static final String CMD_UV = "UV";
    //public static final String CMD_AF = "AF";
    //public static final String CMD_DF = "DF";
    public static final String CMD_FF = "FF";
    //public static final String CMD_QF = "QF";
    public static final String CMD_SO = "SO";
    public static final String CMD_QO = "QO";
    //public static final String CMD_RS = "RS";
    //public static final String CMD_SG = "SG";
    public static final String CMD_DB = "DB";
    //public static final String CMD_GS = "GS";
    public static final String CMD_SF = "SF"; //获取最后一次签名命令
    public static final String CMD_SR = "SR"; //返回电子签名命令
    public static final String CMD_DH = "DH";
    public static final String CMD_HH = "HH";
    public static final String CMD_DT = "DT";
    public static final String CMD_MM = "MM";
    public static final String CMD_ZJ_HID = "ZH";
    public static final String CMD_KA = "KA";


    //ICBC CMD

    //SetStatusAreaStyleData
    public static final String CMD_SA = "SA";
    //SetPosterType
    public static final String CMD_PT = "PT";
    //SimpleShowInfo
    //public  static  final String CMD_SI = "SI" ;
    //SignPdf
    public static final String CMD_SG = "SG";
    //Send Sign Image
    public static final String CMD_RG = "RG";
    //取消
    public static final String CMD_CL = "CL";
    // back send a one frame image
    public static final String BACK_RR = "RR";
    //GetDeviceInfo
    public static final String CMD_GI = "GI";
    //GetWorkingStatus
    public static final String CMD_GS = "GS";
    //SaveFile
    public static final String CMD_AF = "AF";
    //SetupApp
    public static final String CMD_UV = "UV";
    //QueryFile
    public static final String CMD_QF = "QF";
    //DeleteFile
    public static final String CMD_DF = "DF";
    //ClearMainScreen
    public static final String CMD_CS = "CS";
    //ShowStatusArea
    public static final String CMD_SS = "SS";
    //ReadVoice
    public static final String CMD_RV = "RV";
    //Reboot System
    public static final String CMD_RS = "RS";
    //SetVolume
    public static final String CMD_SV = "SV";
    //FindFile
    public static final String CMD_EF = "EF";


    // password
    public static final String CMD_WM = "WM";  //3.1.6
    public static final String CMD_DL = "DL";  //3.1.7
    public static final String CMD_GK = "GK";  //3.4.1
    public static final String CMD_MK = "MK";  //3.4.2
    public static final String CMD_WK = "WK";  //3.4.3
    public static final String CMD_CK = "CK";  //3.4.4
    public static final String CMD_IK = "IK";  //3.4.5


    public static final String BACK_GS = "GA"; //3.1.2
    public static final String BACK_WM = "WI";  //3.1.6
    public static final String BACK_DL = "LI";  //3.1.7
    public static final String BACK_OK = "OK";  //3.1.7
    public static final String BACK_ER = "ER";  //3.1.7


    //建行==========================================================//信息回复
    public static final String BACK_JI = "JI";
    public static final String BACK_IN = "IR";
    public static final String BACK_JF = "FI";
    public static final String BACK_DB = "BR";
    public static final String BACK_AF = "IA";
    public static final String BACK_SF = "RT";
    //public static final String BACK_RR = "RR";
    public static final String BACK_QF = "QR";
    public static final String BACK_DF = "DI";
    public static final String BACK_SO = "HB";
    public static final String BACK_QO = "OW";
    public static final String BACK_DR = "DR";
    public static final String BACK_UV = "VI";
    public static final String BACK_DH = "HR";
    public static final String BACK_HH = "TT";
    public static final String BACK_DT = "HT";
    public static final String BACK_MM = "LL";
    //public static final String BACK_RS = "SV";
    //public static final String BACK_GS = "IS";
    public static final String BACK_PJ = "PK";
    public static final String BACK_KA = "AI";

    //M3==========================================================//命令
    public static final String CMD_DD = "DD";
    public static final String CMD_CD = "CD";
    public static final String CMD_XS = "XS";
    public static final String CMD_RC = "RC";
    public static final String CMD_WC = "WC";
    public static final String CMD_GJ = "GJ";
    public static final String CMD_ZJ = "ZJ";


    //M3==========================================================//信息回复
    public static final String BACK_DD = "DE";
    public static final String BACK_CD = "CE";
    public static final String BACK_XS = "XT";
    public static final String BACK_RC = "RD";
    public static final String BACK_WC = "WD";
    public static final String BACK_GJ = "GK";
    public static final String BACK_ZJ = "ZK";


    //SDCSTLC ====================================================
    //SCCBA_StartInfoHtml
    public static final String CMD_SI = "SI";
    //SCCBA_PlayVoice
    public static final String CMD_PV = "PV";
    //SCCBA_StartElectronicCard 
    public static final String CMD_SC = "SC";
    //SCCBA_StartEvaluate
    public static final String CMD_SE = "SE";
    //SCCBA_ReadPin
    public static final String CMD_RP = "RP";
    //SCCBA_OperateQuestion
    public static final String CMD_OQ = "OQ";
    //SCCBA_GetUserOperateRstHtml
    public static final String CMD_OR = "OR";
    //SCCBA_ActiveSignQryInfo
    public static final String CMD_QI = "QI";
    //SCCBA_getSignPDFState
    public static final String CMD_ES = "ES";
    //SCCBA_ InitPinPad
    public static final String CMD_IP = "IP";
    // SCCBA_UpdateMKey
    public static final String CMD_UM = "UM";
    // SCCBA_DownLoadWKey
    public static final String CMD_UW = "UW";
    // SCCBA_ActiveWKey
    public static final String CMD_AW = "AW";
    //SCCBA_confirmPDF
    public static final String CMD_CP = "CP";
    //SCCBA_ReadDeviceId
    public static final String CMD_RD = "RD";

    public static final String CMD_CY = "CY";
    public static final String CMD_ID = "ID";

    public static final String CMD_ST = "ST";

    public static final String LOAD_MASTER_KEY="LOAD_MASTER_KEY";

    public static final String MASTER_KEY="MASTER_KEY";

    public static final String WORK_KEY="WORK_KEY";


}
