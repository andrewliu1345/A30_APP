package com.joesmate.pageItem;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.joesmate.AudioType;
import com.joesmate.BackCode;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.InteractiveFileData;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class InteractiveFile extends BasePageItem {
	public static final String TAG = "InteractiveFile";
	ImageView ivFile;
	LinearLayout layoutPdf;
	InteractiveFileData fileData;
	Activity context;
	public InteractiveFile(Activity context) {
		super(context);
		this.context = context;
		ivFile = (ImageView) findViewById(id.page_item_interactive_file_img);
		layoutPdf = (LinearLayout) findViewById(id.page_item_interactive_file_pdf);
		fileData = InteractiveFileData.getInstance();
	}

	public void init() {
		if (AudioType.TYPE_255.equals(fileData.getAudioType())) {
			super.init(fileData, ButtonType.NOTHING);
		} else {
			super.init(fileData, ButtonType.OK_CANCEL);
		}
		
		if(fileData.getFileType() == 1){
			//pdf
			layoutPdf.setVisibility(View.VISIBLE);
			ivFile.setVisibility(View.GONE);
			try {
				MuPDFCore muPDFCore = new MuPDFCore(getContext(), fileData.getDataBuffer(),"");
				MuPDFReaderView muPDFReaderView = new MuPDFReaderView(context);
		        muPDFReaderView.setAdapter(new MuPDFPageAdapter(context,null, muPDFCore));
		        muPDFReaderView.setDisplayedViewIndex(0);
		        layoutPdf.removeAllViews();
		        layoutPdf.addView(muPDFReaderView);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else if(fileData.getFileType() == 2){
			//jpg
			ivFile.setVisibility(View.VISIBLE);
			layoutPdf.setVisibility(View.GONE);
			try {
				byte[] dataBuffer = fileData.getDataBuffer();
				ivFile.setBackground(new BitmapDrawable(BitmapFactory
						.decodeByteArray(dataBuffer, 0, dataBuffer.length)));
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			}
		}
		
	}

	void threadSave(final byte[] buffer) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				save(buffer);
			}
		}).start();
	}

	void save(byte[] buffer) {
		File dir = new File("/mnt/sdcard/img");
		if (!dir.exists()) {
			dir.mkdirs();

		}
		File file = new File(dir, "kk.jpg");
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			try {
				fos.write(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		if (v == btOK) {
			fileData.operation(BackCode.CODE_00, BackCode.CODE_00);
		} else if (v == btCancel) {
			fileData.operation(BackCode.CODE_00, BackCode.CODE_01);
		}
		toPlay();
	}

	@Override
	public void timeOut() {
		fileData.operation(BackCode.CODE_80, BackCode.CODE_80);
		toPlay();
	}

	@Override
	public View getContentView() {
		return inflate(getContext(), R.layout.page_item_interactivefile, null);
	}

}
