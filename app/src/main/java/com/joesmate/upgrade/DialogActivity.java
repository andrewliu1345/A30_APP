package com.joesmate.upgrade;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.joesmate.AppAction;
import com.joesmate.FileInf;
import com.joesmate.R;
import com.joesmate.R.id;

public class DialogActivity extends Activity {

	public static final String TAG = "DialogActivity";
	ProgressBar progressBar;
	TextView tvInf;
	int total;
	String srcPath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		srcPath = getIntent().getStringExtra(AppAction.KEY_SRC);
		setContentView(R.layout.dialog_caopy);

		progressBar = (ProgressBar) findViewById(id.upgrade_progress);
		tvInf = (TextView) findViewById(id.upgrade_inf);

		new ProgressBarAsyncTask().execute(new String[] {});

		IntentFilter filter = new IntentFilter();
		filter.addAction(AppAction.ACTION_CLOSE_DIALOG);
		registerReceiver(broadcastReceiver, filter);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			tvInf.setText("(" + msg.what + "/" + total + ")");
		}

	};
	BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			DialogActivity.this.finish();
		}
	};

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}

	class ProgressBarAsyncTask extends AsyncTask<String[], Integer, Integer> {

		FileOutputStream fos = null;
		FileInputStream fis = null;
		File[] dstFiles;
		File srcDir;

		public ProgressBarAsyncTask() {

			File dstDir = new File(srcPath + "/" + "res");
			dstFiles = dstDir.listFiles();
			total = dstFiles.length;
			tvInf.setText("(" + 0 + "/" + total + ")");
			progressBar.setMax(total);
		}

		@Override
		protected Integer doInBackground(String[]... params) {
			clearSrcFile();
			for (int i = 0; i < dstFiles.length; i++) {
				File dstFile = dstFiles[i];
				if (dstFile == null || !dstFile.exists()) {
					continue;
				}
				try {
					fis = new FileInputStream(dstFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				File srcFile = new File(srcDir, dstFile.getName());
				try {
					fos = new FileOutputStream(srcFile);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				byte[] buffer = new byte[1024];
				int length = 0;
				try {
					while ((length = fis.read(buffer)) != -1) {
						fos.write(buffer, 0, length);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				onProgressUpdate(i + 1);
			}

			return 0;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Integer result) {
			Tool.installApk(srcPath + "/" + FileInf.APK_NAME);
			//sendBroadcast(new Intent(AppAction.ACTION_CLOSE_DIALOG));
			DialogActivity.this.finish();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			int value = values[0];
			handler.sendEmptyMessage(value);
			progressBar.setProgress(value);
		}

		@Override
		protected void onCancelled(Integer result) {
			super.onCancelled(result);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		private void clearSrcFile() {
			srcDir = new File(FileInf.RES);
			if (!srcDir.exists()) {
				srcDir.mkdirs();
			} else {
				File[] srcFiles = srcDir.listFiles();
				if (srcFiles != null)
					for (int i = 0; i < srcFiles.length;) {
						if (srcFiles[i] != null || srcFiles[i].exists()) {
							if (srcFiles[i].delete()) {
								Log.d(TAG, "delete：" + i);
								i++;
							}
						}
					}
			}
		}

	}

}
