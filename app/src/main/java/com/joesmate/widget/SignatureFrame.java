package com.joesmate.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.joesmate.bin.SignatureData;
import com.joesmate.signaturepad.views.SignaturePad;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.SharedpreferencesData;


import java.util.List;

public class SignatureFrame extends LinearLayout implements OnClickListener {

    public static final String TAG = "SignatureFrame";
    boolean isFullScreen;
    public SignaturePad signatureView;
    Button btFullScreen, btHide, btReset, btConfirm, btFinger;
    OnSignatureListener onSignatureListener;
    Bitmap cacheBitmp;

    public SignatureFrame(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SignatureFrame(Context context) {
        super(context);
        init(context);
    }

    public void init(Context context) {
        inflate(context, R.layout.signature_frame, this);
        signatureView = (SignaturePad) findViewById(id.signature_frame_view);
        signatureView.setOnSignedListener(onSignedListener);
        signatureView.setPenColor(SharedpreferencesData.getInstance().getPenColorValue());
        signatureView.setMaxWidth(SharedpreferencesData.getInstance().getPenWidth());
        btFullScreen = (Button) findViewById(id.signature_frame_bt_fullscreen);
        btHide = (Button) findViewById(id.signature_frame_bt_hide);
        btReset = (Button) findViewById(id.signature_frame_bt_reset);
        btConfirm = (Button) findViewById(id.signature_frame_bt_confirm);
        btFinger = (Button) findViewById(id.signature_frame_bt_finger);
        setButtonEnabled(false);

        btFullScreen.setOnClickListener(this);
        btHide.setOnClickListener(this);
        btReset.setOnClickListener(this);
        btConfirm.setOnClickListener(this);
        btFinger.setOnClickListener(this);

        setLayoutParams(getPartScreenParams());
    }

    @Override
    public void onClick(View v) {

        if (v == btFullScreen) {
            if (isFullScreen) {
                btFullScreen.setText("全屏");
                isFullScreen = false;
                setLayoutParams(getPartScreenParams());
                //signatureView.setMaxWidth(2);
            } else {
                btFullScreen.setText("分屏");
                isFullScreen = true;
                setLayoutParams(getFullScreenParams());
                //signatureView.setMaxWidth(2);
            }
            signatureView.clear();
        } else if (v == btHide) {
            setVisibility(View.GONE);
            if (onSignatureListener != null) {
                onSignatureListener.hide();
            }
        } else if (v == btReset) {
            signatureView.clear();
        } else if (v == btConfirm) {
            setVisibility(View.GONE);
            // 缓存签名图片数据
            cacheBitmp = Bitmap.createBitmap(signatureView.getTransparentSignatureBitmap());
            signatureView.clear();
            if (onSignatureListener != null) {
                onSignatureListener.confirm();
            }
        } else if (v == btFinger) {
            //SignatureData.getInstance().backData();
            onSignatureListener.hide();
        }


    }

    public Bitmap getSignatureBitmap() {
        return cacheBitmp;
        //return signatureView.getTransparentSignatureBitmap();
    }

    public List<List<float[]>> getSignatureData() {
        return signatureView.getSignatureData();
    }

    private void setButtonEnabled(boolean isEnabled) {
        btFullScreen.setEnabled(!isEnabled);
        btReset.setEnabled(isEnabled);
        btConfirm.setEnabled(isEnabled);
    }

    SignaturePad.OnSignedListener onSignedListener = new SignaturePad.OnSignedListener() {
        public void onStartSigning() {

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

    RelativeLayout.LayoutParams getFullScreenParams() {
        return new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    }

    RelativeLayout.LayoutParams getPartScreenParams() {
        int w = (int) getResources().getDimension(R.dimen.signature_frame_w);
        int h = (int) getResources().getDimension(R.dimen.signature_frame_h);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(w, h);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        return params;
    }

    public void setOnSignatureListener(OnSignatureListener onSignatureListener) {
        this.onSignatureListener = onSignatureListener;
    }

    public static interface OnSignatureListener {
        public void hide();

        public void confirm();
    }

}
