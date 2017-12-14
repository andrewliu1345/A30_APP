package com.joesmate.widget;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.QuestionData;

public class ButtonOption extends LinearLayout implements OnClickListener{

	int optionType;
	private boolean isChecked;
	OnOptionCheckListener checkListener;
    public ImageView ivImg;
	public CustomText tvTxt;
	public ButtonOption(Context context) {
		super(context);
		inflate(context, R.layout.widget_option, this);
		ivImg = (ImageView) findViewById(id.widget_option_img);
		tvTxt = (CustomText) findViewById(id.widget_option_txt);
		setClickable(true);
		setFocusable(true);
		setBackgroundResource(R.drawable.option_check_bg);
		setOnClickListener(this);
	}
	public void setButtonOption(String msg,int optionType,OnOptionCheckListener checkListener){
		this.optionType = optionType;
		if(optionType == QuestionData.TYPE_OPTION_SINGLE){
			ivImg.setVisibility(View.GONE);
			setBackgroundResource(R.drawable.option_radio_bg);
		}
		tvTxt.setText(msg);
		check(optionType, false);
		this.checkListener = checkListener;
	}
	
	public boolean isChecked(){
		return isChecked;
	}
	void check(int optionType,boolean isChecked){
		int resid = 0;
		if(optionType == QuestionData.TYPE_OPTION_DOUBLE){
			resid = isChecked ? R.drawable.btn_check_on_focused_holo_dark:R.drawable.btn_check_off_focused_holo_dark;
		}else if(optionType == QuestionData.TYPE_OPTION_SINGLE){
			resid = isChecked ? R.drawable.btn_radio_on_focused_holo_dark:R.drawable.btn_radio_off_focused_holo_dark;
		}
		ivImg.setBackgroundResource(resid);
	}


	public static interface OnOptionCheckListener {
		public void onCheck(boolean isChecked);
	}



	@Override
	public void onClick(View v) {
		isChecked = isChecked?false:true;
		check(optionType, isChecked);
		if(checkListener != null){
			checkListener.onCheck(isChecked);
		}
		
	}

}
