package com.joesmate;

import com.joesmate.bin.keyBoard.SerialRequestFrame;
import com.joesmate.bin.keyBoard.SerialResponseFrame;
import com.joesmate.bin.keyBoard.SerialUtil;

import java.security.Key;

/**
 * Created by andre on 2017/12/16 .
 */

public class KeyBordProtocol {
    private static KeyBordProtocol m_KeyBordProtocol;

    public static KeyBordProtocol getInstance() {
        if (m_KeyBordProtocol == null) {
            m_KeyBordProtocol = new KeyBordProtocol();
        }
        return m_KeyBordProtocol;
    }

    public byte[] getCheckValues(byte[] in) {
        byte[] BitZero = new byte[8];
        byte[] out = new byte[8];
        try {
            out = SM4Encrypt(BitZero, in);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

    public byte[] SM4Dcrypt(byte[] in, byte[] key) {
        int pos = 0;
        byte[] writeData = new byte[2 + 1 + in.length + key.length];
        System.arraycopy(CMD.SD_CMD_SM4_DEC, 0, writeData, pos, 2);
        pos = pos + 2;

        System.arraycopy(new byte[]{(byte) in.length}, 0, writeData, pos, 1);
        pos = pos + 1;
        System.arraycopy(in, 0, writeData, pos, in.length);
        pos += in.length;
        System.arraycopy(key, 0, writeData, pos, 16);
        pos += key.length;
        final byte[] wbyte = new SerialRequestFrame().make_SD_Package(writeData);
        byte[] tmp = toSend(wbyte, wbyte.length);
        if (tmp != null) {
            if (tmp[4] == 0 && tmp[5] == 0) {
                int len = tmp[6];
                byte[] out = new byte[len];
                System.arraycopy(tmp, 7, out, 0, len);
                return out;
            }
        }
        return null;
    }

    public byte[] SM4Encrypt(byte[] in, byte[] key) {
        int pos = 0;
        byte[] writeData = new byte[2 + 1 + in.length + key.length];
        System.arraycopy(CMD.SD_CMD_SM4_ENC, 0, writeData, pos, 2);
        pos = pos + 2;

        System.arraycopy(new byte[]{(byte) in.length}, 0, writeData, pos, 1);
        pos = pos + 1;
        System.arraycopy(in, 0, writeData, pos, in.length);
        pos += in.length;
        System.arraycopy(key, 0, writeData, pos, 16);
        pos += key.length;
        final byte[] wbyte = new SerialRequestFrame().make_SD_Package(writeData);
        byte[] tmp = toSend(wbyte, wbyte.length);
        if (tmp != null) {
            if (tmp[4] == 0 && tmp[5] == 0) {
                int len = tmp[6];
                byte[] out = new byte[len];
                System.arraycopy(tmp, 7, out, 0, len);
                return out;
            }
        }
        return null;
    }

    public byte[] toSend(byte[] data, int len) {
        final SerialResponseFrame serialResponse = new SerialResponseFrame();
        SerialUtil.getInstance().getSerialPort().write(data, len);
        SerialUtil.getInstance().setStop(false);
        SerialUtil.getInstance().getmSReadThread().run();
        SerialResponseFrame.lock.lock();
        byte[] rbyte = serialResponse.get_SD_Rbyte();
        SerialUtil.getInstance().setStop(true);
        SerialResponseFrame.lock.unlock();
        if (rbyte.length > 6) {

            return rbyte;
        }
        return null;
    }
}
