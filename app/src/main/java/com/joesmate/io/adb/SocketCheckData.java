package com.joesmate.io.adb;

import android.util.Log;

import com.joesmate.AssitTool;

/**
 * 数据包校验
 * <p>
 * 数据包结构 [STX,1byte][DATA_LEN,4byte][DATA,>=2byte][LRC,2byte][ETX,1byte]
 * <P>
 * 
 * 传输中，数据包中除了数据包头（STX）和数据包尾（ETX），其它部分都要进行字节拆分，每个字节拆分高4位、低4位两部分，分别加上0x30，形成两个字节
 * 
 * @author yc.zhang
 * 
 */
public class SocketCheckData {

	public static final String TAG = "CheckData";
	public static final int SPLIT = 0x30;
	public static final int STX = 0x02;
	public static final int ETX = 0x03;
	
	public static final int STX_LENGHT = 1; // stx数据最小长度
	public static final int LRC_LENGHT = 2; // lrc数据最小长度
	public static final int ETX_LENGHT = 1; // etx数据最小长度
	public static final int DATA_LEN_LENGHT = 4;//单元数据长度2个字节表示（拆发后4个字节）
	public static final int MIN_DATA_LENGHT = 4; // 数据最小长度拆分后的4个字节
	public static final int MIN_PACKAGE_LENGHT = 10; // 数据包最小长度

	public static final int DATA_LEN_POST = 1; // buffer中数据长度的起始位置
	
	public static final int DATA_POST = 5;
	public static final int DEF_TIMEOUT = 30;//默认超时时间30秒

	public static String getCmd(byte[] buffer){
		
		byte[] cmdBuf = new byte[2];
		System.arraycopy(buffer, 0, cmdBuf, 0, cmdBuf.length);
		return AssitTool.arrayToString(cmdBuf, AssitTool.UTF_8);
	}
	public static int getTimeout(int timeOut){
		if(timeOut < 0 || timeOut >= 100){
			return DEF_TIMEOUT;
		}
		return timeOut;
	}
	public static byte[] getDataPackage(byte[] buffer) {
		if (buffer == null || buffer.length < MIN_PACKAGE_LENGHT) {
			Log.e(TAG, "data is null or data length < minLenght");
			return null;
		}
		
		if (!isExistSTX(buffer)) {
			return null;
		}
		
		if (!isExistETX(buffer)) {
			return null;
		}
		
		int dataLenght = getDataLenght(buffer);
		Log.d(TAG, "dataLenght:"+dataLenght);
		if(dataLenght < MIN_DATA_LENGHT){
			Log.e(TAG, "dataLenght < min_data_lenght");
			return null;
		}
		
		if(!checkDataLenght(buffer, dataLenght)){
			Log.e(TAG, "dataLenght erro!");
			return null;
		}
		//TODO LRC校验
		
		return getData(buffer, dataLenght);
	}
	/**
	 * 拆分数据
	 * @param buffer
	 * @return
	 */
	public static  byte[] splitBuffer(byte[] buffer){
		if(buffer == null || buffer.length <= 0){
			return null;
		}
		byte[] splitBuf = new byte[buffer.length * 2];
		for(int i = 0 ; i < buffer.length ; i ++){
			
			byte hight = (byte) ((buffer[i] & 0xf0) >> 4);
			byte low = (byte) (buffer[i] & 0x0f);
			
			splitBuf[i * 2] = (byte) (hight + SPLIT);
			splitBuf[(i * 2)+1] = (byte) (low + SPLIT);
		}
		return splitBuf;
	}
	/**
	 * 合并拆分数据
	 * @param buffer
	 * @return
	 */
	public static  byte[] merge(byte[] buffer){
		if(buffer == null || buffer.length <= 0){
			return null;
		}
		
		byte[] mergeBuf = new byte[buffer.length/2];
		for(int i = 0 ; i < mergeBuf.length; i++){
			byte hight = (byte) (buffer[i * 2] - SPLIT);
			byte low = (byte) (buffer[(i * 2)+1] - SPLIT);
			//高低位合并
			mergeBuf[i] = (byte) (((hight << 4) & 0xf0) | low & 0x0f);
		}
		return mergeBuf;
	}
	

	private  static boolean isExistSTX(byte[] buffer) {
		if (buffer[0] == STX) {
			return true;
		}
		Log.e(TAG, "STX not exist!");
		return false;
	}

	private static  boolean isExistETX(byte[] buffer) {
		if (buffer[buffer.length - 1] == ETX) {
			return true;
		}
		Log.e(TAG, "ETX not exist!");
		return false;
	}

	private  static boolean checkDataLenght(byte[] buffer,int dataLenght) {
		int bufferLenght = buffer.length;
		/*包的总长度减去 stx、包长、lrc、etx的长度就是数据单元的长度*/
		int checkDataLenght = bufferLenght - ETX_LENGHT -DATA_LEN_LENGHT -LRC_LENGHT -ETX_LENGHT;
		Log.d(TAG, "checkDataLenght："+checkDataLenght + "dataLenght:"+dataLenght);
		return (dataLenght == checkDataLenght);
	}
	private static  byte[] intToByArray(int value){
		byte[] buffer = new byte[2];
		buffer[0] = (byte) ((value & 0xff00) >> 8); //高位
		buffer[1] = (byte) (value & 0x00ff); //低位
		return buffer;
	}
	
	private  static byte getLRC(byte[] buffer){
		byte[] lrcBuf = new byte[LRC_LENGHT];
		System.arraycopy(buffer, buffer.length - ETX_LENGHT -LRC_LENGHT, lrcBuf, 0, LRC_LENGHT);
		byte[] mergeBuf = merge(lrcBuf);
		if(mergeBuf != null){
			Log.d(TAG, "get LRC："+mergeBuf[0]);
			return mergeBuf[0];
		}
		Log.e(TAG, "get LRC erro!");
		return 0;
	}
	
	private  static byte[] getData(byte[] buffer,int dataLenght){
		byte[] data = new byte[dataLenght];
		System.arraycopy(buffer, STX_LENGHT + DATA_LEN_LENGHT, data, 0, dataLenght);
		return merge(data);
	}

	/**
	 * <p>
	 * 获取数据长度
	 * <p>
	 * 2个字节高位在前，低位在后
	 */
	private  static int getDataLenght(byte[] buffer) {
		
		byte[] dataLen = new byte[DATA_LEN_LENGHT];
		System.arraycopy(buffer, DATA_LEN_POST, dataLen, 0, DATA_LEN_LENGHT);
		
		byte[] mergeDataLen = merge(dataLen);

		byte hight = (byte) ((mergeDataLen[0] << 8) & 0xff00);
		byte low = (byte) (mergeDataLen[1] & 0x00ff);

		return hight | low;
	}


}
