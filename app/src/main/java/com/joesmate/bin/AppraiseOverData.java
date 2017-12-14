package com.joesmate.bin;

import com.joesmate.R;

public class AppraiseOverData extends BaseData{

	
	private static AppraiseOverData appraiseOverData;
	private String showMsg;
	public AppraiseOverData(){
		setTitle(R.string.page_item_title_pjover);
	}
	public static AppraiseOverData getInstance(){
		if(appraiseOverData == null){
			appraiseOverData = new AppraiseOverData();
		}
		return appraiseOverData;
	}
	
	public String getShowMsg() {
		return showMsg;
	}

	public void setShowMsg(String showMsg) {
		this.showMsg = showMsg;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		
		
	}

}
