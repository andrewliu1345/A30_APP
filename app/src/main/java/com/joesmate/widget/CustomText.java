package com.joesmate.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.joesmate.AppAction;
import com.joesmate.AssitTool;
import com.joesmate.SharedpreferencesData;

public class CustomText extends TextView{

    int[] top_colors = {Color.BLACK,Color.WHITE,Color.RED,Color.rgb(250,128,10),Color.YELLOW,Color.GREEN,Color.CYAN,Color.parseColor("#336cba"),Color.rgb(221,12,240)};
	Context mcontext ;
    public static final String TAG = "CustomText";
	public CustomText(Context context, AttributeSet attrs) {
		super(context, attrs);
		mcontext = context;
		/*Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FZSong-RKXX.TTF");
		setTypeface(typeface);*/
	}

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          if(intent.getAction().equals(AppAction.ACTION_BROADCAST_SET_TOPTITLE))
          {
              Log.d(TAG,"----- receive:set new toptitle action ");
              String newTopTitle = intent.getStringExtra("newtitle");
              setText(newTopTitle);
          }
          else  if ( intent.getAction().equals(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE))
          {
              Typeface tf = AssitTool.getFonts(SharedpreferencesData.getInstance().getTop_font_family()) ;
              if( tf != null) {
                  setTypeface(tf);
              }
              //字体大小
              setTextSize(SharedpreferencesData.getInstance().getDefTopFontSize());
              //字体粗细
              if(SharedpreferencesData.getInstance().getTop_font_weight() == 1 ) {
                  getPaint().setFakeBoldText(true);
              }
              //字体颜色
              setTextColor(top_colors[SharedpreferencesData.getInstance().getTop_font_color()]);

              //背景颜色
              setBackgroundColor(top_colors[SharedpreferencesData.getInstance().getTop_bgcolor()]);

              //高度
              //topTitle.setHeight(SharedpreferencesData.getInstance().getTop_height());
              /*LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)getLayoutParams();
              params.weight = App.SCREEN_WIDTH ;
              params.height = SharedpreferencesData.getInstance().getTop_height();
              Log.d(TAG, "--------------top height:" + SharedpreferencesData.getInstance().getTop_height());
              setLayoutParams(params);
              requestLayout();*/

          }
        }
    };

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppAction.ACTION_BROADCAST_SET_TOPTITLE);
        filter.addAction(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE);
        mcontext.registerReceiver(receiver, filter);
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mcontext.unregisterReceiver(receiver);
    }

    @Override
    public boolean isFocused() {
        return true;
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
    }

}
