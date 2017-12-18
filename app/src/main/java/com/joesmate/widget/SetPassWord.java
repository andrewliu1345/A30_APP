package com.joesmate.widget;

import android.app.ActionBar;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
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
import com.joesmate.bin.sdcs.SDCSResetPinData;
import com.joesmate.page.PlayActivity;
import com.joesmate.util.LogMg;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bill on 2016/1/13.
 */
public class SetPassWord extends LinearLayout implements TimerView.OnTimerListener, SerialUtil.OnKeyReceiveListener {

//    private Context mcontext;
//    // GifView  gifView ;
//    // MediaVideoGif mediaVideoGif ;
//    LinearLayout inputPasswordContent;
//    //LinearLayout inputPasswordContentAgain;
//
//    LinearLayout passwordGrp;
//    //LinearLayout passwordAgainGrp;
//    HtmlView htmlContent;
//    //TextView maincontent;
//    TextView txtMsg;
//    //TextView txtMsgAgain;
//    int passwordLength = 6;
//    int passwordNumber = 1;
//    int endType = 0;
//    public static final String TAG = "SetPassWord";
//    //ArrayList<ImageView> passwords;
//    //ArrayList<ImageView> passwordsAgain;
//
//    public final  static  int MSG_ENTER = 1 ;
//    public final  static  int MSG_CANCEL= 2 ;
//    public final  static  int MSG_NUMBER = 3 ;
//
//    int[] passwordValue;
//    //int[] passwordAgainValue;
//
//
//    //boolean secondInput = false;
//
//    int currentIndex = 0;
//
//    public SetPassWord(Context context) {
//        super(context);
//        mcontext = context;
//
//        onFinishInflate();
//
//    }
//
//    public SetPassWord(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        mcontext = context;
//
//        onFinishInflate();
//
//
//    }
//
//
//    Handler myHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case MSG_ENTER:
//                    //txtMsgAgain.setVisibility(INVISIBLE);
//                    txtMsg.setVisibility(INVISIBLE);
//                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ENTER ");
//
//                    if ( SDCSReadPinData.getInstance().getiEncryType() == 1) {
//
//                        SDCSReadPinData.getInstance().sendConfirmResult(getResult());
//                        SerialUtil.getInstance().setStop(true);
//                        Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
//                        intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
//                        getContext().sendBroadcast(intent1);
//
//
//                    }
//
//                    if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {
//                        if (currentIndex < passwordLength) {
//                            txtMsg.setVisibility(VISIBLE);
//                            txtMsg.setText("密码必须输入" + passwordLength + "位");
//
//                        }
//                    }
//                    break;
//                case MSG_NUMBER:
//
//
//                    if (SDCSReadPinData.getInstance().getiEncryType() == 1) {
//                       // char str = (char) intent.getByteExtra("inputChar", (byte) 0);
//                        //int str1 = str - 48;
//                        int str1 = msg.arg1 - 48;
//                        Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR :" + str1);
//                        //txtMsgAgain.setVisibility(INVISIBLE);
//                        txtMsg.setVisibility(INVISIBLE);
//                        if (currentIndex < passwordLength) {
//                            currentIndex = currentIndex + 1;
//                            Log.d(TAG, "----- currentIndex:" + currentIndex);
//                            AddPassword();
//                            passwordValue[currentIndex - 1] = str1;
//                        } else if (currentIndex == passwordLength) {
//
//                            SDCSReadPinData.getInstance().sendConfirmResult(getResult());
//                            Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
//                            intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
//                            getContext().sendBroadcast(intent1);
//                            SDCSReadPinData.getInstance().closeInputChar();
//
//
//                        }
//                    }
//
//                    if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {
//
//                        Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR " );
//                        //txtMsgAgain.setVisibility(INVISIBLE);
//                        txtMsg.setVisibility(INVISIBLE);
//                        if (currentIndex < passwordLength) {
//                            currentIndex = currentIndex + 1;
//                            Log.d(TAG, "----- currentIndex:" + currentIndex);
//                            AddPassword();
//                        }
//                    }
//
//                    break;
//                case MSG_CANCEL:
//                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_BACK ");
//                    if ( SDCSReadPinData.getInstance().getiEncryType() == 1) {
//                        //txtMsgAgain.setVisibility(INVISIBLE);
//                        txtMsg.setVisibility(INVISIBLE);
//                        currentIndex = 0;
//                        resetPassword();
//                        Log.d(TAG, "----- currentIndex:" + currentIndex);
//                        ClearPassword();
//                    }
//
//                    if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {
//                        //txtMsgAgain.setVisibility(INVISIBLE);
//                        txtMsg.setVisibility(INVISIBLE);
//                        currentIndex = 0;
//                        Log.d(TAG, "----- currentIndex:" + currentIndex);
//                        ClearPassword();
//                    }
//                    break;
//            }
//            super.handleMessage(msg);
//        }
//    };
//
//    @Override
//    protected void onFinishInflate() {
//        super.onFinishInflate();
//        if(SDCSReadPinData.getInstance().getiEncryType() == 1)
//        {
//            passwordLength = 50;
//        }
//        else {
//            passwordLength = SDCSReadPinData.getInstance().getiInputPassworfLength();
//        }
//        passwordNumber = SDCSReadPinData.getInstance().getiInputPasswordNumber();
//        endType = SDCSReadPinData.getInstance().getPasswordEndType();
//        //passwordNum = 6 ;
//        inflate(mcontext, R.layout.set_password_layout, this);
//
//
//        inputPasswordContent = (LinearLayout) findViewById(R.id.passwordImg);
//        //inputPasswordContentAgain = (LinearLayout) findViewById(R.id.passwordImgagain);
//
//
//        passwordGrp = (LinearLayout) findViewById(R.id.passowdGrp);
//        //passwordAgainGrp = (LinearLayout) findViewById(R.id.passowdAgainGrp);
//
//
//        txtMsg = (TextView) findViewById(R.id.txtmsg);
//        //txtMsgAgain = (TextView) findViewById(R.id.txtmsgagain);
//        txtMsg.setVisibility(INVISIBLE);
//        //txtMsgAgain.setVisibility(INVISIBLE);
//
//        htmlContent = (HtmlView) findViewById(R.id.htmlContent);
//        htmlContent.loadChar(SDCSReadPinData.getInstance().getDisplayContent());
//       /* if (passwordNumber == 1) {
//            passwordAgainGrp.setVisibility(GONE);
//
//        } else {
//            passwordAgainGrp.setVisibility(VISIBLE);
//        }*/
//
//       /* IntentFilter filter = new IntentFilter();
//        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER);
//        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR);
//        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_BACK);*/
//        //mcontext.registerReceiver(receiver, filter);
//        //passwords = new ArrayList<ImageView>(passwordLength);
//        //passwordsAgain = new ArrayList<ImageView>(passwordLength);
//       /* for (int i = 0; i < passwordLength; ++i) {
//            ImageView imageView = new ImageView(mcontext);
//            LayoutParams params = new LayoutParams(30, 30);
//            params.setMargins(5, 10, 5, 5);
//            imageView.setLayoutParams(params);
//            inputPasswordContent.addView(imageView);
//            imageView.setVisibility(INVISIBLE);
//            imageView.setBackgroundResource(R.drawable.blankdot);
//            passwords.add(imageView);
//
//
//            ImageView imageView1 = new ImageView(mcontext);
//            LayoutParams params1 = new LayoutParams(30, 30);
//            params1.setMargins(5, 10, 5, 5);
//            imageView1.setLayoutParams(params);
//            //inputPasswordContentAgain.addView(imageView1);
//            imageView1.setVisibility(INVISIBLE);
//            imageView1.setBackgroundResource(R.drawable.blankdot);
//           // passwordsAgain.add(imageView1);
//
//
//        }
//*/
//        passwordValue = new int[passwordLength];
//        //passwordAgainValue = new int[passwordLength];
//        ClearPassword();
//
//        SerialUtil.getInstance().setOnKeyReceiveListener(this);
//
//    }
//
// /*   void displayPassWordView() {
//
//        for (int i = 0; i < passwordLength; ++i) {
//            if (i < currentIndex) {
//                passwords.get(i).setVisibility(VISIBLE);
//            } else {
//                passwords.get(i).setVisibility(INVISIBLE);
//            }
//        }
//    }*/
//
//    void AddPassword()
//    {
//        ImageView img = new ImageView(mcontext);
//        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
//        LinearLayout.LayoutParams  params =  new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
//        params.setMargins(1,1,1,1);
//        img.setLayoutParams(params);
//        inputPasswordContent.addView(img);
//    }
//    void ClearPassword() {
//        inputPasswordContent.removeAllViews();
//    }
//
//    void resetPassword() {
//        for (int i = 0; i < passwordLength; ++i) {
//            passwordValue[i] = 10;
//        }
//    }
//
// /*   void resetPasswordAgain() {
//        for (int i = 0; i < passwordLength; ++i) {
//            passwordAgainValue[i] = 10;
//        }
//    }*/
//
//
//    String getResult() {
//        String str = "00";
//        if (passwordNumber == 1) {
//            for (int i = 0; i < passwordLength; ++i) {
//                if (passwordValue[i] != 10) {
//                    str = str + passwordValue[i];
//                }
//            }
//        }
//
//        Log.d("bill", "result:" + str);
//        return str;
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        Log.d(TAG, "----- onDetachedFromWindow 2222");
//        //mcontext.unregisterReceiver(receiver);
//    }
//
//
// /*   BroadcastReceiver receiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR)) {
//                // am broadcast -a test
//
//                if (SDCSReadPinData.getInstance().getiEncryType() == 1) {
//                    char str = (char) intent.getByteExtra("inputChar", (byte) 0);
//                    int str1 = str - 48;
//                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR :" + str1);
//                    //txtMsgAgain.setVisibility(INVISIBLE);
//                    txtMsg.setVisibility(INVISIBLE);
//                    if (currentIndex < passwordLength) {
//                        currentIndex = currentIndex + 1;
//                        Log.d(TAG, "----- currentIndex:" + currentIndex);
//                        displayPassWordView();
//                        passwordValue[currentIndex - 1] = str1;
//                    } else if (currentIndex == passwordLength) {
//
//                            SDCSReadPinData.getInstance().sendConfirmResult(getResult());
//                            Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
//                            intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
//                            getContext().sendBroadcast(intent1);
//                            SDCSReadPinData.getInstance().closeInputChar();
//
//
//                    }
//                }
//
//                if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {
//
//                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR " );
//                    //txtMsgAgain.setVisibility(INVISIBLE);
//                    txtMsg.setVisibility(INVISIBLE);
//                    if (currentIndex < passwordLength) {
//                        currentIndex = currentIndex + 1;
//                        Log.d(TAG, "----- currentIndex:" + currentIndex);
//                        displayPassWordView();
//                    }
//                }
//
//
//
//
//            } else if (intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER)) {
//
//
//                //txtMsgAgain.setVisibility(INVISIBLE);
//                txtMsg.setVisibility(INVISIBLE);
//                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ENTER ");
//
//                if ( SDCSReadPinData.getInstance().getiEncryType() == 1) {
//
//                    SDCSReadPinData.getInstance().sendConfirmResult(getResult());
//                    SerialUtil.getInstance().setStop(true);
//                    Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
//                    intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
//                    getContext().sendBroadcast(intent1);
//
//
//                }
//
//                if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {
//                    if (currentIndex < passwordLength) {
//                        txtMsg.setVisibility(VISIBLE);
//                        txtMsg.setText("密码必须输入" + passwordLength + "位");
//
//                    }
//                }
//
//
//
//            } else if (intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_BACK)) {
//                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_BACK ");
//                if ( SDCSReadPinData.getInstance().getiEncryType() == 1) {
//                   // txtMsgAgain.setVisibility(INVISIBLE);
//                    txtMsg.setVisibility(INVISIBLE);
//                    currentIndex = 0;
//                    resetPassword();
//                    Log.d(TAG, "----- currentIndex:" + currentIndex);
//                    for (int i = 0; i < passwordLength; ++i) {
//                        passwords.get(i).setVisibility(INVISIBLE);
//                    }
//                }
//
//                if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {
//                    //txtMsgAgain.setVisibility(INVISIBLE);
//                    txtMsg.setVisibility(INVISIBLE);
//                    currentIndex = 0;
//                    Log.d(TAG, "----- currentIndex:" + currentIndex);
//                    for (int i = 0; i < passwordLength; ++i) {
//                        passwords.get(i).setVisibility(INVISIBLE);
//                    }
//                }
//
//
//
//            }
//
//
//        }
//    };*/
//
//    @Override
//    public void timeOut() {
//        if (SDCSReadPinData.getInstance().getiEncryType() == 1) {
//            SDCSReadPinData.getInstance().sendConfirmResult(getResult());
//            SDCSReadPinData.getInstance().closeInputChar();
//            SerialUtil.getInstance().setStop(true);
//            Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
//            intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
//            getContext().sendBroadcast(intent1);
//        }
//        else if(SDCSReadPinData.getInstance().getiEncryType() == 5)
//        {
//            SDCSReadPinData.getInstance().closeInputChar();
//            SerialUtil.getInstance().setStop(true);
//            Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
//            intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
//            getContext().sendBroadcast(intent1);
//        }
//    }
//
//    @Override
//    public void onKeyReceive(SerialUtil.KEY key, int data) {
//        if(key.equals(SerialUtil.KEY.ENTER))
//        {
//            myHandler.sendEmptyMessage(MSG_ENTER);
//        }
//        else if (key.equals(SerialUtil.KEY.CANCEL))
//        {
//            myHandler.sendEmptyMessage(MSG_CANCEL);
//        }
//        else if(key.equals(SerialUtil.KEY.NUMBER))
//        {
//            Message message = new Message();
//            message.what = MSG_NUMBER;
//            message.arg1 = data ;
//            myHandler.sendMessage(message);
//
//        }
//    }


