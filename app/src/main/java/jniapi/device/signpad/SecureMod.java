package jniapi.device.signpad;

public class SecureMod {
	
	public static final int RESULT_OK = 0;
	
	public static final int ERROR_OPEN_DEVICE_FAIL = -21;
	public static final int ERROR_TIME_OUT = -28;
	
	
	public static final int PRIMARY_KEY_TYPE = 0;
	public static final int WORKING_KEY_TYPE = 1;
	
	public native static int genSM2PairFromJNI();					// 产生256Bit SM2密钥对
	public native static int getSM2PubFromJNI(byte[] sm2Pub);		// 导出 SM2 公钥
	
	public native static int setPrimKeyFromJNI(byte[] primKey);		// 导入主密钥
	public native static int setSignKeyFromJNI(byte[] signKey);		// 导入工作密钥

	public native static int verifyKeyFromJNI(int keyType, byte[] verifiedData);	// 校验密钥
	
	public native static int getSignDataFromJNI(byte[] encSignData);	// 逐包读取密文
	public native static int destroySingKeyFromJNI();					// 销毁工作密钥
		
	
	public native static int sm2EncryptFromJNI(byte[] sm2PubX, 			//SM2公钥加密软算法
			byte[] sm2PubY, byte[] plainData, byte[] ciphData); 
	
    public native static int sm4EncryptFromJNI(byte[] key, 				//SM4 加密软算法
    		byte[] in, byte[] out, int mode);
    
    public native static int sm4DecryptFromJNI(byte[] key, 				// SM4 解密软算法
    		byte[] in, byte[] out, int mode);	
    
    static {
        System.loadLibrary("signpad_sm");
    }	
}
