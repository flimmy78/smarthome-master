package com.kqt.smarthome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACDeviceMsg;
import com.accloud.service.ACException;
import com.accloud.service.ACTimerTask;
import com.kqt.smarthome.R;
import com.kqt.smarthome.entity.BoxMainSwitch;
import com.kqt.smarthome.entity.BoxShuntSwitch;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.DateTimePickDialogUtil;

import java.text.SimpleDateFormat;
import java.util.Date;


public class TimeTaskDeviceActivity extends BaseActivity implements
        OnClickListener {
    private FrameLayout timetask_layout, type, aciton;
    private TextView way, type_t, action_t;
    public int TYPECODE = 20;
    private int typeId = 0;
    private int CYCLE = 30;
    private int MSGCODE = 40;
    private TextView text_view;
    private LinearLayout time_porit;
    private Bundle bundle;
    private EditText time_task_description;
    private String info_msg, cycle_msg, command_msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_task_acitivity);
        setTitle("定时任务");
        setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", R.drawable.right_finsh_selector, "");
        timetask_layout = (FrameLayout) findViewById(R.id.timetask_layout);
        type = (FrameLayout) findViewById(R.id.time_type_layout);
        aciton = (FrameLayout) findViewById(R.id.time_action_layout);
        way = (TextView) findViewById(R.id.time_way);
        type_t = (TextView) findViewById(R.id.timetask_type_info);
        action_t = (TextView) findViewById(R.id.time_action_text);
        text_view = (TextView) findViewById(R.id.time_porit);
        time_porit = (LinearLayout) findViewById(R.id.time_porit_layout);
        time_task_description = (EditText) findViewById(R.id.time_task_description);

        timetask_layout.setOnClickListener(this);
        type.setOnClickListener(this);
        aciton.setOnClickListener(this);
        time_porit.setOnClickListener(this);
        getIntentData();
    }

    /**
     * recive data
     */
    public void getIntentData() {
        bundle = getIntent().getBundleExtra("bundle");
        if (bundle != null) {
            handler.sendEmptyMessage(7);
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                type_t.setText(info_msg);
            } else if (msg.what == 3) {
                way.setText(cycle_msg);

            } else if (msg.what == 5) {
                action_t.setText(command_msg);

            } else if (msg.what == 7) {
                String timePoint = bundle.getString("timePoint");
                String timeCycle = Getstr(bundle.getString("timeCycle"));
                String description = bundle.getString("description");
                text_view.setText(timePoint);
                way.setText(timeCycle);
                time_task_description.setText(description);
            }


        }
    };

    public String Getstr(String str) {
        String cycle = "";
        if (str.equals("once")) {
            cycle = "仅此一次";
        } else if (str.equals("hours")) {
            cycle = "每小时";
        } else if (str.equals("day")) {
            cycle = "每天";
        } else if (str.equals("mouth")) {
            cycle = "每月";
        } else if (str.equals("year")) {
            cycle = "每年";
        } else {
            cycle = "周计划";
        }
        return cycle;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        handler.removeMessages(3);
        handler.removeMessages(5);
        handler.removeMessages(7);
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {


            String name = Util.GetUser(this).getName();
            String timePoint = text_view.getText().toString().trim();
            String timeCycle = getcycle(way.getText().toString().trim());
            String action = action_t.getText().toString().trim();
            String description = time_task_description.getText().toString().trim();

            if (timeCycle.isEmpty() || timeCycle.equals("从不")) {
                Ttoast.show(this, "循环方式未指定");
                return;
            } else if (timePoint.isEmpty() || timePoint.length() < 5) {
                Ttoast.show(this, "任务时间未设定");
                return;
            } else if (action.isEmpty() || action.equals("无")) {
                Ttoast.show(this, "未设定定时操作");
                return;
            }
            ACDeviceMsg msg = new ACDeviceMsg();
            msg.setCode(100);
            if (typeId == Config.leakageProtection) {  //漏保
                BoxMainSwitch boxMainSwitch = new BoxMainSwitch(this);
                msg.setContent(boxMainSwitch);
            } else if (typeId == Config.splitter) {    //分路
                BoxShuntSwitch boxShuntSwitch = new BoxShuntSwitch(this);
                msg.setContent(boxShuntSwitch);
            }
            String point = timePoint + ":00";
            if (bundle != null) {   //修改任务
                modifyTask(name, timePoint, timeCycle, description, msg);
            } else {
                createTimeTask(name, timePoint, timeCycle, description, msg);
            }
        } else {

            back();
        }
    }

    public String getcycle(String str) {
        String cycle = "";
        if (str.equals("仅此一次")) {
            cycle = "once";
        } else if (str.equals("每小时")) {
            cycle = "hours";
        } else if (str.equals("每天")) {
            cycle = "day";
        } else if (str.equals("每月")) {
            cycle = "mouth";
        } else if (str.equals("每年")) {
            cycle = "year";
        } else {
            cycle = str;

        }
        return cycle;
    }

    /**
     * 修改任务
     */
    public void modifyTask(String name, String timePoint, String timeCycle, String description, ACDeviceMsg msg) {
        long deviceId = BoxSettingActivity.device.getDeviceid();
        long taskId = 0;
        AC.timerMgr().modifyTask(taskId, deviceId, name, timePoint, timeCycle, description, msg, new VoidCallback() {
            @Override
            public void success() {
                Ttoast.show(TimeTaskDeviceActivity.this, "修改成功");
            }

            @Override
            public void error(ACException e) {
                Ttoast.show(TimeTaskDeviceActivity.this, "修改失败");
            }
        });
    }

    /**
     * 创建定时任务
     */
    private void createTimeTask(String name, String timePoint, String timeCycle, String description, ACDeviceMsg msg) {
        long deviceId = BoxSettingActivity.device.getDeviceid();
        //注:这里与文档有些许不同,没有描述
        AC.timerMgr().addTask(ACTimerTask.OP_TYPE.CLOUD, deviceId, name, timePoint, timeCycle, description, msg, new PayloadCallback<ACTimerTask>() {
            @Override
            public void success(ACTimerTask task) {
                //成功添加定时任务，创建后默认为开启状态
                Ttoast.show(TimeTaskDeviceActivity.this, "添加成功");
                setResult(Activity.RESULT_OK);
                finish();
            }

            @Override
            public void error(ACException e) {
                //网络错误或其他，根据e.getErrorCode()做不同的提示或处理，此处一般为参数类型错误，请仔细阅读注意事项
                Log.d("dubug", e.getErrorCode() + "+" + e.getMessage());
                Ttoast.show(TimeTaskDeviceActivity.this, "添加失败");
                finish();
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TYPECODE && resultCode == Activity.RESULT_OK) {
            info_msg = data.getStringExtra("infomation");
            handler.sendEmptyMessage(1);
        } else if (requestCode == CYCLE && resultCode == Activity.RESULT_OK) {
            cycle_msg = data.getStringExtra("timeCycle");
            handler.sendEmptyMessage(3);
        } else if (requestCode == MSGCODE && resultCode == Activity.RESULT_OK) {
            command_msg = data.getStringExtra("command");
            handler.sendEmptyMessage(5);


        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == type.getId()) {
            Intent intent = new Intent();
            intent.setClass(this, TimeTaskSettingTypeActivity.class);
            startActivityForResult(intent, TYPECODE);
        } else if (v.getId() == timetask_layout.getId()) {
            Intent intent = new Intent();
            intent.setClass(this, TimeTaskSettingCycleActivity.class);
            startActivityForResult(intent, CYCLE);
        } else if (v.getId() == aciton.getId()) {
            Intent intent = new Intent();
            intent.setClass(this, TimeTaskSettingMsgActivity.class);
            startActivityForResult(intent, MSGCODE);
        } else if (v.getId() == time_porit.getId()) {
            DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(
                    this, getdate());
            dateTimePickDialogUtil.dateTimePicKDialog(text_view);

        }
    }

    public String getdate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(System.currentTimeMillis());
        String str = dateFormat.format(date);
        System.out.println(str);
        return str;
    }
}
