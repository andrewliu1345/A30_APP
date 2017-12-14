package com.joesmate.pageItem;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

import com.joesmate.R;

public class AppraiseSpecial extends BasePageItem {
	RelativeLayout layout;
	public AppraiseSpecial(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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
		if(layout == null)
		layout = (RelativeLayout) inflate(getContext(), R.layout.page_item_interactivefile, null);
		return layout;
	}

}
