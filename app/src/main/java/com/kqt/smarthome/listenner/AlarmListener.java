package com.kqt.smarthome.listenner;

public interface AlarmListener {
	void callBack_getParam(long UserID, long nType, String param);
	void callBack_setParam(long UserID, long nType, int nResult);
	  
}
