package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.TimeCountUtil;

public class AmendUserphoneActivity extends BaseActivity implements
		OnClickListener {
	private EditText phone, pwd, code;
	private Button code_btn;

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			AmendUserphoneActivity.this.hideProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amendphone_activity);
		setTitle("手机号修改");
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", 0, "完成");
		phone = (EditText) findViewById(R.id.amend_newphone);
		pwd = (EditText) findViewById(R.id.amend_pwd);
		code = (EditText) findViewById(R.id.amend_code);
		code_btn = (Button) findViewById(R.id.amend_code_btn);

		code_btn.setOnClickListener(this);
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			back();
		} else if (titleBar == TitleBar.RIGHT) {
			this.showProgressDialog("");
			String ph = phone.getText().toString().trim();
			String pwssd = pwd.getText().toString().trim();
			String code_str = code.getText().toString().trim();
			if (ph.isEmpty() || pwssd.isEmpty() || code_str.isEmpty()) {
				Ttoast.show(this, "内容不完整!");
				return;
			}
			AC.accountMgr().changePhone(ph, pwssd, code_str,
					new VoidCallback() {

						@Override
						public void success() {

							Ttoast.show(AmendUserphoneActivity.this, "修改成功");
							handler.sendEmptyMessage(0);
							back();
						}

						@Override
						public void error(ACException arg0) {
							Ttoast.show(AmendUserphoneActivity.this,
									Util.GetEorrMsg(arg0.getErrorCode()));
							handler.sendEmptyMessage(0);
						}
					});
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == code_btn.getId()) {
			String ph = phone.getText().toString().trim();
			if (ph.isEmpty()) {
				Ttoast.show(this, "手机号不能为空!");
				return;
			}
			AC.accountMgr().sendVerifyCode(ph, 1, new VoidCallback() {

				@Override
				public void success() {
					new TimeCountUtil(AmendUserphoneActivity.this, 60 * 1000,
							1000, code_btn).start();
					Ttoast.show(AmendUserphoneActivity.this, "发送成功,注意接收");
				}

				@Override
				public void error(ACException arg0) {
					Ttoast.show(AmendUserphoneActivity.this,
							Util.GetEorrMsg(arg0.getErrorCode()));
				}
			});
		}
	}
}
