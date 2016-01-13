package com.kqt.smarthome.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACMsg;
import com.accloud.service.ACObject;
import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.ExpandableListViewAdapter;
import com.kqt.smarthome.entity.BoxManager;
import com.kqt.smarthome.entity.BoxShuntSwitch;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Box_shunt_switchActivity extends BaseActivity {

	private List<BoxShuntSwitch> list = new ArrayList<BoxShuntSwitch>();
	private ExpandableListView expandableListView;
	private ExpandableListViewAdapter expandableListAdapter;
	private boolean hasdata = false;
	private TextView timeout_tv;
	private ProgressBar progressBar;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			progressBar.setVisibility(View.GONE);
			if (msg.what == 0) {
				timeout_tv.setVisibility(View.GONE);
				expandableListView.setVisibility(View.VISIBLE);
				expandableListAdapter.notifyDataSetChanged();
			} else if (msg.what == Util.TIMEOUT_WHAT) {
				timeout_tv.setVisibility(View.VISIBLE);
			}
		}
	};

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			finish();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_shunt_switch_activity);
		setTitle("分路列表");
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", 0, "");
		expandableListView = (ExpandableListView) findViewById(R.id.expandablelistview);
		timeout_tv = (TextView) findViewById(R.id.shunt_data_timeout);
		progressBar = (ProgressBar) findViewById(R.id.refish_shunt_data);
		expandableListAdapter = new ExpandableListViewAdapter(list, this);
		expandableListView.setGroupIndicator(null);
		expandableListView.setAdapter(expandableListAdapter);
		initData();
		expandableListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
							View view, final int position, long id) {
						final BoxShuntSwitch boxShuntSwitch = list
								.get(position);
						final TextView text = (TextView) view
								.findViewById(R.id.box_shunt_switch_name);
						CustomDialog.Builder customDialog = new CustomDialog.Builder(
								Box_shunt_switchActivity.this);
						customDialog.setTitle("改名");
						final EditText editText = new EditText(
								Box_shunt_switchActivity.this);

						editText.setHint(boxShuntSwitch.getName());
						customDialog.setContentView(editText);
						customDialog.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										final String lineName = editText
												.getText().toString().trim();
										// 改名
										BoxManager
												.getintence()
												.modifyChannelDivideLineName(
														boxShuntSwitch
																.getDeviceId(),
														position + 1,
														lineName,
														new PayloadCallback<ACMsg>() {

															@Override
															public void success(
																	ACMsg arg0) {
																text.setText(lineName);
															}

															@Override
															public void error(
																	ACException arg0) {
																Log.d("error",
																		arg0.getErrorCode()
																				+ "");

															}
														});
									}
								});
						customDialog.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								});
						customDialog.create().show();
						return true;
					}
				});

	}

	// 初始化数据   测试专用
	public void initData() {

		list.clear();
		long num = getIntent().getLongExtra("splitter", 0);
		long deviceId = getIntent().getLongExtra("deviceId", 0);
		for (int i = 0; i < num; i++) {
			list.add(new BoxShuntSwitch(deviceId, "分路" + i, false, "", 3.1f,
					3.1f, 3.1f, 10L, 3L, 3.14f, 20L, 10L, 20L, 20L, 10L, 12L,
					13L, 14L, 15L, false, true, true));
		}
		handler.sendEmptyMessage(0);
	}

	/**
	 * 查询分路信息
	 * 
	 * @param deviceId
	 */
	public void queryShuntData(long deviceId) {
		hasdata = false;
		// 查询
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				if (!hasdata)
					handler.sendEmptyMessage(Util.TIMEOUT_WHAT);
			}
		};
		timer.schedule(task, 5000);

		list.clear();
		BoxManager.getintence().queryChannelDivideStateInfo(deviceId, 0,
				new PayloadCallback<ACMsg>() {

					@Override
					public void error(ACException arg0) {
						Log.d("error", arg0.getErrorCode() + "");
					}

					@Override
					public void success(ACMsg arg0) {
						hasdata = true;
						ACObject acObject = arg0.get("stateInfo");
						long deviceId = acObject.getLong("deviceId");
						List<ACObject> list_ac = acObject.getList("value");
						for (int i = 0; i < list_ac.size(); i++) {
							ACObject li = list_ac.get(i);
							String lineName = li.getString("lineName");
							float voltage = li.getFloat("voltage");
							float current = li.getFloat("current");
							float totalElectricityQuantity = li
									.getFloat("totalElectricityQuantity");
							long capacity = li.getLong("capacity");
							long temperature = li.getLong("temperature");
							float powerFactor = li.getFloat("powerFactor");
							long limitedPower = li.getLong("limitedPower");
							long limitingPower = li.getLong("limitingPower");
							long overpressureUpperLimit = li
									.getLong("overpressureUpperLimit");
							long undervoltageLowerLimit = li
									.getLong("undervoltageLowerLimit");
							long currentCapacity = li
									.getLong("currentCapacity");
							long overTempSetting = li
									.getLong("overTempSetting");
							long operationEventCode = li
									.getLong("operationEventCode");
							long alarmEventCode = li.getLong("AlarmEventCode");
							long equipmentFaultCode = li
									.getLong("equipmentFaultCode");
							boolean alarmPushFlag = li
									.getBoolean("alarmPushFlag");
							boolean operationPushFlag = li
									.getBoolean("operationPushFlag");
							boolean operationLock = li
									.getBoolean("operationLock");
							boolean switchState = li.getBoolean("switchState");
							BoxShuntSwitch boxShuntSwitch = new BoxShuntSwitch(
									deviceId, lineName, switchState, "",
									voltage, current, totalElectricityQuantity,
									capacity, temperature, powerFactor,
									limitedPower, limitingPower,
									overpressureUpperLimit,
									undervoltageLowerLimit, currentCapacity,
									overTempSetting, operationEventCode,
									alarmEventCode, equipmentFaultCode,
									alarmPushFlag, operationPushFlag,
									operationLock);
							list.add(boxShuntSwitch);
						}
						handler.sendEmptyMessage(0);

					}
				});
	}
}
