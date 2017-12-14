package com.joesmate.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.Cmds;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.GetSignPDFStateData;
import com.joesmate.bin.icbc.ICBCSignData;
import com.joesmate.bin.icbc.ResposeICBCSignatureData;
import com.joesmate.pageItem.ICBCPage;



import java.util.ArrayList;
import java.util.List;

public class ICBCSignatureFrame extends LinearLayout implements OnClickListener {

	public static final String TAG = "SignatureFrame";
	boolean isFullScreen;
	public SignaturePad signatureView;
	LinearLayout signatureLinearLayout ;
	Button btReturn, btReset, btConfirm;
	OnSignatureListener onSignatureListener;
	Bitmap cacheBitmp;
	TextView txtSignMsg ;
    ICBCPage  micbcPage ;
	Context context;
	public   List<List<float[]>> msignData=new ArrayList<List<float[]>>();
	public   List<float[]> lineData;

	public ICBCSignatureFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	public ICBCSignatureFrame(Context context) {
		super(context);
		this.context = context;
		init(context);
	}


	public void setSignatureSize(int width , int height)
	{
        ViewGroup.LayoutParams params = signatureLinearLayout.getLayoutParams();
		params.height = height;
		params.width = width;
		signatureLinearLayout.setLayoutParams(params);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {

			if(intent.getAction().equals(AppAction.ACTION_BROADCAST_MAKE_SIGN_DATA))
			{
				if(signatureView != null)
				{
					if(signatureView.getVisibility() == VISIBLE)
					{
						makeSignData();
						Log.d("myitm", "send data");
						App.getInstance().fitManagerCCB.getBaseFitBin().setData(Cmds.CMD_RG.getBytes(),
								Cmds.CMD_RG.getBytes().length);
					}
				}
				else
				{
					GetSignPDFStateData.getInstance().SendBackCode(0);

				}
			}
		}
	};

	public void makeSignData()
	{
		cacheBitmp = Bitmap.createBitmap(signatureView.getTransparentSignatureBitmap());
		ResposeICBCSignatureData.getInstance().setResposeBitmap(
				getSignatureBitmap());
		List<List<float[]>> signData = signatureView.getSignatureData();
		String str = "";
		str = str + ICBCSignData.getInstance().getPicWidth()+","+ICBCSignData.getInstance().getPicHeight()+",P1024";
		for(int i = 0 ; i < signData.size(); ++ i)
		{
			str = str + "(";
			for(int j=0; j < signData.get(i).size(); ++ j)
			{
				float k = signData.get(i).get(j)[2] * 1000;
				int z = (int)k;
				if (j == (signData.get(i).size()-1)) {
					str = str  + (int) signData.get(i).get(j)[0] + "," + (int) signData.get(i).get(j)[1] + "," + z +";";
				}
				else
				{
					str = str + (int) signData.get(i).get(j)[0] + "," + (int) signData.get(i).get(j)[1] + "," + z + ";";
				}
			}
			str = str + ")" ;

		}
		byte[] data = str.getBytes();
		Log.d("myitm","-----"+str);
		Log.d("myitm","-----"+data.length);
		ResposeICBCSignatureData.getInstance().setSignData(str);
		ResposeICBCSignatureData.getInstance().MakeResposeData();
	}
	public void init(Context context) {
		inflate(context, R.layout.icbc_signature_frame, this);
		Log.d("bill", "slop:"+ViewConfiguration.get(context).getScaledTouchSlop());
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppAction.ACTION_BROADCAST_MAKE_SIGN_DATA);
		context.registerReceiver(receiver, filter);

		signatureView = (SignaturePad) findViewById(id.signature_frame_view);
		signatureView.setOnSignedListener(onSignedListener);


		msignData = new ArrayList<List<float[]>>();



		signatureView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent motionEvent) {
				//Log.d("bill","event:"+ motionEvent.getDevice().getName());
				if("Hanvon electromagnetic pen".equals(motionEvent.getDevice().getName())) {
					ResposeICBCSignatureData.getInstance().setSignState(1);
					if (motionEvent.getAction() == KeyEvent.ACTION_DOWN) {
						//Log.v(TAG, "--------------------touch down------------");
						txtSignMsg.setVisibility(GONE);
					}

				/*int action =  motionEvent.getAction() ;
				float x = motionEvent.getX();
				float y = motionEvent.getY();
				float z = motionEvent.getPressure();
				switch (action)
				{
					case  MotionEvent.ACTION_DOWN:
						  lineData = new ArrayList<float[]>();
						  lineData.add(new float[]{x,y,z});
                          break;
					case  MotionEvent.ACTION_MOVE:
						  lineData.add(new float[]{x,y,z});
						  break;
					case  MotionEvent.ACTION_UP:
						  lineData.add(new float[]{x,y,z});
						msignData.add(lineData);
						  break;
				}*/
					return false;
				}else
				{
					return  true ;
				}
			}
		});


		//btFullScreen = (Button) findViewById(id.signature_frame_bt_fullscreen);
