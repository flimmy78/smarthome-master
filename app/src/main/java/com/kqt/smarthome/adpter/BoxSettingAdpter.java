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
import com.kqt.smarthome.activity.BoxPowerValueActivity;
import com.kqt.smarthome.activity.SettingBoxNameActivity;
import com.kqt.smarthome.activity.SettingBoxShareActivity;
import com.kqt.smarthome.activity.TimeTaskListActivity;
import com.kqt.smarthome.entity.Device;

import java.util.HashMap;
import java.util.List;

public class BoxSettingAdpter extends BaseAdapter {

	private List<HashMap<String, String>> list;
	private Context context;
	private Device device;

	public BoxSettingAdpter(Context context,
			List<HashMap<String, String>> list, Device device) {
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
				case 0: // 设备改名
					intent = new Intent(context, SettingBoxNameActivity.class);
					context.startActivity(intent);
					break;
				case 1: // 设备分享
					intent = new Intent(context, SettingBoxShareActivity.class);
					context.startActivity(intent);

					break;
				case 2: // 定时任务
					intent = new Intent(context, TimeTaskListActivity.class);
					context.startActivity(intent);
					break;
				case 3: // 报警管理
					break;
				case 4: // 情景管理
					break;
				case 5: // 电量查询
					intent = new Intent(context, BoxPowerValueActivity.class);
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
