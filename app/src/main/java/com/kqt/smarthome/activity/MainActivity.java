package com.kqt.smarthome.activity;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.PendingIntent.OnFinished;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.accloud.cloudservice.AC;
import com.kqt.smarthome.R;
import com.kqt.smarthome.db.DeviceManager;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.fragment.DeviceLayerFragment;
import com.kqt.smarthome.fragment.NewsLayerFragment;
import com.kqt.smarthome.fragment.ShopLayerFragment;
import com.kqt.smarthome.fragment.UserLayerFragment;
import com.kqt.smarthome.listenner.AlarmInformationMsg;
import com.kqt.smarthome.service.BridgeService;
import com.kqt.smarthome.util.Util;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.viewpager.SViewPager;

public class MainActivity extends FragmentActivity implements
		AlarmInformationMsg {
	private IndicatorViewPager indicatorViewPager;
	public static boolean isExit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		BridgeService.setAlarmInformationMsg(this);
		SViewPager viewPager = (SViewPager) findViewById(R.id.tabmain_viewPager);
		Indicator indicator = (Indicator) findViewById(R.id.tabmain_indicator);
		indicatorViewPager = new IndicatorViewPager(indicator, viewPager);
		indicatorViewPager
				.setAdapter(new MyAdapter(getSupportFragmentManager()));
		viewPager.setCanScroll(false);
		viewPager.setOffscreenPageLimit(4);
	}

	private class MyAdapter extends IndicatorFragmentPagerAdapter {
		private String[] tabNames = { "设备", "消息", "商城", "个人" };
		private int[] tabIcons = { R.drawable.maintab_1_selector,
				R.drawable.maintab_2_selector, R.drawable.maintab_3_selector,
				R.drawable.maintab_4_selector };
		private LayoutInflater inflater;

		public MyAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
			inflater = LayoutInflater.from(getApplicationContext());
		}

		@Override
		public int getCount() {
			return tabNames.length;
		}

		@Override
		public View getViewForTab(int position, View convertView,
				ViewGroup container) {
			if (convertView == null) {
				convertView = (TextView) inflater.inflate(R.layout.tab_main,
						container, false);
			}
			TextView textView = (TextView) convertView;
			textView.setText(tabNames[position]);
			textView.setCompoundDrawablesWithIntrinsicBounds(0,
					tabIcons[position], 0, 0);
			return textView;
		}

		@Override
		public Fragment getFragmentForPage(int position) {
			Fragment fragment = null;
			System.out.println(position);

			switch (position) {
			case 0:
				fragment = new DeviceLayerFragment();
				break;
			case 1:
				fragment = new NewsLayerFragment();
				break;
			case 2:
				fragment = new ShopLayerFragment();
				break;
			case 3:
				fragment = new UserLayerFragment();
				break;

			default:
				break;
			}
			return fragment;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		AC.accountMgr().logout(); // 注销

	}



	@Override
	public void alarmMgs(long userid, int ntype) {
		Log.d("zjm", "报警信息---userid-" + userid + " ntype-" + ntype);
		String msg = Util.GetAlarmMsg(ntype);
		Alert(msg, userid);

	}

	public void Alert(final String msg, final long userid) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("报警")
						.setMessage(msg)
						.setPositiveButton("查看",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												MainActivity.this,
												PlayDeviceActivity.class);
										intent.putExtra("userid", userid);
										startActivity(intent);
									}
								})
						.setNegativeButton("忽略",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
									}
								}).show();
			}
		}).start();
		;

	}

	private long exitTime = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit() {
		if ((System.currentTimeMillis() - exitTime) > 2000) {
			Toast.makeText(getApplicationContext(), "再按一次,后台运行",
					Toast.LENGTH_SHORT).show();
			exitTime = System.currentTimeMillis();
		} else {
			BridgeService.isBackground = true;
			Intent localIntent = new Intent(MainActivity.this,
					BridgeService.class);
			MainActivity.this.startService(localIntent);
			moveTaskToBack(true);
		}
	}

}
