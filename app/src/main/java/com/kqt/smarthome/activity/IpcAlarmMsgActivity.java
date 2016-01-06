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
public class IpcAlarmMsgActivity extends BaseActivity implements
		IXListViewListener {
	private XListView listView;
	private List<AlarmMsg> list = null;
	private AlarmMsgAdpter adpter;
	private IpcDevice ipcDevice;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 1) {
				initView();
			} else if (msg.what == 2) {
				listView.stopRefresh();
				listView.stopLoadMore();
				listView.setRefreshTime(Util.getNowTime());
				initView();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ipc_alarm_msg);
		setTitle("摄像头消息");
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "");
		initView();
	}

	@SuppressWarnings("unchecked")
	private void initView() {
		ipcDevice = (IpcDevice) getIntent().getSerializableExtra("device");
		list = DeviceManager.getInstence(this).QueryMsg(ipcDevice.getDeviceid());
		if (list != null) {
			AlarmSortComparator comparator = new AlarmSortComparator();
			Collections.sort(list, comparator);
		}
		listView = (XListView) findViewById(R.id.alarm_listivew);
		adpter = new AlarmMsgAdpter(list, this);
		listView.setAdapter(adpter);
		listView.setPullLoadEnable(false);
		listView.setIXListViewListener(this);

	}

	@Override
	public void onRefresh() {
		handler.sendEmptyMessageDelayed(2, 2000);
	}

	@Override
	public void onLoadMore() {

	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		back();
	}

}
