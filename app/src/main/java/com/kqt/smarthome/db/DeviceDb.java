package com.kqt.smarthome.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DeviceDb extends SQLiteOpenHelper {
	private Context context;
	private static String dbName = "smart.db";
	private static int version = 1;
	public static String tableName = "device";
	public static String tableIPCName = "ipcdevice";
	public static String tableMsg = "alarmmsg";

	public DeviceDb(Context context) {
		super(context, dbName, null, version);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table "
				+ tableName
				+ " (deviceUUid VARCHAR(255) PRIMARY KEY ,device_name VARCHAR(50),device_type INTEGER,deviceuserid INTEGER,status INTEGE, deviceid INTEGER)";
		db.execSQL(sql);
		String sql_ipc = "create table "
				+ tableIPCName
				+ " (device_mac VARCHAR(255) PRIMARY KEY ,device_name VARCHAR(255),device_pwd VARCHAR(255),device_adminname VARCHAR(255),device_pic VARCHAR(255),device_userid INTEGER,device_status INTEGER)";
		db.execSQL(sql_ipc);
		String sql_msg = "create table "
				+ tableMsg
				+ " (msg_id INTEGER PRIMARY KEY ,time VARCHAR(255),device_mac VARCHAR(255),msg VARCHAR(255),filepath VARCHAR(255))";
		db.execSQL(sql_msg);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// if (newVersion > oldVersion) {
		// db.execSQL("drop table device");
		// }
		// onCreate(db);
	}

}