    private Context mcontext;
    // GifView  gifView ;
    // MediaVideoGif mediaVideoGif ;
    LinearLayout inputPasswordContent;
    //LinearLayout inputPasswordContentAgain;

    LinearLayout passwordGrp, pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8, pass9, pass10, pass11, pass12;
    Map<Integer, LinearLayout> passmap;
    // ImageView pass1;


    //LinearLayout passwordAgainGrp;
    HtmlView htmlContent;
    //TextView maincontent;
    TextView txtMsg;
    //TextView txtMsgAgain;
    //密码长度

    int passwordLength = SDCSReadPinData.getInstance().getiInputPassworfLength();
    int passwordNumber = 1;
    int endType = 0;
    int iDisplayType = 0;
    int iEncryType = 0;
    public static final String TAG = "SetPassWord";
    //ArrayList<ImageView> passwords;
    //ArrayList<ImageView> passwordsAgain;

    public final static int MSG_ENTER = 1;
    public final static int MSG_CANCEL = 2;
    public final static int MSG_NUMBER = 3;

    int[] passwordValue;
    //int[] passwordAgainValue;


    //boolean secondInput = false;

    int currentIndex = 0;

    public SetPassWord(Context context) {
        super(context);
        mcontext = context;
        onFinishInflate();
        passlength();


    }

