package com.joesmate.page;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.joesmate.AndroidBug5497Workaround;
import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.DeviceSettings;
import com.joesmate.FileInf;
import com.joesmate.R;
import com.joesmate.SharedpreferencesData;
import com.joesmate.TouchView;
import com.joesmate.ZIP;
import com.joesmate.audio.AudioPlayer;
import com.joesmate.bin.OptionPlayData;
import com.joesmate.bin.UpdateApkData;
import com.joesmate.bin.icbc.ShowStatusAreaData;
import com.joesmate.bin.keyBoard.OptionResData;
import com.joesmate.bin.sdcs.SDCSPlayVoiceData;
import com.joesmate.pageItem.AppraiseCommon;
import com.joesmate.pageItem.AppraiseOver;
import com.joesmate.pageItem.ConfirmPDF;
import com.joesmate.pageItem.ICBCPage;
import com.joesmate.pageItem.ICBCPlayView;
import com.joesmate.pageItem.InputCharacter;
import com.joesmate.pageItem.InteractiveFile;
import com.joesmate.pageItem.InteractiveMsg;
import com.joesmate.pageItem.M3ShowText;
import com.joesmate.pageItem.PlayView;
import com.joesmate.pageItem.Questionnaire;
import com.joesmate.pageItem.Signature;
import com.joesmate.pageItem.WebApp;
import com.joesmate.pageItem.sdcs.ActiveSignQryInfoPage;
import com.joesmate.pageItem.sdcs.StartElectronicCardPage;
import com.joesmate.settings.DevSettingsView;
import com.joesmate.settings.KeyBoradSettingsView;
import com.joesmate.settings.ProgressbarView;

import java.io.File;
import java.util.Arrays;

/**
 * 呈现页面窗口,动态切换不同页面
 *
 * @author yc.zhang
 */
public class PlayActivity extends Activity implements OnSystemUiVisibilityChangeListener {
    //测试方法
    // am broadcast -a test --ei  type 1
    public static final String TAG = "PlayActivity";
    public static final byte[] PAGE_NULL = {0x00, 0x00};
    public static final byte[] PAGE_PLAY = {0x00, 0x01};
    public static final byte[] PAGE_SETTINGS_M3 = {0x00, 0x02};
    public static final byte[] PAGE_SETTINGS_A9 = {0x00, 0x03};
    public static final byte[] PAGE_APPRAISE_OVER = {0x00, 0x04};
    public static final byte[] PAGE_TEST = {0X22, 0X22};
    public static final byte[] PAGE_PROGRESS_BAR = {0x00, 0x05};


    public static int testtype = 0;
    private byte[] curPage;
    private LinearLayout rootView;
    private PlayView playView;
    private ICBCPlayView icbcplayView;
    private InteractiveMsg interactiveMsg;
    private StartElectronicCardPage startElectronicCardPage;
    private ActiveSignQryInfoPage activeSignQryInfoPage;
    private AppraiseCommon appraiseCommon;
    private InputCharacter inputCharacter;
    private Questionnaire questionnaire;
    private InteractiveFile interactiveFile;
    private ConfirmPDF confirmPDF;
    private Signature signature;
    private WebApp webApp;
    private AppraiseOver appraiseOver;
    private DevSettingsView devSettingsView;
    private M3ShowText m3ShowText;
    TouchView touchExit, touchM3Settings, touchA9Settings;
    KeyBoradSettingsView boradSettingsView;
    ProgressbarView progressBarView;

    boolean isRunPlayView = true;
    private LayoutTransition mLayoutTransition;

    private static int SYSTEM_UI_FLAG_SHOW_FULLSCREEN = 0x00000008;

    public static interface OnResUpdateListener {
        public void update();

        public void nothing();
    }

    @Override
    public void onSystemUiVisibilityChange(int arg0) {
        // TODO Auto-generated method stub
        if (arg0 != SYSTEM_UI_FLAG_SHOW_FULLSCREEN) {
            getWindow().getDecorView().setSystemUiVisibility(
                    SYSTEM_UI_FLAG_SHOW_FULLSCREEN);
        }
    }


    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.d(TAG, "onAttachedToWindow");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

/*		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);*/

