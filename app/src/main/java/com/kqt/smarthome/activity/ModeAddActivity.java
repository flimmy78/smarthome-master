package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACDeviceBind;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Config;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ModeAddActivity extends BaseActivity {

	private ListView listview;
	private long gatewayDeviceId = 0; // 网关id

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			finish();
		} else if (titleBar == TitleBar.RIGHT) {

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_main_activity);
		AddDeviceActivity.listAC.add(this);
		setTitle("添加模块");
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", 0, "");
		gatewayDeviceId = getIntent().getLongExtra("gatewayDeviceId", 0);
		listview = (ListView) findViewById(R.id.mode_listview);
		 addSubDevice();
	}

	/**
	 * 开启网关配对
	 */
	private void openGateway() {
		AC.bindMgr().openGatewayMatch(Config.SUBDOMAIN, gatewayDeviceId,
				AC.DEVICE_ACTIVATOR_DEFAULT_TIMEOUT, new VoidCallback() {
					@Override
					public void success() {
						// 由于子设备接入网关是一个异步的过程，所以建议在这里new一个Timer去定时获取新加入的子设备列表，在activity退出时停止Timer
						new Timer().schedule(new TimerTask() {
							@Override
							public void run() {
								// 列举所有新加入的子设备列表
								listSubDevice();
							}
						}, 0, 10000);
					}

					@Override
					public void error(ACException e) {
						// 网络错误或其他，根据e.getErrorCode()做不同的提示或处理，此处一般为设备问题
					}
				});
	}

	private void listSubDevice() {
		AC.bindMgr().listNewDevices(Config.SUBDOMAIN, gatewayDeviceId,
				new PayloadCallback<List<ACDeviceBind>>() {
					@Override
					public void success(List<ACDeviceBind> deviceBinds) {
						// 建议此处更新新加入子设备列表的界面
						Log.d("zjm", deviceBinds.toString());
					}

					@Override
					public void error(ACException e) {
						// 网络错误或其他，根据e.getErrorCode()做不同的提示或处理，此处一般为设备问题
					}
				});
	}

	/**
	 * 网关下绑定子设备
	 */
	private void addSubDevice() {
		AC.bindMgr().addSubDevice(Config.LIGHT_SUBDOMAIN, gatewayDeviceId,
				"4735CFA9E7A15107", "调光灯", new PayloadCallback<ACUserDevice>() {
					@Override
					public void success(ACUserDevice acUserDevice) {
						// 成功绑定该子设备
						Log.d("zjm", "绑定成功");
					}

					@Override
					public void error(ACException e) {
						// 网络错误或其他，根据e.getErrorCode()做不同的提示或处理
					}
				});
	}
}