    public SetPassWord(Context context, AttributeSet attrs) {
        super(context, attrs);
        mcontext = context;
        onFinishInflate();
        passlength();


    }

    public void count(int iNum) {
        for (Map.Entry<Integer, LinearLayout> passEntity :
                passmap.entrySet())
            passEntity.getValue().setVisibility(GONE);
        LinearLayout pass = passmap.get(passwordLength);
        pass.setVisibility(VISIBLE);
        if (iDisplayType == 1 && iEncryType == 1)
            AddPasswordX(pass, iNum);
        else
            AddPasswordY(pass, iNum);
    }

    public void passlength() {
        init();
        LinearLayout pass = passmap.get(passwordLength);
        if (pass != null)
            pass.setVisibility(VISIBLE);
//        switch (passwordLength) {
//            case 1:
//                pass1.setVisibility(VISIBLE);
//                break;
//            case 2:
//                pass2.setVisibility(VISIBLE);
//                break;
//            case 4:
//                pass4.setVisibility(VISIBLE);
//                break;
//            case 5:
//                pass5.setVisibility(VISIBLE);
//                break;
//            case 6:
//                pass6.setVisibility(VISIBLE);
//                break;
//            case 7:
//                pass7.setVisibility(VISIBLE);
//                break;
//            case 8:
//                pass8.setVisibility(VISIBLE);
//                break;
//            case 3:
//                pass3.setVisibility(VISIBLE);
//                break;
//            case 9:
//                pass9.setVisibility(VISIBLE);
//                break;
//            case 10:
//                pass10.setVisibility(VISIBLE);
//                break;
//            case 11:
//                pass11.setVisibility(VISIBLE);
//                break;
//            case 12:
//                pass12.setVisibility(VISIBLE);
//                break;
//        }
    }

