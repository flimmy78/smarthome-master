package com.kqt.smarthome.adpter;

import java.util.ArrayList;
import java.util.List;

import android.R.drawable;
import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.BoxMainActivity;
import com.kqt.smarthome.activity.Box_main_switchActivity;
import com.kqt.smarthome.activity.Box_shunt_switchActivity;
import com.kqt.smarthome.entity.BoxMainSwitch;
import com.kqt.smarthome.entity.BoxMode;
import com.kqt.smarthome.entity.BoxShuntSwitch;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.util.Config;

public class BoxModeAdpter extends BaseAdapter {
	private Context context;
	private List<BoxMode> list = new ArrayList<BoxMode>();

	public BoxModeAdpter(List<BoxMode> list, Context context) {
		this.list = list;
		this.context = context;

		// ****测试
		this.list.add(new BoxMode(5, Config.leakageProtection, 1));
		this.list.add(new BoxMode(5, Config.splitter, 2));

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
	public View getView(int position, View convertView, ViewGroup parent) {
		final Viewholder viewholder;
		if (convertView == null) {
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.box_mode_item, null);
			viewholder.img = (ImageView) convertView
					.findViewById(R.id.zongkai_img);
			viewholder.dname = (TextView) convertView
					.findViewById(R.id.mode_name);

			convertView.setTag(viewholder);
		} else
			viewholder = (Viewholder) convertView.getTag();

		final BoxMode mode = list.get(position);
		final int manager = mode.getMark();
		final long count = mode.getCount();
		final long deviceid = mode.getDeviceId();
		viewholder.dname.setText(GetStr(manager));
		viewholder.img.setImageDrawable(GetDrawable(manager));

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (manager == Config.leakageProtection) {
					Intent intent = new Intent(context,
							Box_main_switchActivity.class);
					intent.putExtra("leakageProtection", count); // 漏保数量
					intent.putExtra("deviceId", deviceid); // 分路数量
					context.startActivity(intent);

				} else if (manager == Config.splitter) {
					Intent intent = new Intent(context,
							Box_shunt_switchActivity.class);
					intent.putExtra("splitter", count); // 分路数量
					intent.putExtra("deviceId", deviceid); // 分路数量
					context.startActivity(intent);

				}
			}
		});

		return convertView;

	}

	private String GetStr(int key) {
		String str = "";
		if (key == Config.leakageProtection) {
			str = context.getString(R.string.leakageProtection);
		} else if (key == Config.splitter) {
			str = context.getString(R.string.splitter);
		}
		return str;
	}

	private Drawable GetDrawable(int key) {
		Drawable str = null;
		if (key == Config.leakageProtection) {
			str = context.getResources().getDrawable(R.drawable.zongkai_mode);
		} else if (key == Config.splitter) {
			str = context.getResources().getDrawable(R.drawable.peidian_mode);
		}
		return str;
	}

	class Viewholder {
		ImageView img;
		TextView dname;

	}
}
