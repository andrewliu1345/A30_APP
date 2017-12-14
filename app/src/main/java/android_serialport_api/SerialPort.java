/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android_serialport_api;
import java.io.File;
import java.io.IOException;
import cn.inhuasoft.gwq.BaseSerial;

public class SerialPort {

	private static final String TAG = "SerialPort";

	private static int fid;
	private static BaseSerial baseSerial;

	public SerialPort(File device, int baudrate, int flags) throws SecurityException, IOException {
		baseSerial = new BaseSerial();
		fid = baseSerial.open(device.getAbsolutePath(), baudrate);
	}

	public  int close()
	{
		if(fid > 0)
			baseSerial.close(fid);
		fid = 0;
		return 0;
	}

	public  int read(byte[] buf, int len, int timeout)
	{
		if(fid > 0)
			return baseSerial.read(fid, buf, len, timeout);
		return 0;
	}

	public  int write(byte[] buf, int len)
	{
		if(fid > 0)
			return baseSerial.write(fid, buf, len);
		return 0;
	}
}
