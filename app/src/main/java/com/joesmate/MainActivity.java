package com.joesmate;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.joesmate.R.id;
import com.joesmate.bin.keyBoard.OptionResData;
import com.joesmate.bin.keyBoard.SettingsM3Data;
import com.joesmate.bin.keyBoard.ShowTextData;
import com.joesmate.widget.ButtonOption;

@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity {

	String TAG = "test";
	LinearLayout layout;
	TextView tvTitle, tvMsg, tvRsult;
	int[] ids = { R.id.rg1, R.id.rg2, R.id.rg3, R.id.rg4, R.id.rg5, R.id.rg6 };
	RadioGroup[] groups = new RadioGroup[6];
	Button btSet, btGet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main2);

		btSet = (Button) findViewById(id.test_set);
		btGet = (Button) findViewById(id.test_get);

		btSet.setOnClickListener(clickListener);
		btGet.setOnClickListener(clickListener);

		layout = (LinearLayout) findViewById(id.test_frame);
		layout.setVisibility(View.INVISIBLE);
		tvTitle = (TextView) findViewById(id.test_title);
		tvMsg = (TextView) findViewById(id.test_msg);
		tvRsult = (TextView) findViewById(id.test_result);

		for (int i = 0; i < ids.length; i++) {
			groups[i] = (RadioGroup) findViewById(ids[i]);
			groups[i].setOnCheckedChangeListener(changeListener);
		}

		registerReceiver();

	}

	OnClickListener clickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if (v == btSet) {
				String set = s1 + s2 + s3 + s4 + s5 + s6;
				Log.d(TAG, "set：" + set);
				SettingsM3Data.getInstance().setSetParam(set);
				App.getInstance().fitManagerTeller.getBaseFitBin().setData(
						Cmds.CMD_WC.getBytes(), 2);
			} else if (v == btGet) {
				App.getInstance().fitManagerTeller.getBaseFitBin().setData(
						Cmds.CMD_RC.getBytes(), 2);
			}

		}
	};
	String s1, s2, s3, s4, s5, s6;
	android.widget.RadioGroup.OnCheckedChangeListener changeListener = new android.widget.RadioGroup.OnCheckedChangeListener() {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			if (group == groups[0]) {
				if (checkedId == R.id.n1_1) {
					s1 = "" + 0;
				} else if (checkedId == R.id.n1_2) {
					s1 = "" + 1;
				}
			} else if (group == groups[1]) {
				if (checkedId == R.id.n2_1) {
					s2 = "" + 0;
				} else if (checkedId == R.id.n2_2) {
					s2 = "" + 1;
				}
			} else if (group == groups[2]) {
				if (checkedId == R.id.n3_1) {
					s3 = "" + 1;
				} else if (checkedId == R.id.n3_2) {
					s3 = "" + 2;
				} else if (checkedId == R.id.n3_3) {
					s3 = "" + 3;
				} else if (checkedId == R.id.n3_4) {
					s3 = "" + 4;
				}
			} else if (group == groups[3]) {
				if (checkedId == R.id.n4_1) {
					s4 = "" + 1;
				} else if (checkedId == R.id.n4_2) {
					s4 = "" + 2;
				} else if (checkedId == R.id.n4_3) {
					s4 = "" + 3;
				}
			} else if (group == groups[4]) {
				if (checkedId == R.id.n5_1) {
					s5 = "" + 0;
				} else if (checkedId == R.id.n5_2) {
					s5 = "" + 1;
				} else if (checkedId == R.id.n5_3) {
					s5 = "" + 2;
				} else if (checkedId == R.id.n5_4) {
					s5 = "" + 3;
				}
			} else if (group == groups[5]) {
				if (checkedId == R.id.n6_1) {
					s6 = "" + 1;
				} else if (checkedId == R.id.n6_2) {
					s6 = "" + 2;
				} else if (checkedId == R.id.n6_3) {
					s6 = "" + 3;
				}
			}

		}
	};

	private void registerReceiver() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(AppAction.ACTION_BROADCAST_CMD);
		registerReceiver(broadcastReceiver, filter);
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			final String action = intent.getAction();
			Log.d(TAG, "receive action：" + action);

			if (AppAction.ACTION_BROADCAST_CMD.equals(action)) {

				final byte[] cmd = intent
						.getByteArrayExtra(AppAction.KEY_BROADCAST_CMD);
				String strCmd = new String(cmd);
				Log.d(TAG, "receive cmd：" + strCmd);
				sendMessage(strCmd);
			}

		}

	};

	private void sendMessage(String cmd) {
		Message message = new Message();
		message.obj = cmd;
		handler.sendMessage(message);
	}

	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String cmd = (String) msg.obj;
			if (Cmds.CMD_DD.equals(cmd)) {
				layout.setVisibility(View.VISIBLE);
				tvTitle.setText(ShowTextData.getInstance().getTitle());
				String msgs = ShowTextData.getInstance().getMsg();
				tvMsg.setText(msgs == null ? "" : msgs);
			} else if (Cmds.CMD_XS.equals(cmd)) {
				layout.setVisibility(View.VISIBLE);
				String msgs = ShowTextData.getInstance().getMsg();
				tvMsg.setText(msgs == null ? "" : msgs);
			} else if (Cmds.CMD_CD.equals(cmd)) {
				layout.setVisibility(View.INVISIBLE);
			} else if (Cmds.BACK_RC.equals(cmd)) {
				byte[] re = SettingsM3Data.getInstance().arrayParam;
				String str = "";
				str += getStr1(re[0]) + "、" + getStr2(re[1]) + "、"
						+ getStr3(re[2]) + "、" + getStr4(re[3]) + "、"
						+ getStr5(re[4]) + "、" + getStr6(re[5]);
				tvRsult.setText(str);
			}else if(Cmds.CMD_GJ.equals(cmd)){
				Toast.makeText(MainActivity.this, "收到关机指令", Toast.LENGTH_SHORT).show();
			}else if(Cmds.BACK_ZJ.equals(cmd)){
				Toast.makeText(MainActivity.this, "z指定播放:"+OptionResData.getInstance().getResName(), Toast.LENGTH_SHORT).show();
			}
		}

	};

	String getStr1(int v) {
		if (v == 0) {
			return "输入受控";
		} else if (v == 1) {
			return "输入常开";
		}
		return "ERRO";
	}

	String getStr2(int v) {
		if (v == 0) {
			return "单件发送";
		} else if (v == 1) {
			return "打包发送";
		}
		return "ERRO";
	}

	String getStr3(int v) {
		if (v == 1) {
			return "小";
		} else if (v == 2) {
			return "中";
		} else if (v == 3) {
			return "大";
		} else if (v == 4) {
			return "最大";
		}
		return "ERRO";
	}

	String getStr4(int v) {
		if (v == 1) {
			return "9600";
		} else if (v == 2) {
			return "1200";
		} else if (v == 3) {
			return "115200";
		}
		return "ERRO";
	}

	String getStr5(int v) {
		if (v == 0) {
			return "RS232";
		} else if (v == 1) {
			return "TTL";
		} else if (v == 2) {
			return "自动识别";
		} else if (v == 3) {
			return "TTL自动识别";
		}
		return "ERRO";
	}

	String getStr6(int v) {
		if (v == 1) {
			return "ASCII";
		} else if (v == 2) {
			return "国光";
		} else if (v == 3) {
			return "实达";
		}
		return "ERRO";
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		
		return true;
	}

	private LayoutParams getWeightParams() {
		LayoutParams params = new LayoutParams(0, LayoutParams.WRAP_CONTENT);
		params.weight = 1.0f;
		return params;
	}

	private LinearLayout getBtLayout(String cn) {
		LinearLayout layout = new LinearLayout(this);
		int width = (int) getResources()
				.getDimension(R.dimen.appraise_bt_width);
		int height = (int) getResources().getDimension(
				R.dimen.appraise_bt_height);
		LayoutParams params = new LayoutParams(100, 60);
		// params.gravity = Gravity.CENTER;

		ButtonOption option = new ButtonOption(this);
		// option.setButtonOption(cn, OptionType.RADIO_BUTTON, null);
		layout.addView(option, params);
		return layout;
	}

}
