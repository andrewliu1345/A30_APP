package com.joesmate;

/**
 * 参数值	字节表示	提示语音信息
 *<P>0	00H	请阅读屏幕信息
 *<P>1	01H	请输入密码
 *<P>2	02H	请再次输入密码
 *<P>3	03H	请确认交易信息，正确请按确认键，如需修改请按取消键
 *<P>4	04H	请阅读调查问卷说明，若同意请按确认键开始答题
 *<P>5	05H	请选择
 *<P>6	06H	请选择，并按确认键提交
 *<P>7	07H	请选择是否打印交易回单，需要回单请按确认键，不需要请按取消键
 *<P>8	08H	请确认屏幕显示信息，同意请按确认键。
 *<P>9	09H	请确认屏幕显示信息，正确请按确认键。
 *<P>10	0AH	请对本次服务进行评价
 *<P>11	0BH	请输入验证码
 *<P>12	0CH	请输入金额
 *<P>13	0DH	请输入账号
 *<P>14	0EH	请输入
 *<P>15	0FH	请核对凭证信息并签名
 *<P>255	FFH	无声
 * @author yc.zhang
 *
 */
public class AudioType {

	public static final String TYPE_00 = "00";
	public static final String TYPE_01 = "00";
	public static final String TYPE_02 = "00";
	public static final String TYPE_03 = "00";
	public static final String TYPE_04 = "00";
	public static final String TYPE_05 = "00";
	public static final String TYPE_06 = "00";
	public static final String TYPE_07 = "00";
	public static final String TYPE_08 = "08";
	public static final String TYPE_09 = "00";
	public static final String TYPE_10 = "00";
	public static final String TYPE_11 = "00";
	public static final String TYPE_12 = "00";
	public static final String TYPE_13 = "00";
	public static final String TYPE_14 = "00";
	public static final String TYPE_15 = "00";
	public static final String TYPE_255 = "255";
	
	public static final int H_00 = 0x00;
	public static final int H_08 = 0x08;
	public static final int H_FF = 0xFF;
	public static final int H_05 = 0x05;
	public static final int H_06 = 0x06;
	

}
