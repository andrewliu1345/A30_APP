package com.joesmate.pageItem;

import android.content.Context;
import android.view.View;

import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.keyBoard.ShowTextData;
import com.joesmate.widget.CustomText;

public class M3ShowText extends BasePageItem{

	private CustomText tvMsg;
	ShowTextData showTextData;
	public M3ShowText(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		tvMsg = (CustomText) findViewById(id.page_item_showtext_txt);
		showTextData = ShowTextData.getInstance();
	}
	public void init(){
		isPlayAudio = false;
		super.init(showTextData, ButtonType.NOTHING);
		String msgs = ShowTextData.getInstance().getMsg();
		tvMsg.setText(msgs == null ? "" : msgs);
		tvMsg.setText(msgs);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void timeOut() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return inflate(getContext(), R.layout.page_item_show_text, null);
	}

}
