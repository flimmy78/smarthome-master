package com.kqt.smarthome.entity;

/**
 * 返回的电箱信息
 * 
 * @author Administrator
 *
 */
public class BoxDevice {
	private long deviceId; // 电箱id
	private long leakageProtectionCount; // 漏保数量
	private long splitterCount; // 分路
	private long totalElectricQuantity; // 总电量
	private long time;

	public BoxDevice() {
		// TODO Auto-generated constructor stub
	}

	public BoxDevice(long deviceId, long leakageProtectionCount,
			long splitterCount, long totalElectricQuantity, long time) {
		super();
		this.deviceId = deviceId;
		this.leakageProtectionCount = leakageProtectionCount;
		this.splitterCount = splitterCount;
		this.totalElectricQuantity = totalElectricQuantity;
		this.time = time;
	}



	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public long getLeakageProtectionCount() {
		return leakageProtectionCount;
	}

	public void setLeakageProtectionCount(long leakageProtectionCount) {
		this.leakageProtectionCount = leakageProtectionCount;
	}

	public long getSplitterCount() {
		return splitterCount;
	}

	public void setSplitterCount(long splitterCount) {
		this.splitterCount = splitterCount;
	}

	public long getTotalElectricQuantity() {
		return totalElectricQuantity;
	}

	public void setTotalElectricQuantity(long totalElectricQuantity) {
		this.totalElectricQuantity = totalElectricQuantity;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
