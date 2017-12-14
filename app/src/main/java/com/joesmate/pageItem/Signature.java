package com.joesmate.pageItem;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.CMD;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.ResposeSignatureData;
import com.joesmate.bin.SignatureData;
import com.joesmate.widget.HtmlView;
import com.joesmate.widget.SignatureFrame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class Signature extends BasePageItem {

	private Button btSignature;
	private SignatureFrame signatureFrame;
	private ImageView ivImage;
	private HtmlView htmlView;
	LinearLayout layoutPdf;
	private SignatureData signatureData;
	Activity context;

	public Signature(Activity context) {
		super(context);
		this.context = context;
		btSignature = (Button) findViewById(id.page_item_signature_bt);
		btSignature.setOnClickListener(this);

		signatureFrame = (SignatureFrame) findViewById(id.page_item_signature_farme);
		signatureFrame.setOnSignatureListener(signatureListener);

		ivImage = (ImageView) findViewById(id.page_item_signature_img);
		htmlView = (HtmlView) findViewById(id.page_item_signature_html);

		layoutPdf = (LinearLayout) findViewById(id.page_item_signature_pdf);

		signatureData = SignatureData.getInstance();
	}
	//modify (1 jpg;2 PDF; 3HTML demo| 协议不同)
	public void init() {
		super.init(signatureData, ButtonType.NOTHING);
		if (signatureData.getFileType() == 3) {
			htmlView.setVisibility(View.VISIBLE);
			ivImage.setVisibility(View.GONE);
			layoutPdf.setVisibility(View.GONE);

			htmlView.loadChar(AssitTool.getString(signatureData.getArrayHtml(),
					AssitTool.UTF_8));
		} else if (signatureData.getFileType() == 1) {
			ivImage.setVisibility(View.VISIBLE);
			htmlView.setVisibility(View.GONE);
			layoutPdf.setVisibility(View.GONE);

			try {
				byte[] dataBuffer = signatureData.getDataBuffer();
				// threadSave(dataBuffer);
				ivImage.setBackground(new BitmapDrawable(BitmapFactory
						.decodeByteArray(dataBuffer, 0, dataBuffer.length)));
			} catch (Exception e) {
				Log.d(TAG, e.getMessage());
			}
		} else if (signatureData.getFileType() == 2) {
			layoutPdf.setVisibility(View.VISIBLE);
			htmlView.setVisibility(View.GONE);
			ivImage.setVisibility(View.GONE);
			try {
				MuPDFCore muPDFCore = new MuPDFCore(getContext(),
						signatureData.getDataBuffer(),"");
				MuPDFReaderView muPDFReaderView = new MuPDFReaderView(context);
				muPDFReaderView.setAdapter(new MuPDFPageAdapter(context,
						null,muPDFCore));
				muPDFReaderView.setDisplayedViewIndex(0);
				layoutPdf.removeAllViews();
				layoutPdf.addView(muPDFReaderView);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onClick(View v) {
		if (btSignature == v) {
			btSignature.setVisibility(View.GONE);
			signatureFrame.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void timeOut() {
		signatureFrame.signatureView.clear();
		toPlay();
	}

	@Override
	public View getContentView() {
		return inflate(getContext(), R.layout.page_item_signature, null);
	}

	SignatureFrame.OnSignatureListener signatureListener = new SignatureFrame.OnSignatureListener() {

		@Override
		public void hide() {
			btSignature.setVisibility(View.VISIBLE);
		}

		@Override
		public void confirm() {
			btSignature.setVisibility(View.VISIBLE);
			ResposeSignatureData.getInstance().setResposeBitmap(
					signatureFrame.getSignatureBitmap());
			App.getInstance().fitManagerCCB.getBaseFitBin().setData(CMD.SR,
					CMD.SR.length);
			testSignature();
			toPlay();
		}

	};

	void testSignatureData() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bitmap = null;
				Canvas canvas = null;
			}
		}).start();
	}

	void testSignature() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				Bitmap bitmap = signatureFrame.getSignatureBitmap();
				File dir = new File("/mnt/sdcard/signature/");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File file = new File(dir, "ss.png");
				if (!file.exists()) {
					try {
						file.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				FileOutputStream fos = null;
				try {
					fos = new FileOutputStream(file);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
			}
		}).start();
	}
}
