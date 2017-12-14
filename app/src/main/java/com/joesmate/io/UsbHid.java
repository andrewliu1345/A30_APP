package com.joesmate.io;

import android.util.Log;

import com.joesmate.listener.OnIoListener;
import cn.inhuasoft.gwq.BaseUsbHid;

public class UsbHid extends BaseIO {
	private static final String TAG = "UsbHid";
	public static final int HID_PACKAGE = 1024;
	private static int fid;
	private int arrayIndex;
	private int writeLen;

	public UsbHid(String deviceName, int deviceBaud, OnIoListener onIoListener) {
		super(onIoListener);
		new BaseUsbHid();
		fid = BaseUsbHid.open(deviceName);
		Log.i(TAG, "fid ==> " + fid);

		if (fid > 0) {
			isRunWrite = true;
			isRunRead = true;
			new Thread(runnableRead).start();
			//new Thread(runnableWrite).start();
		}
	}
	public static void closeHid(){
		isRunWrite = false;
		isRunRead = false;
		BaseUsbHid.close(fid);
	}

	public void write(byte[] buf, int len) {
		/*writeBuf = new byte[len];
		System.arraycopy(buf, 0, writeBuf, 0, len);
		writeLen = len;
		arrayIndex = 0;
		Log.d(TAG, "writeLen:"+writeLen);*/

		if((buf == null) || (len <= 0))
			return;

		int index, length;
		byte[] writebuf = new byte[HID_PACKAGE];
		for(index = 0; index < len; index += HID_PACKAGE )
		{
			length = ((index + HID_PACKAGE) < len) ? HID_PACKAGE : (len - index);
			System.arraycopy(buf, index, writebuf, 0, length);
			BaseUsbHid.write(fid, writebuf, HID_PACKAGE);
		}
	}

	private Runnable runnableWrite = new Runnable() {

		@Override
		public void run() {

			while (isRunWrite) {
				try {
					while (arrayIndex < writeLen) {
						byte[] dst = new byte[HID_PACKAGE];
						int length = writeLen - arrayIndex;
						length = length >= HID_PACKAGE ? HID_PACKAGE : length;
						System.arraycopy(writeBuf, arrayIndex, dst, 0, length);
						int writeLen = BaseUsbHid.write(fid, dst, dst.length);
						Log.d(TAG, "write length：" + writeLen);
						arrayIndex += HID_PACKAGE;
						Thread.sleep(10);
					}
				}
				catch (Exception e) {

				}
			}
		}
	};
	private Runnable runnableRead = new Runnable() {

		@Override
		public void run() {
			while (isRunRead) {
				int length = BaseUsbHid.read(fid, readBuf, readBuf.length);
				if (length != -1) {
					if (onIoListener != null) {
						onIoListener.readBuffer(readBuf, length);
					}
				}

			}
		}
	};

}

