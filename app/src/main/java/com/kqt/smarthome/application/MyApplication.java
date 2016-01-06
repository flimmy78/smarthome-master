package com.kqt.smarthome.application;

import android.R.bool;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.accloud.cloudservice.AC;
import com.accloud.service.ACAccountMgr;
import com.accloud.service.ACBindMgr;
import com.accloud.service.ACNotificationMgr;
import com.accloud.utils.PreferencesUtils;
import com.kqt.smarthome.service.BridgeService;
import com.kqt.smarthome.service.NetService;
import com.kqt.smarthome.util.Config;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.message.proguard.R;

public class MyApplication extends Application {

	public static boolean isF = true;
	

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		String domain = PreferencesUtils.getString(this, "mojrmain",
				Config.MajorDomain);
		long domainId = PreferencesUtils.getLong(this, "mojrmainid",
				Config.MajorDomainId);
		AC.init(this, domain, domainId, AC.TEST_MODE);
		Intent intent = new Intent(this, BridgeService.class);
		startService(intent); // 启动摄像头服务
		Intent net = new Intent(this, NetService.class);
		startService(net); // 启动摄像头服务
		Log.d("zjm", "Token" + AC.notificationMgr().getDeviceToken());
		AC.notificationMgr().setMessageHandler(new UmengMessageHandler() {
			@Override
			public void dealWithCustomMessage(final Context context,
					final UMessage msg) {
				new Handler().post(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						Toast.makeText(context, msg.custom, Toast.LENGTH_LONG)
								.show();
						boolean isClickOrDismissed = true;
						if (isClickOrDismissed) {
							UTrack.getInstance(getApplicationContext())
									.trackMsgClick(msg);
						} else {
							UTrack.getInstance(getApplicationContext())
									.trackMsgDismissed(msg);
						}
					}
				});
			}

			@Override
			public Notification getNotification(Context context, UMessage msg) {
				return super.getNotification(context, msg);
			}
		});
		isF = false;

	}

}
