package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserInfo;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.TimeCountUtil;

public class ForgetPwdActivity extends BaseActivity implements OnClickListener {
	private EditText code, newpwd, iphone;
	private Button btn;
	private Button getcode;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 2:
				ForgetPwdActivity.this.showToast("发送成功,注意接收");
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forgetpwd);
		setTitle("账号验证");
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "");
		code = (EditText) findViewById(R.id.idencode);
		newpwd = (EditText) findViewById(R.id.newpwd);
		iphone = (EditText) findViewById(R.id.forget_phone);
		btn = (Button) findViewById(R.id.rest_btn);
		getcode = (Button) findViewById(R.id.forget_code_btn);
		btn.setOnClickListener(this);
		getcode.setOnClickListener(this);
	}

	@SuppressWarnings("unused")
	private void resetPassword(String phone, String newpsw, String code) {
		AC.accountMgr().resetPassword(phone, newpsw, code,
				new PayloadCallback<ACUserInfo>() {

					@Override
					public void success(ACUserInfo arg0) {
						handler.sendEmptyMessage(0);
						Ttoast.show(ForgetPwdActivity.this, "重置成功");
						finish();
					}

					@Override
					public void error(ACException arg0) {
						Ttoast.show(ForgetPwdActivity.this,
								Util.GetEorrMsg(arg0.getErrorCode()));
					}
				});

	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeMessages(2);
	}

	@Override
	public void onClick(View v) {

		String pwd = newpwd.getText().toString().trim();
		String phone = iphone.getText().toString().trim();

		if (v.getId() == btn.getId()) {
			String code_info = code.getText().toString().trim();
			if (code_info.isEmpty() || pwd.isEmpty() || phone.isEmpty()) {
				showToast("输入内容不完整");
				return;
			}
			resetPassword(phone, pwd, code_info);
		} else if (getcode.getId() == v.getId()) {
			if (phone.isEmpty()) {
				showToast("手机号或邮箱为空!");
				return;
			}

			AC.accountMgr().sendVerifyCode(phone, 1, new VoidCallback() {
				@Override
				public void success() {
					new TimeCountUtil(ForgetPwdActivity.this, 60000, 1000,
							getcode).start();
					handler.sendEmptyMessage(2);
				}
				@Override
				public void error(ACException arg0) {
					Ttoast.show(ForgetPwdActivity.this,
							Util.GetEorrMsg(arg0.getErrorCode()));
				}
			});
		}

	}
}