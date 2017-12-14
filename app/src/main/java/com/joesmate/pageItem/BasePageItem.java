package com.joesmate.pageItem;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.audio.AudioPlayer;
import com.joesmate.bin.BaseData;
import com.joesmate.page.PlayActivity;
import com.joesmate.widget.CustomText;
import com.joesmate.widget.TimerView;
import com.joesmate.widget.TimerView.OnTimerListener;

public abstract class BasePageItem extends LinearLayout implements
		OnClickListener, OnTimerListener {

	public static final String TAG = "BasePageItem";
	public boolean isPlayAudio = true;
	public static enum ButtonType {
		NOTHING, ALL, OK_CANCEL, UP_NEXT, UP_CANCEL_NEXT, OPTION,OK_CANCEL_OPTION
	   ,BT_NEXT,OK,NEXT;
	}

	ButtonType buttonType;
    protected CustomText tvTitle;
	TimerView timerView;
	LinearLayout layoutContent, layoutBtGroup, layoutOK;
	LinearLayout layoutOptionGroup, layoutOptionContent, layoutOptionNext;
	public   Button btOK, btCancel, btUp, btNext, optionUp, optionNext;
	ImageView ivLine ,topLine;
	Resources res;
	View contentView;

	float baseViewWidth, baseViewHeight;
	/** FullHeight 没有按钮的高度，DefHeight 有按钮的高度 **/
	float FullHeight, DefHeight, contentHeight;
	Context context;


	public BasePageItem(Context context) {
		super(context);
		Log.d(TAG, "onCreate BasePageItem");
		this.context = context;
		initDemins();
		inflate(context, R.layout.page_item_base, this);
		onFinishInflate();
	}

	private void initDemins() {
		res = getResources();
		baseViewWidth = res.getDimension(R.dimen.page_base_width);
		baseViewHeight = res.getDimension(R.dimen.page_base_height);
		//baseViewHeight =res.getDimension(R.dimen.height);
		float titleHeight = res.getDimension(R.dimen.page_base_title_height);
		float buttonHeight = res.getDimension(R.dimen.page_base_bt_height);
		float lineHeight = res.getDimension(R.dimen.line_width);
		float marging = res.getDimension(R.dimen.page_base_widget_margin);
		float timerHeight = res.getDimension(R.dimen.page_base_timer_height);
		DefHeight = baseViewHeight - titleHeight - buttonHeight - timerHeight
				- 2 * lineHeight - 2 * marging;
		FullHeight = DefHeight + buttonHeight;

	}

	@Override
	protected void onFinishInflate() {
		// TODO 没有回调暂时主动调用

		tvTitle = (CustomText) findViewById(id.page_base_title);
		//Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZSong-RKXX.TTF");
		//tvTitle.setTypeface(typeface);
		ivLine = (ImageView) findViewById(id.page_item_bt_line);

		topLine = (ImageView) findViewById(id.page_item_top_line);

		timerView = (TimerView) findViewById(id.page_base_timer);

		timerView.setOnTimerListener(this);
        //列表选择  确认和取消  上页 下页
		layoutBtGroup = (LinearLayout) findViewById(id.page_base_btGroup);
		//列表选择  上一题和确认
		layoutOptionGroup = (LinearLayout) findViewById(id.page_base_btGroupOption);
        //上一题中的确认
		Log.d(TAG, "onFinishInflate列表选择111");
		layoutOptionContent = (LinearLayout) findViewById(id.page_base_Option_content);

		layoutOK = (LinearLayout) findViewById(id.page_base_layout_ok);
		layoutOptionNext = (LinearLayout) findViewById(id.page_base_layout_nextOption);
       //上一页
		btUp = (Button) findViewById(id.page_base_bt_upward);
		//确定
		btOK = (Button) findViewById(id.page_base_bt_ok);
		//取消
		btCancel = (Button) findViewById(id.page_base_bt_cancel);
		//下一页
		btNext = (Button) findViewById(id.page_base_bt_next);
		//上一题
		optionUp = (Button) findViewById(id.page_base_bt_upOption);
		//上一题中的确认
		optionNext = (Button) findViewById(id.page_base_bt_nextOption);

		btUp.setOnClickListener(this);
		btOK.setOnClickListener(this);
		btCancel.setOnClickListener(this);
		btNext.setOnClickListener(this);
		optionUp.setOnClickListener(this);
		optionNext.setOnClickListener(this);

		layoutContent = (LinearLayout) findViewById(id.page_base_content);
		contentView = getContentView();
		layoutContent.addView(contentView);

	}

	public void init(BaseData baseData, ButtonType buttonType) {
		if (baseData == null) {
			return;
		}
		setTitle(baseData.getTitle());
		setTimer(baseData.getTimeOut());
		setButtonType(buttonType);
		String audio = baseData.getAudioType();
		if(isPlayAudio){
			AudioPlayer.getInstance(getContext()).play(audio);
		}
	}

	public void setTitle(String title) {
		tvTitle.setText(title);
	}

	public void setTimer(int time) {
		timerView.setTime(time);
		if (time > 0) {
			timerView.setVisibility(View.VISIBLE);
			topLine.setVisibility(VISIBLE);
		} else {
			timerView.setVisibility(View.INVISIBLE);
			topLine.setVisibility(INVISIBLE);
		}
	}

	public void setButtonType(ButtonType buttonType) {
		this.buttonType = buttonType;
		if (buttonType== ButtonType.OK){

			layoutBtGroup.setVisibility(VISIBLE);
			btUp.setVisibility(GONE);
			btNext.setVisibility(GONE);
		}else if (buttonType== ButtonType.NEXT){
			layoutBtGroup.setVisibility(VISIBLE);
			btNext.setVisibility(VISIBLE);

			btNext.setBackgroundColor(Color.parseColor("#ffffff"));
			btNext.setTextColor(Color.parseColor("#000000"));
			btNext.setText("取消");



			btOK.setVisibility(GONE);
			btCancel.setVisibility(GONE);
			btUp.setVisibility(GONE);
		}
		if(buttonType == ButtonType.OK_CANCEL_OPTION)
		{
			layoutOptionGroup.setVisibility(View.VISIBLE);
			btUp.setVisibility(View.GONE);
			btNext.setVisibility(View.GONE);
			optionNext.setVisibility(GONE);
			layoutBtGroup.setVisibility(View.VISIBLE);

		}else if (buttonType == ButtonType.OPTION) {

			layoutOptionGroup.setVisibility(View.VISIBLE);
			layoutBtGroup.setVisibility(View.INVISIBLE);
		} else {
			layoutOptionGroup.setVisibility(View.GONE);
			if (buttonType == ButtonType.NOTHING) {
				layoutBtGroup.setVisibility(View.GONE);
				ivLine.setVisibility(View.GONE);
			} else {

				if (buttonType == ButtonType.OK_CANCEL) {
					layoutBtGroup.setVisibility(VISIBLE);
					ivLine.setVisibility(VISIBLE);
					btCancel.setVisibility(VISIBLE);
					btOK.setVisibility(VISIBLE);
					btUp.setVisibility(View.GONE);
					btNext.setVisibility(View.GONE);
				} else if (buttonType == ButtonType.UP_CANCEL_NEXT) {
					btOK.setVisibility(View.GONE);
					layoutOK.setVisibility(View.GONE);
				} else if (buttonType == ButtonType.UP_NEXT) {
					btOK.setVisibility(View.GONE);
					btCancel.setVisibility(View.GONE);
				}
			}

		}

		contentView.setLayoutParams(getChildParams());
	}

	public void toPlay() {
		Log.e("取消返回","");
		Log.e("确认返回","");

		Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
		intent.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
		getContext().sendBroadcast(intent);
	}

	public void toPlay(byte[] page) {
		Log.e("取消返回","");
		Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
		intent.putExtra(AppAction.KEY_BROADCAST_CMD, page);
		getContext().sendBroadcast(intent);
	}

	public LayoutParams getFillParams() {
//		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
//				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		LayoutParams params = new LayoutParams(
				App.SCREEN_WIDTH, App.SCREEN_HEIGHT);
		return params;
	}

	/**
	 * 确定子视图的高度确定
	 *
	 * @return
	 */
	public LayoutParams getChildParams() {
		contentHeight = FullHeight;
		if (buttonType == ButtonType.NOTHING) {
			contentHeight = FullHeight;
		} else {
			contentHeight = DefHeight;
		}
		Log.d(TAG, "content< width：" + baseViewWidth + "height："
				+ contentHeight + ">");
		// 添加子控件的宽高不能不大于父控件的宽高
		return new LayoutParams(LayoutParams.MATCH_PARENT,
				(int) contentHeight);
	}

	public abstract View getContentView();

}