    Handler myHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ENTER:
                    //txtMsgAgain.setVisibility(INVISIBLE);
                    txtMsg.setVisibility(INVISIBLE);
                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ENTER ");

                    // if (SDCSReadPinData.getInstance().getiEncryType() == 1) {
                    if (currentIndex < passwordLength&&endType == 1) {
                        txtMsg.setVisibility(VISIBLE);
                        txtMsg.setText("必须输入" + passwordLength + "位");
                        Log.e("密码必须输入", "" + 1);
                        count();
                        SerialUtil.getInstance().setStop(true);
                        SDCSReadPinData.getInstance().inputChar();

                        cleanPass();
                    } else {
                        Log.e("密码必须输入", "" + 2);
                        SDCSReadPinData.getInstance().sendConfirmResult(getResult());
                        SerialUtil.getInstance().setStop(true);
                        Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
                        intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
                        getContext().sendBroadcast(intent1);

                    }


                    //     }

//                    if (SDCSReadPinData.getInstance().getiEncryType() == 5) {
//                        if (currentIndex < passwordLength) {
//                            txtMsg.setVisibility(VISIBLE);
//                            txtMsg.setText("必须输入" + passwordLength + "位");
//                            Log.e("密码必须输入", "" + 3);
//                            count();
//                        }
//                    }
                    break;
                case MSG_NUMBER:


//                    if (SDCSReadPinData.getInstance().getiEncryType() == 1) {
                    // char str = (char) intent.getByteExtra("inputChar", (byte) 0);
                    //int str1 = str - 48;
                    int str1 = msg.arg1 - 48;
                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR :" + str1);
                    //txtMsgAgain.setVisibility(INVISIBLE);
                    txtMsg.setVisibility(INVISIBLE);
                    if (currentIndex < passwordLength) {
                        Log.e("密码必须输入", "" + 4);

                        Log.d(TAG, "密码位数:" + currentIndex);
                        count(str1);
                        passwordValue[currentIndex++] = str1;
                        Log.d(TAG, "密码位数:" + str1);
//                        if (currentIndex == passwordLength && endType == 1) {
//                            Log.e("密码必须输入", "" + 5);
//                            SDCSReadPinData.getInstance().sendConfirmResult(getResult());
//                            Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
//                            intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
//                            getContext().sendBroadcast(intent1);
//                            SDCSReadPinData.getInstance().closeInputChar();
//
//
//                        }

                    }
//                    }

//                    if (SDCSReadPinData.getInstance().getiEncryType() == 5) {
//
//                        Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR ");
//                        //txtMsgAgain.setVisibility(INVISIBLE);
//                        txtMsg.setVisibility(INVISIBLE);
//                        if (currentIndex < passwordLength) {
//                            currentIndex++;
//                            Log.e("密码必须输入", "" + 6);
//                            count();
//                            Log.d(TAG, "----- currentIndex:" + currentIndex);
//
//                        }
//                    }

