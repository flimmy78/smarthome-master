package com.kqt.smarthome.activity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.db.DeviceManager;
import com.kqt.smarthome.entity.AlarmMsg;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.listenner.PictureListener;
import com.kqt.smarthome.service.BridgeService;
import com.kqt.smarthome.util.Util;

public class DeviceAlarmActivity extends Activity implements OnClickListener,
		PictureListener {

	private TextView ipcName, alarmMsg, timemsg;
	private LinearLayout check, dimss;
	private long userid;
	private String msg;
	public static boolean isvis = false;
	private SoundPool sound;
	private int music;
	private Vibrator vibrator;
	private Bitmap bitmap;
	private ImageView imageView;
	private String imgstr;
	private IpcDevice device;
	private String date;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == 1) {
				if (bitmap != null)
					imageView.setImageBitmap(bitmap);
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		BridgeService.setPictureListener(this);
		isvis = true;
		setContentView(R.layout.activity_alarm);
		initView();
		vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		long[] pattern = { 100, 400, 100, 400 };
		vibrator.vibrate(pattern, -1);
	}

	private void initView() {
		Intent intent = getIntent();
		userid = intent.getLongExtra("userid", -1);
		int type = intent.getIntExtra("nType", -1);
		String pic = intent.getStringExtra("pic");
		msg = Util.GetAlarmMsg(type);
		device = DeviceManager.getInstence(this).QueryDevice(userid);
		date = Util.getNowTime();
		timemsg = (TextView) findViewById(R.id.alarm_time_text);
		ipcName = (TextView) findViewById(R.id.alarm_ipcname);
		alarmMsg = (TextView) findViewById(R.id.alarm_msg);
		check = (LinearLayout) findViewById(R.id.alarm_check);
		dimss = (LinearLayout) findViewById(R.id.alarm_dimss);
		imageView = (ImageView) findViewById(R.id.alarm_img);
		check.setOnClickListener(this);
		dimss.setOnClickListener(this);
		if (type == -1 && device == null) {
			alarmMsg.setText(msg);
			ipcName.setText("未知设备");

		} else {
			alarmMsg.setText(msg);
			ipcName.setText(device.getName());
		}
		timemsg.setText(date);
		sound = new SoundPool(10, AudioManager.STREAM_MUSIC, 5); // 设置一个声音点
		music = sound.load(this, R.raw.sms_received3, 1); // 加载raw文件内的声音
		sound.setOnLoadCompleteListener(new OnLoadCompleteListener() {
			@Override
			public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
				sound.play(music, 1, 1, 1, 5, 2);
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		vibrator.cancel();
		AlarmMsg alarmmsg = new AlarmMsg(0, date, device.getDeviceid(), msg, imgstr);
		DeviceManager.getInstence(this).SaveMsg(alarmmsg);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		isvis = false;
		vibrator.cancel();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == check.getId()) {
			Intent intent = new Intent(this, PlayDeviceActivity.class);
			intent.putExtra("userid", userid);
			sound.stop(music);
			startActivity(intent);
			finish();
		} else if (id == dimss.getId()) {
			finish();
			sound.stop(music);
			// moveTaskToBack(true);
		}

	}

	@Override
	public void CallBack_RecordPicture(long userid, byte[] buff, int len) {
		try {
			imgstr = new String(buff, "ISO-8859-1");
			bitmap = Util.decodeBitmap(imgstr);
			handler.sendEmptyMessage(1);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
