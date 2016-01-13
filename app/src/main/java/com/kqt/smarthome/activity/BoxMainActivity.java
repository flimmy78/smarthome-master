package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.BoxModeAdpter;
import com.kqt.smarthome.entity.BoxDevice;
import com.kqt.smarthome.entity.BoxMode;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.XListView;
import com.kqt.smarthome.view.XListView.IXListViewListener;

import java.util.ArrayList;
import java.util.List;

public class BoxMainActivity extends BaseActivity implements IXListViewListener {

	private XListView listview;
	private Device device;
	private BoxModeAdpter adpter;
	private BoxDevice boxDevice = null;
	private List<BoxMode> list = new ArrayList<BoxMode>();
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			BoxMainActivity.this.hideProgressDialog();
			if (msg.what == 0) {
				if (boxDevice != null) {
					long leakageProtectionCount = boxDevice
							.getLeakageProtectionCount(); // 漏保
					long splitterCount = boxDevice.getSplitterCount();
					if (leakageProtectionCount > 0) {
						list.add(new BoxMode(leakageProtectionCount,
								Config.leakageProtection, boxDevice
										.getDeviceId()));
					}
 					if (splitterCount > 0) {
						list.add(new BoxMode(splitterCount, Config.splitter,
								boxDevice.getDeviceId()));
					}
					adpter.notifyDataSetChanged();
					listview.stopRefresh();
					listview.stopLoadMore();
					listview.setRefreshTime(Util.getNowTime());

				}
			} else if (msg.what == 1) {
				showToast("数据出错");
			}
		}

	};

	protected void onDestroy() {
		super.onDestroy();
		handler.removeMessages(0);
		handler.removeMessages(1);

	};

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			finish();
		} else if (titleBar == TitleBar.RIGHT) {
//			Intent intent = new Intent(this, ModeAddActivity.class);
//			intent.putExtra("gatewayDeviceId", device.getGatewayDeviceId());
//			startActivity(intent);

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_main_activity);
		// this.showProgressDialog("");
		device = (Device) getIntent().getSerializableExtra("device");
		setTitle("功能类型");
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "",
				R.drawable.right_add_selector, "");
		device = (Device) getIntent().getSerializableExtra("device");
		listview = (XListView) findViewById(R.id.mode_listview);
		listview.setPullLoadEnable(false);
		adpter = new BoxModeAdpter(list, this);
		listview.setAdapter(adpter);
		listview.setIXListViewListener(this);
		initData();
	}

	public void initData() {

		// *******************服务***********************
//		BoxManager.getintence().queryDeviceStateInfo("",
//				new PayloadCallback<ACMsg>() {
//					@Override
//					public void success(ACMsg arg0) {
//						List<ACObject> list = arg0.getList("stateInfoList");
//						for (int i = 0; i < list.size(); i++) {
//							ACObject map = list.get(i);
//							long deviceId = map.getLong("deviceId");
//							long leakageProtectionCount = map.getLong("leakageProtectionCount");
//							long splitterCount = map.getLong("splitterCount");
//							long totalElectricQuantity = map.getLong("totalElectricQuantity");
//							long time = map.getLong("time");
//							if (deviceId == device.getGatewayDeviceId()) {
//								boxDevice = new BoxDevice(deviceId,
//										leakageProtectionCount, splitterCount,
//										totalElectricQuantity, time);
//							}
//
//						}
//
//						handler.sendEmptyMessage(0);
//					}
//
//					@Override
//					public void error(ACException arg0) {
//
//						handler.sendEmptyMessage(1);
//					}
//				});
	}

	@Override
	public void onRefresh() {
		initData();
	}

	@Override
	public void onLoadMore() {

	}
}
