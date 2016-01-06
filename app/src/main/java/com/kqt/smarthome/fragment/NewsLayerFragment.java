package com.kqt.smarthome.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.IpcAlarmDeviceListActivity;
import com.kqt.smarthome.adpter.AlarmDeviceListAdpter;
import com.kqt.smarthome.adpter.AlarmMsgAdpter;
import com.kqt.smarthome.db.DeviceManager;
import com.kqt.smarthome.entity.AlarmMsg;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.util.AlarmSortComparator;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.XListView;
import com.kqt.smarthome.view.XListView.IXListViewListener;
import com.shizhefei.fragment.LazyFragment;

public class NewsLayerFragment extends LazyFragment implements
		IXListViewListener, OnClickListener {
	private XListView listView;
	private List<Device> list = new ArrayList<Device>();
	private AlarmDeviceListAdpter adpter;
	private LinearLayout ipc_layout;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			List<IpcDevice> ipc_list = DeviceManager.getInstence(
					NewsLayerFragment.this.getActivity()).GetIPCListDevice();
			if (ipc_list.size() > 0) {
				ipc_layout.setVisibility(View.VISIBLE);
			} else {
				ipc_layout.setVisibility(View.GONE);
			}

			if (msg.what == 0) {
				listView.stopRefresh();
				listView.stopLoadMore();
				listView.setRefreshTime(Util.getNowTime());
				adpter.notifyDataSetChanged();
			} 

		}
	};

	protected void onResumeLazy() {
		super.onResumeLazy();
		handler.sendEmptyMessage(1);
	};

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_tabnews);
		initView();
	}

	@SuppressWarnings("unchecked")
	private void initView() {

		listView = (XListView) findViewById(R.id.alarm_device_list);
		ipc_layout = (LinearLayout) findViewById(R.id.alarm_msg_ipc);
		adpter = new AlarmDeviceListAdpter(list,
				NewsLayerFragment.this.getActivity());
		listView.setAdapter(adpter);
		listView.setPullLoadEnable(false);
		listView.setIXListViewListener(this);
		ipc_layout.setOnClickListener(this);
		initData();
	}

	@Override
	public void onRefresh() {
		initData();
	}

	private void initData() {
		list.clear();
		AC.bindMgr().listDevices(new PayloadCallback<List<ACUserDevice>>() {
			@Override
			public void success(List<ACUserDevice> arg0) {
				for (int i = 0; i < arg0.size(); i++) {
					ACUserDevice acUserDevice = arg0.get(i);
					String uuid = acUserDevice.getPhysicalDeviceId();
					String name = acUserDevice.getName();
					long deviceid = acUserDevice.getDeviceId();
					long submainID = acUserDevice.getSubDomainId(); // 子域id
					long getwata = acUserDevice.getGatewayDeviceId();
					int status = acUserDevice.getStatus();
					long ownerid = acUserDevice.getOwner();
					Device device = new Device(Config.BOX_TYPE, name, uuid, -1,
							status, deviceid, getwata, submainID, ownerid);
					list.add(device);
				}
				handler.sendEmptyMessage(0); // 初始化列表

			}

			@Override
			public void error(ACException arg0) {
				Log.d("zjm", arg0.getErrorCode() + "");
			}
		});
	}

	@Override
	public void onLoadMore() {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == ipc_layout.getId()) {
			Intent intent = new Intent(NewsLayerFragment.this.getActivity(),
					IpcAlarmDeviceListActivity.class);
			startActivity(intent);
		}
	}
}
