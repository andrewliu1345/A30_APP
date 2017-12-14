package com.joesmate.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.joesmate.R;

public class OptionItem extends ViewGroup {
	private static final int PADDING_HOR = 10;// 水平方向padding
	private static final int PADDING_VERTICAL = 5;// 垂直方向padding
	private static final int SIDE_MARGIN = 10;// 左右间距
	private static final int TEXT_MARGIN = 10;


	public static final int TYPE_OPTION_SINGLE = 1;
	public static final int TYPE_OPTION_DOUBLE = 2;

	public int optionType;
	private int optionIndex = -1;
	Resources resources;
	int textSize;


	/**
	 * @param context
	 * @param attrs
	 */
	public OptionItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		resources = getResources();
		textSize = (int) resources.getDimension(R.dimen.page_base_content_size);
	}
	public void setOptionItems(String[] items,int optionType){
		removeAllViews();
		if(items == null){
			return;
		}
		this.optionType = optionType;
		optionIndex = -1;
		for(int i=0;i < items.length; i++){
			CustomButton button = new CustomButton(getContext());
			button.setText(items[i]);
			button.setTextColor(Color.GRAY);
			button.setTextSize(textSize);
			button.setBackgroundResource(R.drawable.option_unfocus_bg);
			button.setSelect(false);
			button.setOnClickListener(clickListener);
			addView(button);
		}
	}
	public int[] getSelectItems(){
		List<Object> selectList = new ArrayList<Object>();
		for (int i = 0; i < getChildCount(); i++) {
			final CustomButton view = (CustomButton) getChildAt(i);
			if(view == null){
				continue;
			}
			if(view.isSelect()){
				selectList.add(i);
			}
		}
		if(selectList.isEmpty() || selectList.size() == 0){
			return null;
		}
		int[] selectArray = new int[selectList.size()];
		for(int i = 0 ; i < selectArray.length ; i++){
			selectArray[i] = (Integer) selectList.get(i);
		}
		return selectArray;
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int autualWidth = r - l;
		int x = SIDE_MARGIN;// 横坐标开始
		int y = 0;// 纵坐标开始
		int rows = 1;
		for (int i = 0; i < childCount; i++) {
			CustomButton view = (CustomButton) getChildAt(i);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			x += width + TEXT_MARGIN;
			if (x > autualWidth) {
				x = width + SIDE_MARGIN;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN);
			if (i == 0) {
				view.layout(x - width - TEXT_MARGIN, y - height, x
						- TEXT_MARGIN, y);
			} else {
				view.layout(x - width, y - height, x, y);
			}
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int x = 0;// 横坐标
		int y = 0;// 纵坐标
		int rows = 1;// 总行数
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		int actualWidth = specWidth - SIDE_MARGIN * 2;// 实际宽度
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++) {
			View child = getChildAt(index);
			child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR,
					PADDING_VERTICAL);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			x += width + TEXT_MARGIN;
			if (x > actualWidth) {// 换行
				x = width;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN);
		}
		setMeasuredDimension(actualWidth, y);
	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (optionType == TYPE_OPTION_SINGLE && optionIndex != -1) {
				CustomButton customButton = (CustomButton) getChildAt(optionIndex);
				customButton.setBackgroundResource(
						R.drawable.option_unfocus_bg);
				customButton.setSelect(false);

			}
			for (int index = 0; index < getChildCount(); index++) {
				final CustomButton childView = (CustomButton) getChildAt(index);
				
				if(v == childView){
					optionIndex = index;
					int bgredID =0;
					boolean isSelect = false;
					if(optionType == TYPE_OPTION_DOUBLE){
						bgredID = !childView.isSelect()?R.drawable.option_focus_bg:R.drawable.option_unfocus_bg;
						isSelect = !childView.isSelect()?true:false;
					}else{
						bgredID = R.drawable.option_focus_bg;
						isSelect = true;
						
					}
					childView.setBackgroundResource(bgredID);
					childView.setSelect(isSelect);
				}
			}

		}
	};
	
	class CustomButton extends Button{
		private boolean isSelect;
		public CustomButton(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}
		public boolean isSelect() {
			return isSelect;
		}
		public void setSelect(boolean isSelect) {
			this.isSelect = isSelect;
		}
		
	}

}
