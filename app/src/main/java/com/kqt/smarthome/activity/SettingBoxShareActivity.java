package com.kqt.smarthome.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.google.zxing.WriterException;
import com.kqt.smarthome.R;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.view.LoadingDialog;
import com.zxing.encoding.EncodingHandler;

/**
 * 分享
 * 
 * @author Administrator
 *
 */
public class SettingBoxShareActivity extends BaseActivity {
	private Device device;
	private ImageView box_sharp_code_img;
	private LoadingDialog dialog;
	private LinearLayout error, cont;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			SettingBoxShareActivity.this.hideProgressDialog();
			if (msg.what == 0) {
				cont.setVisibility(View.VISIBLE);
				error.setVisibility(View.GONE);
			} else {
				error.setVisibility(View.VISIBLE);
				cont.setVisibility(View.GONE);
			}

		}

	};

	protected void onDestroy() {
		super.onDestroy();
		handler.removeMessages(0);
		handler.removeMessages(1);
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.box_sharp_activity);
		device = BoxSettingActivity.device;
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "刷新", 0, "");
		setTitle("设备分享");
		dialog = new LoadingDialog(this);
		box_sharp_code_img = (ImageView) findViewById(R.id.box_sharp_code_img);
		error = (LinearLayout) findViewById(R.id.error_layout);
		cont = (LinearLayout) findViewById(R.id.content_layout);
		this.showProgressDialog("");
		DeviceSharp();
	}

	public void creatBitmap(String code) {
		try {
			Bitmap bitmap = EncodingHandler.createQRCode(code, 250);
			box_sharp_code_img.setImageBitmap(bitmap);
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void DeviceSharp() {

		AC.bindMgr().getShareCode(Config.GetsubDomain(device.getSubmainID()),
				device.getDeviceid(), new PayloadCallback<String>() {

					@Override
					public void success(String arg0) {
						Log.d("zjm", "设备分享码" + arg0);
						creatBitmap(arg0);
						handler.sendEmptyMessage(0);
					}

					@Override
					public void error(ACException arg0) {
						Log.d("zjm", "分享失败");
						handler.sendEmptyMessage(1);
					}
				});
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		// TODO Auto-generated method stub
		if (titleBar == TitleBar.LIEFT) {
			finish();
		} else if (titleBar == TitleBar.RIGHT) {
			this.showProgressDialog("");
			DeviceSharp();
		}
	}
}
