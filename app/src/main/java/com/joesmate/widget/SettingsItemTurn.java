package com.joesmate.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;

import com.joesmate.AssitTool;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.Rotate3dAnimation;

public class SettingsItemTurn extends LinearLayout implements OnClickListener {

	LinearLayout layoutContent, layoutUnturn, layoutTurn;
	CustomText tvTitle, tvOption;
	private boolean isTurned;
	String[] options;
	List<Button> buttons = new ArrayList<Button>();
	Resources res;
	int checkIndex;

	public SettingsItemTurn(Context context, AttributeSet attrs) {
		super(context, attrs);
		res = getResources();
		inflate(context, R.layout.view_turn, this);
		layoutContent = (LinearLayout) findViewById(id.settings_item_layout);
		layoutContent.setOnClickListener(this);

		layoutUnturn = (LinearLayout) inflate(context,
				R.layout.settings_item_unturn, null);
		layoutContent.addView(layoutUnturn, AssitTool.getLinearParams(-1, -1));
		tvTitle = (CustomText) layoutUnturn
				.findViewById(id.settings_item_unturn_title);
		tvOption = (CustomText) layoutUnturn
				.findViewById(id.settings_item_unturn_option);
		
		layoutTurn = new LinearLayout(context);
		layoutTurn.setOrientation(LinearLayout.HORIZONTAL);
	}

	public void setItem(String title, String[] options, int defIndex) {
		buttons.clear();
		tvTitle.setText(title);
		this.options = options;
		tvOption.setText(options[defIndex]);
		checkIndex = defIndex;

		LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.FILL_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;
		
		LinearLayout.LayoutParams paramsV = new LayoutParams(2, -1);
		for (int i = 0; i < options.length; i++) {
			
			View view = new View(getContext());
			view.setBackgroundColor(res.getColor(R.color.white));
			
			Button button = new Button(getContext());
			button.setOnClickListener(this);
			button.setText(options[i]);
			button.setTextColor(res.getColor(R.color.white));
			button.setTextSize(res
					.getDimension(R.dimen.page_base_content_size));
			if (i == defIndex) {
				button.setBackgroundColor(res.getColor(R.color.bt_ok));
			} else {
				button.setBackgroundColor(res.getColor(R.color.gray));
			}

			layoutTurn.addView(button, params);
			if (i != options.length - 1) {
				layoutTurn.addView(view, paramsV);
			}
			buttons.add(button);
		}
	}

	@Override
	public void onClick(View v) {
		if (isTurned) {
			for (int i = 0; i < buttons.size(); i++) {
				if (v == buttons.get(i)) {
					resetBtBg();
					checkIndex = i;
					v.setBackgroundColor(res.getColor(R.color.bt_ok));
					tvOption.setText(options[checkIndex]);
					isTurned = false;
					layoutContent.startAnimation(getRotation(180, 0));
				}
			}
		} else {
			isTurned = true;
			layoutContent.startAnimation(getRotation(180, 0));

		}
	}
	void resetBtBg(){
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setBackgroundColor(res.getColor(R.color.gray));
		}
	}

	private Rotate3dAnimation getRotation(int fromDegrees, int toDegrees) {

		float centerX = layoutContent.getWidth() / 2.0f;
		float centerY = layoutContent.getHeight() / 2.0f;

		Rotate3dAnimation rotation = new Rotate3dAnimation(fromDegrees,
				toDegrees, centerX, centerY, 10.0f, true);

		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setFillBefore(true);
		rotation.setInterpolator(new AccelerateInterpolator());
		rotation.setAnimationListener(animationListener);

		return rotation;
	}

	
	private Animation.AnimationListener animationListener = new Animation.AnimationListener() {

		@Override
		public void onAnimationStart(Animation animation) {

		}

		@Override
		public void onAnimationEnd(Animation animation) {
			layoutContent.removeAllViews();
			if (isTurned) {
				layoutContent.addView(layoutTurn,  AssitTool.getLinearParams(-1, -1));
			}else{
				layoutContent.addView(layoutUnturn,  AssitTool.getLinearParams(-1, -1));
			}

		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	};
}
