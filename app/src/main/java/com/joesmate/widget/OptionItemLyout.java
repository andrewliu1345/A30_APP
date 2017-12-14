package com.joesmate.widget;

import android.content.Context;
import android.widget.LinearLayout;

import com.joesmate.R;
import com.joesmate.R.id;

public class OptionItemLyout extends LinearLayout {

	CustomText tvTitle, tvOption;
	String[] items;
	int option;
	public OptionItemLyout(Context context) {
		super(context);

		inflate(context, R.layout.option_item_layout, this);
		tvTitle = (CustomText) findViewById(id.option_item_layout_title);
		tvOption = (CustomText) findViewById(id.option_item_layout_option);
	}
	
	public void setText(String title, int arrayId,int option) {
		this.option = option;
		items = getResources().getStringArray(arrayId);
		tvTitle.setText(title);
		tvOption.setText(items[option]);
	}

	public void setOption(int option) {
		this.option = option;
		tvOption.setText(items[option]);
	}

	public int getOption() {
		return option;
	}
	
	

}
