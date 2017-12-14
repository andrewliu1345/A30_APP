package com.joesmate.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.joesmate.R;
import com.joesmate.R.id;

public class ItemSeekBar extends LinearLayout {

	public static final String TAG = "ItemSeekBar";
	OnItemSeekBarListener barListener;
	SeekBar seekBar;
	CustomText tvTitle,tvValue;

	public ItemSeekBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.item_seekbar, this);

		seekBar = (SeekBar) findViewById(id.item_seekbar);
		seekBar.setOnSeekBarChangeListener(seekBarChangeListener);
		seekBar.setOnFocusChangeListener(onFocusChangeListener);
		
		tvTitle = (CustomText) findViewById(id.item_seekbar_title);
		tvValue = (CustomText) findViewById(id.item_seekbar_value);
	
	}
	
	public void init(String title,int progress,int max){
		tvTitle.setText(title);
		tvValue.setText(""+progress);
		seekBar.setMax(max);
		seekBar.setProgress(progress);
	}
	
	public void setProgress(int progress){
		seekBar.setProgress(progress);
	}

	OnFocusChangeListener onFocusChangeListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {

			Log.d(TAG, "hasFocus:" + hasFocus);
		}
	};

	OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			Log.d(TAG, "onStopTrackingTouch:");
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			Log.d(TAG, "onStartTrackingTouch:");
		}

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			Log.d(TAG, "progress:" + progress);
			Log.d(TAG, "fromUser:" + fromUser);
			tvValue.setText(""+progress);
			if(barListener != null){
				barListener.onItemChanged(ItemSeekBar.this, progress);
			}
		}
	};
	
	public void setOnItemSeekBarListener(OnItemSeekBarListener barListener){
		this.barListener = barListener;
	}
	
	public static interface OnItemSeekBarListener{
		public void onItemChanged(ItemSeekBar itemSeekBar,int progress);
	}

}
