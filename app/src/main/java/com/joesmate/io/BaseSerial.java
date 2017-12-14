package cn.inhuasoft.gwq;

public class BaseSerial {
	static {
		System.loadLibrary("sercomm");
	}

	public  native int open(String device, int baud);

	public  native int close(int fd);

	public  native int read(int fd, byte[] buf, int len, int timeout);

	public  native int write(int fd, byte[] buf, int len);
}
