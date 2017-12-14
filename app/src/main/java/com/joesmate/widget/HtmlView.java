package com.joesmate.widget;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.joesmate.AppAction;
import com.joesmate.bin.InputPwChar;
import com.joesmate.listener.OnJsListener;

@SuppressLint("SetJavaScriptEnabled")
public class HtmlView extends WebView {

	public static final String TAG = "HtmlView";
	private dataFromJs fromJs;
	WebSettings webSettings;

	public String  InputName ;

	Context mcontext ;

	public HtmlView(Context context) {
		super(context);
		mcontext = context;
		init();
	}

	public HtmlView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mcontext = context;
		init();
	}
	public void setWebTextSize(int fontSize){
		webSettings.setDefaultFontSize(fontSize);
		
	}

	private void init() {
		setBackgroundColor(0);
		setScrollBarStyle(0);// 滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
		fromJs = new dataFromJs();
		addJavascriptInterface(fromJs, "TlrClient");

		webSettings = this.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setUseWideViewPort(true);
		webSettings.setLoadWithOverviewMode(true);
		webSettings.setDefaultTextEncodingName("utf-8");

       /* setWebChromeClient(new WebChromeClient() {
            @Override
            public void onRequestFocus(WebView view) {
                Log.d("bill1","---------onRequestFocus");
                super.onRequestFocus(view);
            }


			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}
        });


        setWebViewClient( new WebViewClient()
        {
            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
               // Log.d("bill1","---------onFormResubmission");
            }

            @Override
            public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
               // Log.d("bill1","---------shouldOverrideKeyEvent");
                return super.shouldOverrideKeyEvent(view, event);
            }

            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
               // Log.d("bill1","---------shouldInterceptRequest  url:" + url);
                return super.shouldInterceptRequest(view, url);
            }

            @Override
            public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
                // Log.d("bill1","---------onReceivedLoginRequest");
                super.onReceivedLoginRequest(view, realm, account, args);
            }
        });*/

		//webSettings.setSupportZoom(true);
//		// 设置出现缩放工具 
		//webSettings.setBuiltInZoomControls(true);
//		//扩大比例的缩放
		//webSettings.setUseWideViewPort(true);
//		//自适应屏幕
		//webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
		//webSettings.setLoadWithOverviewMode(true);


/*
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER);
		filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR);
		filter.addAction(AppAction.ACTION_BROADCAST_INPUT_PW_BACK);
		mcontext.registerReceiver(receiver, filter);
*/




	}
	@Override
	protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX,
			boolean clampedY) {
		super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
	}
	@Override
	protected int computeVerticalScrollExtent() {
		// TODO Auto-generated method stub
		return super.computeVerticalScrollExtent();
	}
	
	public void setOnJsListener(OnJsListener onJsListener){
		fromJs.setOnJsListener(onJsListener);
	}

	/**
	 * <p>
	 * baseUrl the URL to use as the page's base URL. If null defaults to
	 * 'about:blank'.
	 * <p>
	 * data a String of data in the given encoding
	 * <p>
	 * mimeType the MIMEType of the data, e.g. 'text/html'. If null, defaults to
	 * 'text/html'.
	 * <p>
	 * encoding the encoding of the data
	 * <p>
	 * historyUrl the URL to use as the history entry. If null defaults to
	 * 'about:blank'.
	 */
	public void loadChar(String data){
		loadDataWithBaseURL("about:blank", data, "text/html", "utf-8", "about:blank");
	}
	
	public void loadFile(String path){
		loadUrl("file://" + path);
	}




	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		Log.d(TAG, "----- onDetachedFromWindow 2222");
		/*mcontext.unregisterReceiver(receiver);*/
	}


	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR))
			{
				char str = (char)intent.getByteExtra("inputChar", (byte) 0);
				int str1 = str - 48 ;
				Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ONECHAR :" + str1);
				loadUrl("javascript:setInputValue('"+InputName+"','"+str1+"')");

			}else if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER))
			{
				GetReturnData();
				Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_ENTER ");

			}else if(intent.getAction().equals(AppAction.ACTION_BROADCAST_INPUT_PW_BACK))
			{
				ClearInputData();
				Log.d(TAG, "----- ACTION_BROADCAST_INPUT_PW_BACK ");
			}
		}
	};



	class dataFromJs {
		private String jsJson;
		OnJsListener onJsListener;

		/**
		 * 与js约定的交互函数接口
		 * 
		 * @param str
		 */
		@JavascriptInterface
		public void submit(String str) {
			jsJson = str;
			Log.d(TAG, "submit："+str);
			if(onJsListener != null){
				onJsListener.submit(jsJson);
			}
		}

		@JavascriptInterface
		public void inputchar(String inputname)
		{
			InputName = inputname ;
			InputPwChar.getInstance().inputChar();
			Log.d(TAG, "inputchar , inputname:" + inputname);
		}


		public String getJsJson() {
			return jsJson;
		}
		
		public void setOnJsListener(OnJsListener onJsListener){
			this.onJsListener = onJsListener;
		}
	}

	
	public  void GetReturnData()
	{
		loadUrl("javascript:getReturnData()");
	}

	public  void ExecuteJsFunction(String functionName)
	{
		loadUrl("javascript:"+functionName+"()");
	}

	public  void ClearInputData()
	{
		loadUrl("javascript:ClearInputValue('"+InputName+"')");
	}
}
