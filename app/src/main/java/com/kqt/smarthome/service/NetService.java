/**
 * 
 */
package com.kqt.smarthome.service;

import com.kqt.smarthome.util.Util;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;

/**
 * @author wang.jingui
 */
public class NetService extends Service {
	private boolean isNet = true;
	private NetThread netThread;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		netThread = new NetThread();
		netThread.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (netThread != null) {
			netThread = null;
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	class NetThread extends Thread {

		@Override
		public void run() {
			while (isNet) {
				boolean flg = isOpenNetwork();

				if (!flg) {
					Util.network = false;
				}
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Util.network = true;
			}
		}
	}

	private boolean isOpenNetwork() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}

		return false;
	}
}
