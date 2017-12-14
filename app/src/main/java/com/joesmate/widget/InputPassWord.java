package com.joesmate.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joesmate.AppAction;
import com.joesmate.R;
import com.joesmate.bin.InputPw;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by bill on 2016/1/13.
 */
public class InputPassWord extends LinearLayout {

    private Context mcontext;
    // GifView  gifView ;
    MediaVideoGif mediaVideoGif ;
    LinearLayout imageContent ;
    TextView maincontent;
    int passwordNum = 6 ;
    public static final String TAG = "InputPassWord";
    ArrayList<ImageView>  passwords ;

    int currentIndex = 0 ;

    public InputPassWord(Context context) {
        super(context);
        mcontext = context;

        //inflate(context, R.layout.mediaimages, this);
        onFinishInflate();

    }

    public InputPassWord(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;

        //inflate(context, R.layout.mediaimages, this);
        onFinishInflate();

    }


    public void setSource(ArrayList<File> files, int second) {

    }

    public Handler handler = new Handler();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        inflate(mcontext, R.layout.input_password_layout, this);
        //gifView = (GifView) findViewById(R.id.gif2);
        mediaVideoGif = (MediaVideoGif) findViewById(R.id.video);

        imageContent = (LinearLayout) findViewById(R.id.passwordImg);
        maincontent = (TextView) findViewById(R.id.mainContent);
        maincontent.setText(InputPw.getInstance().getMainContent());
       /* if(App.APP_TYPE == 1 )
        {
            gifView.setGifImage(R.raw.inputpassword1);
        }

        if(App.APP_TYPE == 2 )
        {
            gifView.setGifImage(R.raw.inputpassword2);
        }

        gifView.setGifImageType(GifView.GifImageType.COVER);
        gifView.setShowDimension(App.SCREEN_WIDTH / 2, App.SCREEN_HEIGHT - SharedpreferencesData.getInstance().getTop_height());*/

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER);
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR);
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_BACK);
        mcontext.registerReceiver(receiver, filter);
        passwords = new ArrayList<ImageView>(passwordNum);
        for (int i = 0 ; i < passwordNum ; ++ i)
        {
            ImageView  imageView = new ImageView(mcontext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(30,30);
            params.setMargins(10,10,10,10);
            imageView.setLayoutParams(params);
            imageContent.addView(imageView);
            imageView.setVisibility(INVISIBLE);
            imageView.setBackgroundResource(R.drawable.blankdot);
            passwords.add(imageView);
        }
    }

    void displayPassWordView()
    {
        for (int i = 0 ; i < passwordNum ; ++ i)
        {
          if(i < currentIndex)
          {
              passwords.get(i).setVisibility(VISIBLE);
          }
          else {
              passwords.get(i).setVisibility(INVISIBLE);
          }
        }
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "----- onDetachedFromWindow 2222");
        mcontext.unregisterReceiver(receiver);
    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR))
            {
                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR ");
                if(currentIndex < passwordNum)
                {
                    currentIndex = currentIndex + 1 ;
                    Log.d(TAG, "----- currentIndex:"+currentIndex);
                    displayPassWordView();
                }

            }else if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER))
            {
                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ENTER ");

            }else if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_BACK))
            {
                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_BACK ");
               /* if(currentIndex > 0 )
                {
                    currentIndex = currentIndex - 1 ;
                    displayPassWordView();
                }*/
                currentIndex = 0 ;
                Log.d(TAG, "----- currentIndex:"+currentIndex);
                displayPassWordView();
            }
        }
    };

}

