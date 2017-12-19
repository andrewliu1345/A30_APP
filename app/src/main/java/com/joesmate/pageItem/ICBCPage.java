package com.joesmate.pageItem;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.artifex.mupdfdemo.MuPDFCore;
import com.artifex.mupdfdemo.MuPDFPageAdapter;
import com.artifex.mupdfdemo.MuPDFReaderView;
import com.joesmate.AndroidBug5497Workaround;
import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.AssitTool;
import com.joesmate.Cmds;
import com.joesmate.FileInf;
import com.joesmate.R;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.GetSignPDFStateData;
import com.joesmate.bin.InputPw;
import com.joesmate.bin.icbc.ICBCAddWebFileData;
import com.joesmate.bin.icbc.ICBCSignData;
import com.joesmate.bin.icbc.ResposeICBCSignatureData;
import com.joesmate.bin.icbc.ShowStatusAreaData;
import com.joesmate.bin.sdcs.GetUserOperateRstHtmlData;
import com.joesmate.bin.sdcs.SDCSConfirmPDF;
import com.joesmate.bin.sdcs.SDCSReadPinData;
import com.joesmate.bin.sdcs.StartInfoHtmlData;
import com.joesmate.listener.OnJsListener;
import com.joesmate.page.PlayActivity;
import com.joesmate.pdf.HandWriteToPDF;
import com.joesmate.widget.CustomText;
import com.joesmate.widget.HtmlView;
import com.joesmate.widget.ICBCSignatureFrame;
import com.joesmate.widget.InputPassWord;
import com.joesmate.widget.SerailSetPassWord;
import com.joesmate.widget.SetPassWord;
import com.joesmate.widget.TimerView;
import com.joesmate.widget.TimerView.OnTimerListener;
import com.lowagie.text.pdf.PdfReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import repack.org.bouncycastle.asn1.cms.SignedData;

/**
 * Created by bill on 2016/1/11.
 */
public class ICBCPage extends LinearLayout implements OnTimerListener, OnJsListener {

    public static final String TAG = "ICBCPage";
    int lastX, lastY, screenHeight, screenWidth;
    CustomText topTitle;
    TimerView timerView;
    HtmlView htmlView;
    ICBCPlayView icbcPlayView;
    LinearLayout pageContent, btContainer;
    Button btSignature, btOK, btCancel;
    Context context;
    ICBCSignatureFrame signatureFrame;

    float signPageHeight, signPageWidth;
    public MuPDFReaderView muPDFReaderView;
    public MuPDFCore muPDFCore;
    int mPageType;
    TextView txtPageNumber;
    boolean exitPdf = false;
    int[] top_colors = {Color.BLACK, Color.WHITE, Color.RED, Color.rgb(250, 128, 10), Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.rgb(221, 12, 240)};

