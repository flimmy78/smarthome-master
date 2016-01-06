package com.kqt.smarthome.fragment;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.ShopListAdpter;
import com.kqt.smarthome.util.Util;
import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.LayoutBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar.Gravity;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

public class ShopLayerFragment extends LazyFragment {
	private ProgressBar loading;
	private LinearLayout body;
	private ListView listView;
	private ShopListAdpter adpter;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			loading.setVisibility(View.GONE);
			body.setVisibility(View.VISIBLE);
		}
	};

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_tabshop);
		loading = (ProgressBar) findViewById(R.id.loading);
		body = (LinearLayout) findViewById(R.id.body_msg);
		listView = (ListView) findViewById(R.id.shop_listview);
		adpter = new ShopListAdpter(ShopLayerFragment.this.getActivity(),
				Util.shopGoods());
		listView.setAdapter(adpter);
		handler.sendEmptyMessageDelayed(0, 500);
	}

}
