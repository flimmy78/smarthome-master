package com.kqt.smarthome.adpter;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.kqt.smarthome.entity.AlarmMsg;
import com.kqt.smarthome.entity.BoxMainSwitch;
import com.kqt.smarthome.entity.BoxShuntSwitch;
import com.kqt.smarthome.entity.Device;

public class ShopListAdpter extends BaseAdapter {
	private Context context;
	private List<Integer> list = new ArrayList<Integer>();

	public ShopListAdpter(Context context, List<Integer> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
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
					R.layout.shop_item, null);
			viewholder.time = (TextView) convertView
					.findViewById(R.id.shop_img);

			convertView.setTag(viewholder);
		} else
			viewholder = (Viewholder) convertView.getTag();

		int str = list.get(position);
		viewholder.time.setBackgroundResource(str);
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Uri uri = Uri.parse("http://www.kinqianck.com");
				Intent intent = new Intent("android.intent.action.VIEW", uri);
				context.startActivity(intent);
			}
		});

		return convertView;

	}

	class Viewholder {
		TextView dname, time, msg;

	}
}