                    break;
                case MSG_CANCEL:
                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_BACK ");
                    //if (SDCSReadPinData.getInstance().getiEncryType() == 1) {
                    //txtMsgAgain.setVisibility(INVISIBLE);
                    txtMsg.setVisibility(INVISIBLE);
                    currentIndex = 0;
                    Log.e("密码必须输入", "" + 7);
                    resetPassword();
                    Log.d(TAG, "----- currentIndex:" + currentIndex);
                    cleanPass();
                    //   }

//                    if (SDCSReadPinData.getInstance().getiEncryType() == 5) {
//                        //txtMsgAgain.setVisibility(INVISIBLE);
//                        txtMsg.setVisibility(INVISIBLE);
//                        currentIndex = 0;
//                        Log.e("密码必须输入", "" + 8);
//                        Log.d(TAG, "----- currentIndex:" + currentIndex);
//                        cleanPass();
//                    }
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void init() {
        pass1 = findViewById(R.id.pass1);
        pass2 = findViewById(R.id.pass2);
        pass3 = findViewById(R.id.pass3);
        pass4 = findViewById(R.id.pass4);
        pass5 = findViewById(R.id.pass5);
        pass6 = findViewById(R.id.pass6);
        pass7 = findViewById(R.id.pass7);
        pass8 = findViewById(R.id.pass8);
        pass9 = findViewById(R.id.pass9);
        pass10 = findViewById(R.id.pass10);
        pass11 = findViewById(R.id.pass11);
        pass12 = findViewById(R.id.pass12);

        passmap = new HashMap<Integer, LinearLayout>() {
            {
                put(1, pass1);
                put(2, pass2);
                put(3, pass3);
                put(4, pass4);
                put(5, pass5);
                put(6, pass6);
                put(7, pass7);
                put(8, pass8);
                put(9, pass9);
                put(10, pass10);
                put(11, pass11);
                put(12, pass12);

            }
        };
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        passwordNumber = SDCSReadPinData.getInstance().getiInputPasswordNumber();
        endType = SDCSReadPinData.getInstance().getPasswordEndType();
        iDisplayType = SDCSReadPinData.getInstance().getiDisplayType();
        iEncryType = SDCSReadPinData.getInstance().getiEncryType();
        //passwordNum = 6 ;
        inflate(mcontext, R.layout.set_password_layout, this);
        passlength();
        LinearLayout li = (LinearLayout) findViewById(R.id.rightinfo);


        //密码
        inputPasswordContent = (LinearLayout) findViewById(R.id.passwordImg);

        inputPasswordContent.setVisibility(VISIBLE);
        //inputPasswordContentAgain = (LinearLayout) findViewById(R.id.passwordImgagain);


        passwordGrp = (LinearLayout) findViewById(R.id.passowdGrp);
        passwordGrp.setVisibility(VISIBLE);
        //passwordAgainGrp = (LinearLayout) findViewById(R.id.passowdAgainGrp);


        txtMsg = (TextView) findViewById(R.id.txtmsg);
        //txtMsgAgain = (TextView) findViewById(R.id.txtmsgagain);
        txtMsg.setVisibility(INVISIBLE);
        //txtMsgAgain.setVisibility(INVISIBLE);

        htmlContent = (HtmlView) findViewById(R.id.htmlContent);
        htmlContent.loadChar(SDCSReadPinData.getInstance().getDisplayContent());
       /* if (passwordNumber == 1) {
            passwordAgainGrp.setVisibility(GONE);

        } else {
            passwordAgainGrp.setVisibility(VISIBLE);
        }*/

       /* IntentFilter filter = new IntentFilter();
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER);
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR);
        filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_BACK);*/
        //mcontext.registerReceiver(receiver, filter);
        //passwords = new ArrayList<ImageView>(passwordLength);
        //passwordsAgain = new ArrayList<ImageView>(passwordLength);
       /* for (int i = 0; i < passwordLength; ++i) {
            ImageView imageView = new ImageView(mcontext);
            LayoutParams params = new LayoutParams(30, 30);
            params.setMargins(5, 10, 5, 5);
            imageView.setLayoutParams(params);
            inputPasswordContent.addView(imageView);
            imageView.setVisibility(INVISIBLE);
            imageView.setBackgroundResource(R.drawable.blankdot);
            passwords.add(imageView);


            ImageView imageView1 = new ImageView(mcontext);
            LayoutParams params1 = new LayoutParams(30, 30);
            params1.setMargins(5, 10, 5, 5);
            imageView1.setLayoutParams(params);
            //inputPasswordContentAgain.addView(imageView1);
            imageView1.setVisibility(INVISIBLE);
            imageView1.setBackgroundResource(R.drawable.blankdot);
           // passwordsAgain.add(imageView1);


        }
*/
        passwordValue = new int[passwordLength];
        //passwordAgainValue = new int[passwordLength];
        cleanPass();

        SerialUtil.getInstance().setOnKeyReceiveListener(this);

    }

