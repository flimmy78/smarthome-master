package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.view.View;

import com.kqt.smarthome.R;


public class BoxPowerValueActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.boxpowervalue);
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", 0, "");
		setTitle("电量查询");
		initData();
		initView();
   
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initData() {
	}

	private void initView() {
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		finish();
	}
}
