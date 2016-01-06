package com.kqt.smarthome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accloud.service.ACUserInfo;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Util;

public class UserInfomationActivity extends BaseActivity implements
		OnClickListener {
	private LinearLayout username_layout, userpwd_layout, useriphone_layout;
	private TextView username, userphone;
	private ACUserInfo acUserInfo;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (acUserInfo != null) {
				username.setText(acUserInfo.getName());
			}
		}
	};

	protected void onResume() {
		super.onResume();
		sendUI();

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfomation);
		setTitle("个人资料");
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "");
		username_layout = (LinearLayout) findViewById(R.id.user_name_layout);
		userpwd_layout = (LinearLayout) findViewById(R.id.user_pwd_layout);
		useriphone_layout = (LinearLayout) findViewById(R.id.user_iphone_layout);
		username = (TextView) findViewById(R.id.user_name_text);
		userphone = (TextView) findViewById(R.id.user_iphone_text);
		username_layout.setOnClickListener(this);
		userpwd_layout.setOnClickListener(this);
		useriphone_layout.setOnClickListener(this);
		sendUI();
	}

	private void sendUI() {
		acUserInfo = Util.GetUser(this);
		handler.sendEmptyMessage(0);
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			back();
		} else if (titleBar == TitleBar.RIGHT) {

		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == username_layout.getId()) {
			Intent intent = new Intent(this, AmendUsernameActivity.class);
			intent.putExtra("name", acUserInfo.getName());
			startActivity(intent);
		} else if (v.getId() == useriphone_layout.getId()) {
			Intent intent = new Intent(this, AmendUserphoneActivity.class);
			startActivity(intent);
		} else if (v.getId() == userpwd_layout.getId()) {
			Intent intent = new Intent(this, AmendUserPwdActivity.class);
			startActivity(intent);
		}
	}
}
