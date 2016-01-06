package com.kqt.smarthome.entity;

import org.eclipse.jetty.server.ResourceCache.Content;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACDeviceMsgMarshaller;
import com.accloud.service.ACException;
import com.accloud.service.ACKLVMessage;
import com.accloud.service.ACKLVObject;
import com.kqt.smarthome.activity.BoxMainActivity;
import com.kqt.smarthome.util.Config;

import android.R.bool;
import android.content.Context;
import android.util.Log;

/**
 * 分路
 * 
 * @author Administrator
 *
 */
public class BoxShuntSwitch {
	public static String ON = "1";
	public static String OFF = "0";
	private Context  context;

	private long deviceId; // 网关id
	private String name;
	private boolean status;
	private String record; // 操作记录

	private float voltage; // 电压
	private float current;// 电流
	private float totalElectricityQuantity;// 累计电量
	private long capacity;// 功率
	private long temperature;// 模块温度
	private float powerFactor;// 功率因素
	private long limitedPower;// 限定电量
	private long limitingPower;// 限定功率
	private long overpressureUpperLimit;// 过压上限值
	private long undervoltageLowerLimit;// 过压下限值
	private long currentCapacity;// 电流容量值
	private long overTempSetting;// 超温设定值
	private long operationEventCode;// 操作事件码
	private long AlarmEventCode;// 报警事件码
	private long equipmentFaultCode;// 设备故障码
	private boolean alarmPushFlag;// 报警事件是否推送
	private boolean operationPushFlag;// 操作事件是否推送
	private boolean operationLock;// 控制所

	
	public BoxShuntSwitch(Context content) {
		AC.bindMgr().setDeviceMsgMarshaller(new ACDeviceMsgMarshaller() {
			@Override
			public byte[] marshal(ACDeviceMsg msg) throws Exception {
				return (byte[]) msg.getContent();
			}

			@Override
			public ACDeviceMsg unmarshal(int msgCode, byte[] payload)
					throws Exception {
				return new ACDeviceMsg(msgCode, payload);
			}
		});

	}

	public BoxShuntSwitch(long deviceId, String name, boolean status,
			String record) {
		super();
		this.deviceId = deviceId;
		this.name = name;
		this.status = status;
		this.record = record;
	}

	public BoxShuntSwitch(long deviceId, String name, boolean status,
			String record, float voltage, float current,
			float totalElectricityQuantity, long capacity, long temperature,
			float powerFactor, long limitedPower, long limitingPower,
			long overpressureUpperLimit, long undervoltageLowerLimit,
			long currentCapacity, long overTempSetting,
			long operationEventCode, long alarmEventCode,
			long equipmentFaultCode, boolean alarmPushFlag,
			boolean operationPushFlag, boolean operationLock) {
		super();
		this.deviceId = deviceId;
		this.name = name;
		this.status = status;
		this.record = record;
		this.voltage = voltage;
		this.current = current;
		this.totalElectricityQuantity = totalElectricityQuantity;
		this.capacity = capacity;
		this.temperature = temperature;
		this.powerFactor = powerFactor;
		this.limitedPower = limitedPower;
		this.limitingPower = limitingPower;
		this.overpressureUpperLimit = overpressureUpperLimit;
		this.undervoltageLowerLimit = undervoltageLowerLimit;
		this.currentCapacity = currentCapacity;
		this.overTempSetting = overTempSetting;
		this.operationEventCode = operationEventCode;
		this.AlarmEventCode = alarmEventCode;
		this.equipmentFaultCode = equipmentFaultCode;
		this.alarmPushFlag = alarmPushFlag;
		this.operationPushFlag = operationPushFlag;
		this.operationLock = operationLock;
	}

	public float getVoltage() {
		return voltage;
	}

	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}

	public float getCurrent() {
		return current;
	}

	public void setCurrent(float current) {
		this.current = current;
	}

	public float getTotalElectricityQuantity() {
		return totalElectricityQuantity;
	}

	public void setTotalElectricityQuantity(float totalElectricityQuantity) {
		this.totalElectricityQuantity = totalElectricityQuantity;
	}

	public long getCapacity() {
		return capacity;
	}

	public void setCapacity(long capacity) {
		this.capacity = capacity;
	}

	public long getTemperature() {
		return temperature;
	}

	public void setTemperature(long temperature) {
		this.temperature = temperature;
	}

	public float getPowerFactor() {
		return powerFactor;
	}

	public void setPowerFactor(float powerFactor) {
		this.powerFactor = powerFactor;
	}

	public long getLimitedPower() {
		return limitedPower;
	}

	public void setLimitedPower(long limitedPower) {
		this.limitedPower = limitedPower;
	}

	public long getLimitingPower() {
		return limitingPower;
	}

	public void setLimitingPower(long limitingPower) {
		this.limitingPower = limitingPower;
	}

	public long getOverpressureUpperLimit() {
		return overpressureUpperLimit;
	}

	public void setOverpressureUpperLimit(long overpressureUpperLimit) {
		this.overpressureUpperLimit = overpressureUpperLimit;
	}

	public long getUndervoltageLowerLimit() {
		return undervoltageLowerLimit;
	}

	public void setUndervoltageLowerLimit(long undervoltageLowerLimit) {
		this.undervoltageLowerLimit = undervoltageLowerLimit;
	}

	public long getCurrentCapacity() {
		return currentCapacity;
	}

	public void setCurrentCapacity(long currentCapacity) {
		this.currentCapacity = currentCapacity;
	}

	public long getOverTempSetting() {
		return overTempSetting;
	}

	public void setOverTempSetting(long overTempSetting) {
		this.overTempSetting = overTempSetting;
	}

	public long getOperationEventCode() {
		return operationEventCode;
	}

	public void setOperationEventCode(long operationEventCode) {
		this.operationEventCode = operationEventCode;
	}

	public long getAlarmEventCode() {
		return AlarmEventCode;
	}

	public void setAlarmEventCode(long alarmEventCode) {
		AlarmEventCode = alarmEventCode;
	}

	public long getEquipmentFaultCode() {
		return equipmentFaultCode;
	}

	public void setEquipmentFaultCode(long equipmentFaultCode) {
		this.equipmentFaultCode = equipmentFaultCode;
	}

	public boolean isAlarmPushFlag() {
		return alarmPushFlag;
	}

	public void setAlarmPushFlag(boolean alarmPushFlag) {
		this.alarmPushFlag = alarmPushFlag;
	}

	public boolean isOperationPushFlag() {
		return operationPushFlag;
	}

	public void setOperationPushFlag(boolean operationPushFlag) {
		this.operationPushFlag = operationPushFlag;
	}

	public boolean isOperationLock() {
		return operationLock;
	}

	public void setOperationLock(boolean operationLock) {
		this.operationLock = operationLock;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getRecord() {
		return record;
	}

	public void setRecord(String record) {
		this.record = record;
	}

}
