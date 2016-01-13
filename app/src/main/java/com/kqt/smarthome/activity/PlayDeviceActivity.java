package com.kqt.smarthome.activity;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.listenner.GraphicListener;
import com.kqt.smarthome.listenner.PlayListener;
import com.kqt.smarthome.service.BridgeService;
import com.kqt.smarthome.util.AudioPlayer;
import com.kqt.smarthome.util.CustomAudioRecorder;
import com.kqt.smarthome.util.CustomAudioRecorder.AudioRecordResult;
import com.kqt.smarthome.util.CustomBuffer;
import com.kqt.smarthome.util.CustomBufferData;
import com.kqt.smarthome.util.CustomBufferHead;
import com.kqt.smarthome.util.MyRender;
import com.kqt.smarthome.util.MyRender.RenderListener;
import com.kqt.smarthome.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import hsl.p2pipcam.nativecaller.DeviceSDK;

public class PlayDeviceActivity extends BaseActivity implements PlayListener,
		RenderListener, AudioRecordResult, OnClickListener, GraphicListener {

	private long userid;
	private CustomAudioRecorder customAudioRecorder;
	private CustomBuffer AudioBuffer;
	private AudioPlayer audioPlayer;
	private MyRender myRender;
	private LinearLayout progressLayout, down_lt;
	private GLSurfaceView glSurfaceView1;
	private FrameLayout suff_lt;
	private ImageView full_img;
	private boolean flg = false, audio_isdio = false;
	private LinearLayout capture, audio, definition_layout, infread, luminance,
			contrast;
	private SoundPool sound;
	private int music, resolution = 0;
	private long start, end;
	private TextView audioing;
	private PopupWindow resolutionWindow, infreadpopupWindow, luminancepop;
	private int luminanceP, contrastP; // 亮度和对比度的值
	private LoadTask task;
	private boolean isfrst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.playdevice);
		setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", 0, "");
		setTitle("实时视频");
		BridgeService.setPlayListener(this);
		BridgeService.setGraphicListener(this);
		userid = getIntent().getLongExtra("userid", 0);
		customAudioRecorder = new CustomAudioRecorder(this);
		AudioBuffer = new CustomBuffer();
		audioPlayer = new AudioPlayer(AudioBuffer);
		DeviceSDK.getDeviceParam(userid, 0x2025); // 获取图象那个参数
		initView();
		task = new LoadTask();
		task.execute();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		System.out.println("change");

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏
		// 检测屏幕的方向：纵向或横向
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			// 当前为横屏， 在此处添加额外的处理代码
			down_lt.setVisibility(View.GONE);
			PlayDeviceActivity.this.titleShow(true);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT);
			suff_lt.setLayoutParams(lp);

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
			// 当前为竖屏， 在此处添加额外的处理代码
			down_lt.setVisibility(View.VISIBLE);
			PlayDeviceActivity.this.titleShow(false);
			DisplayMetrics dm = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(dm);
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(this,
							250));

			suff_lt.setLayoutParams(lp);
		}
		super.onConfigurationChanged(newConfig);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closePop();
		task = null;
		isfrst = true;

	}

	private void initView() {
		glSurfaceView1 = (GLSurfaceView) findViewById(R.id.glsurfaceview1);
		progressLayout = (LinearLayout) findViewById(R.id.progressLayout1);
		capture = (LinearLayout) findViewById(R.id.capture_img);
		audio = (LinearLayout) findViewById(R.id.audio_layout);
		definition_layout = (LinearLayout) findViewById(R.id.definition_layout);
		infread = (LinearLayout) findViewById(R.id.infrared);
		luminance = (LinearLayout) findViewById(R.id.luminance);
		contrast = (LinearLayout) findViewById(R.id.contrast);
		full_img = (ImageView) findViewById(R.id.full_img);
		down_lt = (LinearLayout) findViewById(R.id.down_lt);
		suff_lt = (FrameLayout) findViewById(R.id.suface_framlt);
		audioing = (TextView) findViewById(R.id.audioing_text);
		myRender = new MyRender(glSurfaceView1);
		myRender.setListener(this);
		glSurfaceView1.setRenderer(myRender);
		audioing.setVisibility(View.GONE);
		sound = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5); // 设置一个声音点
		music = sound.load(this, R.raw.photoshutter, 1); // 加载raw文件内的声音
		resolutionWindow();
		initinfreadpop();

		glSurfaceView1.setOnClickListener(this);

		full_img.setOnClickListener(this);
		capture.setOnClickListener(this);
		audio.setOnClickListener(this);
		infread.setOnClickListener(this);
		luminance.setOnClickListener(this);
		contrast.setOnClickListener(this);
		definition_layout.setOnClickListener(this);
	}

	private Handler frushHandler = new Handler() {

		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			progressLayout.setVisibility(View.GONE);
		}
	};

	public void cameraGetParamsResult(long userid, String cameraParams) {
		// TODO Auto-generated method stub

	}

	public void callBackAudioData(long userID, byte[] pcm, int size) {
		if (userID == userid) {
			CustomBufferHead head = new CustomBufferHead();
			CustomBufferData data = new CustomBufferData();
			head.length = size;
			head.startcode = 0xff00ff;
			data.head = head;
			data.data = pcm;
			if (audioPlayer.isAudioPlaying())
				AudioBuffer.addData(data);
		}

	}

	public void callBackVideoData(long userID, byte[] data, int type, int size) {

	}

	public void smartAlarmCodeGetParamsResult(long userid, String params) {
		Log.d("zjm", params);

	}

	public void smartAlarmNotify(long userid, String message) {
		// TODO Auto-generated method stub

	}

	private class LoadTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			DeviceSDK.setRender(userid, myRender);
			DeviceSDK.startPlayStream(userid, 10, 1);

			try {
				JSONObject obj = new JSONObject();
				obj.put("param", 13);
				obj.put("value", 1024);
				DeviceSDK.setDeviceParam(userid, 0x2026, obj.toString());
				JSONObject obj1 = new JSONObject();
				obj1.put("param", 6);
				obj1.put("value", 15);
				DeviceSDK.setDeviceParam(userid, 0x2026, obj1.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

	}

	public void initComplete(int size, int width, int height) {
		// TODO Auto-generated method stub
		frushHandler.sendEmptyMessage(0);
	}

	public void takePicture(byte[] imageBuffer, int width, int height) {
		// TODO Auto-generated method stub

	}

	public void AudioRecordData(byte[] data, int len) {
		// TODO Auto-generated method stub
		DeviceSDK.SendTalkData(userid, data, len);
	}

	@Override
	public void viewEvent(TitleBar titleBar, View v) {
		// TODO Auto-generated method stub
		finish();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == full_img.getId()) {
			if (!flg) {
				flg = true;

				if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
				}
			} else {
				flg = false;
				if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
				}
			}
		} else if (capture.getId() == id) {
			sound.play(music, 1, 1, 0, 0, 1);
			IpcDevice.capturePicture(this, userid);

			showToast("抓拍成功");
		} else if (id == R.id.audio_layout) { // 录像
			if (!audio_isdio) {
				System.out.println("..................");
				int temp = IpcDevice.StartRecord(this, userid, 480, 800, 20);
				System.out.println(temp);
				if (temp > 0) {
					System.out.println("..................");
					showToast("开始录像");
					audioing.setVisibility(View.VISIBLE);
					audio.setBackgroundResource(R.color.gray_2);
					start = System.currentTimeMillis();
					audio_isdio = true;
				}
			} else {
				end = System.currentTimeMillis();
				long time = end - start;
				if (time > 10 * 1000) {
					int isrecord = IpcDevice.StopRecord(userid);
					if (isrecord > 0) {
						audio_isdio = false;
						showToast("录像已停止");
						audioing.setVisibility(View.VISIBLE);
						audio.setBackgroundResource(R.color.gray);
						audioing.setVisibility(View.GONE);

					}
				} else {
					showToast("录像时间低于10秒!");

				}
			}

		} else if (id == definition_layout.getId()) {
			closePop();
			this.resolutionWindow.showAtLocation(this.glSurfaceView1, 17, 0, 0);
		} else if (id == infread.getId()) {
			closePop();
			this.infreadpopupWindow.showAtLocation(this.glSurfaceView1, 17, 0,
					0);
		} else if (id == contrast.getId()) {
			closePop();
			initluminancepop(0);
			this.luminancepop.showAtLocation(this.glSurfaceView1, 17, 0, 0);
		} else if (id == luminance.getId()) {
			initluminancepop(1);
			closePop();
			this.luminancepop.showAtLocation(this.glSurfaceView1, 17, 0, 0);
		}
	}

	public void initinfreadpop() {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.infread_pop, null);

		LinearLayout localButton1 = (LinearLayout) layout
				.findViewById(R.id.infread_on);
		LinearLayout localButton2 = (LinearLayout) layout
				.findViewById(R.id.infread_off);
		localButton1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) { // 开
				closePop();
				JSONObject param = new JSONObject();
				try {
					param.put("param", 14);
					param.put("value", 1);
					long temp = DeviceSDK.setDeviceParam(userid, 0x2026,
							param.toString());
					if (temp > 0) {
						showToast("已开启");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		localButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) { // 红外功能关
				closePop();
				JSONObject param = new JSONObject();
				try {
					param.put("param", 14);
					param.put("value", 0);
					long temp = DeviceSDK.setDeviceParam(userid, 0x2026,
							param.toString());
					if (temp > 0) {
						showToast("已关闭");
					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		infreadpopupWindow = new PopupWindow(layout, 200,
				LayoutParams.WRAP_CONTENT);
		this.infreadpopupWindow.setFocusable(true);
		this.infreadpopupWindow.setOutsideTouchable(true);
		this.infreadpopupWindow.setBackgroundDrawable(new ColorDrawable(0));

	}

	@SuppressWarnings("unused")
	private void initluminancepop(final int id) {
		LinearLayout layout = (LinearLayout) LayoutInflater.from(this).inflate(
				R.layout.luminance_pop, null);
		final SeekBar seek = (SeekBar) layout.findViewById(R.id.luminance_seek);
		final TextView tv = (TextView) layout.findViewById(R.id.luminance_tv);
		seek.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				if (id == 1) { // 亮度
					try {
						seek.setProgress(seekBar.getProgress());
						PlayDeviceActivity.this.luminanceP = seekBar
								.getProgress();
						tv.setText(PlayDeviceActivity.this.luminanceP + "/255");
						JSONObject jsonObject = new JSONObject();
						jsonObject.put("param", 1);
						jsonObject.put("value", seekBar.getProgress());
						DeviceSDK.setDeviceParam(userid, 0x2026,
								jsonObject.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				} else { // 对比度
					try {
						JSONObject object = new JSONObject();
						PlayDeviceActivity.this.contrastP = seekBar
								.getProgress();
						seek.setProgress(PlayDeviceActivity.this.contrastP);
						tv.setText(seekBar.getProgress() + "/255");
						object.put("param", 2);
						object.put("value", seekBar.getProgress());
						DeviceSDK.setDeviceParam(userid, 0x2026,
								object.toString());
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});
		if (id == 1) {
			seek.setProgress(this.luminanceP);
			tv.setText(this.luminanceP + "/255");
		} else {
			seek.setProgress(this.contrastP);
			tv.setText(this.contrastP + "/255");

		}
		int i = getWindow().getWindowManager().getDefaultDisplay().getWidth();
		luminancepop = new PopupWindow(layout, i * 2 / 3,
				LayoutParams.WRAP_CONTENT);
		luminancepop.setFocusable(true);
		luminancepop.setOutsideTouchable(true);
		this.luminancepop.setBackgroundDrawable(new ColorDrawable(0));
	}

	public void resolutionWindow() {
		// if (this.isStartRecording)
		// return;
		LinearLayout localLinearLayout = (LinearLayout) LayoutInflater.from(
				this).inflate(R.layout.resolutionpop, null);
		TextView localButton1 = (TextView) localLinearLayout
				.findViewById(R.id.pop_high);
		TextView localButton2 = (TextView) localLinearLayout
				.findViewById(R.id.standard_pop);
		TextView localButton3 = (TextView) localLinearLayout
				.findViewById(R.id.pu_pop);

		this.resolutionWindow = new PopupWindow(localLinearLayout, 200,
				LayoutParams.WRAP_CONTENT);
		// this.resolutionWindow.setAnimationStyle(R.style.CustomDialog);
		this.resolutionWindow.setFocusable(true);
		this.resolutionWindow.setOutsideTouchable(true);
		this.resolutionWindow.setBackgroundDrawable(new ColorDrawable(0));
		localButton1.setOnClickListener(new OnClickListener() {
			public void onClick(View paramAnonymousView) {
				closePop();
				if (PlayDeviceActivity.this.resolution != 0) {
					PlayDeviceActivity.this.resolution = 0;
					IpcDevice.stopPlayStream(userid);
					IpcDevice.startPlayStream(userid, resolution);
				}
			}
		});
		localButton2.setOnClickListener(new OnClickListener() {
			public void onClick(View paramAnonymousView) {
				closePop();
				if (PlayDeviceActivity.this.resolution != 1) {
					PlayDeviceActivity.this.resolution = 1;
					IpcDevice.stopPlayStream(userid);
					IpcDevice.startPlayStream(userid, resolution);
				}
			}
		});
		localButton3.setOnClickListener(new OnClickListener() {
			public void onClick(View paramAnonymousView) {
				closePop();
				if (PlayDeviceActivity.this.resolution != 2) {
					PlayDeviceActivity.this.resolution = 2;
					IpcDevice.stopPlayStream(userid);
					IpcDevice.startPlayStream(userid, resolution);
				}
			}
		});
	}

	private void closePop() {
		if (resolutionWindow != null && resolutionWindow.isShowing()) {
			resolutionWindow.dismiss();
		}
		if (infreadpopupWindow != null && infreadpopupWindow.isShowing()) {
			infreadpopupWindow.dismiss();
		}
		if (luminancepop != null && luminancepop.isShowing()) {
			luminancepop.dismiss();
		}
	}

	@Override
	public void callBack_getParam(long UserID, long nType, String param) {
		// 图像参数返回值
		Log.d("zjm", "图像参数");
		if (nType == 0x2025) {
			try {
				JSONObject jsonObject = new JSONObject(param);
				this.luminanceP = jsonObject.getInt("vbright");
				this.contrastP = jsonObject.getInt("vcontrast");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void callBack_setParam(long UserID, long nType, int nResult) {
		// 图像参数设置回馈
	}

}
