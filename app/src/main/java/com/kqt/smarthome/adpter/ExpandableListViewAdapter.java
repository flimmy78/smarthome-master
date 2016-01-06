package com.kqt.smarthome.adpter;

import java.util.List;

import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;
import com.accloud.service.ACMsg;
import com.kqt.smarthome.R;
import com.kqt.smarthome.entity.BoxManager;
import com.kqt.smarthome.entity.BoxShuntSwitch;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.view.CustomDialog;

/**
 * 折叠式listview的自定义BaseExpandableListAdapter
 * 
 * @author Administrator
 * 
 */
public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

	private List<BoxShuntSwitch> list;
	private Context context;

	public ExpandableListViewAdapter(List<BoxShuntSwitch> list, Context context) {
		// TODO Auto-generated constructor stub
		this.list = list;
		this.context = context;

	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewChlidholder chlidholder;
		final BoxShuntSwitch manager = list.get(groupPosition);
		chlidholder = new ViewChlidholder();
		convertView = LayoutInflater.from(context).inflate(R.layout.item_child,
				null);
		chlidholder.AlarmEventCode = (TextView) convertView
				.findViewById(R.id.AlarmEventCode);
		chlidholder.alarmPushFlag = (TextView) convertView
				.findViewById(R.id.alarmPushFlag);
		chlidholder.capacity = (TextView) convertView
				.findViewById(R.id.capacity);
		chlidholder.current = (TextView) convertView.findViewById(R.id.current);
		chlidholder.equipmentFaultCode = (TextView) convertView
				.findViewById(R.id.equipmentFaultCode);
		chlidholder.limitedPower = (TextView) convertView
				.findViewById(R.id.limitedPower);
		chlidholder.operationEventCode = (TextView) convertView
				.findViewById(R.id.operationEventCode);
		chlidholder.operationLock = (TextView) convertView
				.findViewById(R.id.operationLock);
		chlidholder.operationPushFlag = (TextView) convertView
				.findViewById(R.id.operationPushFlag);
		chlidholder.overpressureUpperLimit = (TextView) convertView
				.findViewById(R.id.overpressureUpperLimit);
		chlidholder.overTempSetting = (TextView) convertView
				.findViewById(R.id.overTempSetting);
		chlidholder.powerFactor = (TextView) convertView
				.findViewById(R.id.powerFactor);
		chlidholder.temperature = (TextView) convertView
				.findViewById(R.id.temperature);
		chlidholder.voltage = (TextView) convertView.findViewById(R.id.voltage);
		chlidholder.undervoltageLowerLimit = (TextView) convertView
				.findViewById(R.id.undervoltageLowerLimit);
		chlidholder.totalElectricityQuantity = (TextView) convertView
				.findViewById(R.id.totalElectricityQuantity);

		chlidholder.equipmentFaultCode.setText(manager.getEquipmentFaultCode()
				+ "");
		chlidholder.alarmPushFlag.setText(manager.isAlarmPushFlag() + "");
		chlidholder.current.setText(manager.getCurrent() + "");
		chlidholder.capacity.setText(manager.getCapacity() + "");
		chlidholder.operationPushFlag.setText(manager.isOperationPushFlag()
				+ "");
		chlidholder.operationLock.setText(manager.isOperationLock() + "");
		chlidholder.operationEventCode.setText(manager.getOperationEventCode()
				+ "");
		chlidholder.limitedPower.setText(manager.getLimitedPower() + "");
		chlidholder.temperature.setText(manager.getTemperature() + "");
		chlidholder.powerFactor.setText(manager.getPowerFactor() + "");
		chlidholder.overTempSetting.setText(manager.getOverTempSetting() + "");
		chlidholder.overpressureUpperLimit.setText(manager
				.getOverpressureUpperLimit() + "");
		chlidholder.voltage.setText(manager.getVoltage() + "");
		chlidholder.AlarmEventCode.setText(manager.getAlarmEventCode() + "");
		chlidholder.undervoltageLowerLimit.setText(manager
				.getUndervoltageLowerLimit() + "");
		chlidholder.totalElectricityQuantity.setText(manager
				.getTotalElectricityQuantity() + "");

		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Viewholder viewholder;
		if (convertView == null) {
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.box_shunt_switch_item, null);
			viewholder.img = (ImageView) convertView
					.findViewById(R.id.box_shunt_switch_img);
			viewholder.dname = (TextView) convertView
					.findViewById(R.id.box_shunt_switch_name);
			convertView.setTag(viewholder);

		} else {
			viewholder = (Viewholder) convertView.getTag();
		}
		final BoxShuntSwitch manager = list.get(groupPosition);
		final long deviceId = manager.getDeviceId();
		final String name = manager.getName();
		boolean status = manager.isStatus();
		viewholder.dname.setText(name);
		viewholder.img.setImageResource(status ? R.drawable.checkbox_on
				: R.drawable.checkbox_off);

		viewholder.img.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String stuts ="0";
				boolean isopen = false;
				BoxShuntSwitch boxShuntSwitch = new BoxShuntSwitch(context);
				int code = 100;
				if (manager.isStatus()) {
					stuts="0";

				} else {
					isopen = true;
					stuts = "";
				}
				//发送指令到服务
				BoxManager.getintence().controlChannelDevice(deviceId,
						groupPosition, Config.MODE_TYPE_POWER,
						stuts, new PayloadCallback<ACMsg>() {
							@Override
							public void success(ACMsg arg0) {
								String code = arg0.getString("code");
								if (code.equals("1")) {
									manager.setStatus(true);
								} else
									Log.e("controlChannelDevice", "控制失败");

							}

							@Override
							public void error(ACException arg0) {
								Log.d("error", arg0.getErrorCode() + "");
							}
						});
				//发送指令到设备
				BoxManager.getintence().SendToDeviceControl(context, code,
						deviceId, boxShuntSwitch,
						new PayloadCallback<ACDeviceMsg>() {

							@Override
							public void success(ACDeviceMsg arg0) {
								// 发送成功

							}

							@Override
							public void error(ACException arg0) {

							}
						});
			}
		});

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return false;
	}

	class Viewholder {
		ImageView img;
		TextView dname;

	}

	class ViewChlidholder {
		TextView voltage, current, totalElectricityQuantity, capacity,
				temperature, operationLock, overpressureUpperLimit,
				powerFactor, limitedPower, undervoltageLowerLimit,
				overTempSetting, operationEventCode, AlarmEventCode,
				equipmentFaultCode, alarmPushFlag, operationPushFlag;

	}

}
