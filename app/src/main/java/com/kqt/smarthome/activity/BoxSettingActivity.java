package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.BoxSettingAdpter;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.util.Util;

import java.util.HashMap;
import java.util.List;

public class BoxSettingActivity extends BaseActivity {
	private ListView listview;
	private TextView title_left;
	private List<HashMap<String, String>> list = null;
	private BoxSettingAdpter adpter;
	public static Device device;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings_screen);
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", 0, "");
		setTitle("电箱设置");
		initData();
		initView();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		device = null;
	}

	private void initData() {
		list = Util.BoxSettingData();
		device = (Device) getIntent().getSerializableExtra(Config.BOX_D);
	}

	private void initView() {
		listview = (ListView) findViewById(R.id.setting_listview);
		adpter = new BoxSettingAdpter(this, list, device);
		listview.setAdapter(adpter);
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		finish();
	}
}
