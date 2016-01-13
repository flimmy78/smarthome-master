package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;

public class AmendUserPwdActivity extends BaseActivity {
	private EditText opwd, npwd;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			AmendUserPwdActivity.this.hideProgressDialog();
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.amendpwd_activity);
		setTitle("密码修改");
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", 0, "完成");
		opwd = (EditText) findViewById(R.id.amend_oldpwd);
		npwd = (EditText) findViewById(R.id.amend_newpwd);
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			back();
		} else if (titleBar == TitleBar.RIGHT) {
			this.showProgressDialog("");
			String opwssd = opwd.getText().toString().trim();
			String npwssd = npwd.getText().toString().trim();
			if (opwssd.isEmpty() || npwssd.isEmpty()) {
				Ttoast.show(this, "内容不完整!");
				return;
			}
			AC.accountMgr().changePassword(opwssd, npwssd, new VoidCallback() {

				@Override
				public void success() {
					Ttoast.show(AmendUserPwdActivity.this, "修改成功");
					handler.sendEmptyMessage(0);
					back();
				}

				@Override
				public void error(ACException arg0) {
					Ttoast.show(AmendUserPwdActivity.this,
							Util.GetEorrMsg(arg0.getErrorCode()));
					handler.sendEmptyMessage(0);
				}
			});
		}

	}

}
