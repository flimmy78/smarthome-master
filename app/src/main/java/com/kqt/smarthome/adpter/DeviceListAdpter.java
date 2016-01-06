package com.kqt.smarthome.adpter;

import java.util.List;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.BoxMainActivity;
import com.kqt.smarthome.activity.BoxSettingActivity;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.util.Util;

public class DeviceListAdpter extends BaseAdapter {
	private Context context;
	private List<Device> list;

	public DeviceListAdpter(List<Device> list, Context context) {
		this.list = list;
		this.context = context;
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
		Viewholder viewholder;
		if (convertView == null) {
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.device_list_item, null);
			viewholder.img = (ImageView) convertView
					.findViewById(R.id.list_item_dimg);
			viewholder.setimg = (LinearLayout) convertView
					.findViewById(R.id.device_seting_img);
			viewholder.dname = (TextView) convertView
					.findViewById(R.id.list_item_dname);
			viewholder.dmode = (TextView) convertView
					.findViewById(R.id.list_item_dmode);
			viewholder.online = (TextView) convertView
					.findViewById(R.id.devie_item_isonline);

			convertView.setTag(viewholder);
		} else
			viewholder = (Viewholder) convertView.getTag();

		final Device manager = list.get(position);

		viewholder.dname.setText(manager.getdName());
		viewholder.dmode.setText(manager.getUuid());
		if (manager.getdType() == Config.IPC_TYPE) {
			viewholder.online.setText(Util.state(manager.getStatus()));
		} else if (manager.getdType() == Config.BOX_TYPE) {
			viewholder.online.setText(manager.getStatus() > 1 ? "不在线" : "在线");
		}

		getType(manager.getdType(), viewholder.img);

		viewholder.setimg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, BoxSettingActivity.class);
				intent.putExtra(Config.BOX_D, manager);
				context.startActivity(intent);
			}
		});

		return convertView;

	}

	public void getType(long key, ImageView imageView) {
		int id = (int) key;
		switch (id) {
		case 1: // 电箱
			imageView.setImageResource(R.drawable.electric_box);
			break;
		case 2: // ipc camer
			imageView.setImageResource(R.drawable.ipc_camer);
			break;

		default:
			break;
		}
	}

	class Viewholder {
		ImageView img;
		TextView dname, dmode, online;
		LinearLayout setimg;
	}
}
