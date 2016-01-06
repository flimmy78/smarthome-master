package com.kqt.smarthome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.kqt.smarthome.R;

public class TimeTaskSettingMsgActivity extends BaseActivity implements
        OnClickListener {
    private Handler hanlder = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            timeTaskMsgOnLayout.setVisibility(on_B ? View.VISIBLE : View.GONE);
            timeTaskMsgOffLayout.setVisibility(off_B ? View.VISIBLE : View.GONE);
        }
    };
    private FrameLayout timeTaskMsgOn;
    private LinearLayout timeTaskMsgOnLayout;
    private FrameLayout timeTaskMsgOff;
    private LinearLayout timeTaskMsgOffLayout;
    private boolean on_B = true, off_B = false;
    private String command = "";

    private void assignViews() {
        timeTaskMsgOn = (FrameLayout) findViewById(R.id.time_task_msg_on);
        timeTaskMsgOnLayout = (LinearLayout) findViewById(R.id.time_task_msg_on_layout);
        timeTaskMsgOff = (FrameLayout) findViewById(R.id.time_task_msg_off);
        timeTaskMsgOffLayout = (LinearLayout) findViewById(R.id.time_task_msg_off_layout);

        timeTaskMsgOn.setOnClickListener(this);
        timeTaskMsgOff.setOnClickListener(this);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetask_setting_msg);
        setTitle("操作");
        setNaView(R.drawable.left_back, "", 0, "", 0, "", R.drawable.right_finsh_selector, "");
        assignViews();
        hanlder.sendEmptyMessage(0);
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            if (on_B) {
                command = "开";
            } else if (off_B) {
                command = "关";
            }
            Intent intent = new Intent();
            intent.putExtra("command", command);
            setResult(Activity.RESULT_OK, intent);
            back();
        } else {
            back();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == timeTaskMsgOn.getId()) {
            if (on_B) {
                on_B = true;
                off_B = false;
            } else {
                on_B = true;
                off_B = false;
                hanlder.sendEmptyMessage(0);
            }
        } else if (v.getId() == timeTaskMsgOff.getId()) {
            if (off_B) {
                on_B = false;
                off_B = true;
            } else {
                on_B = false;
                off_B = true;
                hanlder.sendEmptyMessage(0);
            }
        }
    }


}
