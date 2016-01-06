package com.kqt.smarthome.entity;

public class TypeDevice {

	private int dType; // 设备类型

	private String dName; // 设备名称

	
	
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

	public TypeDevice(int dType, String dName) {
		super();
		this.dType = dType;
		this.dName = dName;
	}

	
	
}
