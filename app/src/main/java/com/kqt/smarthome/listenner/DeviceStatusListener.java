package com.kqt.smarthome.listenner;


public interface DeviceStatusListener {
	public void receiveDeviceStatus(long userid, int status);
}
