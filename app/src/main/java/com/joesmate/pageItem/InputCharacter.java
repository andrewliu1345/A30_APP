package com.joesmate.pageItem;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.joesmate.App;
import com.joesmate.AppAction;
import com.joesmate.BackCode;
import com.joesmate.R;
import com.joesmate.R.id;
import com.joesmate.bin.InputCharacterData;
import com.joesmate.bin.sdcs.SDCSInputCharacterData;
import com.joesmate.page.PlayActivity;
import com.joesmate.widget.CustomText;
import com.joesmate.widget.InputKeyBoard;
import com.joesmate.widget.InputKeyBoard.KeyBoard;

public class InputCharacter extends BasePageItem {

    CustomText tvMsg, tvChara;
    InputKeyBoard inputKeyBoard;
    SDCSInputCharacterData characterData;
    int charLen;
    int displayType;
    StringBuffer stringBuffer = new StringBuffer();

    public InputCharacter(Context context) {
        super(context);
        tvMsg = (CustomText) findViewById(id.page_item_inputcharacter_msg);
        tvChara = (CustomText) findViewById(id.page_item_inputcharacter_chara);
        inputKeyBoard = (InputKeyBoard) findViewById(id.page_item_inputcharacter_keyboard);
        inputKeyBoard.setOnKeyBoardListener(boardListener);

        characterData = SDCSInputCharacterData.getInstance();
    }

    public void init() {
        super.init(characterData, ButtonType.NOTHING);
        App.getInstance().tts.Read(SDCSInputCharacterData.getInstance().getVoiceText(), 1);
        stringBuffer.delete(0, stringBuffer.length());
        characterData.setTimeOut(10);
        setTitle("请输入");
        tvChara.setText("");

        //tvMsg.setText(characterData.getHitMsg());

        int keyboardType = characterData.getKeyBoardType();
        //keyboardType = 0;
        if (SDCSInputCharacterData.TYPE_KEYBOARD_00 == keyboardType) {
            inputKeyBoard.setKeyBoardType(KeyBoard.All);
        } else if (SDCSInputCharacterData.TYPE_KEYBOARD_01 == keyboardType) {
            inputKeyBoard.setKeyBoardType(KeyBoard.ACCOUNT);
        } else if (SDCSInputCharacterData.TYPE_KEYBOARD_02 == keyboardType) {
            inputKeyBoard.setKeyBoardType(KeyBoard.MONEY);
        }

        //displayType = characterData.getDisplayType();
        displayType = 0;
        charLen = characterData.getCharLen();
        //charLen = 6;

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void timeOut() {
        //超时
        characterData.sendConfirmCode(BackCode.CODE_10);
        toPlay();
    }

    InputKeyBoard.OnKeyBoardListener boardListener = new InputKeyBoard.OnKeyBoardListener() {

        @Override
        public void modification() {
            stringBuffer.delete(0, stringBuffer.length());
            tvChara.setText("");
        }

        @Override
        public void character(String character) {
            //如果是金额
            if (InputCharacterData.TYPE_KEYBOARD_02 == characterData.getKeyBoardType()) {
                /*if(!isNumber(character)){
                    Toast.makeText(getContext(), "输入数字不正确", Toast.LENGTH_SHORT).show();
					return;
				}*/
            }

            stringBuffer.append(character);
            if (InputCharacterData.TYPE_DIAPLAY_COMMON == displayType) {
                tvChara.setText(stringBuffer.toString());
            } else if (InputCharacterData.TYPE_DIAPLAY_PASSWORD == displayType) {
                tvChara.setText(getPassWord(stringBuffer.toString()));
            }
            if (charLen == 0) {

            } else {

                if (stringBuffer.length() == charLen) {
                    characterData.sendConfirmResult(stringBuffer.toString().trim());
                    toPlay();
                }
            }
        }

        @Override
        public void back() {
            Intent intent = new Intent(AppAction.ACTION_BROADCAST_CMD);
            intent.putExtra(AppAction.KEY_BROADCAST_CMD, PlayActivity.PAGE_PLAY);
            getContext().sendBroadcast(intent);
        }

        @Override
        public void affirm() {
            if (charLen == 0) {
                characterData.sendConfirmResult(stringBuffer.toString().trim());
                toPlay();
            } else {
                if (stringBuffer.length() == charLen) {
                    characterData.sendConfirmResult(stringBuffer.toString().trim());
                    toPlay();
                } else {
                    tvChara.setText("输入数字长度不正确");
                    //Toast.makeText(getContext(), "输入数字长度不正确", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public static boolean isNumber(String str) {
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("[0-9]*");
        java.util.regex.Matcher match = pattern.matcher(str.trim());
        return match.matches();
    }

    private String getPassWord(String str) {
        String pw = "";
        for (int i = 0; i < str.length(); i++) {
            pw += "*";
        }
        return pw;
    }

    @Override
    public View getContentView() {
        return inflate(getContext(), R.layout.page_item_inputcharacter, null);
    }


}
