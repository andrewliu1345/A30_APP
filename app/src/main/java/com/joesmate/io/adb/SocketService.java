package com.joesmate.io.adb;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class SocketService implements Runnable {
	public static final String TAG = "SocketService";
	public static boolean isServerSocketRun;
	ServerSocket serverSocket;
	Socket client;
	OnSocketListener socketListener;
	SocketClient socketClient;

	public SocketService(int port, OnSocketListener socketListener) {
		Log.d(TAG, "onCreate =================================");
		this.socketListener = socketListener;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			if (socketListener != null) {
				socketListener.iOException();
			}

			serverSocket = null;
			e.printStackTrace();
			Log.e(TAG, TAG + " create IOException");
		}
		if (serverSocket != null) {
			new Thread(this).start();
		}else{
			Log.e(TAG, TAG + " serverSocket is null");
		}
	}
	
	public void write(byte[] buffer){
		if(socketClient != null){
			Log.d(TAG, "write");
			socketClient.write(buffer);
		}
	}

	@Override
	public void run() {
		isServerSocketRun = true;
		while (isServerSocketRun) {
			try {
				client = serverSocket.accept();
			} catch (IOException e) {

				if (socketListener != null) {
					socketListener.iOException();
				}
				client = null;
				isServerSocketRun = false;
				e.printStackTrace();
				Log.e(TAG, TAG + " accept IOException");
			}
			if (client != null && client.isConnected()) {
				socketClient = new SocketClient(client,socketListener);
			}
		}
	}

	public static interface OnSocketListener {
		public void iOException();
		public void readBuffer(byte[] buffer,int lenght);
	}

}
