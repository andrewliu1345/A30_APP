package com.joesmate.pageItem;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.sdcs.SDCSConfirmPDF;

public class ConfirmPDF extends BasePageItem {
	public static final String TAG = "ConfirmPDF";
	LinearLayout layoutPdf;
	SDCSConfirmPDF baseData ;
	Activity context;
	public ConfirmPDF(Activity context) {
		super(context);
		this.context = context;
		layoutPdf = (LinearLayout) findViewById(id.page_item_interactive_file_pdf);
		baseData = SDCSConfirmPDF.getInstance();
	}

	public void init() {
		tvTitle.setVisibility(GONE);
		if (baseData.getIsUserConfirm() == 1) {
			super.init(baseData, ButtonType.OK_CANCEL);
			btCancel.setText(baseData.getLeftText());
			btOK.setText(baseData.getRightText());
		} else if(baseData.getIsUserConfirm()==0){
			super.init(baseData, ButtonType.NEXT);

			btNext.setText("取消");

		}
		try {
			MuPDFCore muPDFCore = new MuPDFCore(getContext(), baseData.getArrayFileData(),"");
			MuPDFReaderView muPDFReaderView = new MuPDFReaderView(context);
			muPDFReaderView.setAdapter(new MuPDFPageAdapter(context,null, muPDFCore));
			muPDFReaderView.setDisplayedViewIndex(0);
			new Handler().postDelayed( muPDFReaderView
					, 300);
			layoutPdf.removeAllViews();
			layoutPdf.addView(muPDFReaderView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	@Override
	public void onClick(View v) {
		if (v == btOK) {
			//fileData.operation(BackCode.CODE_00, BackCode.CODE_00);
			baseData.sendResult(0);
		} else if (v == btCancel) {
			//fileData.operation(BackCode.CODE_00, BackCode.CODE_01);
			baseData.sendResult(1);
		}else if (v==btNext){
			baseData.sendResult(3);
		}
		toPlay();
	}

	@Override
	public void timeOut() {
		//fileData.operation(BackCode.CODE_80, BackCode.CODE_80);
		if (baseData.getIsUserConfirm() == 1) {
			baseData.sendResult(2);
		}
		else
		{
			baseData.sendResult(3);
	}
	toPlay();
	}

	@Override
	public View getContentView() {
		return inflate(getContext(), R.layout.page_item_confirmpdf, null);
	}

}
