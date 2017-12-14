package com.joesmate.pageItem;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.joesmate.AudioType;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.SharedpreferencesData;
import com.joesmate.bin.InteractiveMsgData;
import com.joesmate.widget.HtmlView;

/**
 * 信息交互
 * <p>
 * Html数据/业务PC解析数据流
 * 
 * @author yc.zhang
 * 
 */
public class InteractiveMsg extends BasePageItem {

	public static final String TAG = "InteractiveMsg";
	InteractiveMsgData msgData;
	HtmlView hvContent;

	public InteractiveMsg(Context context) {
		super(context);

		msgData = InteractiveMsgData.getInstance();

		hvContent = (HtmlView) findViewById(id.page_item_interactive_msg_content);
	}
	public void init() {
		hvContent.setWebTextSize(SharedpreferencesData.getInstance().getJhFontSize());
		if (AudioType.TYPE_00.equals(msgData.getAudioType())) {
			isPlayAudio = false;
			super.init(msgData, ButtonType.NOTHING);
		} else {
			isPlayAudio = true;
			super.init(msgData, ButtonType.OK_CANCEL);
		}
		hvContent.loadChar(msgData.getMessage());
	}

	@Override
	public void onClick(View v) {

		if (v == btOK) {
			msgData.operation((byte) 1);
		} else if (v == btCancel) {
			msgData.operation((byte) 0);
		}
		toPlay();
	}

	@Override
	public View getContentView() {
		return inflate(getContext(), R.layout.page_item_interactivemsg, null);
	}

	@Override
	public void timeOut() {
		// TODO 进入播放\返回超时数据
		Log.d(TAG, "timeOut");
		msgData.operation((byte) 0);
		toPlay();
	}

}
