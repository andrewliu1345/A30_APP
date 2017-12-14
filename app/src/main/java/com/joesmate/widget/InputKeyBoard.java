package com.joesmate.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.joesmate.App;
import com.joesmate.R;
import com.joesmate.audio.AudioPlayer;

/**
 * TODO 对键盘输入过滤,java正则表达式
 *
 * @author yc.zhang
 */
public class InputKeyBoard extends LinearLayout implements OnClickListener {

    public static final int[] BT_IDS = {R.id.input_keyboard_1,
            R.id.input_keyboard_2, R.id.input_keyboard_3,
            R.id.input_keyboard_4, R.id.input_keyboard_5,
            R.id.input_keyboard_6, R.id.input_keyboard_7,
            R.id.input_keyboard_8, R.id.input_keyboard_9,
            R.id.input_keyboard_0, R.id.input_keyboard_point,
            R.id.input_keyboard_ok, R.id.input_keyboard_modification,
            R.id.input_keyboard_back};
    public static final String[] CHARACTERS = {"1", "2", "3", "4", "5", "6",
            "7", "8", "9", "0", ".",};

    public static enum KeyBoard {
        All, MONEY, ACCOUNT
    }

    Button[] buttons = new Button[BT_IDS.length];
    OnKeyBoardListener boardListener;
    KeyBoard boardType;

    public InputKeyBoard(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.input_keyboard, this);
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = (Button) findViewById(BT_IDS[i]);
            buttons[i].setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {

        AudioPlayer.getInstance(App.getInstance().getApplicationContext()).playDi();
        if (v == buttons[buttons.length - 1]) {// back
            if (boardListener != null) {
                boardListener.back();
            }
        } else if (v == buttons[buttons.length - 2]) {// modification
            if (boardListener != null) {
                boardListener.modification();
            }
        } else if (v == buttons[buttons.length - 3]) {// affirm
            if (boardListener != null) {
                boardListener.affirm();
            }
        } else {// input character
            for (int i = 0; i < buttons.length; i++) {
                if (v == buttons[i]) {
                    if (boardListener != null) {
                        boardListener.character(CHARACTERS[i]);
                    }
                }
            }
        }

    }

    public void setKeyBoardType(KeyBoard keyBoard) {
        this.boardType = keyBoard;
        if (boardType == KeyBoard.ACCOUNT) { // hide zero button
            buttons[buttons.length - 3].setVisibility(View.GONE);
        } else {
            buttons[buttons.length - 3].setVisibility(View.VISIBLE);
        }
    }

    public void setOnKeyBoardListener(OnKeyBoardListener boardListener) {
        this.boardListener = boardListener;
    }

    public static interface OnKeyBoardListener {
        public void affirm();

        public void modification();

        public void character(String character);

        public void back();
    }

}
