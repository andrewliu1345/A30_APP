package com.joesmate.pageItem;

import android.content.Context;
import android.view.View;

import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.icbc.ResposeICBCSignatureData;
import com.joesmate.bin.sdcs.SDCSStartEvaluateData;
import com.joesmate.widget.CustomText;
/***
 *
 * 评价返回界面*
 *
 * **/
public class AppraiseOver extends BasePageItem{

	//AppraiseOverData overData;
	SDCSStartEvaluateData baseData ;
	CustomText tvHit,tvResult,blackButton;
	public AppraiseOver(Context context) {
		super(context);
		tvHit = (CustomText) findViewById(id.appraise_over_hit);
		tvResult = (CustomText) findViewById(id.appraise_over_result);


		//评价后返回界面的返回取消
		blackButton=(CustomText)findViewById(id.PingBlack);
		blackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ResposeICBCSignatureData.getInstance().setSignState(3);
				toPlay();
			}
		});
		baseData = SDCSStartEvaluateData.getInstance();
	}

	public void init(){
		super.init(baseData, ButtonType.NOTHING);
		tvHit.setText(baseData.getAfterMsg());
		tvResult.setText(baseData.getResult());
		setTimer(baseData.getAfterTimeout());

		setTitle(getResources().getString(R.string.page_item_title_pjover));
	}




	@Override
	public void onClick(View v) {

	}

	@Override
	public void timeOut() {
		toPlay();
	}

	@Override
	public View getContentView() {
		return inflate(getContext(), R.layout.page_item_appraise_over, null);
	}

}
