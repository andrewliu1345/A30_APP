package cn.inhuasoft.gwq;

public class BaseUsbHid {
	static {
		System.loadLibrary("usbcomm");
	}

	public static native int open(String device);
	public static native int close(int fd);
	public static native int read(int fd, byte[] buf, int len);
	public static native int write(int fd, byte[] buf, int len);
}