 /*   void displayPassWordView() {

        for (int i = 0; i < passwordLength; ++i) {
            if (i < currentIndex) {
                passwords.get(i).setVisibility(VISIBLE);
            } else {
                passwords.get(i).setVisibility(INVISIBLE);
            }
        }
    }*/

    void AddPassword6() {

        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 18.7f, (int) 18.7f, (int) 18.7f, (int) 18.7f);
        img.setLayoutParams(params);

        pass6.addView(img);
    }

    void AddPassword7() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 18.9f, (int) 18.9f, (int) 18.9f, (int) 18.9f);
        img.setLayoutParams(params);

        pass7.addView(img);
    }

    void AddPassword8() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 19f, (int) 19f, (int) 19f, (int) 19f);
        img.setLayoutParams(params);

        pass8.addView(img);
    }

    void AddPassword9() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 19f, (int) 19f, (int) 19f, (int) 19f);
        img.setLayoutParams(params);

        pass9.addView(img);
    }

    void AddPassword10() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 19.5f, (int) 19.5f, (int) 19.5f, (int) 19.5f);
        img.setLayoutParams(params);

        pass10.addView(img);
    }

    void AddPassword11() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(20, (int) 20, (int) 20, (int) 20);
        img.setLayoutParams(params);

        pass11.addView(img);
    }

    void AddPassword12() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 19.9f, 20, 20, 20);
        img.setLayoutParams(params);

        pass12.addView(img);
    }

    void AddPassword1() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(14, 14, 14, 14);
        img.setLayoutParams(params);

        pass1.addView(img);
    }

    void AddPassword2() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 15.5f, (int) 15.5f, (int) 15.5f, (int) 15.5f);
        img.setLayoutParams(params);

        pass2.addView(img);
    }

    void AddPassword3() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 16.2f, (int) 16.2f, (int) 16.2f, (int) 16.2f);
        img.setLayoutParams(params);

        pass3.addView(img);
    }

    void AddPassword4() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 16.5f, (int) 16.5f, (int) 16.5f, (int) 16.5f);
        img.setLayoutParams(params);

        pass4.addView(img);
    }

    void AddPassword5() {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 19.5f, (int) 19.5f, (int) 19.5f, (int) 19.5f);
        img.setLayoutParams(params);

        pass5.addView(img);
    }

    //明文显示
    void AddPasswordX(LinearLayout pass, int iINnum) {
        //添加密码
        Log.e("添加密码", "秒123");
        TextView txt = new TextView(mcontext);
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 22.8f, (int) 12f, (int) 25f, (int) 19.5f);
//        params.weight=305;
//        params.height=247;
        txt.setLayoutParams(params);
        float w1 = pass.getWeightSum();
        int w2 = pass.getWidth();
        LogMg.d(TAG, "w1=%f", w1);
        LogMg.d(TAG, "w2=%d", w2);
        pass.addView(txt);
        txt.setTextSize(35);
        txt.setTextColor(Color.BLACK);
        txt.setText("" + iINnum);
    }

    //圆点显示
    void AddPasswordY(LinearLayout pass, int iINnum) {
        //添加密码
        Log.e("添加密码", "秒123");
        ImageView img = new ImageView(mcontext);
        img.setImageDrawable(getResources().getDrawable(R.drawable.blankdot));
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.setMargins((int) 19.5f, (int) 19.5f, (int) 19.5f, (int) 19.5f);
        img.setLayoutParams(params);
        pass.addView(img);
    }

    void resetPassword() {
        Log.e("重置密码", "秒123");
        for (int i = 0; i < passwordLength; ++i) {
            passwordValue[i] = 10;
        }
    }

 /*   void resetPasswordAgain() {
        for (int i = 0; i < passwordLength; ++i) {
            passwordAgainValue[i] = 10;
        }
    }*/


    String getResult() {
        String str = "";
        if (passwordNumber == 1) {
            for (int i = 0; i < passwordLength; ++i) {
                if (passwordValue[i] != 10) {
                    str = str + passwordValue[i];
                }
            }
        }

        Log.d("bill", "result:" + str);
        return str;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.d(TAG, "----- onDetachedFromWindow 2222");
        //mcontext.unregisterReceiver(receiver);
    }


 /*   BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR)) {
                // am broadcast -a test

                if (SDCSReadPinData.getInstance().getiEncryType() == 1) {
                    char str = (char) intent.getByteExtra("inputChar", (byte) 0);
                    int str1 = str - 48;
                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR :" + str1);
                    //txtMsgAgain.setVisibility(INVISIBLE);
                    txtMsg.setVisibility(INVISIBLE);
                    if (currentIndex < passwordLength) {
                        currentIndex = currentIndex + 1;
                        Log.d(TAG, "----- currentIndex:" + currentIndex);
                        displayPassWordView();
                        passwordValue[currentIndex - 1] = str1;
                    } else if (currentIndex == passwordLength) {

                            SDCSReadPinData.getInstance().sendConfirmResult(getResult());
                            Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
                            intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
                            getContext().sendBroadcast(intent1);
                            SDCSReadPinData.getInstance().closeInputChar();


                    }
                }

                if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {

                    Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR " );
                    //txtMsgAgain.setVisibility(INVISIBLE);
                    txtMsg.setVisibility(INVISIBLE);
                    if (currentIndex < passwordLength) {
                        currentIndex = currentIndex + 1;
                        Log.d(TAG, "----- currentIndex:" + currentIndex);
                        displayPassWordView();
                    }
                }




            } else if (intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER)) {


                //txtMsgAgain.setVisibility(INVISIBLE);
                txtMsg.setVisibility(INVISIBLE);
                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ENTER ");

                if ( SDCSReadPinData.getInstance().getiEncryType() == 1) {

                    SDCSReadPinData.getInstance().sendConfirmResult(getResult());
                    SerialUtil.getInstance().setStop(true);
                    Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
                    intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
                    getContext().sendBroadcast(intent1);


                }

                if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {
                    if (currentIndex < passwordLength) {
                        txtMsg.setVisibility(VISIBLE);
                        txtMsg.setText("密码必须输入" + passwordLength + "位");

                    }
                }



            } else if (intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_BACK)) {
                Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_BACK ");
                if ( SDCSReadPinData.getInstance().getiEncryType() == 1) {
                   // txtMsgAgain.setVisibility(INVISIBLE);
                    txtMsg.setVisibility(INVISIBLE);
                    currentIndex = 0;
                    resetPassword();
                    Log.d(TAG, "----- currentIndex:" + currentIndex);
                    for (int i = 0; i < passwordLength; ++i) {
                        passwords.get(i).setVisibility(INVISIBLE);
                    }
                }

                if ( SDCSReadPinData.getInstance().getiEncryType() == 5) {
                    //txtMsgAgain.setVisibility(INVISIBLE);
                    txtMsg.setVisibility(INVISIBLE);
                    currentIndex = 0;
                    Log.d(TAG, "----- currentIndex:" + currentIndex);
                    for (int i = 0; i < passwordLength; ++i) {
                        passwords.get(i).setVisibility(INVISIBLE);
                    }
                }



            }


        }
    };*/

    @Override
    public void timeOut() {
        if (SDCSReadPinData.getInstance().getiEncryType() == 1) {
            SDCSReadPinData.getInstance().sendConfirmResult(getResult());
        }
        SDCSReadPinData.getInstance().closeInputChar();
        SerialUtil.getInstance().setStop(true);
        Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
        intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
        getContext().sendBroadcast(intent1);
//        } else if (SDCSReadPinData.getInstance().getiEncryType() == 5) {
//            SDCSReadPinData.getInstance().closeInputChar();
//            SerialUtil.getInstance().setStop(true);
//            Intent intent1 = new Intent(AppAction.ACTION_BROADCAST_CMD);
//            intent1.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
//            getContext().sendBroadcast(intent1);
//        }
    }

    @Override
    public void onKeyReceive(SerialUtil.KEY key, int data) {
        if (key.equals(SerialUtil.KEY.ENTER)) {
            myHandler.sendEmptyMessage(MSG_ENTER);
        } else if (key.equals(SerialUtil.KEY.CANCEL)) {
            myHandler.sendEmptyMessage(MSG_CANCEL);
        } else if (key.equals(SerialUtil.KEY.NUMBER)) {
            Message message = new Message();
            message.what = MSG_NUMBER;
            message.arg1 = data;
            myHandler.sendMessage(message);

        }
    }

    public void count() {

        switch (passwordLength) {
            case 1:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);


                pass1.setVisibility(VISIBLE);
                AddPassword1();
                break;
            case 2:
                pass2.setVisibility(VISIBLE);
                pass1.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                AddPassword2();
                break;
            case 3:
                pass3.setVisibility(VISIBLE);
                AddPassword3();
                pass2.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                break;
            case 4:
                pass4.setVisibility(GONE);
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                pass4.setVisibility(VISIBLE);
                AddPassword4();
                break;
            case 5:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                pass5.setVisibility(VISIBLE);
                AddPassword5();
                break;
            case 6:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                pass6.setVisibility(VISIBLE);
                //AddPasswordX(pass6, 1);
                AddPassword6();
                break;
            case 7:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                pass7.setVisibility(VISIBLE);
                AddPassword7();
                break;
            case 8:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                pass8.setVisibility(VISIBLE);
                AddPassword8();
                break;
            case 9:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                pass9.setVisibility(VISIBLE);
                AddPassword9();
                break;
            case 10:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass12.setVisibility(GONE);

                pass10.setVisibility(VISIBLE);
                AddPassword10();
                break;
            case 11:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass12.setVisibility(GONE);
                pass11.setVisibility(VISIBLE);
                AddPassword11();
                break;

            case 12:
                pass2.setVisibility(GONE);
                pass3.setVisibility(GONE);
                pass4.setVisibility(GONE);
                pass5.setVisibility(GONE);
                pass7.setVisibility(GONE);
                pass6.setVisibility(GONE);
                pass8.setVisibility(GONE);
                pass9.setVisibility(GONE);
                pass1.setVisibility(GONE);
                pass11.setVisibility(GONE);
                pass10.setVisibility(GONE);
                pass5.setVisibility(GONE);
                AddPassword12();
                break;

        }

    }

    public void cleanPass() {
        currentIndex = 0;
        passwordValue = new int[passwordLength];
        switch (passwordLength) {
            case 1:
                pass1.removeAllViews();

                break;
            case 2:
                pass2.removeAllViews();

                break;
            case 3:
                pass3.removeAllViews();

                break;
            case 4:
                pass4.removeAllViews();

                break;
            case 5:
                pass5.removeAllViews();

                break;
            case 6:
                pass6.removeAllViews();

                break;
            case 7:
                pass7.removeAllViews();

                break;
            case 8:
                pass8.removeAllViews();

                break;
            case 9:
                pass9.removeAllViews();

                break;
            case 10:
                pass10.removeAllViews();

                break;
            case 11:
                pass11.removeAllViews();

                break;

            case 12:
                pass5.removeAllViews();

                break;


        }
    }
}




