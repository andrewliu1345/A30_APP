package com.joesmate.test;

import com.joesmate.FileInf;
import com.joesmate.bin.AppraiseCommonData;
import com.joesmate.bin.InputCharacterData;
import com.joesmate.bin.InteractiveMsgData;
import com.joesmate.bin.QuestionData;

public class InitBin {

	public static void initData(){
		initInteractiveMsgData();
		initAppraise();
		initInputCharacterData();
		initQuestionData(1);
	}
	static void initInteractiveMsgData(){
		InteractiveMsgData data = InteractiveMsgData.getInstance();
		data.setTimeOut(30);
		//data.setAudioType(AudioType.TYPE_08);
		data.setMessage("你当前汇款金额为1000万");
	}
	static void initAppraise(){
		AppraiseCommonData data = AppraiseCommonData.getinstance();
		String[] strs = {"非常满意","满意","一般","差","非常差"};
		for(String str : strs){
			data.addButton(str);
		}
		data.setTimeOut(30);
		data.setAge("5");
		data.setJob("高级柜员");
		data.setName("李秋水");
		data.setPhoto(FileInf.HEAD+"/ph.png");
	}
	static void initInputCharacterData(){
		InputCharacterData data = InputCharacterData.getInstance();
		data.setTimeOut(30);
		data.setTitle("请出入账号");
		data.setHitMsg("请在右侧输入你的账号");
		data.setAudioType(1);
		//data.setKeyBoardType("01");
		data.setDisplayType(0);
	}
	public static void initQuestionData(int i){
		String[] strs = { "A 、一直投资于股票等高风险", "B 、一直投资于股票等高风险", "C、 一直投资于股票等高风险", "D、 一万",
				"E、 2万", "F、 一直投资于股票等高风险", "G、 一直投资于股票等高风险" };
		
		String[] options = {"A","B","C","D"};
		QuestionData data = QuestionData.getInstance();
		data.setTimeOut(30);
		//data.setAudioType("12");
		data.setOptionItems(options);
		data.setTotalNum(2);
		data.setCurIndex(i);
		data.setQuestionTitle(i+"、请选择你的年收入以及你的购买股票(多选题)");
		data.setOptionType(QuestionData.TYPE_OPTION_DOUBLE);
	}

}
