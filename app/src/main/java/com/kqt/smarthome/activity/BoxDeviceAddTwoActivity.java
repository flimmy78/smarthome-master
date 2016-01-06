package com.kqt.smarthome.activity;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.accloud.utils.PreferencesUtils;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.decoding.CaptureActivity;

import android.annotation.SuppressLint;
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

public class BoxDeviceAddTwoActivity extends BaseActivity implements
		OnClickListener {

	private EditText boxN, boxU;
	private String uuid, name;
	private Button bind_btn;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				showToast("绑定成功");
				finishAc();
			}
			BoxDeviceAddTwoActivity.this.hideProgressDialog();

		}

	};

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			finish();
		} else if (titleBar == TitleBar.RIGHT) {
			Intent openCameraIntent = new Intent(BoxDeviceAddTwoActivity.this,
					CaptureActivity.class);
			startActivityForResult(openCameraIntent, 0); // 扫描
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_device_bind);
		setTitle("第二步");
		setNaView(0, "上一步", 0, "", 0, "", 0, "扫一扫");
		initview();
		AddDeviceActivity.listAC.add(this);
	}

	@SuppressLint("NewApi")
	private void initview() {
		boxN = (EditText) findViewById(R.id.box_name);
		boxU = (EditText) findViewById(R.id.box_uuid);
		bind_btn = (Button) findViewById(R.id.bind_btn);
		uuid = PreferencesUtils.getString(this, "uuid", "");
		if (!uuid.isEmpty()) {
			boxU.setText(uuid);
		}

		bind_btn.setOnClickListener(this);
	}

	@SuppressLint("NewApi")
	private void bindDevice() {
		name = boxN.getText().toString().trim();
		uuid = boxU.getText().toString().trim();
		if (uuid.isEmpty()) {
			showToast("设备ID为空");
			return;
		}
		if (name.isEmpty()) {
			name = "DEVICE";
		}
		this.showProgressDialog("");
		AC.bindMgr().bindGateway(Config.SUBDOMAIN, uuid, name,
				new PayloadCallback<ACUserDevice>() {
					@Override
					public void success(ACUserDevice userDevice) {
						// 绑定成功后返回设备信息
						handler.sendEmptyMessage(0);

					}

					@Override
					public void error(ACException e) {
						// 网络错误或其他，根据e.getErrorCode()做不同的提示或处理
						Log.d("zjm", e.getErrorCode() + "'");
						showToast(Util.GetEorrMsg(e.getErrorCode()));
						handler.sendEmptyMessage(1);
					}
				});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			String da = data.getStringExtra(CaptureActivity.CODE_RESULT).split(
					"\\+")[1];
			Log.d("zjm", da);
			Message message = handler.obtainMessage();
			message.what = 0;
			message.obj = da;
			handler.sendMessage(message);

		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	private void finishAc() {
		for (Activity ac : AddDeviceActivity.listAC) {
			if (ac != null) {
				ac.finish();
			}
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == bind_btn.getId()) {
			bindDevice();

		}
	}
}
