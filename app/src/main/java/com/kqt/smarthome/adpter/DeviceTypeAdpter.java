package com.kqt.smarthome.adpter;

import java.util.ArrayList;
import java.util.List;

import com.kqt.smarthome.R;
import com.kqt.smarthome.entity.TypeDevice;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceTypeAdpter extends BaseAdapter {
	private Context context;
	private List<TypeDevice> list;

	public DeviceTypeAdpter(Context context, List<TypeDevice> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (list == null) {
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
		Viewholder viewholder = null;
		if (convertView == null) {
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.device_type_item, null);
			viewholder.img = (ImageView) convertView
					.findViewById(R.id.type_device_img);
			viewholder.dname = (TextView) convertView
					.findViewById(R.id.type_device_name);

			convertView.setTag(viewholder);
		} else
			viewholder = (Viewholder) convertView.getTag();

		TypeDevice manager = list.get(position);
		switch (manager.getdType()) {
		case 1:
			viewholder.img.setImageResource(R.drawable.electric_box);
			break;
		case 2:
			viewholder.img.setImageResource(R.drawable.light);
			break;
		case 3:
			viewholder.img.setImageResource(R.drawable.ipc_camer);

			break;

		default:
			break;
		}

		viewholder.dname.setText(manager.getdName());

		return convertView;

	}

	class Viewholder {
		ImageView img;
		TextView dname;

	}
}
