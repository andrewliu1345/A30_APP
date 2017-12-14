package com.joesmate.pageItem;

import android.content.Context;
import android.view.View;

import com.joesmate.BackCode;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.AddWebFileData;
import com.joesmate.listener.OnJsListener;
import com.joesmate.widget.HtmlView;

public class WebApp extends BasePageItem implements OnJsListener{

	HtmlView htmlView;
	AddWebFileData webFileData;
	public WebApp(Context context) {
		super(context);
		htmlView = (HtmlView) findViewById(id.page_item_web_app);
		htmlView.setOnJsListener(this);
		webFileData = AddWebFileData.getInstance(); 
	}
	
	public void init(){
		setButtonType(ButtonType.OK_CANCEL);
		setTimer(webFileData.getTimeOut());
		setTitle("Web应用");
		htmlView.loadFile(webFileData.getHomeFile());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		toPlay();
	}

	@Override
	public void timeOut() {
		webFileData.sendJhData(BackCode.CODE_80, null);
		toPlay();
	}

	@Override
	public View getContentView() {
		// TODO Auto-generated method stub
		return inflate(getContext(), R.layout.page_item_web_app, null);
	}

	@Override
	public void submit(String str) {
		webFileData.sendJhData(BackCode.CODE_00, str);
		
	}

}
