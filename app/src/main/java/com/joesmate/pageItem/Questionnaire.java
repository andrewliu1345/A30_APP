package com.joesmate.pageItem;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.joesmate.App;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.QuestionData;
import com.joesmate.bin.sdcs.OperateQuestionData;
import com.joesmate.widget.ButtonOption;
import com.joesmate.widget.CustomText;
import com.joesmate.widget.HtmlView;

import java.util.ArrayList;
import java.util.List;

public class Questionnaire extends BasePageItem {

	public static final String TAG = "Questionnaire";
	CustomText tvNavigation;
	HtmlView htmlView;
	//QuestionData questionData;
    OperateQuestionData questionData;
	int optionType;
	List<ButtonOption> buttonOptions = new ArrayList<ButtonOption>();
	public Questionnaire(Context context) {
		super(context);
		//questionData = QuestionData.getInstance();
		questionData = OperateQuestionData.getInstance();
		tvNavigation = (CustomText) findViewById(id.page_item_question_navigation);
		htmlView = (HtmlView) findViewById(id.page_item_question_html);
	}

	public void init() {
		super.init(questionData, ButtonType.OK_CANCEL_OPTION);
		App.getInstance().tts.Read(questionData.getVoiceText(), 1);
		setTitle("问卷调查");
		buttonOptions.clear();
		layoutOptionContent.removeAllViews();
		//optionType = questionData.getOptionType();
		optionType = questionData.getQuestionClass();
		layoutOptionContent.setVisibility(VISIBLE);
		layoutBtGroup.setVisibility(VISIBLE);

		if(questionData.getQuestionCurIndex() == 1)
		{
			optionUp.setVisibility(GONE);
		}
		else
		{
			optionUp.setVisibility(VISIBLE);
		}

		tvNavigation.setText("(第" + questionData.getQuestionCurIndex() + "题  " + "共"
				+ questionData.getQuestiontotalNum() + "题)");
		for (String option : questionData.getOptionItems()) {
				layoutOptionContent.addView(
						getOptionLayout(option, questionData.getQuestionClass()),
						getWeightParams());
			}

		htmlView.loadChar(questionData.getQuestionData());
		}
		
/*
	private String getResult(int[] selectArray) {
		String result = "";
		for (int i = 0; i < selectArray.length; i++) {
			if (i == 0) {
				result += (char) (selectArray[i] + 65);
			} else {
				result += "|" + (char) (selectArray[i] + 65);
			}

		}
		return result;
	}*/


	private void submitData(int[] selectArray) {
		if (selectArray == null || selectArray.length == 0) {
			Toast.makeText(getContext(), "请选择答案后再点击下一页", Toast.LENGTH_SHORT)
					.show();
		} else {
			/*questionData.setBack(BackCode.CODE_00, BackCode.CODE_00,
					getResult(selectArray));*/
		}
	}

	@Override
	public void onClick(View v) {
/*		if (v == btNext) {
			if (questionData.getCurIndex() == questionData.getTotalNum()) {
				Log.d(TAG, "submit");
				// submitData(optionItem.getSelectItems());
				toPlay();
			} else {
				// submitData(optionItem.getSelectItems());
			}
		} else if (v == btUp) {
			questionData.setBack(BackCode.CODE_00, BackCode.CODE_01,
					BackCode.CODE_01);
		} else if (v == btCancel) {
			toPlay();
			questionData.setBack(BackCode.CODE_20, BackCode.CODE_20,
					BackCode.CODE_20);
		}*/
		if(v == optionUp){
			if (questionData.getQuestionCurIndex() == 1) {
				Toast.makeText(getContext(), "没有上一题", Toast.LENGTH_SHORT).show();
				return;
			}
			questionData.sendConfirmResult("2","");
			toPlay();
		}else if(v == optionNext){
			confirm();
		}
		else if(v == btCancel )
		{
			questionData.sendConfirmResult("1","");
			toPlay();
		}
		else if(v == btOK)
		{
			confirm();
		}
	}
	private void confirm(){
	/*	questionData.response(CMD.CODE_01H, questionData.getCurIndex(), getAnswer());
		if (questionData.getCurIndex() == questionData.getTotalNum()) {
			toPlay();
		}*/
		if(!"".equals(getAnswer()))
		{
			questionData.sendConfirmResult("0",getAnswer());
			toPlay();
		}

	}

	private LayoutParams getWeightParams() {
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = 1.0f;
		return params;
	}

	private LinearLayout getOptionLayout(String msg, int optionType) {
		LinearLayout layout = new LinearLayout(getContext());
		int width = (int) getResources().getDimension(
				R.dimen.appraise_option_width);
		int height = (int) getResources().getDimension(
				R.dimen.appraise_option_height);
		LayoutParams params = new LayoutParams(width, height);
		params.gravity = Gravity.RIGHT;

		ButtonOption option = new ButtonOption(getContext());

		option.setButtonOption(msg, optionType, checkListener);
		buttonOptions.add(option);
		layout.addView(option, params);
		return layout;
	}

	@Override
	public void timeOut() {
		questionData.sendConfirmResult("3","");
		toPlay();
		//questionData.erroCode(CMD.CODE_80H);
	}

	@Override
	public View getContentView() {
		return inflate(getContext(), R.layout.page_item_questionnaire, null);
	}
	private String getAnswer(){
		StringBuffer buffer = new StringBuffer();
		for(int i= 0; i< buttonOptions.size(); i++){
			if(buttonOptions.get(i).isChecked()){
				if(buffer.length() != 0){
					buffer.append(",");
				}
				buffer.append(buttonOptions.get(i).tvTxt.getText());
			}
		}
		Log.d("bill","getAnswer:"+buffer.toString());
		return buffer.toString();
	}
	
	ButtonOption.OnOptionCheckListener checkListener = new ButtonOption.OnOptionCheckListener(){

		@Override
		public void onCheck(boolean isChecked) {
			if(optionType == QuestionData.TYPE_OPTION_SINGLE){
				confirm();
			}/*else if(optionType == QuestionData.TYPE_OPTION_DOUBLE){
				int selected = 0;
				for(int i= 0; i< buttonOptions.size(); i++){
					if(buttonOptions.get(i).isChecked()){
						selected ++;
					}
				}
				if(selected == 0){
					optionNext.setVisibility(View.INVISIBLE);
				}else{
					optionNext.setVisibility(View.VISIBLE);
				}
			}*/
			
		}
		
	};

}
