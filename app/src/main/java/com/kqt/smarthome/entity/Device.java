package com.kqt.smarthome.entity;

import java.io.Serializable;

public class Device implements Serializable {

	private int dType; // 设备类型

	private String dName; // 设备名称

	private String uuid; // 设备序列号

	private int userid; // 设备的userid (主要用于兼容摄像头)

	private int status; // 设备状态

	private long deviceid; // 该id是调用list接口返回的id

	private long gatewayDeviceId; // 网关id

	private long submainID; // 子域id

	private long Ownerid;  //管理员id

	public long getOwnerid() {
		return Ownerid;
	}

	public void setOwnerid(long ownerid) {
		Ownerid = ownerid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}


	public long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(long deviceid) {
		this.deviceid = deviceid;
	}

	public Device() {
	}

	public long getSubmainID() {
		return submainID;
	}

	public void setSubmainID(long submainID) {
		this.submainID = submainID;
	}

	

	public Device(int dType, String dName, String uuid, int userid, int status,
			long deviceid, long gatewayDeviceId, long submainID, long ownerid) {
		super();
		this.dType = dType;
		this.dName = dName;
		this.uuid = uuid;
		this.userid = userid;
		this.status = status;
		this.deviceid = deviceid;
		this.gatewayDeviceId = gatewayDeviceId;
		this.submainID = submainID;
		Ownerid = ownerid;
	}

	public long getGatewayDeviceId() {
		return gatewayDeviceId;
	}

	public void setGatewayDeviceId(long gatewayDeviceId) {
		this.gatewayDeviceId = gatewayDeviceId;
	}

	public int getdType() {
		return dType;
	}

	public void setdType(int dType) {
		this.dType = dType;
	}

	public String getdName() {
		return dName;
	}

	public void setdName(String dName) {
		this.dName = dName;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

}
