package com.kqt.smarthome.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACAccountMgr;
import com.accloud.service.ACBindMgr;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.accloud.service.ACUserInfo;
import com.accloud.utils.PreferencesUtils;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.LoadingDialog;

public class LoginActivity extends Activity implements OnClickListener {

	private TextView forgetpsw, login_register;
	private EditText login_name, login_psw;
	private Button login_btn;
	private ACAccountMgr acaccountMgr;
	private ACBindMgr acBindMgr;
	private boolean isHave = true;
	private LoadingDialog dialog;
	private Handler hanlder = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 0) {

			} else if (msg.what == 1) {
				Toast.makeText(LoginActivity.this, "账号已在线", Toast.LENGTH_SHORT)
						.show();
			} else if (msg.what == 2) {
				login_psw.setText("");
			}
			dialog.dismiss();
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		if (acaccountMgr.isLogin()) {
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		AC.notificationMgr().init();
		acaccountMgr = AC.accountMgr();
		acBindMgr = AC.bindMgr();
		initView();
	}

	private void initView() {
		dialog = new LoadingDialog(this);
		forgetpsw = (TextView) findViewById(R.id.login_forgetpsw);
		login_register = (TextView) findViewById(R.id.login_register);
		login_name = (EditText) findViewById(R.id.login_name);
		login_psw = (EditText) findViewById(R.id.login_psw);
		login_btn = (Button) findViewById(R.id.login_btn);
		login_register.setOnClickListener(this);
		forgetpsw.setOnClickListener(this);
		login_btn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		switch (id) {
		case R.id.login_forgetpsw:
			Intent intent1 = new Intent(this, ForgetPwdActivity.class);
			startActivity(intent1);
			break;
		case R.id.login_register:
			Intent intent = new Intent(this, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.login_btn:
			if (!Util.network) {
				Ttoast.show(this, "网络断开,请检查网络连接!");
				return;
			}
			String name = login_name.getText().toString().trim();
			String psw = login_psw.getText().toString().trim();
			if (name.isEmpty() || psw.isEmpty()) {
				return;
			}

			dialog.show();
			boolean islogin = acaccountMgr.isLogin();
			if (!islogin) {
				acaccountMgr.login(name, psw,
						new PayloadCallback<ACUserInfo>() {
							@Override
							public void success(ACUserInfo arg0) {

								PreferencesUtils.putLong(LoginActivity.this,
										"userid", arg0.getUserId());
								hanlder.sendEmptyMessage(0);

								boolean flg = Util.SaveUserInfo(
										LoginActivity.this, arg0);
								Intent intent = new Intent(LoginActivity.this,
										MainActivity.class);
								startActivity(intent);
								finish();
								AC.notificationMgr().addAlias(arg0.getUserId(),
										new VoidCallback() {
											@Override
											public void success() {
												Log.d("zjm", "添加推送别名成功");
											}

											@Override
											public void error(ACException e) {
												Log.d("zjm", e.getErrorCode()
														+ "");
												Ttoast.show(LoginActivity.this,
														e.getErrorCode() + "");
												hanlder.sendEmptyMessage(0);
											}
										});

							}

							@Override
							public void error(ACException arg0) {
								Ttoast.show(LoginActivity.this,
										Util.GetEorrMsg(arg0.getErrorCode()));
								hanlder.sendEmptyMessage(2);

							}
						});
			} else
				hanlder.sendEmptyMessage(1);
			break;
		}
	}
}
