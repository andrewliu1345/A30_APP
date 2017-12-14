package com.joesmate.upgrade;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.joesmate.App;

public class Tool {

	public static final String TAG = "Tool";

	// 安装apk
	public static void installApk(String path) {
		if(path == null){
			return;
		}
		File file = new File(path);
		if(file == null || !file.exists()){
			return;
		}
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(file),
				"application/vnd.android.package-archive");
		App.getInstance().startActivity(intent);
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	public static void rmDir(String dir) {
		try {
			execRootCommand("rm -r " + dir);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static Process execRootCommand(String command) throws IOException {
		if (command == null) {
			Log.w(TAG, "command is null");
			return null;
		}

		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec("su");

		OutputStream os = proc.getOutputStream();
		String cmd = command + "\n";
		os.write(cmd.getBytes());
		os.flush();

		return proc;
	}

	public static boolean isExistsFile(String path, String fileName) {
		if (path == null) {
			return false;
		}
		File dir = new File(path);
		if (dir == null || !dir.exists()) {
			return false;
		}
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) {
			return false;
		}
		for (File file : files) {
			if (file == null || !file.exists()) {
				continue;
			}

			if (file.getName().equals(fileName)) {
				Log.d(TAG, "find file:" + file.getAbsolutePath());
				return true;
			}

		}
		return false;
	}
}
