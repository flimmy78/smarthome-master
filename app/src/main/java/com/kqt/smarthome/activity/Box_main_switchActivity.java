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
import android.widget.TextView;

import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACMsg;
import com.accloud.service.ACObject;
import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.ExpandableListViewAdapter_1;
import com.kqt.smarthome.entity.BoxManager;
import com.kqt.smarthome.entity.BoxShuntSwitch;
import com.kqt.smarthome.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

public class Box_main_switchActivity extends BaseActivity {

	private List<BoxShuntSwitch> list = new ArrayList<>();
	private ExpandableListView expandableListView;
	private ExpandableListViewAdapter_1 expandableListAdapter_1;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
                expandableListAdapter_1.notifyDataSetChanged();
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
		setContentView(R.layout.box_main_switch_activity);
		AddDeviceActivity.listAC.add(this);
		setTitle("总路列表");
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "");
		expandableListView = (ExpandableListView) findViewById(R.id.main_expandablelistview);
        expandableListAdapter_1 = new ExpandableListViewAdapter_1(list, this);
		expandableListView.setGroupIndicator(null);
		expandableListView.setAdapter(expandableListAdapter_1);
		initData();


		expandableListView
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> parent,
												   View view, final int position, long id) {
						final BoxShuntSwitch boxShuntSwitch = list
								.get(position);
						final TextView text = (TextView) view
								.findViewById(R.id.box_main_switch_name);
						CustomDialog.Builder customDialog = new CustomDialog.Builder(
								Box_main_switchActivity.this);
						customDialog.setTitle("改名");
						final EditText editText = new EditText(
								Box_main_switchActivity.this);

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
												.modifyLeakProtectionLineName(
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

	// 初始化数据
	public void initData() {
		list.clear();
		long num = getIntent().getLongExtra("leakageProtection", 0);
		long deviceId = getIntent().getLongExtra("deviceId", 0);
		for (int i = 0; i < num; i++) {
			list.add(new BoxShuntSwitch(deviceId, "总路" + i, false, "", 3.1f,
					3.1f, 3.1f, 10L, 3L, 3.14f, 20L, 10L, 20L, 20L, 10L, 12L,
					13L, 14L, 15L, false, true, true));
		}
		handler.sendEmptyMessage(0);
	}

	/**
	 * 查询分路信息
	 *
	 * @param
	 */
	public void queryShuntData(long deviceId) {
		// 查询
		BoxManager.getintence().queryLeakProtectStateInfo(deviceId, 0,
				new PayloadCallback<ACMsg>() {

					@Override
					public void error(ACException arg0) {
						Log.d("error", arg0.getErrorCode() + "");
					}

					@Override
					public void success(ACMsg arg0) {
						ACObject acObject_1 = arg0.get("stateInfo");
						long deviceId = acObject_1.getLong("deviceId");
						List<ACObject> list_ac = acObject_1
								.getList(deviceId + "");
						for (int i = 0; i < list_ac.size(); i++) {
							ACObject li_1 = list_ac.get(i);
							String lineName = li_1.getString("lineName");
							float voltage = li_1.getFloat("voltage");
							float current = li_1.getFloat("current");
							float totalElectricityQuantity = li_1
									.getFloat("totalElectricityQuantity");
							long capacity = li_1.getLong("capacity");
							long temperature = li_1.getLong("temperature");
							float powerFactor = li_1.getFloat("powerFactor");
							long limitedPower = li_1.getLong("limitedPower");
							long limitingPower = li_1.getLong("limitingPower");
							long overpressureUpperLimit = li_1
									.getLong("overpressureUpperLimit");
							long undervoltageLowerLimit = li_1
									.getLong("undervoltageLowerLimit");
							long currentCapacity = li_1
									.getLong("currentCapacity");
							long overTempSetting = li_1
									.getLong("overTempSetting");
							long operationEventCode = li_1
									.getLong("operationEventCode");
							long alarmEventCode = li_1.getLong("AlarmEventCode");
							long equipmentFaultCode = li_1
									.getLong("equipmentFaultCode");
							boolean alarmPushFlag = li_1
									.getBoolean("alarmPushFlag");
							boolean operationPushFlag = li_1
									.getBoolean("operationPushFlag");
							boolean operationLock = li_1
									.getBoolean("operationLock");
							boolean switchState = li_1.getBoolean("switchState");
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
