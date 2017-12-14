package com.joesmate.bin.keyBoard;

import android.content.Intent;
import android.util.Log;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.CMD;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import android_serialport_api.SerialPort;


/**
 * Created by zwzhu on 2016/1/15.
 */
public class SerialUtil {

    public static SerialUtil serialUtil = null;

    public static String ttyDevice = "/dev/ttyS4";

    public static int baudrate = 9600;

    public static int flag = 0;

    private OnDataReceiveListener onDataReceiveListener = null;
    private OnKeyReceiveListener onKeyReceiveListener = null;

    public static SerialPort serialPort = null;

    public boolean isStop;//= false;

    private ReadThread mReadThread;
    private MReadThread mMReadThread;
    private SReadThread mSReadThread;

    public static enum KEY {
        ENTER, CANCEL, NUMBER
    }

    public static Object object;

    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }

    public interface OnKeyReceiveListener {
        void onKeyReceive(KEY key, int data);
    }

    public void setOnDataReceiveListener(
            OnDataReceiveListener dataReceiveListener) {
        onDataReceiveListener = dataReceiveListener;
    }

    public void setOnKeyReceiveListener(OnKeyReceiveListener keyReceiveListener) {
        onKeyReceiveListener = keyReceiveListener;
    }

    private SerialUtil() {
        try {
            serialPort = new SerialPort(new File(ttyDevice), baudrate, flag);
            object = new Object();
        } catch (IOException e) {
            e.printStackTrace();
        }

        mReadThread = new ReadThread();
        mMReadThread = new MReadThread();
        mSReadThread = new SReadThread();
        isStop = true;
        //mReadThread.start();
    }

    public static synchronized SerialUtil getInstance() {
        if (serialUtil == null) {
            serialUtil = new SerialUtil();
        }
        return serialUtil;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public ReadThread getmReadThread() {
        return mReadThread;
    }

    public MReadThread getmMReadThread() {
        return mMReadThread;
    }

    public SReadThread getmSReadThread() {
        return mSReadThread;
    }

    public void setStop(boolean isStop) {
        this.isStop = isStop;
    }

    public class ReadThread extends Thread {

        @Override
        public void run() {
            //super.run();
            SerialResponseFrame.lock.lock();
            byte[] buffer = new byte[1024];
            int size = 0;
            int copylen = 0;
            byte[] temp = new byte[1024];
            byte[] flagbyte = new byte[2];
            Intent intent1, intent2, intent3;
            intent1 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR);
            intent2 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER);
            intent3 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_BACK);
            while (!isStop && !isInterrupted()) {
                try {
                    Arrays.fill(temp, (byte) 0);
                    size = serialPort.read(temp, 1, 2);
                    sleep(10);
                    if (size > 0) {
                        Log.d("SerialUtil", "length is:" + size + ",data is:" + new String(temp, 0, size));
                        //bill add start
                        if (size == 1) {
                            Log.d("SerialUtil", " ------ one size------");
                            //input one char
                            if (temp[0] == 0x30 || temp[0] == 0x31 || temp[0] == 0x32 || temp[0] == 0x33 ||
                                    temp[0] == 0x34 || temp[0] == 0x35 || temp[0] == 0x36 || temp[0] == 0x37
                                    || temp[0] == 0x38 || temp[0] == 0x39 || temp[0] == 0x2a) {
                                // AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                intent1.putExtra("inputChar", temp[0]);
                                App.getInstance().sendBroadcast(intent1);
                                byte[] send = new byte[]{temp[0]};
                                if (App.isThirdpartySerial) {
                                    App.getInstance().fitManagerSerial.writeSomeBuffer(send, 1);
                                }
                                Log.d("SerialUtil", "send broadcast: input char key" + temp[0]);
                                continue;

                            }
                            //input enter
                            if (temp[0] == 0x0d) {
                                // AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                App.getInstance().sendBroadcast(intent2);
                                byte[] send = new byte[]{temp[0]};
                                if (App.isThirdpartySerial) {
                                    App.getInstance().fitManagerSerial.writeSomeBuffer(send, 1);
                                }
                                Log.d("SerialUtil", "send broadcast: input enter key");
                                break;
                            }

                            if (temp[0] == 0x08) {
                                // AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                App.getInstance().sendBroadcast(intent3);
                                byte[] send = new byte[]{temp[0]};
                                if (App.isThirdpartySerial) {
                                    App.getInstance().fitManagerSerial.writeSomeBuffer(send, 1);
                                }
                                Log.d("SerialUtil", "send broadcast: input cancel key");
                                continue;
                            }

                        } else {

                            //bill add end
                            System.arraycopy(temp, size - 2, flagbyte, 0, 2);
                            if (!Arrays.equals(flagbyte, CMD.SD_CMD_TAIL)) {
                                System.arraycopy(temp, 0, buffer, copylen, size);
                                copylen += size;
                                continue;
                            } else {
                                System.arraycopy(temp, 0, buffer, copylen, size);
                                copylen += size;
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SerialResponseFrame.lock.unlock();
                    return;
                }

            }
            Log.d("SerialUtil", "data finish,length is:" + size + ",data is:" + new String(buffer, 0, copylen));
            SerialResponseFrame.lock.unlock();
            if (size > 0) {
                if (null != onDataReceiveListener) {
                    onDataReceiveListener.onDataReceive(buffer, copylen);
                }
            }
        }
    }


    //密文读取线程
    public class SReadThread extends Thread {

        @Override
        public void run() {
            //super.run();
            SerialResponseFrame.lock.lock();
            byte[] buffer = new byte[1024];
            int size = 0;
            int copylen = 0;
            byte[] temp = new byte[1024];
            byte[] flagbyte = new byte[2];
         /*   Intent intent1,intent2,intent3;
            intent1 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR);
            intent2 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER);
            intent3 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_BACK);*/
            while (!isStop && !isInterrupted()) {
                try {
                    Arrays.fill(temp, (byte) 0);
                    size = serialPort.read(temp, 1, 2);
                    sleep(10);
                    if (size > 0) {
                        Log.d("SerialUtil", " sread  length is:" + size + ",data is:" + new String(temp, 0, size));
                        for (int i = 0; i < size; ++i) {
                            //input one char
                            if (temp[i] == 0x2a) {
                                //AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                //intent1.putExtra("inputChar", temp[i]);
                                //App.getInstance().sendBroadcast(intent1);
                                if (null != onKeyReceiveListener) {
                                    onKeyReceiveListener.onKeyReceive(KEY.NUMBER, temp[i]);
                                }
                                byte[] send = new byte[]{temp[i]};
                                if (App.isThirdpartySerial) {
                                    App.getInstance().fitManagerSerial.writeSomeBuffer(send, 1);
                                }
                                Log.d("SerialUtil", " sread  send broadcast: input char key" + temp[0]);
                                continue;

                            } else if (temp[i] == 0x08) {
                                // AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                //App.getInstance().sendBroadcast(intent3);
                                if (null != onKeyReceiveListener) {
                                    onKeyReceiveListener.onKeyReceive(KEY.CANCEL, 0);
                                }
                                byte[] send = new byte[]{temp[i]};
                                if (App.isThirdpartySerial) {
                                    App.getInstance().fitManagerSerial.writeSomeBuffer(send, 1);
                                }
                                Log.d("SerialUtil", " sread  send broadcast: input cancel key");
                                continue;
                            } else {
                                Log.d("SerialUtil", "sread  copy a byte to buffer");
                                System.arraycopy(new byte[]{temp[i]}, 0, buffer, copylen, 1);
                                copylen = copylen + 1;
                            }
                        }

                        //head + tail = 2 +　2
                        if (copylen > 3) {
                            System.arraycopy(buffer, copylen - 2, flagbyte, 0, 2);
                            if (Arrays.equals(flagbyte, CMD.SD_CMD_TAIL)) {
                                Log.d("SerialUtil", "sread  find SD_CMD_TAIL");
                                break;
                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    SerialResponseFrame.lock.unlock();
                    return;
                }

            }
            Log.d("SerialUtil", "sread data finish,length is:" + copylen + ",data is:" + new String(buffer, 0, copylen));
            SerialResponseFrame.lock.unlock();
            if (copylen > 0) {
                if (null != onDataReceiveListener) {
                    onDataReceiveListener.onDataReceive(buffer, copylen);
                }
            }
            Log.d("SerialUtil", " ---- sread end ");
        }
    }

    //明文读取线程
    public class MReadThread extends Thread {

        @Override
        public void run() {
            SerialResponseFrame.lock.lock();
            int size = 0;
            byte[] temp = new byte[30];
          /*  Intent intent1,intent2,intent3;
            intent1 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_ONECHAR);
            intent2 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_ENTER);
            intent3 = new Intent(AppAction.ACTION_BROADCAST_INPUT_PW_BACK);*/
            while (!isStop && !isInterrupted()) {
                try {
                    size = serialPort.read(temp, 1, 2);
                    sleep(10);
                    if (size > 0) {
                        Log.d("SerialUtil", " mread  length is:" + size + ",data is:" + new String(temp, 0, size));
                        //bill add start
                        for (int i = 0; i < size; ++i) {

                            Log.d("SerialUtil", " ------ one size------");
                            //input one char
                            if (temp[i] == 0x30 || temp[i] == 0x31 || temp[i] == 0x32 || temp[i] == 0x33 ||
                                    temp[i] == 0x34 || temp[i] == 0x35 || temp[i] == 0x36 || temp[i] == 0x37
                                    || temp[i] == 0x38 || temp[i] == 0x39) {
                                // AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                //intent1.putExtra("inputChar", temp[i]);
                                // App.getInstance().sendBroadcast(intent1);
                                if (null != onKeyReceiveListener) {
                                    onKeyReceiveListener.onKeyReceive(KEY.NUMBER, temp[i]);
                                }
                                byte[] send = new byte[]{temp[i]};
                                if (App.isThirdpartySerial) {
                                    App.getInstance().fitManagerSerial.writeSomeBuffer(send, 1);
                                }
                                Log.d("SerialUtil", "send broadcast: input char key" + temp[i]);
                                // continue;

                            }
                            //input enter
                            if (temp[i] == 0x0d) {
                                //  AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                // App.getInstance().sendBroadcast(intent2);
                                if (null != onKeyReceiveListener) {
                                    onKeyReceiveListener.onKeyReceive(KEY.ENTER, 0);
                                }
                                byte[] send = new byte[]{temp[i]};
                                if (App.isThirdpartySerial) {
                                    App.getInstance().fitManagerSerial.writeSomeBuffer(send, 1);
                                }
                                Log.d("SerialUtil", "send broadcast: input enter key");
                                SerialResponseFrame.lock.unlock();
                                return;
                            }
                            //input cancel
                            if (temp[i] == 0x08) {
                                //  AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
                                //App.getInstance().sendBroadcast(intent3);
                                if (null != onKeyReceiveListener) {
                                    onKeyReceiveListener.onKeyReceive(KEY.CANCEL, 0);
                                }
                                byte[] send = new byte[]{temp[i]};
                                if (App.isThirdpartySerial) {
                                    App.getInstance().fitManagerSerial.writeSomeBuffer(send, 1);
                                }
                                Log.d("SerialUtil", "send broadcast: input cancel key");
                                //continue;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    SerialResponseFrame.lock.unlock();
                    return;
                }

            }
            Log.d("SerialUtil", "mread end ........");
            SerialResponseFrame.lock.unlock();
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {

        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mMReadThread != null) {
            mMReadThread.interrupt();
        }
        if (serialPort != null) {
            serialPort.close();
        }
    }
}
