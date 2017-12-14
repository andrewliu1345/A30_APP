package com.joesmate.io.adb;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;

import android.content.Intent;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.io.adb.SocketService.OnSocketListener;

public class SocketClient {

	public static final String TAG = "SocketClient";
	public static boolean isReadRun, isWriteRun, isBeginWrite;
	BufferedOutputStream bos = null;
	BufferedInputStream bis = null;
	Socket socket;
	OnSocketListener onSocketListener;
	int MAX_BUFFER_BYTES = 65535;
	byte[] readBuf = new byte[MAX_BUFFER_BYTES];
	byte[] writeBuf = new byte[MAX_BUFFER_BYTES];
	int writeLen;

	public SocketClient(Socket socket, OnSocketListener onSocketListener) {
		Log.d(TAG, "create SocketClient");
		this.socket = socket;
		this.onSocketListener = onSocketListener;

		if (openStream()) {
			new Thread(runnableRead).start();
			new Thread(runnableWrite).start();
		}

	}

	private void onDestroy() {
		releaseInStream(bis);
		releaseOutStream(bos);
		releaseSocket(socket);
	}

	private boolean openStream() {
		boolean isOpen = false;
		if (socket == null) {
			return isOpen;
		}
		if (!socket.isConnected()) {
			return isOpen;
		}

		try {
			bos = new BufferedOutputStream(socket.getOutputStream());
			bis = new BufferedInputStream(socket.getInputStream());
			isOpen = true;
		} catch (IOException e) {
			e.printStackTrace();
			isOpen = false;
			releaseOutStream(bos);
			releaseInStream(bis);
			releaseSocket(socket);
		}
		Log.d(TAG, "open Stream is：" + isOpen);
		return isOpen;
	}

	private void releaseSocket(Socket socket) {
		if (socket != null) {
			if (socket.isConnected()) {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			socket = null;
		}
	}

	private void releaseOutStream(BufferedOutputStream out) {
		if (out != null) {
			try {
				out.flush();
				out.close();
				out = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void releaseInStream(BufferedInputStream in) {
		if (in != null) {
			try {
				in.close();
				in = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void write(byte[] buffer) {
		if (buffer == null) {
			return;
		}
		Log.d(TAG, "write");
		synchronized (writeBuf) {
			writeLen =  buffer.length;
			System.arraycopy(buffer, 0, writeBuf, 0, writeLen);
			isBeginWrite = true;
		}
	}

	private Runnable runnableWrite = new Runnable() {

		@Override
		public void run() {
			isWriteRun = true;

			try {
				while (isWriteRun) {
					if (isBeginWrite) {
						if(bos != null)
						synchronized (writeBuf) {
							
							bos.write(writeBuf, 0, writeLen);
							bos.flush();
						}
						isBeginWrite = false;
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				releaseOutStream(bos);
				isWriteRun = false;
			}

		}
	};
	private Runnable runnableRead = new Runnable() {

		@Override
		public void run() {
			isReadRun = true;
			try {
				while (isReadRun) {
					if (bis != null) {

						int lenght = bis.read(readBuf);
						if (lenght > 0) {
							if (onSocketListener != null) {
								onSocketListener.readBuffer(readBuf, lenght);
							}
						}
					}
				}

			} catch (IOException e) {
				e.printStackTrace();
				releaseInStream(bis);
				isReadRun = false;
			}

		}
	};

	
	// 读取命令
	public String testInputMessage(byte[] buffer, int numReadedBytes) {
		String msg = "";
		try {
			msg = new String(buffer, 0, numReadedBytes, "utf-8");
		} catch (Exception e) {
			Log.v(TAG, Thread.currentThread().getName() + "---->"
					+ "readFromSocket error");
			e.printStackTrace();
		}
		Log.v(TAG, "msg=" + msg);
		Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
		intent.putExtra(AppAction.KEY_BROADCAST_CMD, msg);
		App.getInstance().sendBroadcast(intent);
		return msg;
	}

}