        initScreenSize();
        App.getInstance().setNavigation(false);
        setContentView();
        AndroidBug5497Workaround.assistActivity(this);
        getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(this);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        registerReceiver();

        runnablePlayView.run();

        handler1.sendEmptyMessageDelayed(0, 3 * 1000);
        //测试html url encode and  decode
        String str = "中文 CPU123";
        Log.v(TAG, AssitTool.URLEncode(str));
        Log.v(TAG, AssitTool.URLDecode(AssitTool.URLEncode(str)));

        //ICBCPage icbcpage = new ICBCPage(getApplicationContext());
        //icbcpage.init(null,0,this);
        //rootView.removeAllViews();
        //rootView.addView(icbcpage);

		/*new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d(TAG,"------sign pdf------");
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
				Date curDate = new Date(System.currentTimeMillis());//获取当前时间
				String currentSystemTime = formatter.format(curDate);
				String InPdfFilePath = "/sdcard/test.pdf";
			    String	OutPdfFilePath = "/sdcard/test_sign_"+currentSystemTime+".pdf";
			    String InPicFilePath = "/sdcard/sign.png";
				HandWriteToPDF handWriteToPDF = new HandWriteToPDF(InPdfFilePath, OutPdfFilePath, InPicFilePath);
				handWriteToPDF.addText(1,100,100,100,100);
			}
		}).start();*/

/*		String filePath = FileInf.getFilePath(4);
		FileInf.FileType fileType1 = FileInf.getFileType(4);
		String queryFile = AssitTool.getQueryFile(filePath,fileType1);
		Log.d(TAG,"queryFile: "+  queryFile);*/

