package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserInfo;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;

public class AmendUsernameActivity extends BaseActivity {
	private EditText name_input;
	private ACUserInfo info;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			saveData();
			AmendUsernameActivity.this.hideProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amendname_activity);
		setTitle("名称修改");
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "完成");
		name_input = (EditText) findViewById(R.id.username_input);
		info = Util.GetUser(this);
		if (info != null)
			name_input.setHint(info.getName());
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			back();
		} else if (titleBar == TitleBar.RIGHT) {
			this.showProgressDialog("");
			final String name = name_input.getText().toString().trim();
			if (name.isEmpty()) {
				Ttoast.show(this, "内容不完整!");
				return;
			}
			AC.accountMgr().changeNickName(name, new VoidCallback() {

				@Override
				public void success() {
					Ttoast.show(AmendUsernameActivity.this, "修改成功");
					info.setName(name);
					handler.sendEmptyMessage(0);
					back();
				}

				@Override
				public void error(ACException arg0) {
					Ttoast.show(AmendUsernameActivity.this,
							Util.GetEorrMsg(arg0.getErrorCode()));
				}
			});
		}

	}

	private void saveData() {
		Util.SaveUserInfo(this, info);
	}
}
