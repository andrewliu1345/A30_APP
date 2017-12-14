package com.joesmate.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.joesmate.R;
public class WeightItem extends LinearLayout implements OnClickListener{

	Resources res;
	OnWeightItemListener itemListener;
	List<Button> buttons = new ArrayList<Button>();
	int itemIndex;
	String[] items;
	public WeightItem(Context context) {
		super(context);
		setOrientation(LinearLayout.HORIZONTAL);
		res = getResources();
	}
	@SuppressLint("ResourceAsColor") public void setItems(int arrayId,int defIndex,OnWeightItemListener itemListener){
		 items = getResources().getStringArray(arrayId);
		itemIndex = defIndex;
		this.itemListener = itemListener;
		LinearLayout.LayoutParams params = new LayoutParams(0, LayoutParams.FILL_PARENT);
		params.weight = 1.0f;
		params.gravity = Gravity.CENTER;
		
		View tag = new View(getContext());
		tag.setBackgroundColor(R.color.title_blue);
		int tagWidth = (int) res.getDimension(R.dimen.keyboard_items_margin_left);
		LinearLayout.LayoutParams paramsT = new LayoutParams(tagWidth, LayoutParams.FILL_PARENT);
		addView(tag, paramsT);
		LinearLayout.LayoutParams paramsV = new LayoutParams(2, -1);
		for (int i = 0; i < items.length; i++) {
			
			View view = new View(getContext());
			view.setBackgroundColor(res.getColor(R.color.white));
			
			Button button = new Button(getContext());
			button.setOnClickListener(this);
			button.setText(items[i]);
			button.setSingleLine(true);
			button.setEllipsize(TextUtils.TruncateAt.MARQUEE);
			button.setFocusable(true);
			button.setFocusableInTouchMode(true);
			button.setTextColor(res.getColor(R.color.white));
			button.setTextSize(res
					.getDimension(R.dimen.page_base_content_size));
			if (i == defIndex) {
				button.setBackgroundColor(res.getColor(R.color.bt_ok));
			} else {
				button.setBackgroundColor(res.getColor(R.color.content_blue));
			}

			addView(button, params);
			if (i != items.length - 1) {
				addView(view, paramsV);
			}
			buttons.add(button);
		}
	}
	public void setItem(int index){
		itemIndex = index;
		for (int i = 0; i < buttons.size(); i++) {
			if (itemIndex == i) {
				resetBtBg();
				buttons.get(i).setBackgroundColor(res.getColor(R.color.bt_ok));
				if(itemListener != null){
					//itemListener.itemClick(WeightItem.this,items[itemIndex],itemIndex);
				}
			}
		}
	}
	@Override
	public void onClick(View v) {
		
		for (int i = 0; i < buttons.size(); i++) {
			if (v == buttons.get(i)) {
				resetBtBg();
				itemIndex = i;
				v.setBackgroundColor(res.getColor(R.color.bt_ok));
				if(itemListener != null){
					itemListener.itemClick(WeightItem.this,itemIndex);
				}
			}
		}
		
	}
	private void resetBtBg(){
		for (int i = 0; i < buttons.size(); i++) {
			buttons.get(i).setBackgroundColor(res.getColor(R.color.content_blue));
		}
	}
	public static interface OnWeightItemListener{
		public void itemClick(WeightItem weightItem,int index);
	}

}
