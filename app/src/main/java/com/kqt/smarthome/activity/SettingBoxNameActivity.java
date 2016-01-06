package com.kqt.smarthome.activity;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.kqt.smarthome.R;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.listenner.NameListener;
import com.kqt.smarthome.service.BridgeService;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.LoadingDialog;

public class SettingBoxNameActivity extends BaseActivity {
	private EditText text;
	private Device device;
	private LoadingDialog dialog;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				showToast("修改成功");
				dialog.dismiss();
				finish();
			} else {
				showToast("修改失败");
				dialog.dismiss();
			}

		}

	};

	protected void onDestroy() {
		super.onDestroy();
		handler.removeMessages(0);
		handler.removeMessages(1);
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settingname);
		device = BoxSettingActivity.device;
		setNaView(R.drawable.left_back, "", 0, "上页", 0, "完成", 0, "");
		setTitle("名称修改");
		dialog = new LoadingDialog(this);
		text = (EditText) findViewById(R.id.setting_name);
		if (device != null) {
			text.setHint(device.getdName());
		}
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		// TODO Auto-generated method stub
		if (titleBar == TitleBar.LIEFT) {
			finish();
		} else if (titleBar == TitleBar.RIGHT) {
			dialog.setTitle("修改中...");
			String name = text.getText().toString().trim();
			if (!name.isEmpty()) {
				AC.bindMgr().changeName(
						Config.GetsubDomain(device.getSubmainID()),
						device.getDeviceid(), name, new VoidCallback() {

							@Override
							public void success() {
								handler.sendEmptyMessage(0);
							}

							@Override
							public void error(ACException arg0) {
								handler.sendEmptyMessage(1);
							}
						});

			} else {
				showToast("名称输出为空!");

			}
		}
	}
}
