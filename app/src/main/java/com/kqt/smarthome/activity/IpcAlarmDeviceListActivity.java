package com.kqt.smarthome.activity;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.AlarmMsgAdpter;
import com.kqt.smarthome.adpter.IpcAlarmListAdpter;
import com.kqt.smarthome.adpter.IpcSettingAdpter;
import com.kqt.smarthome.db.DeviceManager;
import com.kqt.smarthome.entity.AlarmMsg;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.fragment.NewsLayerFragment;
import com.kqt.smarthome.util.AlarmSortComparator;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.XListView;
import com.kqt.smarthome.view.XListView.IXListViewListener;

/**
 * 消息中摄像头列表
 * 
 * @author Administrator
 *
 */
public class IpcAlarmDeviceListActivity extends BaseActivity implements
		IXListViewListener {
	private XListView xListView;
	private IpcAlarmListAdpter adpter;
	private List<IpcDevice> list;

	protected void onResume() {
		super.onResume();
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ipc_alarm_list);
		setTitle("列表");
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "");
		initView();
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		xListView = (XListView) findViewById(R.id.ipc_alarm_listivew);
		list = DeviceManager.getInstence(this).GetIPCListDevice();
		adpter = new IpcAlarmListAdpter(this, list);
		xListView.setAdapter(adpter);
		xListView.setPullLoadEnable(false);
		xListView.setIXListViewListener(this);
	}

	@Override
	public void onRefresh() {
	}

	@Override
	public void onLoadMore() {
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
     back();
	}

}
