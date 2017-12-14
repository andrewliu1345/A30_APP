package com.joesmate.bin;

import java.util.Arrays;

import android.util.Log;

import com.joesmate.AssitTool;
import com.joesmate.CMD;
import com.joesmate.R;

public class QuestionData extends BaseData{
	public static final int TYPE_OPTION_SINGLE = 1;
	public static final int TYPE_OPTION_DOUBLE = 2;
	private String questionTitle;
	
	private int optionType,totalNum,curIndex; //optionType 1单选，2多选
	private String[] optionItems;
	private byte[] dataBuffer;

	private static QuestionData questionData;
	public QuestionData(){
		setTitle(R.string.page_item_title_question);
	}
	public static QuestionData getInstance(){
		if(questionData == null){
			questionData = new QuestionData();
		}
		return questionData;
	}
	
	public byte[] getDataBuffer() {
		return dataBuffer;
	}

	public void setDataBuffer(byte[] dataBuffer) {
		this.dataBuffer = dataBuffer;
	}

	public String[] getOptionItems() {
		return optionItems;
	}

	public void setOptionItems(String[] optionItems) {
		this.optionItems = optionItems;
	}
	
	public int getTotalNum() {
		return totalNum;
	}

	public void setTotalNum(int totalNum) {
		this.totalNum = totalNum;
	}

	public int getCurIndex() {
		return curIndex;
	}

	public void setCurIndex(int curIndex) {
		this.curIndex = curIndex;
	}

	public String getQuestionTitle() {
		return questionTitle;
	}


	public void setQuestionTitle(String questionTitle) {
		this.questionTitle = questionTitle;
	}


	public int getOptionType() {
		return optionType;
	}


	public void setOptionType(int optionType) {
		this.optionType = optionType;
	}

	@Override
	public void setData(byte[] buffer, byte[] cmd) {
		setCmd(cmd);
		if(buffer == null || buffer.length < 9){
			return;
		}
		int audioType = buffer[2];
		setAudioType(audioType);
		
		int timeOut = buffer[3];
		setTimeOut(timeOut);
		
		optionType = buffer[4];
		curIndex = buffer[5];
		totalNum = buffer[6];
		Log.d(TAG, "optionType:"+optionType);
		Log.d(TAG, "curIndex:"+curIndex);
		Log.d(TAG, "totalNum:"+totalNum);
		
		int optionLen = buffer[7];
		Log.d(TAG, "optionLen:"+optionLen);
		byte[] arrayOption = new byte[optionLen];
		System.arraycopy(buffer, 8, arrayOption, 0, arrayOption.length);
		setOptionItems(AssitTool.getSplit(arrayOption,AssitTool.SPLIT_LINE));
		
		int dataIndex = buffer[7 + optionLen + 1];
		Log.d(TAG, "dataIndex:"+dataIndex);
		byte[] data = new byte[buffer.length - 9 - optionLen ];
		System.arraycopy(buffer, 7 + optionLen + 2, data, 0, data.length);
		FileData fileData = new FileData(data.length);
		fileData.setBuffer(data);
		fileDatas.add(fileData);
		if(dataIndex == 1){
			dataBuffer = getTotalFileData(fileDatas);
			fileDatas.clear();
			legalData();
		}
		sendConfirmCode();
	}
	private void sendConfirmCode(){
		backData(CMD.CODE_0000H);
	}
	public void erroCode(byte[] erroCode){
		byte[] arrayCode = new byte[CMD.CODE_0000H.length + erroCode.length];
		System.arraycopy(CMD.CODE_0000H, 0, arrayCode, 0, CMD.CODE_0000H.length);
		System.arraycopy(erroCode, 0, arrayCode, CMD.CODE_0000H.length, erroCode.length);
		backData(arrayCode);
	}
	public void response(byte[] operation,int index,String answer){
		int cmdLen = CMD.CODE_0000H.length;
		int operationLen = operation.length;
		int indexLen = 1;
		int answerLen =0;
		
		
		if(Arrays.equals(operation, CMD.CODE_01H)){
			Log.d(TAG, "answer："+answer);
			answerLen = answer.length();
		}
		byte[] arrayResponse = new byte[cmdLen+operationLen+indexLen+answerLen];
		System.arraycopy(CMD.CODE_0000H, 0, arrayResponse, 0, cmdLen);
		System.arraycopy(operation, 0, arrayResponse, cmdLen, operationLen);
		System.arraycopy(new byte[]{(byte) index}, 0, arrayResponse, cmdLen + operationLen, indexLen);
		if(Arrays.equals(operation, CMD.CODE_01H)){
			System.arraycopy(answer.getBytes(), 0, arrayResponse, cmdLen + operationLen+indexLen, answerLen);
		}
		backData(arrayResponse);
	}
/*	private String[] getOptionItems(String strOption){
		Log.d(TAG, "strOption:"+strOption);
		return strOption.split("|");
	}*/


}
