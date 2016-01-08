package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACAccountMgr;
import com.accloud.service.ACException;
import com.accloud.service.ACUserInfo;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.view.TimeCountUtil;

public class RegisterActivity extends BaseActivity implements OnClickListener {

	private Button code;
	private EditText phone, name, psw, email, code_input;
	private String phone_str, name_str, psw_str, email_str, code_input_str;
	private Button register;
	private ACAccountMgr accountMgr;
	private TimeCountUtil timec;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "");
		setTitle("注册界面");

		initView();
	}

	private void initView() {
		code = (Button) findViewById(R.id.register_code_btn);
		phone = (EditText) findViewById(R.id.register_iphone);
		name = (EditText) findViewById(R.id.register_name);
		psw = (EditText) findViewById(R.id.register_psw);
		email = (EditText) findViewById(R.id.register_email);
		code_input = (EditText) findViewById(R.id.register_idencode);
		register = (Button) findViewById(R.id.register);

		accountMgr = AC.accountMgr();
		code.setOnClickListener(this);
		register.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		phone_str = phone.getText().toString().trim();
		name_str = name.getText().toString().trim();
		psw_str = psw.getText().toString().trim();
		email_str = email.getText().toString().trim();
		code_input_str = code_input.getText().toString().trim();
		switch (id) {
		case R.id.register_code_btn:
			if ("".equals(phone_str) || phone.length() != 11) {
				showToast("手机号为空!");
				return;
			}
			timec = new TimeCountUtil(this, 60000, 1000, code);
			timec.start();
			AC.accountMgr().sendVerifyCode(phone_str, 1, new VoidCallback() {
				@Override
				public void success() {
					Ttoast.show(RegisterActivity.this, "发送成功,注意接收");
				}

				@Override
				public void error(ACException e) {
					Ttoast.show(RegisterActivity.this, "发送失败,请重试");
				}
			});
			break;
		case R.id.register:
			accountMgr.register(email_str, phone_str, psw_str, name_str,
					code_input_str, new PayloadCallback<ACUserInfo>() {

						@Override
						public void success(ACUserInfo arg0) {
							// TODO Auto-generated method stub
							Ttoast.show(
									RegisterActivity.this,
									RegisterActivity.this
											.getResources()
											.getString(R.string.register_sucess));
							finish();
						}

						@Override
						public void error(ACException arg0) {
							Ttoast.CheckErrorCode(RegisterActivity.this,
									arg0.getErrorCode());
						}
					});
			break;
		default:
			break;
		}
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		// TODO Auto-generated method stub
		finish();
	}
}
