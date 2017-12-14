package com.joesmate.page;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.joesmate.R;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;

public class PWDActivity extends Activity implements SerialUtil.OnDataReceiveListener{

    private static byte[] rbyte;
    private  TextView txtPWD;
    private TextView infinish;
    private boolean isOK = false;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    setInfinish();
                    break;
                case 1:
                    setTxtPWD();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd);
        txtPWD = (TextView) findViewById(R.id.textView2);
        infinish = (TextView) findViewById(R.id.textView3);
        getPWDFromSerial();
    }

    @Override
    public void onDataReceive(byte[] buffer, int size) {
        rbyte = new byte[size];
        System.arraycopy(buffer, 0, rbyte, 0, size);
    }

    public void getPWDFromSerial()
    {SerialUtil.getInstance().getSerialPort();
        final SerialResponseFrame serialResponse = new SerialResponseFrame();Log.d("55555", "6666666" + serialResponse);
        new Thread(
                new Runnable(){

                    @Override
                    public void run() {
                        for(int i =1;i<=7;i++) {
                        /*SerialUtil.getInstance().isStop = false;Log.d("55555", "666start6666");
                        SerialUtil.getInstance().getmReadThread().run();
//
                        Log.d("55555", "6666666");
                        SerialResponseFrame.lock.lock();
                        byte[] rbyte = serialResponse.getRbyte();
                        Log.d("aaaaabbbbbbbaaaaaaaa", "" + Arrays.toString(rbyte) );
                        SerialUtil.getInstance().isStop = true;
                        SerialResponseFrame.lock.unlock();*/

                            try {
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if(i<=6)
                                handler.sendEmptyMessage(1);
                            else
                            {
                                isOK = true;
                                handler.sendEmptyMessage(0);
                            }

                        }

                    }}).start();

    }

    public void setInfinish()
    {
        infinish.setVisibility(View.VISIBLE);
        infinish.setHintTextColor(Color.RED);
        infinish.setText("OK");
    }
    public void setTxtPWD()
    {
        txtPWD.setText(txtPWD.getText()+"*");
    }
}
