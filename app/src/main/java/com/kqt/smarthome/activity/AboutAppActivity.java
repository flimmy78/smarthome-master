package com.kqt.smarthome.activity;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kqt.smarthome.R;

public class AboutAppActivity extends BaseActivity {
	private TextView version;
	private PackageInfo info;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_acitivty);
		setTitle("软件关于");
		setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "");
		version = (TextView) findViewById(R.id.app_version);

		try {
			info = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		version.setText(info.versionName);
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		// TODO Auto-generated method stub
		back();
	}
}