    public ICBCPage(Context context) {
        super(context);
        Log.d(TAG, "onCreate ICBCPage");
        this.context = context;
        inflate(context, R.layout.icbc_page_item, this);
        signatureFrame = (ICBCSignatureFrame) findViewById(R.id.page_item_signature_farme);
        signatureFrame.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        int top = v.getTop() + dy;
                        int left = v.getLeft() + dx;
                        if (top <= 0) {
                            top = 0;
                        }
                        if (top >= screenHeight - signatureFrame.getHeight()) {
                            top = screenHeight - signatureFrame.getHeight();
                        }
                        if (left >= screenWidth - signatureFrame.getWidth()) {
                            left = screenWidth - signatureFrame.getWidth();
                        }
                        if (left <= 0) {
                            left = 0;
                        }
                        v.layout(left, top, left + signatureFrame.getWidth(), top + signatureFrame.getHeight());
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();

                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return false;
            }
        });
        Display dis = ((Activity) context).getWindowManager().getDefaultDisplay();
        screenWidth = dis.getWidth();
        screenHeight = dis.getHeight();
        onFinishInflate();

    }


    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE)) {
                //高度
                //topTitle.setHeight(SharedpreferencesData.getInstance().getTop_height());
                LayoutParams params = (LayoutParams) topTitle.getLayoutParams();
                params.weight = App.SCREEN_WIDTH;
                params.height = SharedpreferencesData.getInstance().getTop_height();
                Log.d(TAG, "--------------top height:" + SharedpreferencesData.getInstance().getTop_height());
                topTitle.setLayoutParams(params);

                LayoutParams params1 = (LayoutParams) pageContent.getLayoutParams();
                params1.weight = App.SCREEN_WIDTH;
                params1.height = App.SCREEN_HEIGHT - SharedpreferencesData.getInstance().getTop_height();
                pageContent.setLayoutParams(params1);

                pageContent.getParent().requestLayout();

            } else if (intent.getAction().equals(AppAction.ACTION_BROADCAST_MAKE_SIGN_DATA)) {
                if (signatureFrame != null) {
                    if (signatureFrame.getVisibility() == VISIBLE) {
                        makeSignData();
                        Log.d("myitm", "send data");
                        App.getInstance().fitManagerCCB.getBaseFitBin().setData(Cmds.CMD_RG.getBytes(),
                                Cmds.CMD_RG.getBytes().length);
                    }
                } else {
                    GetSignPDFStateData.getInstance().SendBackCode(0);

                }
            }
        }
    };


    //page type
    // 0  html 状态栏
    // 1  1/3 html  2/3  告
    // 2  html 状态栏  等待反馈  倒计时
    // 3  html 状态栏  电子签名
    // 4  确认pdf 签名
    public void init(PlayActivity playActivity, int pagetype) {
        /*if (baseData == null) {
            return;
        }*/

        IntentFilter filter = new IntentFilter();
        filter.addAction(AppAction.ACTION_BROADCAST_STATUS_AREA_STYLE);
        //filter.addAction(AppAction.ACTION_BROADCAST_MAKE_SIGN_DATA);
        context.registerReceiver(receiver, filter);


        mPageType = pagetype;
        if (pagetype == AssitTool.PAGE_VIEW_HTML) {
            ICBCAddWebFileData baseData = ICBCAddWebFileData.getInstance();
            if (baseData.getDisplayType() == 0) {
                timerView.setVisibility(GONE);
                btSignature.setVisibility(GONE);
                int statusheight = 0;
                if ("".equals(baseData.getStatusContent()) || baseData.getStatusContent() == null) {
                    topTitle.setVisibility(GONE);
                } else {
                    statusheight = SharedpreferencesData.getInstance().getTop_height();
                    topTitle.setVisibility(VISIBLE);
                }
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight);
                pageContent.setLayoutParams(params);
                htmlView = new HtmlView(context);
                //htmlView.setBackgroundColor(Color.GREEN);
                LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                pageContent.addView(htmlView, params1);
                htmlView.loadFile(baseData.getFilePath());
                topTitle.setText(baseData.getStatusContent());
                App.getInstance().tts.Read(baseData.getVoiceText(), 1);

            }

            if (baseData.getDisplayType() == 1) {
                topTitle.setVisibility(GONE);
                timerView.setVisibility(GONE);
                btSignature.setVisibility(GONE);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT);
                pageContent.setLayoutParams(params);
                htmlView = new HtmlView(context);
                //htmlView.setBackgroundColor(Color.GREEN);
                LayoutParams params1 = new LayoutParams(App.SCREEN_WIDTH / 3, App.SCREEN_HEIGHT);
                pageContent.addView(htmlView, params1);
                htmlView.loadFile(baseData.getFilePath());


                LayoutParams params2 = new LayoutParams((App.SCREEN_WIDTH / 3) * 2 + 5, App.SCREEN_HEIGHT);
                icbcPlayView = new ICBCPlayView(context, playActivity.onResUpdateListener, params2);
                //icbcPlayView.setBackgroundColor(Color.BLACK);
                icbcPlayView.play(SharedpreferencesData.getInstance().getShowTime());
                pageContent.addView(icbcPlayView, params2);
                App.getInstance().tts.Read(baseData.getVoiceText(), 1);
            }
            //带timeout 和 js 交互
            if (baseData.getDisplayType() == 2) {
                topTitle.setVisibility(VISIBLE);
                timerView.setVisibility(VISIBLE);
                btSignature.setVisibility(GONE);

                int statusheight = 0;
                if ("".equals(baseData.getStatusContent()) || baseData.getStatusContent() == null) {
                    topTitle.setVisibility(GONE);
                } else {
                    statusheight = SharedpreferencesData.getInstance().getTop_height();
                    topTitle.setVisibility(VISIBLE);
                }
                // bottom button layout 50dp
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight - 50);

                //LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - SharedpreferencesData.getInstance().getTop_height() );
                //LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT);
                pageContent.setLayoutParams(params);
                htmlView = new HtmlView(context);
                //htmlView.setBackgroundColor(Color.GREEN);
                LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                pageContent.addView(htmlView, params1);
                htmlView.loadFile(baseData.getFilePath());
                topTitle.setText(baseData.getStatusContent());
                htmlView.setOnJsListener(this);
                timerView.setOnTimerListener(new OnTimerListener() {
                    @Override
                    public void timeOut() {
                        htmlView.GetReturnData();
                    }
                });
                timerView.setTime(baseData.getTimeOut());
                App.getInstance().tts.Read(baseData.getVoiceText(), 1);
            }
        }

        /*******
         *
         *
         * 交互信息  不需要确认和不需要确认
         *
         *
         *
         * *****/
        if (pagetype == AssitTool.PAGE_VIEW_SDCS_SHOW_HTML) {
            final StartInfoHtmlData baseData = StartInfoHtmlData.getInstance();
            timerView.setTime(baseData.getTimeOut());
            // timerView.setTime(20);
            topTitle.setVisibility(VISIBLE);
            btSignature.setVisibility(GONE);
            btContainer.setVisibility(VISIBLE);
            //需要确认
            if (baseData.getDisplayType() == 1) {
                btOK.setVisibility(VISIBLE);
                btCancel.setVisibility(VISIBLE);
            } else if (baseData.getDisplayType()==0){//不需要确认
                btOK.setVisibility(GONE);
                btCancel.setVisibility(GONE);

                btSignature.setVisibility(VISIBLE);
                btSignature.setText("返回");
                btSignature.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        baseData.sendConfirmResult("0");
                        toPlay();
                    }
                });
            }else if (baseData.getDisplayType()==2)
            {
                btOK.setVisibility(GONE);
                btCancel.setVisibility(GONE);

                btSignature.setVisibility(VISIBLE);
                btSignature.setText("返回");
                btSignature.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        baseData.sendConfirmResult("0");
                        toPlay();
                    }
                });
                baseData.sendConfirmResult("0");
            }
            btOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseData.sendConfirmResult("0");
                    toPlay();
                }
            });
            btCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseData.sendConfirmResult("1");
                    toPlay();
                }
            });
            timerView.setOnTimerListener(new OnTimerListener() {
                @Override
                public void timeOut() {
                    if (baseData.getDisplayType() == 1) {
                        baseData.sendConfirmResult("2");
                        toPlay();
                    } else {
                        baseData.sendConfirmResult("2");
                        toPlay();
                    }
                }
            });
            int statusheight = SharedpreferencesData.getInstance().getTop_height();
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight - 70);
            SharedpreferencesData.getInstance().setTop_bgcolor(7);
            SharedpreferencesData.getInstance().setTop_font_size(32);
            SharedpreferencesData.getInstance().setTop_font_weight(1);
            SharedpreferencesData.getInstance().setTop_font_color(1);
            topTitle.setText("  请确认信息");
            topTitle.setGravity(Gravity.CENTER);
            pageContent.setLayoutParams(params);
            htmlView = new HtmlView(context);
            //htmlView.setBackgroundColor(Color.GREEN);
            LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pageContent.addView(htmlView, params1);
            //htmlView.loadChar("<b>dshhsjj</b>djdj99399999");
            htmlView.loadChar(baseData.getHtmlData());
            App.getInstance().tts.Read(baseData.getVoiceText(), 1);

        }
        if (pagetype == AssitTool.PAGE_VIEW_SDCS_GetUserOperateRstHtml) {
            final GetUserOperateRstHtmlData baseData = GetUserOperateRstHtmlData.getInstance();
            timerView.setTime(baseData.getTimeOut());
            // timerView.setTime(20);
            topTitle.setVisibility(GONE);
            btSignature.setVisibility(GONE);
            btContainer.setVisibility(VISIBLE);

            btOK.setVisibility(VISIBLE);
            btCancel.setVisibility(VISIBLE);

            btOK.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    htmlView.ExecuteJsFunction("check_sub");
                    forceHideSoftKeyboard();
                }
            });
            btCancel.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    baseData.sendConfirmResult("1", "");
                    forceHideSoftKeyboard();
                    toPlay();
                }
            });
            timerView.setOnTimerListener(new OnTimerListener() {
                @Override
                public void timeOut() {
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(pageContent.getWindowToken(), 0);
                    baseData.sendConfirmResult("2", "");
                    forceHideSoftKeyboard();
                    toPlay();
                }
            });
            int statusheight = SharedpreferencesData.getInstance().getTop_height();
            //LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight-70);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            SharedpreferencesData.getInstance().setTop_bgcolor(7);
            SharedpreferencesData.getInstance().setTop_font_size(32);
            SharedpreferencesData.getInstance().setTop_font_weight(1);
            SharedpreferencesData.getInstance().setTop_font_color(1);
            // topTitle.setText("问卷调查");
            // topTitle.setGravity(Gravity.CENTER);
            pageContent.setLayoutParams(params);
            htmlView = new HtmlView(context);
            htmlView.setOnJsListener(new OnJsListener() {
                @Override
                public void submit(String str) {
                    baseData.sendConfirmResult("0", str);
                    toPlay();
                }
            });
            //LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  App.SCREEN_HEIGHT - statusheight-70-100);
            LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            pageContent.addView(htmlView, params1);
            htmlView.loadChar(baseData.getDisplayContent() + "<br/>\n" +
                    "<br/>\n" +
                    "<br/>");
            AndroidBug5497Workaround.assistActivity(playActivity);
            App.getInstance().tts.Read(baseData.getVoiceText(), 1);

        }


        if (pagetype == AssitTool.PAGE_VIEW_INPUTPASSWORD) {
            topTitle.setVisibility(VISIBLE);
            timerView.setVisibility(VISIBLE);
            btSignature.setVisibility(GONE);
            timerView.setOnTimerListener(this);
            timerView.setTime(InputPw.getInstance().getTimeOut());

            int statusheight = 0;
            if ("".equals(InputPw.getInstance().getStatusContent()) || InputPw.getInstance().getStatusContent() == null) {
                topTitle.setVisibility(GONE);
            } else {
                statusheight = SharedpreferencesData.getInstance().getTop_height();
                topTitle.setVisibility(VISIBLE);
            }
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight);

            //LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - SharedpreferencesData.getInstance().getTop_height()-50);
            pageContent.setLayoutParams(params);

            //htmlView = new HtmlView(context);
            //htmlView.setBackgroundColor(Color.GREEN);
            InputPassWord inputPassWordView = new InputPassWord(context);
            LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            //LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - SharedpreferencesData.getInstance().getTop_height()-50);
            pageContent.addView(inputPassWordView, params1);

            topTitle.setText(InputPw.getInstance().getStatusContent());
            App.getInstance().tts.Read(InputPw.getInstance().getVoiceText(), 1);

        }


        if (pagetype == AssitTool.PAGE_VIEW_ReadPin) {
            final SDCSReadPinData baseData = SDCSReadPinData.getInstance();
            timerView.setTime(baseData.getTimeOut());
            btContainer.setVisibility(GONE);
            topTitle.setVisibility(GONE);
            timerView.setVisibility(VISIBLE);
            btSignature.setVisibility(GONE);


            SharedpreferencesData.getInstance().setTop_bgcolor(7);
            SharedpreferencesData.getInstance().setTop_font_size(32);
            SharedpreferencesData.getInstance().setTop_font_weight(1);
            SharedpreferencesData.getInstance().setTop_font_color(1);

            int statusheight = 0;
            // statusheight = SharedpreferencesData.getInstance().getTop_height();
            topTitle.setVisibility(VISIBLE);

            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight);

            //LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - SharedpreferencesData.getInstance().getTop_height()-50);
            pageContent.setLayoutParams(params);

            //htmlView = new HtmlView(context);
            //htmlView.setBackgroundColor(Color.GREEN);
            SetPassWord setPassWordView = new SetPassWord(context);
            timerView.setOnTimerListener(setPassWordView);
            LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pageContent.addView(setPassWordView, params1);

            //topTitle.setText(baseData.getDisplayContent());
            App.getInstance().tts.Read(baseData.getVoiceText(), 1);

        }


        if (pagetype == AssitTool.PAGE_VIEW_SERIAL_INPUT || pagetype == AssitTool.PAGE_VIEW_SERIAL_INPUT_AGAIN) {
            //timerView.setTime(20);
            btContainer.setVisibility(GONE);
            topTitle.setVisibility(GONE);
            timerView.setVisibility(GONE);
            btSignature.setVisibility(GONE);


            SharedpreferencesData.getInstance().setTop_bgcolor(7);
            SharedpreferencesData.getInstance().setTop_font_size(32);
            SharedpreferencesData.getInstance().setTop_font_weight(1);
            SharedpreferencesData.getInstance().setTop_font_color(1);
            SharedpreferencesData.getInstance().setTop_height(80);

            int statusheight = 0;
            statusheight = SharedpreferencesData.getInstance().getTop_height();
            topTitle.setVisibility(VISIBLE);
            topTitle.setGravity(Gravity.CENTER);
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight);
            pageContent.setLayoutParams(params);
            String str = "";
            if (pagetype == AssitTool.PAGE_VIEW_SERIAL_INPUT) {
                str = "请输入密码";

            }

            if (pagetype == AssitTool.PAGE_VIEW_SERIAL_INPUT_AGAIN) {
                str = "请再次输入密码";
            }
            SerailSetPassWord setPassWordView = new SerailSetPassWord(context, str);
            topTitle.setText(str);
            LayoutParams params1 = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            pageContent.addView(setPassWordView, params1);
            App.getInstance().tts.Read(str, 1);

        }

        if (pagetype == AssitTool.PAGE_VIEW_STATUSAREA) {
            topTitle.setVisibility(VISIBLE);
            timerView.setVisibility(GONE);
            btSignature.setVisibility(GONE);

            int statusheight = 0;
            if ("".equals(ShowStatusAreaData.getInstance().getNewToptitle()) || ShowStatusAreaData.getInstance().getNewToptitle() == null) {
                topTitle.setVisibility(GONE);
            } else {
                statusheight = SharedpreferencesData.getInstance().getTop_height();
                topTitle.setVisibility(VISIBLE);
            }

            LayoutParams params = new LayoutParams(App.SCREEN_WIDTH, App.SCREEN_HEIGHT - statusheight);
            pageContent.setLayoutParams(params);


            LayoutParams params1 = new LayoutParams(App.SCREEN_WIDTH, ViewGroup.LayoutParams.MATCH_PARENT);
            icbcPlayView = new ICBCPlayView(context, playActivity.onResUpdateListener, params1);
            //icbcPlayView.setBackgroundColor(Color.BLACK);
            icbcPlayView.play(SharedpreferencesData.getInstance().getShowTime());
            pageContent.addView(icbcPlayView, params1);

            // ShowStatusAreaData.getInstance().setNewToptitle("cececececee");
            // ShowStatusAreaData.getInstance().setVoiceText("123456");

            topTitle.setText(ShowStatusAreaData.getInstance().getNewToptitle());
            App.getInstance().tts.Read(ShowStatusAreaData.getInstance().getVoiceText(), 1);
        }

        if (pagetype == AssitTool.PAGE_VIEW_PDF) {

            final ICBCSignData baseData = ICBCSignData.getInstance();
            //显示pdf 文档，不需要签名
            if (baseData.getSignType() == AssitTool.PAGE_PDF_NOSIGN) {
                mPageType = AssitTool.PAGE_PDF_NOSIGN;
                btContainer.setVisibility(GONE);
                topTitle.setVisibility(GONE);
                timerView.setVisibility(VISIBLE);
                //btSignature.setVisibility(INVISIBLE);
                exitPdf = true;
                btSignature.setVisibility(VISIBLE);
                btSignature.setText(baseData.getStatusContent());
                signatureFrame.setVisibility(INVISIBLE);
                timerView.setOnTimerListener(new OnTimerListener() {
                    @Override
                    public void timeOut() {
                        toPlay();
                    }
                });
                timerView.setTime(baseData.getTimeOut());
                topTitle.setText(baseData.getTitleA());
                txtPageNumber.setVisibility(VISIBLE);
                btSignature.setVisibility(GONE);
                btSignature.setText("取消");
//                btSignature.setOnClickListener(new OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        App.getInstance().fitManagerCCB.getBaseFitBin().backData(Cmds.CMD_CL.getBytes());
//                        toPlay();
//                    }
//                });

                int statusheight = 0;
                topTitle.setVisibility(GONE);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight);
                pageContent.setLayoutParams(params);

                // add pdf
                try {
                    muPDFCore = new MuPDFCore(getContext(),
                            readFileSdcardFile(baseData.getPDFPath()), "");

                    muPDFReaderView = new MuPDFReaderView(playActivity) {
                        @Override
                        protected void onMoveToChild(int i) {
                            super.onMoveToChild(i);
                            txtPageNumber.setText(String.format("%d / %d", i + 1,
                                    muPDFCore.countPages()));

                        }
                    };
                    muPDFReaderView.setAdapter(new MuPDFPageAdapter(context,
                            null, muPDFCore));
                    muPDFReaderView.setDisplayedViewIndex(0);
                    float scale = muPDFCore.getPageSize(0).y / App.SCREEN_HEIGHT;
                    //muPDFReaderView.SetViewScale(scale);
                    new Handler().postDelayed(muPDFReaderView
                            , 1200);
                    pageContent.removeAllViews();
                    ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

                    pageContent.addView(muPDFReaderView, params2);
                    Log.d(TAG, "---------------" + muPDFCore.getPageSize(1).toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                App.getInstance().tts.Read(baseData.getVoiceText(), 1);


            }
            ////显示pdf的电子签名,第一次签名
            if (baseData.getSignType() == AssitTool.PAGE_PDF_SIGN) {
                mPageType = AssitTool.PAGE_PDF_SIGN;
                btContainer.setVisibility(GONE);
                topTitle.setVisibility(VISIBLE);
                timerView.setVisibility(VISIBLE);
                btSignature.setVisibility(GONE);
//                btSignature.setVisibility(INVISIBLE);
                exitPdf = true;
                Button back = (Button) findViewById(R.id.back);
                back.setVisibility(VISIBLE);
                back.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        App.getInstance().fitManagerCCB.getBaseFitBin().backData(Cmds.CMD_CL.getBytes());
                        toPlay();
                    }
                });
                signatureFrame.setVisibility(VISIBLE);
                timerView.setOnTimerListener(new OnTimerListener() {
                    @Override
                    public void timeOut() {
                        ResposeICBCSignatureData.getInstance().setSignState(3);
                        toPlay();
                    }
                });
                timerView.setTime(baseData.getTimeOut());
                topTitle.setText(baseData.getTitleA());
                txtPageNumber.setVisibility(VISIBLE);
                ResposeICBCSignatureData.getInstance().setSignState(0);
                int statusheight = 0;
                if ("".equals(baseData.getStatusContent()) || baseData.getStatusContent() == null) {
                    topTitle.setVisibility(GONE);
                } else {
                    statusheight = SharedpreferencesData.getInstance().getTop_height();
                    topTitle.setVisibility(VISIBLE);
                }
                statusheight = 0;
                topTitle.setVisibility(GONE);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight);
                pageContent.setLayoutParams(params);

                // add pdf
                try {
                    muPDFCore = new MuPDFCore(getContext(),
                            readFileSdcardFile(baseData.getPDFPath()), "");

                    muPDFReaderView = new MuPDFReaderView(playActivity) {
                        @Override
                        protected void onMoveToChild(int i) {
                            super.onMoveToChild(i);
                            txtPageNumber.setText(String.format("%d / %d", i + 1,
                                    muPDFCore.countPages()));
                           /* if(muPDFReaderView.getDisplayedViewIndex()+1 == baseData.getPdfPageNumber() )
                            {
                               signatureFrame.setVisibility(VISIBLE);
                            }
                            else
                            {
                               signatureFrame.setVisibility(INVISIBLE);
                            }*/


                        }


                        @Override
                        public boolean onScale(ScaleGestureDetector detector) {
                            float scale = muPDFCore.getPageSize(0).y / App.SCREEN_HEIGHT;
                            Log.d("bill", "----scale:" + muPDFReaderView.mScale);
                            //signatureFrame.ZoomLayout(muPDFReaderView.mScale);
                            return super.onScale(detector);

                        }

                        @Override
                        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                            Log.d("bill", "===== distanceX:" + distanceX + "===== distanceY:" + distanceY);
                            return super.onScroll(e1, e2, distanceX, distanceY);
                        }


                    };
                    muPDFReaderView.setAdapter(new MuPDFPageAdapter(context,
                            null, muPDFCore));
                    muPDFReaderView.setDisplayedViewIndex(0);
                    float scale = muPDFCore.getPageSize(0).y / App.SCREEN_HEIGHT;
                    //muPDFReaderView.SetViewScale(scale);
                    new Handler().postDelayed(muPDFReaderView
                            , 1200);
                    pageContent.removeAllViews();
                    signPageHeight = muPDFCore.getPageSize(baseData.getPdfPageNumber()).y;
                    signPageWidth = muPDFCore.getPageSize(baseData.getPdfPageNumber()).x;
                    Log.e("bill", "signPageHeight:" + signPageHeight);
                    Log.e("bill", "signPageWidth:" + signPageWidth);
                    ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    pageContent.addView(muPDFReaderView, params2);
                    Log.d(TAG, "---------------" + muPDFCore.getPageSize(1).toString());


                    try {
                        PdfReader reader = new PdfReader(ICBCSignData.getInstance().getPDFPath());//选择需要印章的pdf;
                        float scale1 = (float) ICBCSignData.getInstance().getSignWidth() / (float) ICBCSignData.getInstance().getSignHeight();
                        Log.e("bill", " getSignWidth:" + ICBCSignData.getInstance().getSignWidth() + "  getSignHeight: " + ICBCSignData.getInstance().getSignHeight());
                        int width = 600;
                        int height = (int) (600 / scale1);
                        ICBCSignData.getInstance().setPicHeight(height);
                        ICBCSignData.getInstance().setPicWidth(width);
                        Log.e("bill", "scale:" + scale1 + " width:" + width + "  height: " + height);
                        signatureFrame.setSignatureSize(width, height);
                        if (muPDFReaderView != null) {
                            muPDFReaderView.setDisplayedViewIndex(ICBCSignData.getInstance().getPdfPageNumber() - 1);
                            //muPDFReaderView.scrollTo(0, ICBCSignData.getInstance().getSignY());
                            //muPDFReaderView.setMode(MuPDFReaderView.Mode.Signing);
                        }
                    } catch (Exception e) {

                    }


                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                App.getInstance().tts.Read("请签名", 1);
            }


            ////显示pdf的电子签名,确认签名信息
            if (baseData.getSignType() == AssitTool.PAGE_PDF_SHOWSIGN) {
                ResposeICBCSignatureData.getInstance().setSignState(2);
                mPageType = AssitTool.PAGE_PDF_SHOWSIGN;
                btContainer.setVisibility(VISIBLE);
                baseData.setShowSignedPdf(false);
                topTitle.setVisibility(GONE);
                timerView.setVisibility(VISIBLE);
                btSignature.setVisibility(INVISIBLE);
                signatureFrame.setVisibility(INVISIBLE);
                timerView.setOnTimerListener(new OnTimerListener() {
                    @Override
                    public void timeOut() {
                        ResposeICBCSignatureData.getInstance().setSignState(3);
                        toPlay();
                    }
                });
                timerView.setTime(baseData.getRemainTime());
                topTitle.setText(baseData.getTitleB());
                txtPageNumber.setVisibility(VISIBLE);

                int statusheight = 0;
                if ("".equals(baseData.getStatusContent()) || baseData.getStatusContent() == null) {
                    topTitle.setVisibility(GONE);
                } else {
                    statusheight = SharedpreferencesData.getInstance().getTop_height();
                    topTitle.setVisibility(VISIBLE);
                }

                statusheight = 0;
                topTitle.setVisibility(GONE);
                LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, App.SCREEN_HEIGHT - statusheight);
                pageContent.setLayoutParams(params);
                try {
                    muPDFCore = new MuPDFCore(getContext(),
                            readFileSdcardFile(baseData.getPDFSignPath()), "");
                    muPDFReaderView = new MuPDFReaderView(playActivity) {
                        @Override
                        protected void onMoveToChild(int i) {
                            super.onMoveToChild(i);
                            txtPageNumber.setText(String.format("%d / %d", i + 1,
                                    muPDFCore.countPages()));

                        }
                    };
                    muPDFReaderView.setAdapter(new MuPDFPageAdapter(context,
                            null, muPDFCore));
                    muPDFReaderView.setDisplayedViewIndex(baseData.getPdfPageNumber() - 1);
                    // muPDFReaderView.scrollTo(0,baseData.getSignY());
                    float scale = muPDFCore.getPageSize(0).y / App.SCREEN_HEIGHT;
                    Log.d("test", "mscale: " + scale + " width:" + muPDFCore.getPageSize(1).x);
                    //muPDFReaderView.SetViewScale(scale);
                    new Handler().postDelayed(muPDFReaderView
                            , 1200);
                    pageContent.removeAllViews();
                    ViewGroup.LayoutParams params2 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    pageContent.addView(muPDFReaderView, params2);
                    Log.d(TAG, "---------------" + muPDFCore.getPageSize(1).toString());
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                App.getInstance().tts.Read(SDCSConfirmPDF.getInstance().getVoiceText(), 1);
            }
        }


    }


    public void forceHideSoftKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindowToken(), 0);

    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (receiver != null) {
            context.unregisterReceiver(receiver);
        }
        if (muPDFCore != null) {
            muPDFCore.onDestroy();
            muPDFCore = null;
        }
    }

    public byte[] readFileSdcardFile(String fileName) {
        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];
            fin.read(buffer);
            fin.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    @Override
    public void submit(String str) {
        Log.d("BaseData", "-------------data come from js :" + str);
        ICBCAddWebFileData.getInstance().sendJsData(str);
        toPlay();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Log.d(TAG, "onFinishInflate");
        topTitle = (CustomText) findViewById(R.id.page_base_title);
        timerView = (TimerView) findViewById(R.id.page_base_timer);
        pageContent = (LinearLayout) findViewById(R.id.page_base_content);
        //icbcPlayView = (ICBCPlayView) findViewById(R.id.page_item_playview);
        //htmlView = (HtmlView) findViewById(R.id.page_item_web_app);


        //字体
        //Typeface tf= Typeface.createFromAsset(context.getAssets(), "fonts/"+ SharedpreferencesData.getInstance().getTop_font_family() +".ttf");
        Typeface tf = AssitTool.getFonts(SharedpreferencesData.getInstance().getTop_font_family());
        if (tf != null) {
            topTitle.setTypeface(tf);
        }
        //字体大小
        topTitle.setTextSize(SharedpreferencesData.getInstance().getDefTopFontSize());
        //字体粗细
        if (SharedpreferencesData.getInstance().getTop_font_weight() == 1) {
            topTitle.getPaint().setFakeBoldText(true);
        }
        //字体颜色
        topTitle.setTextColor(top_colors[SharedpreferencesData.getInstance().getTop_font_color()]);

        //背景颜色
        topTitle.setBackgroundColor(top_colors[SharedpreferencesData.getInstance().getTop_bgcolor()]);

        //高度
        //topTitle.setHeight(SharedpreferencesData.getInstance().getTop_height());
        LayoutParams params = (LayoutParams) topTitle.getLayoutParams();
        params.weight = App.SCREEN_WIDTH;
        params.height = SharedpreferencesData.getInstance().getTop_height();
        Log.d(TAG, "--------------top height:" + SharedpreferencesData.getInstance().getTop_height());
        topTitle.setLayoutParams(params);


        btSignature = (Button) findViewById(R.id.page_item_signature_bt);

        btSignature.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (exitPdf) {

                    App.getInstance().fitManagerCCB.getBaseFitBin().backData(Cmds.CMD_CL.getBytes());
                    toPlay();
                } else {
                    btSignature.setVisibility(View.GONE);
                    signatureFrame.setVisibility(View.VISIBLE);
                    try {
                        PdfReader reader = new PdfReader(ICBCSignData.getInstance().getPDFPath());//选择需要印章的pdf;
                        Log.e("bill", " getSignWidth:" + ICBCSignData.getInstance().getSignWidth() + "  getSignHeight: " + ICBCSignData.getInstance().getSignHeight());
                        float scale = (float) ICBCSignData.getInstance().getSignWidth() / (float) ICBCSignData.getInstance().getSignHeight();
                        int width = 600;
                        int height = (int) (600 / scale);
                        ICBCSignData.getInstance().setPicHeight(height);
                        ICBCSignData.getInstance().setPicWidth(width);
                        Log.e("bill", "scale:" + scale + " width:" + width + "  height: " + height);
                        signatureFrame.setSignatureSize(width, height);
                        if (muPDFReaderView != null) {
                            muPDFReaderView.setDisplayedViewIndex(ICBCSignData.getInstance().getPdfPageNumber() - 1);
                            //muPDFReaderView.scrollTo(0, ICBCSignData.getInstance().getSignY());
                            muPDFReaderView.setMode(MuPDFReaderView.Mode.Signing);
                        }
                    } catch (Exception e) {

                    }
                }

            }
        });


        signatureFrame = (ICBCSignatureFrame) findViewById(R.id.page_item_signature_farme);
        signatureFrame.setOnSignatureListener(signatureListener);
        signatureFrame.setIcbcPage(this);

        txtPageNumber = (TextView) findViewById(R.id.pageNumber);


        //add ok ,cancel button
        btOK = (Button) findViewById(R.id.page_base_bt_ok);
        btCancel = (Button) findViewById(R.id.page_base_bt_cancel);
        btContainer = (LinearLayout) findViewById(R.id.btContainer);


    }

    ICBCSignatureFrame.OnSignatureListener signatureListener = new ICBCSignatureFrame.OnSignatureListener() {

        @Override
        public void hide() {
            btSignature.setVisibility(View.INVISIBLE);
            if (muPDFReaderView != null) {
                muPDFReaderView.setDisplayedViewIndex(0);
                muPDFReaderView.setMode(MuPDFReaderView.Mode.Viewing);
            }
        }

        @Override
        public void confirm() {
            btSignature.setVisibility(View.INVISIBLE);
            // ResposeSignatureData.getInstance().setResposeBitmap(
            //        signatureFrame.getSignatureBitmap());
            //  App.getInstance().fitManagerCCB.getBaseFitBin().setData(CMD.SR,
            //          CMD.SR.length);
            //testSignature();
            //toPlay();

            makeSignData();

            //同步时马上提交，异步时暂时不提交
            if (ICBCSignData.getInstance().getSignSyncType() == 0) {

                Log.d("myitm", "send data");
                App.getInstance().fitManagerCCB.getBaseFitBin().setData(Cmds.CMD_RG.getBytes(),
                        Cmds.CMD_RG.getBytes().length);
            }
            //  ResposeICBCSignatureData.getInstance().setData(Cmds.CMD_RG.getBytes(),
            //          Cmds.CMD_RG.getBytes());

             /*if (muPDFReaderView != null && muPDFCore != null) {                 //2017.2.27取消签字完成后合成图片
                //signaturePdf();
                signPdfAsyncTask asyncTask = new signPdfAsyncTask(context);
                asyncTask.execute();
            }*/
//            toPlay();

            ResposeICBCSignatureData.getInstance().setSignState(2);
            Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
            intent.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
            getContext().sendBroadcast(intent);
        }

    };


    public void makeSignData() {
        ResposeICBCSignatureData.getInstance().setResposeBitmap(
                signatureFrame.getSignatureBitmap());

        List<List<float[]>> signData = signatureFrame.getSignatureData();
        //List<List<float[]>> signData = signatureFrame.msignData;
        String str = "";
        //str = str + ICBCSignData.getInstance().getPicWidth() + "," + ICBCSignData.getInstance().getPicHeight() + ",P1024,";
        for (List<float[]> strokes : signData
                ) {
            str += "(";
            for (float[] point :
                    strokes) {
                float k = point[2] * 100;
                int z = (int) k;
                str += String.format("%d,%d,%d,%d;", (int) point[0], (int) point[1], z, (int) point[3]);
            }
            str += ")";
        }
//        for (int i = 0; i < signData.size(); ++i) {
//            str = str + "(";
//            for (int j = 0; j < signData.get(i).size(); ++j) {
//                float k = signData.get(i).get(j)[2] * 100;
//                int z = (int) k;
//                if (j == (signData.get(i).size() - 1)) {
//                    str = str + (int) signData.get(i).get(j)[0] + "," + (int) signData.get(i).get(j)[1] + "," + z + ";";
//                } else {
//                    str = str + (int) signData.get(i).get(j)[0] + "," + (int) signData.get(i).get(j)[1] + "," + z + ";";
//                }
//            }
//            str = str + ")";
//
//        }
        byte[] data = str.getBytes();
        Log.d("myitm", "-----" + str);
        Log.d("myitm", "-----" + data.length);

        ResposeICBCSignatureData.getInstance().setSignData(str);
        signaturePdf();
        ResposeICBCSignatureData.getInstance().setSignPdfData();
        ResposeICBCSignatureData.getInstance().MakeResposeData();
    }

    void testSignature() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                Bitmap bitmap = signatureFrame.getSignatureBitmap();
                File dir = new File("/mnt/sdcard/signature/");
                if (!dir.exists()) {
                    dir.mkdirs();
                }
                File file = new File(dir, "ss.png");
                if (!file.exists()) {
                    try {
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            }
        }).start();
    }


    /**
     * 功能：将耗时的操作放到异步任务
     *
     * @author LI
     */
    class signPdfAsyncTask extends AsyncTask<Void, Integer, Integer> {
        private Context context;

        public signPdfAsyncTask(Context context) {
            this.context = context;
        }

        /**
         * 运行在UI线程中，在调用doInBackground()之前执行
         */
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            ResposeICBCSignatureData.getInstance().setSignState(2);
            Toast.makeText(context, getResources().getString(R.string.sign_pdf_info), Toast.LENGTH_SHORT).show();
        }

        /**
         * 后台运行的方法，可以运行非UI线程，可以执行耗时的方法
         */
        @Override
        protected Integer doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            signaturePdf();
            return null;
        }

        /**
         * 运行在ui线程中，在doInBackground()执行完毕后执行
         */
        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            //super.onPostExecute(result);
            // createUI(InstanceState);
            ResposeICBCSignatureData.getInstance().setSignState(2);
            ICBCSignData.getInstance().setRemainTime(timerView.getCurTime());
            Toast.makeText(context, getResources().getString(R.string.sign_pdf_ok), Toast.LENGTH_SHORT).show();
     /*       if(ICBCSignData.getInstance().getSignSyncType()== 0 ) {
                toPlay();
            }else {*/
            ICBCSignData.getInstance().setShowSignedPdf(true);
            Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
            intent.putExtra(AppAction.KEY_BROADCAST_CMD, Cmds.CMD_SG.getBytes());
            App.getInstance().sendBroadcast(intent);
          /*  }*/
        }

        /**
         * 在publishProgress()被调用以后执行，publishProgress()用于更新进度
         */
        @Override
        protected void onProgressUpdate(Integer... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }


    void signaturePdf() {
        //creat signature bitmap
        int width = ICBCSignData.getInstance().getSignWidth(), height = ICBCSignData.getInstance().getSignHeight();
//        Bitmap bm = signatureFrame.getSignatureBitmap();
//        int w = bm.getWidth();
//        int h = bm.getHeight();
//        float scaleWidth = (float) width / w;
//        float scaleHeight = (float) height / h;
//        Matrix matrix = new Matrix();
//        matrix.postScale(scaleWidth, scaleHeight);
//        Bitmap bitmap = Bitmap.createBitmap(bm, 0, 0, w, h,matrix,true);
        File dir = new File(FileInf.PDF);
        if (!dir.exists()) {
            dir.mkdirs();
        }
//        File file = new File(ICBCSignData.getInstance().getSignImagePath());
//        if (!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        //sign to pdf
        String InPdfFilePath = ICBCSignData.getInstance().getPDFPath();
        String OutPdfFilePath = ICBCSignData.getInstance().getPDFSignPath();
        String InPicFilePath = ICBCSignData.getInstance().getSignImagePath();
        HandWriteToPDF handWriteToPDF = new HandWriteToPDF(InPdfFilePath, OutPdfFilePath, InPicFilePath);
        Log.e("bill", "signPageHeight:" + signPageHeight);
        Log.e("bill", "signPageWidth:" + signPageWidth);
        handWriteToPDF.addText(ICBCSignData.getInstance().getPdfPageNumber(), width,
                height,
                ICBCSignData.getInstance().getSignX(), ICBCSignData.getInstance().getSignY());

    }


    @Override
    public void timeOut() {


        App.getInstance().tts.Read("操作超时", 1);
        Log.d("bill", "mPageType:" + mPageType);
        if ((mPageType == AssitTool.PAGE_VIEW_INPUTPASSWORD) ||
                (mPageType == AssitTool.PAGE_PDF_SIGN) || (mPageType == AssitTool.PAGE_VIEW_ReadPin)) {
            toPlay();
        }
    }

    public void setTimer(int time) {
        timerView.setTime(time);
        if (time > 0) {
            timerView.setVisibility(View.VISIBLE);
        } else {
            timerView.setVisibility(View.INVISIBLE);
        }
    }


    public void toPlay() {
        ResposeICBCSignatureData.getInstance().setSignState(0);
        Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
        intent.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
        getContext().sendBroadcast(intent);
    }

    public void toPlay(byte[] page) {
        Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
        intent.putExtra(AppAction.KEY_BROADCAST_CMD, page);
        getContext().sendBroadcast(intent);
    }
}
