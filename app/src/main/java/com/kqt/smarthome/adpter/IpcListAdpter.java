package com.kqt.smarthome.adpter;

import hsl.p2pipcam.nativecaller.DeviceSDK;

import java.io.UnsupportedEncodingException;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.IpcSettingsActivity;
import com.kqt.smarthome.activity.PlayDeviceActivity;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;

public class IpcListAdpter extends BaseAdapter {

	private List<IpcDevice> list;
	private Context context;
	private Handler handler;

	public IpcListAdpter(Context context, List<IpcDevice> list, Handler handler) {
		this.context = context;
		this.list = list;
		this.handler = handler;
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
					R.layout.device_listview_item, null);
			view.name = (TextView) convertView
					.findViewById(R.id.devicelist_item_ipcname);
			view.state = (TextView) convertView
					.findViewById(R.id.devicelist_item_state);
			view.ipcimg = (ImageView) convertView.findViewById(R.id.ipc_img);
			view.img = (ImageView) convertView.findViewById(R.id.setting_img);

			convertView.setTag(view);
		} else {
			view = (Myview) convertView.getTag();
		}
		final IpcDevice map = list.get(position);
		view.name.setText(map.getName());
		view.state.setText(Util.state(map.getConnect_state()));
		if ("".equals(map.getPic())) {
			view.ipcimg.setImageResource(R.drawable.index_default);
		} else {
			Bitmap bitmap = Util.decodeBitmap(map.getPic());
			if (bitmap == null) {
				view.ipcimg.setImageResource(R.drawable.index_default);

			} else
				view.ipcimg.setImageBitmap(Util.decodeBitmap(map.getPic()));
		}

		view.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (map.getConnect_state() != 100) {
					Ttoast.show(context, "设备必须在线");
					return;
				}
				Intent intent = new Intent(context, IpcSettingsActivity.class);
				intent.putExtra("device", map);
				context.startActivity(intent);

			}
		});

		return convertView;
	}

	class Myview {
		TextView name, state;
		ImageView img, ipcimg;
	}

}