//		btReturn = (Button) findViewById(id.signature_frame_bt_return);
		btReset = (Button) findViewById(id.signature_frame_bt_reset);
		btConfirm = (Button) findViewById(id.signature_frame_bt_confirm);

		txtSignMsg = (TextView) findViewById(id.sign_msg);
		signatureLinearLayout = (LinearLayout) findViewById(id.signature_grp);
		setButtonEnabled(false);

		//btFullScreen.setOnClickListener(this);
//		btReturn.setOnClickListener(this);
		btReset.setOnClickListener(this);
		btConfirm.setOnClickListener(this);
		//setLayoutParams(getPartScreenParams());
	}



	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if(receiver != null ) {
			context.unregisterReceiver(receiver);
		}

	}

    public  void  setIcbcPage(ICBCPage icbcPage)
    {
        micbcPage = icbcPage ;
    }
	@Override
	public void onClick(View v) {
//		if (v == btReturn) {
//			setVisibility(View.GONE);
//			ResposeICBCSignatureData.getInstance().setSignState(4);
//			if(onSignatureListener != null){
//				onSignatureListener.hide();
//			}
//		} else

		if (v == btReset) {
			ResposeICBCSignatureData.getInstance().setSignState(0);
			txtSignMsg.setVisibility(VISIBLE);
			msignData.clear();
			signatureView.clear();
		} else if (v == btConfirm) {

			ResposeICBCSignatureData.getInstance().setSignState(2);
			setVisibility(View.GONE);
			// 缓存签名图片数据
			cacheBitmp = Bitmap.createBitmap(signatureView.getTransparentSignatureBitmap());
			if(onSignatureListener != null){
                msignData=signatureView.getSignatureData();
				onSignatureListener.confirm();
                signatureView.clear();
			}
		}

	}
	public Bitmap getSignatureBitmap(){
		return cacheBitmp;
		//return signatureView.getTransparentSignatureBitmap();
	}
	public List<List<float[]>> getSignatureData() {
		return msignData;
	}
	private void setButtonEnabled(boolean isEnabled){
//		btReturn.setEnabled(!isEnabled);
		btReset.setEnabled(isEnabled);
		btConfirm.setEnabled(isEnabled);
	}
	final SignaturePad.OnSignedListener onSignedListener = new SignaturePad.OnSignedListener(){
	public 	void onStartSigning(){

		}
		@Override
		public void onSigned() {
			// TODO Auto-generated method stub
			setButtonEnabled(true);
		}

		@Override
		public void onClear() {
			// TODO Auto-generated method stub
			setButtonEnabled(false);
		}
		
	};
	
	RelativeLayout.LayoutParams getFullScreenParams(){
		//return new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		//int w = (int) getResources().getDimension(R.dimen.signature_frame_w);
		//int h = (int) getResources().getDimension(R.dimen.signature_frame_h);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(832,520);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		return params;
	}
	RelativeLayout.LayoutParams getPartScreenParams(){
		int w = (int) getResources().getDimension(R.dimen.signature_frame_w);
		int h = (int) getResources().getDimension(R.dimen.signature_frame_h);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w,h);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		return params;
	}


    public void ZoomLayout (float scale)
    {
		/*
        float w = (int) getResources().getDimension(R.dimen.signature_frame_w)*scale*0.5f;
        float h = (int) getResources().getDimension(R.dimen.signature_frame_h)*scale*0.5f;

        if(w > 832)
        {
            w = 832;
        }
        if(h > 520 )
        {
            h = 520;
        }

        if(w < 448)
        {
            w = 448;
        }

        if(h < 280)
        {
            h = 280;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int)w,(int)h);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        //txtSignMsg.setTextSize(25f*scale);
        float textScale = 1.0f ;
        if ( 0.7f*scale > 2.0f )
        {
            textScale = 2.0f ;
        }
        if(0.6f*scale < 1.0f)
        {
            textScale = 1.0f;
        }
        txtSignMsg.setScaleX(textScale);
        txtSignMsg.setScaleY(textScale);
        setLayoutParams(params);
        */
		setScaleX(scale);
		setScaleY(scale);
    }

	
	public void setOnSignatureListener(OnSignatureListener onSignatureListener){
		this.onSignatureListener = onSignatureListener;
	}
	
	public static interface OnSignatureListener{
		public void hide();
		public void confirm();
	}

}
