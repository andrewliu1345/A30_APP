package com.joesmate.crypto;

/**
 * Created by andre on 2017/12/16 .
 */

public class SM4Utils {
    public static byte[] SM4_ECB(byte[] in, byte[] key, int CryptFlag) {
        byte[] out = null;
        SM4_Context ctx = new SM4_Context();
        ctx.isPadding = false;
        ctx.mode = CryptFlag;
        SM4 sm4 = new SM4();
        try {
            if (CryptFlag == SM4.SM4_DECRYPT) {
                sm4.sm4_setkey_dec(ctx, key);
            } else {
                sm4.sm4_setkey_enc(ctx, key);
            }

            byte[] tmp = sm4.sm4_crypt_ecb(ctx, in);
            int len = tmp.length;
            out = new byte[len];
            System.arraycopy(tmp, 0, out, 0, len);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return out;
    }

    public static byte[] getCheckValues(byte[] in) {
        byte[] BitZero = new byte[8];
        byte[] out = new byte[8];
        try {
            out = SM4_ECB(BitZero, in, SM4.SM4_ENCRYPT);
            return out;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return out;
    }

//    public byte[] SM4_CBC(byte[] in, byte[] key,int CryptFlag) throws Exception {
//        byte[] out = null;
//        SM4_Context ctx = new SM4_Context();
//        ctx.isPadding = true;
//        ctx.mode = CryptFlag;
//        SM4 sm4 = new SM4();
//        sm4.sm4_setkey_enc(ctx, key);
//        out = sm4.sm4_crypt_cbc(ctx, in);
//        return out;
//    }


}
