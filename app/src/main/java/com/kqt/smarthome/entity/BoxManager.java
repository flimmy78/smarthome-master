package com.kqt.smarthome.entity;

import android.content.Context;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACMsg;
import com.accloud.utils.PreferencesUtils;
import com.kqt.smarthome.util.Config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BoxManager {

	private static BoxManager boxManager;

	public static BoxManager getintence() {
		if (boxManager == null)
			boxManager = new BoxManager();

		return boxManager;

	}

	/*
	 * 查询电箱信息
	 */
	public void queryDeviceStateInfo(long deviceId,
			PayloadCallback<ACMsg> callback) {

		List<HashMap<String, Object>> deviceIdList = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("deviceId", deviceId);
		deviceIdList.add(hashMap);

		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("queryDeviceStateInfo");
		acMsg.put("deviceIdList", deviceIdList);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);
	}

	/**
	 * 查询电箱操作日志
	 */
	public void queryOperationEventLog(String deviceId, String startTime,
			String endTime, PayloadCallback<ACMsg> callback) {
		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("queryOperationEventLog");
		acMsg.put("deviceId", deviceId);
		acMsg.put("startTime", startTime);
		acMsg.put("endTime", endTime);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);
	}

	/**
	 * 查询报警日志
	 * 
	 * @param deviceId
	 * @param startTime
	 * @param endTime
	 * @param callback
	 */
	public void queryAlamEventLog(String deviceId, String startTime,
			String endTime, PayloadCallback<ACMsg> callback) {
		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("queryAlamEventLog");
		acMsg.put("deviceId", deviceId);
		acMsg.put("startTime", startTime);
		acMsg.put("endTime", endTime);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);
	}

	/**
	 * 处理来自APP端的控制命令
	 * 
	 * @param deviceId
	 * @param lineNum
	 * @param action
	 * @param value
	 * @param callback
	 */
	public void controlChannelDevice(long deviceId, int lineNum, String action,
			String value, PayloadCallback<ACMsg> callback) {
		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("controlChannelDevice");
		acMsg.put("deviceId", deviceId);
		acMsg.put("lineNum", lineNum);
		acMsg.put("action", action);
		acMsg.put("value", value);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);
	}

	/**
	 * 直接发送消息到设备
	 * @param context
	 * @param code
	 * @param device
	 * @param boxShuntSwitch
	 * @param callback
	 */
	public void SendToDeviceControl(Context context, int code, long device,
			BoxShuntSwitch boxShuntSwitch, PayloadCallback<ACDeviceMsg> callback) {
		ACDeviceMsg deviceMsg = new ACDeviceMsg(code, boxShuntSwitch);
		String submain = PreferencesUtils.getString(context, Config.SUBDOMAIN);
		AC.bindMgr().sendToDeviceWithOption(submain, device, deviceMsg,
				AC.LOCAL_FIRST, callback);
	}

	/**
	 * 查询电量
	 * 
	 * @param deviceId
	 * @param lineNum
	 * @param reportType
	 * @param callback
	 */
	public void queryChannelElectriStatInfo(long deviceId, int lineNum,
			int reportType, PayloadCallback<ACMsg> callback) {
		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("queryChannelElectriStatInfo");
		acMsg.put("deviceId", deviceId);
		acMsg.put("lineNum", lineNum);
		acMsg.put("reportType", reportType);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);

	}

	/**
	 * 查询智能分路器状态信息
	 * 
	 * @param deviceId
	 * @param lineNum
	 *            分路号为0时,查询所有路的信息
	 * @param callback
	 */
	public void queryChannelDivideStateInfo(long deviceId, int lineNum,
			PayloadCallback<ACMsg> callback) {
		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("queryChannelDivideStateInfo");
		acMsg.put("deviceId", deviceId);
		acMsg.put("lineNum", lineNum);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);

	}

	/**
	 * 查询漏电保护器分路信息
	 * @param deviceId
	 * @param lineNum
	 * @param callback
	 */
	public void queryLeakProtectStateInfo(long deviceId, int lineNum,
			PayloadCallback<ACMsg> callback) {
		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("queryLeakProtectStateInfo");
		acMsg.put("deviceId", deviceId);
		acMsg.put("lineNum", lineNum);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);

	}

	/**
	 * 修改分路名称
	 * 
	 * @param deviceId
	 * @param lineNum
	 * @param lineName
	 * @param callback
	 */
	public void modifyChannelDivideLineName(long deviceId, int lineNum,
			String lineName, PayloadCallback<ACMsg> callback) {
		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("modifyChannelDivideLineName");
		acMsg.put("deviceId", deviceId);
		acMsg.put("lineNum", lineNum);
		acMsg.put("lineName", lineName);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);

	}

	/**
	 * 修改漏保分路名称
	 * @param deviceId
	 * @param lineNum
	 * @param lineName
	 * @param callback
	 */
	public void modifyLeakProtectionLineName(long deviceId, int lineNum,
			String lineName, PayloadCallback<ACMsg> callback) {
		ACMsg acMsg = new ACMsg();
		acMsg = ACMsg.getACMsg(Config.MajorDomain);
		acMsg.setName("modifyLeakProtectionLineName");
		acMsg.put("deviceId", deviceId);
		acMsg.put("lineNum", lineNum);
		acMsg.put("lineName", lineName);
		AC.sendToService(Config.SUBDOMAIN, Config.SERVICENAME, 1, acMsg,
				callback);

	}
}
