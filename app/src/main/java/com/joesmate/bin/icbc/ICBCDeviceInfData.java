package com.joesmate.bin.icbc;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.joesmate.App;
import com.joesmate.AssitTool;
import com.joesmate.Cmds;
import com.joesmate.DeviceSettings;
import com.joesmate.bin.BaseData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

public class ICBCDeviceInfData extends BaseData {

	private static ICBCDeviceInfData iCBCDeviceInfData;

	private static String cpuUsageRate = " ";
	private static String mem_Total = " ";
	private static String mem_Avail = " ";
	private static String flash_Total = " ";
	private static String flash_Avail = " ";
	private static String hw_Version = " ";
	private static String dev_Voltage = " ";//"3800mV";
	private static String dev_Temperature = " "; //"30℃";
	private static String screen_Pixel = " ";//1280*800
	private static String dev_Vid = " ";
	private static String dev_Pid = " ";

	public ICBCDeviceInfData(){
		IntentFilter filterbat = new IntentFilter();
		filterbat.addAction(Intent.ACTION_BATTERY_CHANGED);
		App.getInstance().registerReceiver(broadcastReceiverBattery, filterbat);
	}

	public static ICBCDeviceInfData getInstance() {
		if (iCBCDeviceInfData == null) {
			iCBCDeviceInfData = new ICBCDeviceInfData();
		}
		return iCBCDeviceInfData;
	}

