package com.kqt.smarthome.adpter;

import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.BoxMainActivity;
import com.kqt.smarthome.entity.BoxMainSwitch;
import com.kqt.smarthome.entity.BoxShuntSwitch;
import com.kqt.smarthome.entity.Device;

public class BoxMainAdpter extends BaseAdapter {
	private Context context;
	private List<BoxMainSwitch> list;

	public BoxMainAdpter(List<BoxMainSwitch> list, Context context) {
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
		final Viewholder viewholder;
		if (convertView == null) {
			viewholder = new Viewholder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.box_main_switch_item, null);
			viewholder.img = (ImageView) convertView
					.findViewById(R.id.box_main_switch_img);
			viewholder.dname = (TextView) convertView
					.findViewById(R.id.box_main_switch_name);

			convertView.setTag(viewholder);
		} else
			viewholder = (Viewholder) convertView.getTag();

		final BoxMainSwitch manager = list.get(position);

		viewholder.dname.setText(manager.getName());
		viewholder.img
				.setImageResource(manager.isStatus() ? R.drawable.checkbox_on
						: R.drawable.checkbox_off);
		viewholder.img.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				viewholder.dname.setText("11");
			}
		});
		return convertView;

	}

	class Viewholder {
		ImageView img;
		TextView dname;

	}
}
