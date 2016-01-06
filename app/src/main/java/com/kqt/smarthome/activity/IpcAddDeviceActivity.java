package com.kqt.smarthome.activity;

import java.util.ArrayList;
import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kqt.smarthome.R;
import com.kqt.smarthome.callback.InitCallBack;
import com.kqt.smarthome.db.DeviceManager;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.listenner.SeachListener;
import com.kqt.smarthome.service.BridgeService;
import com.kqt.smarthome.view.CustomDialog;
import com.kqt.smarthome.view.LoadingDialog;
import com.kqt.smarthome.decoding.CaptureActivity;

public class IpcAddDeviceActivity extends BaseActivity implements
		SeachListener, OnClickListener {

	private LinearLayout wifi_search_device, auto_add_device;
	private EditText device_mac, device_name, device_admin, device_psw;
	private LoadingDialog dialog;
	private List<IpcDevice> searchlist = new ArrayList<IpcDevice>(); // 搜索结果返回的列表
	private List<IpcDevice> querylist; // 查询返回的列表
	private Builder builder;
	private Dialog alertDialog;
	private IpcDevice ipc;
	private InitCallBack callBack;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			int tem = msg.arg1;
			int what = msg.what;
			if (what == 2) {
				alertDialog.dismiss();
				IpcDevice ipcDevice = searchlist.get(tem);
				if (ipcDevice != null) {
					device_mac.setText(ipcDevice.getDeviceid() + "");
					device_name.setText(ipcDevice.getName());
					device_psw.setHint("默认");
				}
			} else if (msg.what == 1) {
				MyAlert();
				dialog.dismiss();

			}
		}

	};

	protected void onStart() {
		super.onStart();

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_ipc);
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "完成");
		setTitle("设备添加");
		BridgeService.setSearchListener(this);
		DeviceSDK.initSearchDevice();
		dialog = new LoadingDialog(this);
		wifi_search_device = (LinearLayout) findViewById(R.id.wifi_search_device);
		auto_add_device = (LinearLayout) findViewById(R.id.auto_add_device);
		device_mac = (EditText) findViewById(R.id.device_mac);
		device_name = (EditText) findViewById(R.id.device_name);
		device_admin = (EditText) findViewById(R.id.device_admin);
		device_psw = (EditText) findViewById(R.id.device_psw);

		wifi_search_device.setOnClickListener(this);
		auto_add_device.setOnClickListener(this);
		initView();
	}

	public void initView() {
		querylist = DeviceManager.getInstence(this).GetIPCListDevice();

	}

	@SuppressLint("NewApi")
	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		if (titleBar == TitleBar.LIEFT) {
			finish();
			setResult(1);
		} else { // 完成
			String dname = device_name.getText().toString().trim();
			String did = device_mac.getText().toString().trim();
			String dadmin = device_admin.getText().toString().trim();
			String dpsw = device_psw.getText().toString().trim();
			if (dname.isEmpty() || did.isEmpty() || dadmin.isEmpty()) {
				Toast.makeText(this, "设备信息输入不全,重新输入", Toast.LENGTH_SHORT)
						.show();
				return;
			} else {
				// dialog.show();
				// dialog.setText("添加中..");
				ipc = new IpcDevice();
				ipc.setName(dname);
				ipc.setDeviceid(did);
				ipc.setAdmin_name(dadmin);
				ipc.setPsw(dpsw);

				long temp = DeviceSDK.createDevice(ipc.getAdmin_name(), "",
						ipc.getIp(), ipc.getPort(), ipc.getDeviceid(), 1);
				if (temp > 0) {
					long flg1 = DeviceSDK.openDevice(temp);
					if (flg1 > 0) {
						ipc.setUserid(temp);
						boolean flg = false;
						if (querylist != null) {
							for (IpcDevice d : querylist) {
								if (ipc.getDeviceid().equals(d.getDeviceid())) {
									flg = true;
									Toast.makeText(IpcAddDeviceActivity.this,
											"该设备已存在", 0).show();
									// dialog.dismiss();
									return;
								}
							}
						} else {
							flg = false;
						}
						if (!flg) {
							DeviceManager
									.getInstence(IpcAddDeviceActivity.this)
									.SaveIpcDevice(ipc);
							Toast.makeText(IpcAddDeviceActivity.this, "添加成功",
									Toast.LENGTH_SHORT).show();
							setResult(1);
							finish();
						}

					}

				}

			}

		}
	}

	@Override
	public void callBack_SeachData(String Deviceinfo) {
		Log.e("seach", "接收到搜索信息");
		// 回调搜索的信息
		boolean temp = false;
		try {
			JSONObject jsonObject = new JSONObject(Deviceinfo);
			IpcDevice device = new IpcDevice();
			device.setMac(jsonObject.getString("Mac"));
			device.setName(jsonObject.getString("DeviceName"));
			device.setDeviceid(jsonObject.getString("DeviceID"));
			device.setIp(jsonObject.getString("IP"));
			device.setPort(jsonObject.getInt("Port"));
			if (searchlist.size() == 0) {
				temp = false;
			} else {
				for (IpcDevice ipc : searchlist) {
					if (ipc.getDeviceid().equals(device.getDeviceid())) {
						temp = true;
					}
				}
			}
			if (!temp) {
				searchlist.add(device);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == wifi_search_device.getId()) {
			DeviceSDK.searchDevice(); // 初始化搜索
			dialog.setText("搜索中..");
			dialog.show();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
						if (searchlist.size() != 0) {
							// 跳转显示页
							handler.sendEmptyMessage(1);

						} else {
							dialog.dismiss();
							Looper.prepare();
							Toast.makeText(IpcAddDeviceActivity.this,
									"没有搜到相关设备", 0).show();
							Looper.loop();
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).start();
		} else if (id == auto_add_device.getId()) {
			Intent intent = new Intent(this, CaptureActivity.class);
			startActivityForResult(intent, Activity.RESULT_OK);

		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Activity.RESULT_OK) {
			// 返回的图片信息
		}

	}

	public void MyAlert() {
		builder = new Builder(this);

		ListView listView = new ListView(this);
		Myadpter myadpter = new Myadpter();
		listView.setAdapter(myadpter);
		builder.setTitle("设备列表");
		builder.setView(listView);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		alertDialog = builder.create();
		alertDialog.show();

	}

	class Myadpter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return searchlist.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return searchlist.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("ViewHolder")
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			convertView = LayoutInflater.from(IpcAddDeviceActivity.this)
					.inflate(R.layout.search_device_item, null);
			ImageView img = (ImageView) convertView
					.findViewById(R.id.search_device_img);
			TextView name = (TextView) convertView
					.findViewById(R.id.search_device_name);
			TextView userid = (TextView) convertView
					.findViewById(R.id.search_device_userid);
			IpcDevice ipcDevice = searchlist.get(position);
			name.setText(ipcDevice.getName());
			userid.setText(ipcDevice.getDeviceid());
			convertView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// device = ipcDevice;
					Message message = handler.obtainMessage();
					message.arg1 = position;
					message.what = 2;
					handler.sendMessage(message);
				}
			});
			return convertView;
		}
	}

}
