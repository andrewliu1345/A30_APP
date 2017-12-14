package com.joesmate.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.joesmate.R;
import com.joesmate.R.id;

public class AppraiseButton extends LinearLayout{

	private CustomText tvCH,tvEN;
	public AppraiseButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.appraise_button, this);
		onFindView();
	}
	
	public AppraiseButton(Context context) {
		super(context);
		inflate(context, R.layout.appraise_button, this);
		onFindView();
	}
	void onFindView(){
		tvCH = (CustomText) findViewById(id.appraise_button_ch);
		tvEN = (CustomText) findViewById(id.appraise_button_en);
		this.setClickable(true);
	}
	
	public void setText(String cn,String en,int bgResid){
		if("null".equals(cn)){
			tvCH.setVisibility(View.GONE);
		}else{
			tvCH.setText(cn);
		}
		
		if("null".equals(en)){
			tvEN.setVisibility(View.GONE);
		}else{
			tvEN.setText(en);
		}
		setBackgroundResource(bgResid);
	}

}
