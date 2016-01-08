package com.kqt.smarthome.adpter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.SettingIpcAPActivity;
import com.kqt.smarthome.activity.SettingIpcAlarmActivity;
import com.kqt.smarthome.activity.SettingIpcFTPActivity;
import com.kqt.smarthome.activity.SettingIpcNameActivity;
import com.kqt.smarthome.activity.SettingIpcSharpActivity;
import com.kqt.smarthome.activity.SettingIpcTimeActivity;
import com.kqt.smarthome.activity.SettingIpcUserActivity;
import com.kqt.smarthome.activity.SettingIpcWiFiActivity;
import com.kqt.smarthome.activity.SettingOverturnActivity;
import com.kqt.smarthome.entity.IpcDevice;

import java.util.HashMap;
import java.util.List;


public class IpcSettingAdpter extends BaseAdapter {

	private List<HashMap<String, String>> list;
	private Context context;
	private IpcDevice device;

	public IpcSettingAdpter(Context context,
			List<HashMap<String, String>> list, IpcDevice device) {
		this.context = context;
		this.list = list;
		this.device = device;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null || list.size() == 0) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		Myview view;
		if (convertView == null) {
			view = new Myview();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.device_settting_item, null);
			view.name = (TextView) convertView
					.findViewById(R.id.device_setting_text);
			view.img = (ImageView) convertView
					.findViewById(R.id.device_setting_img);
			convertView.setTag(view);
		} else {
			view = (Myview) convertView.getTag();
		}
		final HashMap<String, String> map = list.get(position);
		view.name.setText(map.get("name"));
		final int temp = Integer.valueOf(map.get("id"));
		int id = Integer.valueOf(map.get("img"));
		view.img.setImageResource(id);

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = null;
				switch (temp) {
				case 0: // ipc参数设置
					intent = new Intent(context,
							SettingIpcNameActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);
					break;
				case 1: // 用户设置
					intent = new Intent(context,
							SettingIpcUserActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);

					break;
				case 2: // 时钟设置
					intent = new Intent(context,
							SettingIpcTimeActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);
					break;
				case 3: // 报警设置
					intent = new Intent(context, SettingIpcAlarmActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);
					break;
				case 4: // AP设置
					intent = new Intent(context, SettingIpcAPActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);
					break;
				case 5: // FTP设置
					intent = new Intent(context, SettingIpcFTPActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);
					break;
				case 6: // WIFI设置
					intent = new Intent(context, SettingIpcWiFiActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);
					break;
				case 7: // 图像翻转
					intent = new Intent(context, SettingOverturnActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);
					break;
				case 8: // 图像翻转
					intent = new Intent(context, SettingIpcSharpActivity.class);
					intent.putExtra("device", device);
					context.startActivity(intent);
					break;

				default:
					break;
				}
			}
		});
		return convertView;
	}

	class Myview {
		TextView name;
		ImageView img;
	}

	public void Vod() {

	}

}