        //DeviceSettings.reboot();
	/*	try {
			Tool.execRootCommand("reboot");
		}
		catch (Exception e)
		{
			Log.d("bill","---------");
		}*/

    }

    public void initScreenSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        App.SCREEN_WIDTH = displayMetrics.widthPixels;
        App.SCREEN_HEIGHT = displayMetrics.heightPixels + 48;

        float density = displayMetrics.density;

        Log.d(TAG, "<====>initScreenSize<====>");
        Log.d(TAG, "SCREEN_WIDTH:" + App.SCREEN_WIDTH);
        Log.d(TAG, "SCREEN_HEIGHT:" + App.SCREEN_HEIGHT);
        Log.d(TAG, "density:" + density);

    }

    private void setContentView() {
        rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        if (App.APP_TYPE == 1) {
            rootView.setBackgroundResource(R.drawable.play_nothing_bg);
        }
        if (App.APP_TYPE == 2) {
            rootView.setBackgroundResource(R.drawable.play_nothing_bg);
        }
        setContentView(rootView);
        mLayoutTransition = new LayoutTransition();
        rootView.setLayoutTransition(mLayoutTransition);
        mLayoutTransition.setStagger(LayoutTransition.CHANGE_APPEARING, 30);
        mLayoutTransition.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30);
        mLayoutTransition.setDuration(300);
        customLayoutTransition();


        touchExit = new TouchView(0, 0,
                TouchView.SIZE, TouchView.SIZE, AppAction.ACTION_BROADCAST_EXIT);
		
		/*touchM3Settings = new TouchView(App.SCREEN_WIDTH - TouchView.SIZE, 0,
				TouchView.SIZE, TouchView.SIZE, AppAction.ACTION_BROADCAST_SETTINGS_M3);
		touchA9Settings = new TouchView(0, 0, 
				TouchView.SIZE, TouchView.SIZE, AppAction.ACTION_BROADCAST_SETTINGS_A9);*/

        sendMessage(PAGE_PLAY);
    }

    public void customLayoutTransition() {
        /**
         * Add Button
         * LayoutTransition.APPEARING
         * 增加一个Button时，设置该Button的动画效果
         */
        ObjectAnimator mAnimatorAppearing = ObjectAnimator.ofFloat(null, "rotationY", 90.0f, 0.0f)
                .setDuration(mLayoutTransition.getDuration(LayoutTransition.APPEARING));
        //为LayoutTransition设置动画及动画类型
        mLayoutTransition.setAnimator(LayoutTransition.APPEARING, mAnimatorAppearing);
        mAnimatorAppearing.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation);
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setRotationY(0.0f);
            }
        });

        /**
         * Delete Button
         * LayoutTransition.DISAPPEARING
         * 当删除一个Button时，设置该Button的动画效果
         */
        ObjectAnimator mObjectAnimatorDisAppearing = ObjectAnimator.ofFloat(null, "rotationX", 0.0f, 90.0f)
                .setDuration(mLayoutTransition.getDuration(LayoutTransition.DISAPPEARING));
        mLayoutTransition.setAnimator(LayoutTransition.DISAPPEARING, mObjectAnimatorDisAppearing);
        mObjectAnimatorDisAppearing.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // TODO Auto-generated method stub
                super.onAnimationEnd(animation);
                View view = (View) ((ObjectAnimator) animation).getTarget();
                view.setRotationX(0.0f);
            }
        });
    }

    @SuppressLint("ShowToast")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchExit.onTouch(event);
        //touchM3Settings.onTouch(event);
        //touchA9Settings.onTouch(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //Toast.makeText(PlayActivity.this, "x:"+event.getX()+" y:"+event.getY(), Toast.LENGTH_SHORT).show();
        }
        return super.onTouchEvent(event);
    }

    private void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppAction.ACTION_BROADCAST_CMD);
        filter.addAction(AppAction.ACTION_BROADCAST_EXIT);
        filter.addAction(AppAction.ACTION_CLOSE_DIALOG);
        filter.addAction(AppAction.ACTION_BROADCAST_SETTINGS_A9);
        filter.addAction(AppAction.ACTION_BROADCAST_SETTINGS_M3);
        filter.addAction(AppAction.ACTION_BROADCAST_SETTINGS_UPDATE);
        filter.addAction(AppAction.ACTION_BROADCAST_SHOW_PROGRESSBAR);
        filter.addAction(AppAction.ACTION_BROADCAST_CLOSE_PROGRESSBAR);
        filter.addAction(AppAction.ACTION_BROADCAST_CLEAR_SCREEN);
        filter.addAction(AppAction.ACTION_BROADCAST_SERIAL_INPUT);
        filter.addAction(AppAction.ACTION_BROADCAST_SERIAL_INPUT_AGAIN);
        filter.addAction(AppAction.ACTION_BROADCAST_SERIAL_CANCEL_INPUT);
        filter.addAction("test");
        registerReceiver(broadcastReceiver, filter);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.d(TAG, "receive action：" + action);

            if (AppAction.ACTION_BROADCAST_CMD.equals(action)) {
                final byte[] cmd = intent.getByteArrayExtra(AppAction.KEY_BROADCAST_CMD);
                Log.d(TAG, "receive cmd：" + cmd);
                sendMessage(cmd);
            } else if (AppAction.ACTION_BROADCAST_EXIT.equals(action)) {
                Log.d(TAG, "open settings");
                //if (Arrays.equals(curPage, PAGE_SETTINGS_A9)) {
                //UsbHid.closeHid();
                //Serial.close();
                //App.getInstance().setNavigation(true);
                Intent intent2 = new Intent();
                intent2.setClassName("com.android.settings", "com.android.settings.Settings");
                startActivity(intent2);
                //PlayActivity.this.finish();
                //Toast.makeText(getApplicationContext(), "exit", Toast.LENGTH_SHORT).show();
                //}
            } else if (AppAction.ACTION_CLOSE_DIALOG.equals(action)) {
                if (!AssitTool.isPlay()) {
                    return;
                }
                if (Arrays.equals(PAGE_NULL, curPage)) {
                    Log.d(TAG, "res update");
                    sendMessage(PAGE_PLAY);
                }
            } else if (AppAction.ACTION_BROADCAST_SETTINGS_M3.equals(action)) {
                if (Arrays.equals(curPage, PAGE_SETTINGS_M3)) {
                    sendMessage(PAGE_PLAY);
                } else {
                    sendMessage(PAGE_SETTINGS_M3);
                }
            } else if (AppAction.ACTION_BROADCAST_SETTINGS_A9.equals(action)) {
                if (Arrays.equals(curPage, PAGE_SETTINGS_A9)) {
                    sendMessage(PAGE_PLAY);
                } else {
                    sendMessage(PAGE_SETTINGS_A9);
                }
            } else if (AppAction.ACTION_BROADCAST_SERIAL_INPUT.equals(action)) {
                App.isThirdpartySerial = true;
                ICBCPage icbcpage = new ICBCPage(PlayActivity.this);
                icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_SERIAL_INPUT);
                rootView.removeAllViews();
                rootView.addView(icbcpage);
                App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));
            } else if (AppAction.ACTION_BROADCAST_SERIAL_INPUT_AGAIN.equals(action)) {
                App.isThirdpartySerial = true;
                ICBCPage icbcpage = new ICBCPage(PlayActivity.this);
                icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_SERIAL_INPUT_AGAIN);
                rootView.removeAllViews();
                rootView.addView(icbcpage);
                App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));
            } else if (AppAction.ACTION_BROADCAST_SERIAL_CANCEL_INPUT.equals(action)) {
                sendMessage(PAGE_PLAY);

            } else if ("test".equals(action)) {
                testtype = intent.getIntExtra("type", 0);
                sendMessage(PAGE_TEST);

            } else if (AppAction.ACTION_BROADCAST_SETTINGS_UPDATE.equals(action)) {
                int brigthness = intent.getIntExtra(AppAction.KEY_BROADCAST_SETTINGS, 0);
                int type = intent.getIntExtra(AppAction.KEY_BROADCAST_SETTINGS_TYPE, 0);
                if (type == 3) {
                    DeviceSettings.setAppWindowBrightness(PlayActivity.this, brigthness);
                    DeviceSettings.saveSystemBrightness(brigthness);
                }
            } else if (AppAction.ACTION_BROADCAST_SHOW_PROGRESSBAR.equals(action)) {
                sendMessage(PAGE_PROGRESS_BAR);
            } else if (AppAction.ACTION_BROADCAST_CLEAR_SCREEN.equals(action)) {
                rootView.removeAllViews();
                Log.d("bill", "change poster type ...............");
                PlayActivity.this.sendMessage(PAGE_PLAY);
                //PlayActivity.this.sendMessage(PAGE_NULL);
                Log.d("page", "----- cur page:" + curPage);
                curPage = PAGE_PLAY;
            } else if (AppAction.ACTION_BROADCAST_CLOSE_PROGRESSBAR.equals(action)) {
                sendMessage(PAGE_PLAY);

            }

        }
    };

    private void sendMessage(byte[] page) {
        Message message = new Message();
        message.obj = page;
        handlerPage.sendMessage(message);
    }

    private void displayPage(View view, LayoutParams params) {
        rootView.removeAllViews();
        rootView.addView(view, params);
    }

    private Handler handlerPage = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            byte[] page = (byte[]) msg.obj;
            if (page == null) {
                return;
            }
            Log.d(TAG, "===page：" + page[0] + "-" + page[1]);
            //Log.d("bill", "==curpage"+ curPage[0]+"-"+curPage[1]) ;
            if (Arrays.equals(Cmds.BACK_RC.getBytes(), page)) {
                return;
            }

            //icbc  SetPosterType
            if (Arrays.equals(Cmds.CMD_PT.getBytes(), page)) {
                if (curPage != null) {
                    Log.d("bill", "==curpage" + curPage[0] + "-" + curPage[1]);
                }
                if (Arrays.equals(PAGE_PLAY, curPage) || Arrays.equals(PAGE_NULL, curPage)) {
                    rootView.removeAllViews();
                    Log.d("bill", "change poster type ...............");
                    PlayActivity.this.sendMessage(PAGE_PLAY);
                    //PlayActivity.this.sendMessage(PAGE_NULL);
                    curPage = PAGE_PLAY;
                    Log.d("page", "----curpage:" + curPage);
                    return;
                }
            }

            if (!Arrays.equals(Cmds.CMD_SS.getBytes(), page) && !Arrays.equals(Cmds.CMD_CS.getBytes(), page)) {
                curPage = page;
                Log.d("page", "-----curpage:" + AssitTool.getString(curPage, AssitTool.UTF_8));
            }
            Log.d("page", "curpage:" + AssitTool.getString(curPage, AssitTool.UTF_8));
            if (Arrays.equals(PAGE_PLAY, page)) {
                Log.d(TAG, String.format("getPoster_type()=%d", SharedpreferencesData.getInstance().getPoster_type()));
                if (SharedpreferencesData.getInstance().getPoster_type() == 0) {
                    Log.d("bill", "Poster_type  0");
                    if (AssitTool.isImgCanPlay()) {
                        icbcplayView = new ICBCPlayView(PlayActivity.this, onResUpdateListener, AssitTool.getLayoutParams());
                        icbcplayView.play(SharedpreferencesData.getInstance().getShowTime());
                        displayPage(icbcplayView, AssitTool.getLayoutParams());

                    } else {
                        rootView.removeAllViews();
                        PlayActivity.this.sendMessage(PAGE_NULL);
                    }

                }

                if (SharedpreferencesData.getInstance().getPoster_type() == 1) {
                    Log.d("bill", "Poster_type  1");
                    if (AssitTool.isVideoCanPlay()) {
                        icbcplayView = new ICBCPlayView(PlayActivity.this, onResUpdateListener, AssitTool.getLayoutParams());
                        icbcplayView.play(SharedpreferencesData.getInstance().getShowTime());
                        displayPage(icbcplayView, AssitTool.getLayoutParams());

                    } else {
                        rootView.removeAllViews();
                        PlayActivity.this.sendMessage(PAGE_NULL);
                    }
                }
                if (SharedpreferencesData.getInstance().getPoster_type() == 2) {
 Log.d("bill", "Poster_type  2");
                    if (AssitTool.isImgCanPlay() && AssitTool.isVideoCanPlay()) {
                        icbcplayView = new ICBCPlayView(PlayActivity.this, onResUpdateListener, AssitTool.getLayoutParams());
                        icbcplayView.play(SharedpreferencesData.getInstance().getShowTime());
                        displayPage(icbcplayView, AssitTool.getLayoutParams());

                    } else {
                        rootView.removeAllViews();
                        PlayActivity.this.sendMessage(PAGE_NULL);
                    }
                }


            }
            if (Arrays.equals(Cmds.CMD_ZJ.getBytes(), page)) {
                if (AssitTool.isPlay()) {
                    if (playView == null) {
                        playView = new PlayView(PlayActivity.this, onResUpdateListener);
                    }
                    if (playView.optionRes(OptionResData.getInstance().getResName(),
                            OptionPlayData.getInstance().getPlayTime())) {
                        OptionResData.getInstance().backCode(BackCode.CODE_00);
                        displayPage(playView, playView.getLayoutParams());
                    } else {
                        //节目资源不存在
                        OptionResData.getInstance().backCode(BackCode.CODE_01);
                    }
                } else {
                    //节目资源不存在
                    OptionResData.getInstance().backCode(BackCode.CODE_01);
                    rootView.removeAllViews();
                    PlayActivity.this.sendMessage(PAGE_NULL);
                }
            }
            if (Arrays.equals(Cmds.CMD_ZJ_HID.getBytes(), page)) {
                int resName = OptionPlayData.getInstance().getResName();
                if (OptionPlayData.getInstance().getPlayType() == 1) {
                    if (AudioPlayer.getInstance(PlayActivity.this).play(resName < 10 ? "0" + resName : "" + resName)) {
                        OptionPlayData.getInstance().backCode(BackCode.CODE_00);
                    } else {
                        OptionPlayData.getInstance().backCode(BackCode.CODE_01);
                    }
                } else if (OptionPlayData.getInstance().getPlayType() == 2) {

                    if (AssitTool.isPlay()) {
                        if (playView == null) {
                            playView = new PlayView(PlayActivity.this, onResUpdateListener);
                        }
                        if (playView.optionRes(resName, OptionPlayData.getInstance().getPlayTime())) {
                            OptionPlayData.getInstance().backCode(BackCode.CODE_00);
                            displayPage(playView, playView.getLayoutParams());
                        } else {
                            //节目资源不存在
                            OptionPlayData.getInstance().backCode(BackCode.CODE_01);
                        }
                    } else {
                        //节目资源不存在
                        OptionPlayData.getInstance().backCode(BackCode.CODE_01);
                        rootView.removeAllViews();
                        PlayActivity.this.sendMessage(PAGE_NULL);
                    }
                }

            } else if (Arrays.equals(PAGE_SETTINGS_M3, page)) {
                if (boradSettingsView == null) {
                    boradSettingsView = new KeyBoradSettingsView(getApplicationContext());
                }
                boradSettingsView.init();
                displayPage(boradSettingsView, AssitTool.getLinearParams(App.SCREEN_WIDTH, App.SCREEN_HEIGHT));
            } else if (Arrays.equals(PAGE_SETTINGS_A9, page)) {
                if (devSettingsView == null) {
                    devSettingsView = new DevSettingsView(PlayActivity.this);
                }
                devSettingsView.init();
                displayPage(devSettingsView, AssitTool.getLinearParams(App.SCREEN_WIDTH, App.SCREEN_HEIGHT));
            } else if (Arrays.equals(PAGE_NULL, page)) {
                //PlayActivity.this.sendMessage(Cmds.CMD_JH);
            } else if (Arrays.equals(PAGE_TEST, page)) {
/*				ICBCPage icbcpage = new ICBCPage(getApplicationContext());
				icbcpage.init(PlayActivity.this,AssitTool.PAGE_VIEW_INPUTPASSWORD);
				rootView.removeAllViews();
				rootView.addView(icbcpage);*/

                PlayActivity.this.sendMessage(Cmds.CMD_SC.getBytes());

            } else if (Arrays.equals(Cmds.CMD_SS.getBytes(), page)) {
                Log.d("page", "curPage:" + AssitTool.getString(curPage, AssitTool.UTF_8));
                if ((Arrays.equals(Cmds.CMD_SG.getBytes(), curPage) || Arrays.equals(Cmds.CMD_SI.getBytes(), curPage) || Arrays.equals(Cmds.CMD_RP.getBytes(), curPage) || Arrays.equals(Cmds.CMD_IP.getBytes(), curPage))) {
                    Log.d("page", "have toptitle,  update toptile  ");
                    Intent intent = new Intent(AppAction.ACTION_BROADCAST_SET_TOPTITLE);
                    //ShowStatusAreaData.getInstance().setNewToptitle(");
                    intent.putExtra("newtitle", ShowStatusAreaData.getInstance().getNewToptitle());
                    sendBroadcast(intent);


                } else {
                    Log.d("page", " no toptitle, add ad and  toptile  ");
                    ICBCPage icbcpage = new ICBCPage(PlayActivity.this);
                    icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_STATUSAREA);
                    rootView.removeAllViews();
                    rootView.addView(icbcpage);
                    App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));
                }


                curPage = page;

            } else if (Arrays.equals(Cmds.CMD_IP.getBytes(), page)) {
                curPage = Cmds.CMD_IP.getBytes();
                ICBCPage icbcpage = new ICBCPage(PlayActivity.this);
                icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_INPUTPASSWORD);
                rootView.removeAllViews();
                rootView.addView(icbcpage);
                App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));

            } else if (Arrays.equals(Cmds.CMD_RP.getBytes(), page)) {
                curPage = Cmds.CMD_RP.getBytes();
                ICBCPage icbcpage = new ICBCPage(PlayActivity.this);
                icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_ReadPin);
                rootView.removeAllViews();
                rootView.addView(icbcpage);
                App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));

            } else if (Arrays.equals(Cmds.CMD_PV.getBytes(), page)) {
                PlayActivity.this.sendMessage(PAGE_PLAY);
                App.getInstance().tts.Read(SDCSPlayVoiceData.getInstance().getVoiceText(), 1);
            } else if (Arrays.equals(CMD.JH, page)) {
                if (interactiveMsg == null) {
                    interactiveMsg = new InteractiveMsg(PlayActivity.this);
                }
                interactiveMsg.init();
                displayPage(interactiveMsg, interactiveMsg.getFillParams());
            } else if (Arrays.equals(Cmds.CMD_SC.getBytes(), page)) {
                if (startElectronicCardPage == null) {
                    startElectronicCardPage = new StartElectronicCardPage(PlayActivity.this);
                }
                startElectronicCardPage.init();
                displayPage(startElectronicCardPage, startElectronicCardPage.getFillParams());
            } else if (Arrays.equals(Cmds.CMD_QI.getBytes(), page)) {
                if (activeSignQryInfoPage == null) {
                    activeSignQryInfoPage = new ActiveSignQryInfoPage(PlayActivity.this);
                }
                activeSignQryInfoPage.init();
                displayPage(activeSignQryInfoPage, activeSignQryInfoPage.getFillParams());
            } else if (Arrays.equals(CMD.JF, page)) {
                if (interactiveFile == null) {
                    interactiveFile = new InteractiveFile(PlayActivity.this);
                }
                interactiveFile.init();
                displayPage(interactiveFile, interactiveFile.getFillParams());

            } else if (Arrays.equals(Cmds.CMD_CP.getBytes(), page)) {
                if (confirmPDF == null) {
                    confirmPDF = new ConfirmPDF(PlayActivity.this);
                }
                confirmPDF.init();
                displayPage(confirmPDF, confirmPDF.getFillParams());

            } else if (Arrays.equals(CMD.QS, page)) {
			/*	if(questionnaire == null){
					questionnaire = new Questionnaire(getApplicationContext());
				}
				questionnaire.init();
				displayPage(questionnaire,questionnaire.getFillParams());*/
            } else if (Arrays.equals(Cmds.CMD_OQ.getBytes(), page)) {
                if (questionnaire == null) {
                    questionnaire = new Questionnaire(PlayActivity.this);
                }
                questionnaire.init();
                displayPage(questionnaire, questionnaire.getFillParams());
            } else if (Arrays.equals(CMD.PJ, page) || Arrays.equals(CMD.PH, page)) {
                if (appraiseCommon == null) {
                    appraiseCommon = new AppraiseCommon(PlayActivity.this);
                }
                appraiseCommon.init();
                displayPage(appraiseCommon, appraiseCommon.getFillParams());
            } else if (Arrays.equals(Cmds.CMD_SE.getBytes(), page)) {
                if (appraiseCommon == null) {
                    appraiseCommon = new AppraiseCommon(PlayActivity.this);
                }
                appraiseCommon.init();
                displayPage(appraiseCommon, appraiseCommon.getFillParams());
            } else if (Arrays.equals(Cmds.CMD_IN.getBytes(), page)) {
                if (inputCharacter == null) {
                    inputCharacter = new InputCharacter(PlayActivity.this);
                }
                inputCharacter.init();
                displayPage(inputCharacter, inputCharacter.getFillParams());

            } else if (Arrays.equals(PAGE_PROGRESS_BAR, page)) {
                if (progressBarView == null) {
                    progressBarView = new ProgressbarView(PlayActivity.this);
                }
                progressBarView.init();
                displayPage(progressBarView, AssitTool.getLinearParams(App.SCREEN_WIDTH, App.SCREEN_HEIGHT));
            } else if (Arrays.equals(CMD.SG, page)) {
                curPage = Cmds.CMD_SG.getBytes();
                ICBCPage icbcpage = new ICBCPage(PlayActivity.this);
                icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_PDF);
                rootView.removeAllViews();
                rootView.addView(icbcpage);
                App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));

            } else if (Arrays.equals(CMD.UV, page)) {
                File dir = new File(FileInf.APK);
                if (dir == null || !dir.exists()) {
                    Log.d(TAG, "unexists --" + FileInf.APK);
                    return;
                }
                File file = new File(dir, FileInf.APK_NAME);
                if (file == null || !file.exists()) {
                    Log.d(TAG, "unexists --" + FileInf.APK_NAME);
                    return;
                }
                if (UpdateApkData.getInstance().isUpdateApk()) {
                    AssitTool.installApk(file);
                } else {
                    Log.w(TAG, "update apk erro");
                }
            } else if (Arrays.equals(Cmds.CMD_HH.getBytes(), page) ||
                    Arrays.equals(Cmds.CMD_MM.getBytes(), page)) {
                if (webApp == null) {
                    webApp = new WebApp(PlayActivity.this);
                }
                webApp.init();
                displayPage(webApp, webApp.getFillParams());
            } else if (Arrays.equals(PAGE_APPRAISE_OVER, page)) {
                if (appraiseOver == null) {
                    appraiseOver = new AppraiseOver(PlayActivity.this);
                }
                appraiseOver.init();
                displayPage(appraiseOver, appraiseOver.getFillParams());
            } else if (Arrays.equals(Cmds.CMD_SI.getBytes(), page)) {
				/*curPage = Cmds.CMD_SI.getBytes();
				ICBCPage icbcpage = new ICBCPage(getApplicationContext());
				icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_HTML);
				rootView.removeAllViews();
				rootView.addView(icbcpage);
				App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));*/

                curPage = Cmds.CMD_SI.getBytes();
                ICBCPage icbcpage = new ICBCPage(PlayActivity.this);
                icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_SDCS_SHOW_HTML);
                rootView.removeAllViews();
                rootView.addView(icbcpage);
                App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));
            } else if (Arrays.equals(Cmds.CMD_OR.getBytes(), page)) {
                curPage = Cmds.CMD_OR.getBytes();
                ICBCPage icbcpage = new ICBCPage(PlayActivity.this);
                icbcpage.init(PlayActivity.this, AssitTool.PAGE_VIEW_SDCS_GetUserOperateRstHtml);
                rootView.removeAllViews();
                rootView.addView(icbcpage);
                App.getInstance().sendBroadcast(new Intent(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE));
            } else if (Arrays.equals(Cmds.CMD_CS.getBytes(), page)) {
                Log.d("page", "curPage:" + AssitTool.getString(curPage, AssitTool.UTF_8));
                if (Arrays.equals(PAGE_PLAY, curPage)) {
                    Log.d("bill", " return ...............");
                    return;
                }
                rootView.removeAllViews();
                Log.d("bill", "change poster type ...............");
                PlayActivity.this.sendMessage(PAGE_PLAY);
                //PlayActivity.this.sendMessage(PAGE_NULL);
                Log.d("page", "----- cur page:" + curPage);
                curPage = PAGE_PLAY;
                return;
            } else if (Arrays.equals(Cmds.CMD_DD.getBytes(), page) ||
                    Arrays.equals(Cmds.CMD_XS.getBytes(), page)) {
                if (m3ShowText == null) {
                    m3ShowText = new M3ShowText(PlayActivity.this);
                }
                m3ShowText.init();
                displayPage(m3ShowText, m3ShowText.getFillParams());
            } else if (Arrays.equals(Cmds.CMD_CD.getBytes(), page)) {
                PlayActivity.this.sendMessage(PAGE_PLAY);
            }
        }

    };
    /**
     * 轮询素材是否可播放
     * TODO:轮询效果还可以，文件监听FileObserver暂时不考虑
     */
    Runnable runnablePlayView = new Runnable() {
        Handler handler = new Handler();

        @Override
        public void run() {
            if (!isRunPlayView) {
                return;
            }
            onResUpdateListener.update();
            handler.postDelayed(runnablePlayView, 30 * 1000);
        }
    };


    Runnable runnableunzip = new Runnable() {
        @Override
        public void run() {
            try {
                ZIP.UnZipFolder("/sdcard/sbtp_4_gr.zip", "/sdcard/sbtp_4_gr");
            } catch (Exception e) {
                Log.d(TAG, "unzip have  a  error");
            }
        }
    };


    Handler handler1 = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            App.getInstance().setNavigation(false);
        }

    };

    public OnResUpdateListener onResUpdateListener = new OnResUpdateListener() {

        @Override
        public synchronized void update() {
            if (!AssitTool.isPlay()) {
                return;
            }
            if (Arrays.equals(PAGE_NULL, curPage)) {
                Log.d(TAG, "res update");
                sendMessage(PAGE_PLAY);
            }
        }

        @Override
        public void nothing() {
            sendMessage(PAGE_NULL);
        }
    };

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        //AndroidBug5497Workaround.getInstance(PlayActivity.this).setListener();
        sendBroadcast(new Intent("com.inhuasoft.systemui_hide"));
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onStop() {
        //AndroidBug5497Workaround.getInstance(PlayActivity.this).removeListener();
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
        isRunPlayView = false;
        Log.d(TAG, "onDestroy");
    }

}
