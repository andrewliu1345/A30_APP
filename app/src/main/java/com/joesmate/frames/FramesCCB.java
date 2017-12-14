package com.joesmate.frames;

import android.util.Log;

import com.joesmate.AssitTool;

/**
 * 数据包校验
 * <p>
 * 数据包结构 [STX,1byte][DATA_LEN,4byte][DATA,DATA_LEN byte][LRC,2byte][ETX,1byte]
 * <P>
 * 
 * 传输中，数据包中除了数据包头（STX）和数据包尾（ETX），其它部分都要进行字节拆分，每个字节拆分高4位、低4位两部分，分别加上0x30，形成两个字节
 * 
 * @author yc.zhang
 * 
 */
public class FramesCCB extends BaseFrames {

	public static final String TAG = "FramesCCB";
	public static final int SPLIT = 0x30;
	public static final int STX = 0x02;
	public static final int ETX = 0x03;
	public static final int PACKAGE_MIN_LENGHT = 8;

	public byte[] makePackage(byte[] message, int lenght) {
		if(message == null){
			Log.e(TAG, "makePackage buffer is null");
			return null;
		}
		byte lrc = 0;
		for (int i = 0; i < message.length; i++) {
			lrc ^= message[i];
		}
		Log.d(TAG, "make lrc：" + lrc);
		byte[] mergeData = new byte[2 + lenght + 1]; // 数据包长+数据包+lrc
		System.arraycopy(AssitTool.integerToArray(lenght), 0, mergeData, 0, 2);
		System.arraycopy(message, 0, mergeData, 2, message.length);
		mergeData[mergeData.length - 1] = lrc;
		byte[] splitData = AssitTool.splitBuffer(mergeData, SPLIT);

		byte[] packData = new byte[splitData.length + 2];
		packData[0] = STX;
		System.arraycopy(splitData, 0, packData, 1, splitData.length);
		packData[packData.length - 1] = ETX;
		return packData;
	}

	public byte[] saxPackage(byte[] buffer, int length) {
		if (buffer == null || length < PACKAGE_MIN_LENGHT) {
			Log.e(TAG, "buffer is null or buffer length < minLength");
			return null;
		}

		/* 合并数据单元数据-不包含包头与包尾 */
		byte[] needBuffer = new byte[length - 2]; // 去除包头包尾的数据
		System.arraycopy(buffer, 1, needBuffer, 0, needBuffer.length);
		byte[] mergeBuffer = AssitTool.merge(needBuffer, SPLIT);

		/* 数据长度 */
		int msgLen = AssitTool.arrayToInteger(new byte[] { mergeBuffer[0],
				mergeBuffer[1] });
		Log.d(TAG, "msgLen:" + msgLen);

		if (mergeBuffer.length < msgLen) {
			Log.d(TAG, "mergeBuffer.length:" + mergeBuffer.length);
			return null;
		}
		/* 数据 */
		byte[] msgData = new byte[msgLen];
		System.arraycopy(mergeBuffer, 2, msgData, 0, msgLen);

		/* LRC校验-拆分数据单元 */
		byte lrc = mergeBuffer[mergeBuffer.length - 1];
		Log.d(TAG, "lrc：" + lrc);
		byte testLrc = 0;
		for (int i = 0; i < msgData.length; i++) {
			testLrc ^= msgData[i];
		}
		Log.d(TAG, "testLrc" + testLrc);
		if (lrc != testLrc) {
			Log.e(TAG, "fail to test lrc");
			return null;
		}
		Log.i(TAG, "--<< saxPackage success! >>--");
		return msgData;
	}

	@Override
	public byte[] extract(byte[] readBuffer, int readLen) {

		int indexSTX = 0;
		int indexETX = 0;

		if (readBuffer == null) {
			//Log.d(TAG, "--<< readBuffer is null >>--");
			return null;
		}
		/*for(int i =0 ;i < readBuffer.length; i ++){
			//Log.d(TAG, "["+i+"]:"+readBuffer[i]);
		}*/

		//find STX
		if (!isFindStx) {
			indexSTX = findTag(readBuffer, FramesCCB.STX);
			if (indexSTX != -1) {
				clearBuffer();
				isFindStx = true;
				Log.d(TAG, "--<< find STX >>--");
			}
		}

		//find ETX
		if (isFindStx) {
			indexETX = findTag(readBuffer, FramesCCB.ETX);
			if (indexETX != -1) {
				int length = indexETX - indexSTX + 1;
				if(!addBuffer(readBuffer, indexSTX, length)){
					return null;
				}
				Log.d(TAG, "--<< find ETX >>--");
				return copyNeedBuffer(index);
			}
		}

		int length = readLen - indexSTX;
		addBuffer(readBuffer, indexSTX, length);

		return null;
	}

}

