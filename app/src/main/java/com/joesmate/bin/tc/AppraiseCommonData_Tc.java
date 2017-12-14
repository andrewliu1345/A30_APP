package com.joesmate.bin.tc;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.BackCode;
import com.joesmate.CMD;
import com.joesmate.Cmds;
import com.joesmate.R;
import com.joesmate.bin.AppraiseOverData;
import com.joesmate.bin.BaseData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AppraiseCommonData_Tc extends BaseData {

	private List<String> listButton = new ArrayList<String>();
	private List<String> listButtonEn = new ArrayList<String>();
	private String id, name, job, age, photo;
	private static AppraiseCommonData_Tc commonData;
	private String hitMsg;
	private String appraiseResult;

	public static AppraiseCommonData_Tc getinstance() {
		if (commonData == null) {
			commonData = new AppraiseCommonData_Tc();
		}
		return commonData;
	}
	
	
	public String getAppraiseResult() {
		return appraiseResult;
	}


	public void setAppraiseResult(String appraiseResult) {
		this.appraiseResult = appraiseResult;
	}


	public String getHitMsg() {
		return hitMsg;
	}

	public void setHitMsg(String hitMsg) {
		this.hitMsg = hitMsg;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void addButton(String msg) {
		listButton.add(msg);
	}

	public List<String> getBtList() {
		return listButton;
	}

	public void addButtonEn(String msg) {
		listButtonEn.add(msg);
	}

	public List<String> getBtEnList() {
		return listButtonEn;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPhoto() {
		return photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		
		setCmd(cmd);
		setTitle(R.string.page_item_title_pj);
		int pos = 2;
		if (Arrays.equals(cmd, CMD.PJ)) {
			int audioType = AssitTool.getArrayCount(new byte[] { buffer[2],
					buffer[3] });
			setAudioType(audioType);
			Log.d(TAG, "PJ audioType："+audioType);
			int showTime = AssitTool.getArrayCount(new byte[] { buffer[4],
					buffer[5] });
			Log.d(TAG, "showTime:"+showTime);
			AppraiseOverData.getInstance().setTimeOut(showTime);

			int timeOut = AssitTool.getArrayCount(new byte[] { buffer[6],
					buffer[7] });
			Log.d(TAG, "timeOut:"+timeOut);
			setTimeOut(timeOut);

			int tellerLen = AssitTool.getArrayCount(new byte[] { buffer[8],
					buffer[9], buffer[10] });
			Log.d(TAG, "tellerLen:"+tellerLen);
			byte[] arrayTeller = new byte[tellerLen];
			System.arraycopy(buffer, 11, arrayTeller, 0, arrayTeller.length);
			
			String[] tellers = AssitTool.getSplit(arrayTeller,AssitTool.SPLIT_LINE);
			if (tellers != null) {
				if (tellers.length == 4) {
					setId(tellers[0]);
					setName(tellers[1]);
					setJob(tellers[2]);
					setAge(tellers[3]);
					
					Log.d(TAG, "getId:"+getId());
					Log.d(TAG, "getName:"+getName());
					Log.d(TAG, "getJob:"+getJob());
					Log.d(TAG, "getAge:"+getAge());
				}
			}
			
			int optionIndex = 11 + tellerLen;
			Log.d(TAG, "optionIndex："+optionIndex);
			int optionLen = AssitTool.getArrayCount(new byte[] { buffer[optionIndex],
					buffer[optionIndex + 1], buffer[optionIndex +2] });
			byte[] arrayOption = new byte[optionLen];
			optionIndex += 3;
			System.arraycopy(buffer, optionIndex, arrayOption, 0, arrayOption.length);
			String[] options = AssitTool.getSplit(arrayOption,AssitTool.SPLIT_LINE);
			listButtonEn.clear();
			listButton.clear();
			for(String s : options){
				Log.d(TAG, "option："+s);
				String ch = AssitTool.getSplit(s, AssitTool.SPLIT_COMMA, false);
				String en = AssitTool.getSplit(s, AssitTool.SPLIT_COMMA, true);
				Log.d(TAG, "ch:"+ch);
				Log.d(TAG, "en:"+en);
				if(ch != null)
				listButton.add(ch);
				if(en != null)
				listButtonEn.add(en);
			}
			
			optionIndex += optionLen;
			int hitLen = AssitTool.getArrayCount(new byte[]{buffer[optionIndex],buffer[optionIndex+1],buffer[optionIndex+2]});
			
			byte[] arrayHit = new byte[hitLen];
			optionIndex += 3;
			System.arraycopy(buffer, optionIndex, arrayHit, 0, hitLen);
			String hitMsg = AssitTool.getString(arrayHit, AssitTool.UTF_8);
			Log.d(TAG, "hitMsg："+hitMsg);
			setHitMsg(hitMsg);
			
			optionIndex += hitLen;
			int showLen = AssitTool.getArrayCount(new byte[]{buffer[optionIndex],buffer[optionIndex+1],buffer[optionIndex+2]});
			Log.d(TAG, "showLen："+showLen);
			byte[] arrayShow = new byte[showLen];
			optionIndex += 3;
			System.arraycopy(buffer, optionIndex, arrayShow, 0, showLen);
			String showMsg = AssitTool.getString(arrayShow, AssitTool.UTF_8);
			Log.d(TAG, "showMsg："+showMsg);
			AppraiseOverData.getInstance().setShowMsg(showMsg);
			legalData();
		}else if(Arrays.equals(cmd, CMD.PH)){
			pos = 2;
			int audioType = buffer[pos];
			Log.d(TAG, "PH audioType："+audioType);
			setAudioType(audioType);
			
			pos = 3;
			int showTime = buffer[pos];
			AppraiseOverData.getInstance().setTimeOut(showTime);
			
			pos = 4;
			int timeOut = buffer[pos];
			setTimeOut(timeOut);
			
			pos = 5;
			int optionLen = buffer[pos];
			byte[] arrayOption = new byte[optionLen];
			
			pos = 6;
			System.arraycopy(buffer, pos, arrayOption, 0, optionLen);
			String[] options = AssitTool.getSplit(arrayOption,AssitTool.SPLIT_LINE);
			listButtonEn.clear();
			listButton.clear();
			for(String s : options){
				Log.d(TAG, "option："+s);
				String ch = AssitTool.getSplit(s, AssitTool.SPLIT_COMMA, false);
				String en = AssitTool.getSplit(s, AssitTool.SPLIT_COMMA, true);
				Log.d(TAG, "ch:"+ch);
				Log.d(TAG, "en:"+en);
				if(ch != null)
				listButton.add(ch);
				if(en != null)
				listButtonEn.add(en);
			}
			
			pos += optionLen;
			int hitLen = buffer[pos];
			byte[] arrayHit = new byte[hitLen];
			
			pos += 1;
			System.arraycopy(buffer, pos, arrayHit, 0, hitLen);
			String hitMsg = AssitTool.getString(arrayHit, AssitTool.UTF_8);
			Log.d(TAG, "hitMsg："+hitMsg);
			setHitMsg(hitMsg);
			
			pos += hitLen;
			int showLen = buffer[pos];
			byte[] arrayShow = new byte[showLen];
			
			pos +=1;
			System.arraycopy(buffer, pos, arrayShow, 0, showLen);
			String showMsg = AssitTool.getString(arrayShow, AssitTool.UTF_8);
			Log.d(TAG, "showMsg："+showMsg);
			AppraiseOverData.getInstance().setShowMsg(showMsg);
			
			backData(new byte[]{0,0});
			
			legalData();
		}

	}
	
	public void backPK(String erroCode,int result){
		String backCode = Cmds.BACK_PJ;
		if(BackCode.CODE_00.equals(erroCode)){
			backCode += (erroCode + (result < 10 ? "0"+result : ""+result));
			setAppraiseResult(listButton.get(result -1));
		}else{
			backCode += erroCode;
		}
		backData(backCode.getBytes());
		
	}
	
	public void backPH(byte [] backCode,int erroCode,int result){
		byte[] arrayBack = new byte[3];
		if(Arrays.equals(CMD.CODE_0000H, backCode)){
			arrayBack[0] = CMD.CODE_00H[0];
			arrayBack[1] = CMD.CODE_00H[0];
			arrayBack[2] = (byte) result;
			setAppraiseResult(listButton.get(result -1));
		}else{
			arrayBack[0] = CMD.CODE_1051H[0];
			arrayBack[1] = CMD.CODE_1051H[1];
			arrayBack[2] = (byte) erroCode;
		}
		backData(arrayBack);
	}


}
