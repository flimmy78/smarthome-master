package com.kqt.smarthome.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.ACDeviceActivator;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceBind;
import com.accloud.service.ACException;
import com.accloud.utils.PreferencesUtils;
import com.kqt.smarthome.R;

public class BoxDeviceAddActivity extends BaseActivity implements
		OnClickListener {
	private Button active;
	private EditText name, pwd;
	private ACDeviceActivator deviceActivator;
	private String ssid = "";

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_device_active);
		AddDeviceActivity.listAC.add(this);
		setTitle("第一步");
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "");
		deviceActivator = AC.deviceActivator(AC.DEVICE_HF);
		ssid = deviceActivator.getSSID();
		initView();
		active.setOnClickListener(this);
	}

	private void initView() {
		active = (Button) findViewById(R.id.active_btn);
		name = (EditText) findViewById(R.id.active_wifiname);
		pwd = (EditText) findViewById(R.id.active_wifipwd);
		name.setText(ssid);
	}

	// 激活
	private void startAbleLink() {
		String psw = pwd.getText().toString().trim();
		if (psw.isEmpty()) {
			showToast("WIFI密码为空!");
			return;
		}
		String password = psw;
		deviceActivator.startAbleLink(ssid, password, 5000,
				new PayloadCallback<List<ACDeviceBind>>() {

					@Override
					public void success(List<ACDeviceBind> arg0) {
						// 可通过得到设备列表获取对应submajorid,和物理id

						String uuid = arg0.get(0).getPhysicalDeviceId();
						PreferencesUtils.putString(BoxDeviceAddActivity.this,
								"uuid", uuid); // 保存设备的物理id
					}

					@Override
					public void error(ACException arg0) {
						showToast(arg0.getErrorCode() + "");
						// 1993为配置超时
					}
				});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		deviceActivator.stopAbleLink(); // 终止激活

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == active.getId()) {
			Intent intent = new Intent(this, BoxDeviceAddTwoActivity.class);
			startActivity(intent);
		}

	}
}
