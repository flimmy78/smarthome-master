package com.kqt.smarthome.fragment;

import org.w3c.dom.Text;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.accloud.cloudservice.AC;
import com.accloud.service.ACUserInfo;
import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.AboutAppActivity;
import com.kqt.smarthome.activity.LoginActivity;
import com.kqt.smarthome.activity.UserInfomationActivity;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.CustomDialog;
import com.kqt.smarthome.view.LoadingDialog;
import com.shizhefei.fragment.LazyFragment;
import com.shizhefei.view.indicator.Indicator;
import com.shizhefei.view.indicator.IndicatorViewPager;
import com.shizhefei.view.indicator.IndicatorViewPager.IndicatorFragmentPagerAdapter;
import com.shizhefei.view.indicator.slidebar.ColorBar;
import com.shizhefei.view.indicator.slidebar.LayoutBar;
import com.shizhefei.view.indicator.slidebar.ScrollBar.Gravity;
import com.shizhefei.view.indicator.transition.OnTransitionTextListener;

public class UserLayerFragment extends LazyFragment implements OnClickListener {
	private ProgressBar loading;
	private LinearLayout body;
	private TextView username;
	private Button logout;
	private ACUserInfo acUserInfo;
	private LinearLayout info, check, about;
	private LoadingDialog dialog;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
				loading.setVisibility(View.GONE);
				body.setVisibility(View.VISIBLE);
				if (acUserInfo.getName() != null) {
					username.setText(acUserInfo.getName());
				} else {
					username.setText("未知");

				}
			} else if (msg.what == 1) {
				Ttoast.show(UserLayerFragment.this.getActivity(), "已是最新版本");
				dialog.dismiss();
			}
		}
	};

	protected void onResumeLazy() {
		super.onResumeLazy();
		sendUI();
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeMessages(0);
	}

	@Override
	protected void onCreateViewLazy(Bundle savedInstanceState) {
		super.onCreateViewLazy(savedInstanceState);
		setContentView(R.layout.fragment_tabuser);
		loading = (ProgressBar) findViewById(R.id.loading);
		body = (LinearLayout) findViewById(R.id.body_msg);
		logout = (Button) findViewById(R.id.logout_btn);
		username = (TextView) findViewById(R.id.user_name);
		info = (LinearLayout) findViewById(R.id.amend_info_layout);
		check = (LinearLayout) findViewById(R.id.check_app);
		about = (LinearLayout) findViewById(R.id.about_app);
		dialog = new LoadingDialog(UserLayerFragment.this.getActivity());
		info.setOnClickListener(this);
		about.setOnClickListener(this);
		check.setOnClickListener(this);
		logout.setOnClickListener(this);

	}

	public void sendUI() {
		acUserInfo = Util.GetUser(UserLayerFragment.this.getActivity());
		handler.sendEmptyMessageDelayed(0, 500);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == logout.getId()) {
			CustomDialog.Builder customDialog = new CustomDialog.Builder(
					UserLayerFragment.this.getActivity());
			customDialog.setTitle("提示");
			customDialog.setMessage("是否退出登录");
			customDialog.setPositiveButton("确定",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							AC.accountMgr().logout(); // 注销
							UserLayerFragment.this.getActivity().finish();
							Intent intent = new Intent(UserLayerFragment.this
									.getActivity(), LoginActivity.class);
							startActivity(intent);
						}
					});
			customDialog.setNegativeButton("取消",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method
							// stub
							dialog.dismiss();
						}
					});
			customDialog.create().show();

		} else if (id == info.getId()) {
			Intent intent = new Intent(UserLayerFragment.this.getActivity(),
					UserInfomationActivity.class);
			startActivity(intent);

		} else if (id == check.getId()) {
			dialog.setTitle("检查中..");
			dialog.show();
			handler.sendEmptyMessage(1);
		} else if (id == about.getId()) {
			Intent intent = new Intent(UserLayerFragment.this.getActivity(),
					AboutAppActivity.class);
			startActivity(intent);
		}
	}
}