	private static void getCpuInfo()
	{
		String Result=null;
		try{
			Process p=Runtime.getRuntime().exec("top -n 1");

			BufferedReader br=new BufferedReader(new InputStreamReader(p.getInputStream ()));
			while((Result=br.readLine())!=null)
			{
				if(Result.trim().length()<1){
					continue;
				}else{
					String[] CPUusr = Result.split(" ");
					String[] CPUusage = CPUusr[1].split(",");
					cpuUsageRate = CPUusage[0];
					break;
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getFlashInfo()
	{
		File root = Environment.getDataDirectory();
		StatFs sf = new StatFs(root.getPath());
		long blockSize = sf.getBlockSizeLong();
		long availCount = sf.getAvailableBlocksLong();
		long totalCount = sf.getBlockCountLong();
		flash_Avail = (availCount * blockSize) / 1024 / 1024 +"MB";
		flash_Total = (totalCount * blockSize) / 1024 / 1024 +"MB";
	}

	private void getAvailableMemory() {
		ActivityManager am = (ActivityManager) App.getInstance().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(memoryInfo);
		mem_Avail = memoryInfo.availMem/1024/1024 + "MB";
	}

	private void getTotalMemorySize() {
		String dir = "/proc/meminfo";
		try {
			FileReader fr = new FileReader(dir);
			BufferedReader br = new BufferedReader(fr, 2048);
			String memoryLine = br.readLine();
			String subMemoryLine = memoryLine.substring(memoryLine.indexOf("MemTotal:"));
			br.close();
			mem_Total =  Integer.parseInt(subMemoryLine.replaceAll("\\D+", "")) /1024 + "MB";
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getScreenInfo() {
		WindowManager wm = (WindowManager) App.getInstance().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics  dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		screen_Pixel = dm.widthPixels + "*" + dm.heightPixels;
	}

	private void getProductInfo() {
		dev_Vid = " ";
		dev_Pid = " ";
	}

	private void getDeviceInfo()
	{
		hw_Version = DeviceSettings.getVersionName(App.getInstance().getApplicationContext());
		getCpuInfo();
		getTotalMemorySize();
		getAvailableMemory();
		getFlashInfo();
		getScreenInfo();
		getProductInfo();
	}
	@Override
	public void setData(byte[] buffer, byte[] cmd) {


		getDeviceInfo();
		int cpuUsageRateLen = cpuUsageRate.length();
		int mem_TotalLen = mem_Total.length();
		int mem_AvailLen = mem_Avail.length();
		int flash_TotalLen = flash_Total.length();
		int flash_AvailLen = flash_Avail.length();
		int hw_VersionLen = hw_Version.length();
		int dev_VoltageLen = dev_Voltage.length();
		int dev_TemperatureLen = dev_Temperature.length();
		int screen_PixelLen = screen_Pixel.length();
		int dev_VidLen = dev_Vid.length();
		int dev_PidLen = dev_Pid.length();

		byte[] arrayData = new byte[cpuUsageRateLen + mem_TotalLen +
				mem_AvailLen + flash_TotalLen +
				flash_AvailLen + hw_VersionLen +
				dev_VoltageLen + dev_TemperatureLen +
				screen_PixelLen + dev_VidLen +
				dev_PidLen + 2*12 ];

		int pos = 0;
		System.arraycopy(Cmds.CMD_GI.getBytes(), 0, arrayData, pos,2);

		/*pos = 2;
		System.arraycopy(new byte[]{0x30, 0x30}, 0, arrayData, pos, 2);*/

		pos = 2;
		byte[] arraycpuUsageRate = AssitTool.integerToArray(cpuUsageRateLen);
		System.arraycopy(arraycpuUsageRate, 0, arrayData, pos, 2);

		pos = 4;
		try {
			Log.d(TAG,"cpuUsageRate:"+cpuUsageRate);
			System.arraycopy(cpuUsageRate.getBytes(AssitTool.UTF_8), 0, arrayData, pos, cpuUsageRateLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += cpuUsageRateLen;
		byte[] arraymem_Total = AssitTool.integerToArray(mem_TotalLen);
		System.arraycopy(arraymem_Total, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"mem_Total:"+mem_Total);
			System.arraycopy(mem_Total.getBytes(AssitTool.UTF_8), 0, arrayData, pos, mem_TotalLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += mem_TotalLen;
		byte[] arraymem_Avail = AssitTool.integerToArray(mem_AvailLen);
		System.arraycopy(arraymem_Avail, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"mem_Avail:"+mem_Avail);
			System.arraycopy(mem_Avail.getBytes(AssitTool.UTF_8), 0, arrayData, pos, mem_AvailLen);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		pos += mem_AvailLen;
		byte[] arrayflash_Total = AssitTool.integerToArray(flash_TotalLen);
		System.arraycopy(arrayflash_Total, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"flash_Total:"+flash_Total);
			System.arraycopy(flash_Total.getBytes(AssitTool.UTF_8), 0, arrayData, pos, flash_TotalLen);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		pos += flash_TotalLen;
		byte[] arrayflash_Avail = AssitTool.integerToArray(flash_AvailLen);
		System.arraycopy(arrayflash_Avail, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"flash_Avail:"+flash_Avail);
			System.arraycopy(flash_Avail.getBytes(AssitTool.UTF_8), 0, arrayData, pos, flash_AvailLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += flash_AvailLen;
		byte[] arrayhw_Version = AssitTool.integerToArray(hw_VersionLen);
		System.arraycopy(arrayhw_Version, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"hw_Version:"+hw_Version);
			System.arraycopy(hw_Version.getBytes(AssitTool.UTF_8), 0, arrayData, pos, hw_VersionLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += hw_VersionLen;
		byte[] arraydev_Voltage = AssitTool.integerToArray(dev_VoltageLen);
		System.arraycopy(arraydev_Voltage, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"dev_Voltage:"+dev_Voltage);
			System.arraycopy(dev_Voltage.getBytes(AssitTool.UTF_8), 0, arrayData, pos, dev_VoltageLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += dev_VoltageLen;
		byte[] arraydev_Temperature = AssitTool.integerToArray(dev_TemperatureLen);
		System.arraycopy(arraydev_Temperature, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"dev_Temperature:"+dev_Temperature);
			System.arraycopy(dev_Temperature.getBytes(AssitTool.UTF_8), 0, arrayData, pos, dev_TemperatureLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += dev_TemperatureLen;
		byte[] arrayscreen_Pixel = AssitTool.integerToArray(screen_PixelLen);
		System.arraycopy(arrayscreen_Pixel, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"screen_Pixel:"+screen_Pixel);
			System.arraycopy(screen_Pixel.getBytes(AssitTool.UTF_8), 0, arrayData, pos, screen_PixelLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += screen_PixelLen;
		byte[] arraydev_Vid = AssitTool.integerToArray(dev_VidLen);
		System.arraycopy(arraydev_Vid, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"dev_Vid:"+dev_Vid);
			System.arraycopy(dev_Vid.getBytes(AssitTool.UTF_8), 0, arrayData, pos, dev_VidLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += dev_VidLen;
		byte[] arraydev_Pid = AssitTool.integerToArray(dev_PidLen);
		System.arraycopy(arraydev_Pid, 0, arrayData, pos, 2);

		pos += 2;
		try {
			Log.d(TAG,"dev_Pid:"+dev_Pid);
			System.arraycopy(dev_Pid.getBytes(AssitTool.UTF_8), 0, arrayData, pos, dev_PidLen);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		pos += dev_PidLen;
		/*System.arraycopy(new byte[]{0x31}, 0, arrayData, pos, 1);*/

		backData(arrayData);

	}

	private BroadcastReceiver broadcastReceiverBattery = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction()))
			{
				//dev_Voltage = intent.getIntExtra("voltage", 0)+"mV"; //电池电压
				//dev_Temperature = intent.getIntExtra("temperature", 0)*0.1+"℃"; //电池温度
				dev_Voltage = "3800mV"; //电池电压
				dev_Temperature = "30℃"; //电池温度
			}
		}
	};

}
