package com.kqt.smarthome.entity;

public class BoxMode {
	private long count; // 数量
	private int mark; // 标识
	private long deviceId;

	public BoxMode(long count, int mark, long deviceId) {
		super();
		this.count = count;
		this.mark = mark;
		this.deviceId = deviceId;
	}

	public long getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(long deviceId) {
		this.deviceId = deviceId;
	}

	public BoxMode() {
		// TODO Auto-generated constructor stub
	}

	public long getCount() {
		return count;
	}

	public void setCount(long count) {
		this.count = count;
	}

	public int getMark() {
		return mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

}
