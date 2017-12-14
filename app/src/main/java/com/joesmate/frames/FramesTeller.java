package com.joesmate.frames;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.CRC16M;

/**
 * 检测通讯帧,且返回信息包
 * 
 * @author zyc
 * 
 */
public class FramesTeller extends BaseFrames {

	public static final String TAG = "FramesTeller";

	public static final int PACKAGE_MIN_LENGHT = 18;
	public static final int STX = 0x02;
	public static final int ADDRESS = 0x30;
	public static final int ETX = 0x03;
	public static final int CRC_SIZE = 4;

	public byte[] makePackage(byte[] message, int lenght) {
		if (message == null) {
			Log.e(TAG, "makePackage buffer is null");
			return null;
		}
		int msgLength = lenght;
		byte[] data = new byte[14 + msgLength];
		data[0] = STX;

		data[1] = ADDRESS;
		data[2] = ADDRESS;
		data[3] = ADDRESS;
		data[4] = ADDRESS;
		data[5] = ADDRESS;
		data[6] = ADDRESS;
		data[7] = ADDRESS;
		data[8] = ADDRESS;

		data[9] = AssitTool.getAscall((msgLength / 1000) % 10);
		data[10] = AssitTool.getAscall((msgLength / 100) % 10);
		data[11] = AssitTool.getAscall((msgLength / 100) % 10);
		data[12] = AssitTool.getAscall(msgLength % 10);
		for (byte msg : message) {
			msg = AssitTool.getAscall(msg);
		}
		System.arraycopy(message, 0, data, 13, msgLength);
		data[12 + msgLength + 1] = ETX;

		byte[] crc = CRC16M.getInstance().getSendBuf(data);

		byte[] buffer = new byte[data.length + crc.length];
		System.arraycopy(data, 0, buffer, 0, data.length);
		System.arraycopy(crc, 0, buffer, data.length, crc.length);
		return buffer;
	}

	public byte[] saxPackage(byte[] buffer, int lenght) {
		if (buffer == null || lenght < PACKAGE_MIN_LENGHT) {
			Log.e(TAG, "buffer is null or buffer length < minLength");
			return null;
		}

		Log.d(TAG, "lenght :" + lenght);
		for (int i = 0; i < lenght; i++) {
			Log.d(TAG, "buffer:" + "[" + i + "]" + buffer[i]);
		}
		if (buffer[1] != ADDRESS || buffer[2] != ADDRESS
				|| buffer[3] != ADDRESS || buffer[4] != ADDRESS
				|| buffer[5] != ADDRESS || buffer[6] != ADDRESS
				|| buffer[7] != ADDRESS || buffer[8] != ADDRESS) {
			Log.d(TAG, "address illegal");
			return null;
		}

		byte[] crc16 = new byte[CRC_SIZE];
		System.arraycopy(buffer, lenght - CRC_SIZE, crc16, 0, CRC_SIZE);

		byte[] data = new byte[CRC_SIZE - CRC_SIZE];
		System.arraycopy(buffer, 0, data, 0, data.length);
		byte[] testCrc16 = CRC16M.getInstance().getSendBuf(data);

		if (testCrc16 == null || testCrc16.length != 4) {
			Log.w(TAG, "test CRC16 erro");
			return null;
		}
		for (int i = 0; i < CRC_SIZE; i++) {
			Log.d(TAG, "crc16" + i + "-" + crc16[i]);
			Log.d(TAG, "testCrc16-" + i + "-" + testCrc16[i]);
			if (crc16[i] != testCrc16[i]) {
				//return null;
			}
		}
		byte[] messageCount = new byte[4];
		System.arraycopy(buffer, 9, messageCount, 0, messageCount.length);
		int count = AssitTool.getArrayCount(messageCount);
		Log.d(TAG, "message count:" + count);
		if (count < 0) {
			Log.w(TAG, "message count < 0");
			return null;
		}
		byte[] messsage = new byte[count];
		try {
			System.arraycopy(buffer, 13, messsage, 0, messsage.length);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return messsage;
	}

	@Override
	public byte[] extract(byte[] readBuffer, int readLen) {
		for (int i = 0; i < readLen; i++) {
			Log.d(TAG, "readBuffer:" + "[" + i + "]:" + readBuffer[i]);
		}
		int indexSTX = 0;
		int indexETX = 0;

		if (readBuffer == null) {
			Log.d(TAG, "--<< readBuffer is null >>--");
			return null;
		}

		// find STX
		if (!isFindStx) {
			indexSTX = findTag(readBuffer, FramesTeller.STX);
			if (indexSTX != -1) {
				clearBuffer();
				isFindStx = true;
				Log.d(TAG, "--<< find STX >>--");
			}
		}

		// find ETX
		if (isFindStx && !isFindEtx) {
			indexETX = findTag(readBuffer, FramesTeller.ETX);
			if (indexETX != -1) {
				isFindEtx = true;
				Log.d(TAG, "--<< find ETX >>--");
			}
		}

		// find CRC
		if (isFindStx && isFindEtx) {
			if (indexETX + CRC_SIZE < readLen) {
				int packLen = 0;
				int baseIndexETX = findTag(baseBuffer, FramesCCB.ETX);

				if (baseIndexETX != -1) {
					packLen = baseIndexETX + 1 + CRC_SIZE;
				} else {
					int length = indexETX + 1 + CRC_SIZE;
					if (!addBuffer(readBuffer, indexSTX, length)) {
						return null;
					}
					packLen = index;
				}
				Log.d(TAG, "--<< find CRC >>--");
				return copyNeedBuffer(packLen);
			}

		}

		int length = readLen - indexSTX;
		addBuffer(readBuffer, indexSTX, length);

		return null;
	}
}
