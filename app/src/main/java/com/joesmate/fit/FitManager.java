package com.joesmate.fit;

import android.util.Log;

import com.joesmate.frames.BaseFrames;
import com.joesmate.frames.FramesCCB;
import com.joesmate.frames.FramesTeller;
import com.joesmate.io.BaseIO;
import com.joesmate.io.Serial;
import com.joesmate.io.UsbHid;
import com.joesmate.listener.OnIoListener;

public class FitManager implements OnIoListener {
	public static final String TAG = "FitManager";
	private static final String DEVICE_NAME_SERIAL = "/dev/ttyS3";
	private static final String DEVICE_NAME_HID = "/dev/hidg0";
	private static final int DEVICE_BAUD = 9600;
	private BaseFrames baseFrames;
	public BaseIO baseIO;
	private BaseFitBin baseFitBin;

	public static enum IoType {
		SERIAL, HID
	};

	public static enum FrameType {
		TELLER, CCB
	}

	IoType ioType;

	public FitManager(IoType ioType, FrameType frameType) {
		this.ioType = ioType;
		if (ioType == IoType.SERIAL) {
			baseIO = new Serial(DEVICE_NAME_SERIAL, DEVICE_BAUD, this);
		} else if (ioType == IoType.HID) {
			baseIO = new UsbHid(DEVICE_NAME_HID, 194000, this);
		}

		if (frameType == FrameType.TELLER) {
			baseFrames = new FramesTeller();
			baseFitBin = new FitBinTeller(this);
		} else if (frameType == FrameType.CCB) {
			baseFrames = new FramesCCB();
			baseFitBin = new FitBinCCB(this);
		}

	}

	public BaseFrames getBaseFrames() {
		return baseFrames;
	}

	public BaseIO getBaseIO() {
		return baseIO;
	}

	public BaseFitBin getBaseFitBin() {
		return baseFitBin;
	}

	@Override
	public void readBuffer(byte[] buffer, int length) {
		//if (length > 0) {
		    try {
				byte[] extractBuffer = baseFrames.extract(buffer, length);
				if (extractBuffer != null) {
					byte[] msgData = baseFrames.saxPackage(extractBuffer,
							extractBuffer.length);
					if (msgData != null) {
						baseFitBin.setData(msgData, msgData.length);
					}
				} else {
					Log.d(TAG, "extractBuffer is null");
				}
			}
			catch (Exception e)
			{
				Log.d(TAG,e.toString());
			}
		//}
	}
	@Override
	public void writeBuffer(byte[] buffer, int length) {
		Log.i(TAG, "=========<writeBuffer>=========");
		byte[] makePackage = baseFrames.makePackage(buffer, length);
		baseIO.write(makePackage, makePackage.length);
		for (int i = 0; i < makePackage.length; i++) {
			//Log.d(TAG, "makePackage[" + i + "]:" + makePackage[i]);
		}
	}

	public void writeSomeBuffer(byte[] buffer, int length) {
		baseIO.write(buffer, length);
	}

/*	@Override
	public void writeBuffer(byte[] buffer, int length) {
		Log.i(TAG, "=========<writeBuffer>=========");
		byte[] makePackage = baseFrames.makePackage(buffer, length);
		for (int i = 0; i < makePackage.length; i++) {
			Log.d(TAG, "makePackage[" + i + "]:" + makePackage[i]);
		}

		if (ioType == IoType.HID) {
			float fLen = makePackage.length;
			int totalNum = (int) Math.ceil(fLen / BaseIO.HID_MAX);
			Log.w(TAG, "64-totalNum：" + totalNum);

			for (int i = 0; i < totalNum; i++) {
				byte[] arraySplit = new byte[(int) BaseIO.HID_MAX];
				System.arraycopy(makePackage, i * (int) BaseIO.HID_MAX,
						arraySplit, 0,
						fLen >= (int) BaseIO.HID_MAX ? (int) BaseIO.HID_MAX
								: (int) fLen);
				baseIO.write(arraySplit, arraySplit.length);
				fLen -= BaseIO.HID_MAX;
				for(int j = 0 ; j < arraySplit.length; j++)
				Log.w(TAG, "arraySplit[" + i+"_"+j+"]:"+arraySplit[j]);
			}

		
			
		} else if(ioType == IoType.SERIAL){
			baseIO.write(makePackage, makePackage.length);
		}
	}*/

}
