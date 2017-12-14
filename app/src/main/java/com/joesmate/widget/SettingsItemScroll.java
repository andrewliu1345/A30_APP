package com.joesmate.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.joesmate.R;
import com.joesmate.R.id;

public class SettingsItemScroll extends LinearLayout{

	List<View> views = new ArrayList<View>();
	ViewPager viewPager;
	PagerAdapter adapter;
	OnSettingsItemScrollListener itemScrollListener;
	public SettingsItemScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.view_scroll, this);
		viewPager = (ViewPager) findViewById(id.view_scroll_viewpager);
		viewPager.setOnPageChangeListener(pageChangeListener);
		adapter = new PagerAdapter();
		viewPager.setAdapter(adapter);
	}
	public void toFirstPage(){
		viewPager.setCurrentItem(0, true);
	}
	public void setView(View view,OnSettingsItemScrollListener itemScrollListener){
		this.itemScrollListener = itemScrollListener;
		view.setOnClickListener(clickListener);
		views.add(view);
		adapter.notifyDataSetChanged();
	}
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			if(itemScrollListener != null)
			itemScrollListener.onPageSelected(SettingsItemScroll.this,arg0);
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}
	};
	OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			setCurrentItem();
		}
	};
	public void setCurrentItem(){
		int scrollIndex = viewPager.getCurrentItem() ==0 ? 1:0;
		viewPager.setCurrentItem(scrollIndex, true);
	}
	private class PagerAdapter extends android.support.v4.view.PagerAdapter{

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(views.get(position));
			//TODO position params
			return views.get(position);
		}
		@Override
		public int getCount() {
			return views.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		
	}
	public static interface OnSettingsItemScrollListener{
		public void onPageSelected(SettingsItemScroll settingsItemScroll,int arg0); 
	}

}
