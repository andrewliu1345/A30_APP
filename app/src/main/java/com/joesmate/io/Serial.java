package com.joesmate.io;

import android.content.Intent;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.bin.sdcs.SDCSReadPinData;
import com.joesmate.listener.OnIoListener;
import cn.inhuasoft.gwq.BaseSerial;

public class Serial extends BaseIO{
	private static final String TAG = "Serial";
	private static final long SLEEP_TIME = 1;   //500;
	private static int fid;
	private static BaseSerial baseSerial;

	public Serial(String deviceName, int deviceBaud,OnIoListener onIoListener) {
		super(onIoListener);
		baseSerial = new BaseSerial();
		fid = baseSerial.open(deviceName, deviceBaud);
		//fid = 0 ;
		Log.i(TAG, "fid ==> " + fid);

		if (fid > 0) {
			isRunWrite = true;
			isRunRead = true;
			new Thread(runnableRead).start();
			//new Thread(runnableWrite).start();
		}
	}
	public static void close(){
		isRunWrite = false;
		isRunRead = false;
		baseSerial.close(fid);
	}

	public void write(final byte[] buf, final int len) {
		//writeBuf = new byte[len];
		//System.arraycopy(buf, 0, writeBuf, 0, len);
		//isWrite = true;
		baseSerial.write(fid, buf, len);
	}

	private Runnable runnableWrite = new Runnable() {

		@Override
		public void run() {

			while (isRunWrite) {
				if (isWrite) {
					baseSerial.write(fid, writeBuf, writeBuf.length);
					isWrite = false;
				}
			}
		}
	};
	private Runnable runnableRead = new Runnable() {

		@Override
		public void run() {
			int index;
			byte[] data = new byte[2];
			while (isRunRead) {
				int length = baseSerial.read(fid, readBuf, readBuf.length, 0);

				for(index = 0; index < length; index++)
				{
					if(readBuf[index] == (byte) 0x82)
					{
						SDCSReadPinData.getInstance().inputChar();
						App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_SERIAL_INPUT));
					}
					else if(readBuf[index] == (byte) 0x81)
					{
						SDCSReadPinData.getInstance().inputCharAgain();
						App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_SERIAL_INPUT_AGAIN));
					}
					else if(readBuf[index] == (byte)0x83)
					{
						SDCSReadPinData.getInstance().closeInputChar();
						App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_SERIAL_CANCEL_INPUT));
					}
					else if (onIoListener != null) {
						data[0] = readBuf[index];
						onIoListener.readBuffer(data, 1);
					}
				}

				/*try {
					Thread.sleep(SLEEP_TIME);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
			}
		}
	};

}


