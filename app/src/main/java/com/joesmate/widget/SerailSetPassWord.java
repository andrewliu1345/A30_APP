package com.joesmate.widget;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joesmate.AppAction;
import com.joesmate.R;
import com.joesmate.bin.keyBoard.SerialUtil;
import com.joesmate.bin.sdcs.SDCSReadPinData;
import com.joesmate.page.PlayActivity;

/**
 * Created by bill on 2016/1/13.
 */
public class SerailSetPassWord extends LinearLayout implements SerialUtil.OnKeyReceiveListener {

    public static final String TAG = "SerailSetPassWord";
    private Context mcontext;
    LinearLayout passwordGrp;
    TextView txtTitle ;
    String strTitle;
    public final  static  int MSG_ENTER = 1 ;
    public final  static  int MSG_CANCEL= 2 ;
    public final  static  int MSG_NUMBER = 3 ;

    public SerailSetPassWord(Context context,String title) {
        super(context);
        mcontext = context;
        strTitle = title;
        onFinishInflate();

    }

    public SerailSetPassWord(Context context,String title, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        strTitle = title;
        onFinishInflate();

    }



    public Handler handler = new Handler();

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        inflate(mcontext, R.layout.serial_set_password_layout, this);
        txtTitle = (TextView) findViewById(R.id.txttitle);
        passwordGrp = (LinearLayout) findViewById(R.id.passwordImg);
        SerialUtil.getInstance().setOnKeyReceiveListener(this);
        //txtTitle.setText(strTitle);
        /*IntentFilter filter = new IntentFilter();
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER);
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR);
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_BACK);
        mcontext.registerReceiver(receiver, filter);*/

    }


    private  void addOneChar ()
    {
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LinearLayout.LayoutParams  params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(1,1,1,1);
        img.setLayoutParams(params);
        passwordGrp.addView(img);

    }

    private  void clearAllChar()
    {
        passwordGrp.removeAllViews();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "----- onDetachedFromWindow 2222");
       // mcontext.unregisterReceiver(receiver);
    }

/*
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR))
            {
                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR ");
                addOneChar();


            }else if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER))
            {
                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ENTER ");
                toplay();


            }else if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_BACK))
            {
                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_BACK ");
                clearAllChar();


            }
        }
    };*/


    public void toplay() {
        SDCSReadPinData.getInstance().closeInputChar();
        Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
        intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
        getContext().sendBroadcast(intent1);
    }


    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ENTER:
                    toplay();
                    break;
                case MSG_NUMBER:
                    addOneChar();
                    break;
                case MSG_CANCEL:
                    clearAllChar();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    public void onKeyReceive(SerialUtil.KEY key, int data) {
        if(key.equals(SerialUtil.KEY.ENTER))
        {
            myHandler.sendEmptyMessage(MSG_ENTER);
        }
        else if (key.equals(SerialUtil.KEY.CANCEL))
        {
            myHandler.sendEmptyMessage(MSG_CANCEL);
        }
        else if(key.equals(SerialUtil.KEY.NUMBER))
        {
            Message message = new Message();
            message.what = MSG_NUMBER;
            message.arg1 = data ;
            myHandler.sendMessage(message);

        }
    }
}

