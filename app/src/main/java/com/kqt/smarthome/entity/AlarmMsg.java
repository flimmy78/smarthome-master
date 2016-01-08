package com.kqt.smarthome.entity;

public class AlarmMsg {

	private int id;
	private String time;
	private String mac;
	private String msg;
	private String filepath;
	public String getFilepath() {
		return filepath;
	}

	public void setFilepath(String filepath) {
		this.filepath = filepath;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public AlarmMsg(int id, String time, String mac, String msg, String filepath) {
		super();
		this.id = id;
		this.time = time;
		this.mac = mac;
		this.msg = msg;
		this.filepath = filepath;
	}

	public AlarmMsg() {
		// TODO Auto-generated constructor stub
	}
}
