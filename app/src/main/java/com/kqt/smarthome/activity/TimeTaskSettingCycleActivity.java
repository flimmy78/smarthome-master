package com.kqt.smarthome.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kqt.smarthome.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TimeTaskSettingCycleActivity extends BaseActivity implements
        OnClickListener {

    private boolean[] ischose = new boolean[]{true, false, false, false, false, false};
    private List<HashMap<String, Object>> list = new ArrayList<>();
    private TextView week_T;
    private Dialog dialog;
    private String week_str = "";  //周计划字符串
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            timeTaskCycleOnceLt.setVisibility(ischose[0] ? View.VISIBLE : View.GONE);
            timeTaskCycleHoursLt.setVisibility(ischose[1] ? View.VISIBLE : View.GONE);
            timeTaskCycleDayLt.setVisibility(ischose[2] ? View.VISIBLE : View.GONE);
            timeTaskCycleMouthLt.setVisibility(ischose[3] ? View.VISIBLE : View.GONE);
            timeTaskCycleYearLt.setVisibility(ischose[4] ? View.VISIBLE : View.GONE);
            timeTaskCycleWeekLt.setVisibility(ischose[5] ? View.VISIBLE : View.GONE);
            if (msg.what == 2) {
                String st = (String) msg.obj;
                week_T.setText(st);

            }
        }
    };
    private FrameLayout timeTaskCycleOnce;
    private LinearLayout timeTaskCycleOnceLt;
    private FrameLayout timeTaskCycleHours;
    private LinearLayout timeTaskCycleHoursLt;
    private FrameLayout timeTaskCycleDay;
    private LinearLayout timeTaskCycleDayLt;
    private FrameLayout timeTaskCycleMouth;
    private LinearLayout timeTaskCycleMouthLt;
    private FrameLayout timeTaskCycleYear;
    private LinearLayout timeTaskCycleYearLt;
    private FrameLayout timeTaskCycleWeek;
    private LinearLayout timeTaskCycleWeekLt;

    private void assignViews() {
        timeTaskCycleOnce = (FrameLayout) findViewById(R.id.time_task_cycle_once);
        timeTaskCycleOnceLt = (LinearLayout) findViewById(R.id.time_task_cycle_once_lt);
        timeTaskCycleHours = (FrameLayout) findViewById(R.id.time_task_cycle_hours);
        timeTaskCycleHoursLt = (LinearLayout) findViewById(R.id.time_task_cycle_hours_lt);
        timeTaskCycleDay = (FrameLayout) findViewById(R.id.time_task_cycle_day);
        timeTaskCycleDayLt = (LinearLayout) findViewById(R.id.time_task_cycle_day_lt);
        timeTaskCycleMouth = (FrameLayout) findViewById(R.id.time_task_cycle_mouth);
        timeTaskCycleMouthLt = (LinearLayout) findViewById(R.id.time_task_cycle_mouth_lt);
        timeTaskCycleYear = (FrameLayout) findViewById(R.id.time_task_cycle_year);
        timeTaskCycleYearLt = (LinearLayout) findViewById(R.id.time_task_cycle_year_lt);
        timeTaskCycleWeek = (FrameLayout) findViewById(R.id.time_task_cycle_week);
        timeTaskCycleWeekLt = (LinearLayout) findViewById(R.id.time_task_cycle_week_lt);
        week_T = (TextView) findViewById(R.id.week_text);
        timeTaskCycleOnce.setOnClickListener(this);
        timeTaskCycleHours.setOnClickListener(this);
        timeTaskCycleDay.setOnClickListener(this);
        timeTaskCycleMouth.setOnClickListener(this);
        timeTaskCycleYear.setOnClickListener(this);
        timeTaskCycleWeek.setOnClickListener(this);
        alertInit();
    }

    private void initData() {
        for (int i = 0; i < 8; i++) {
            HashMap<String, Object> map = new HashMap<>();
            switch (i) {
                case 0:
                    map.put("week", "周一");
                    break;
                case 1:
                    map.put("week", "周二");
                    break;
                case 2:
                    map.put("week", "周三");
                    break;
                case 3:
                    map.put("week", "周二");
                    break;
                case 4:
                    map.put("week", "周四");
                    break;
                case 5:
                    map.put("week", "周五");
                    break;
                case 6:
                    map.put("week", "周六");
                    break;
                case 7:
                    map.put("week", "周天");
                    break;

            }
            map.put("check", false);
            list.add(map);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.time_task_setting_cycle);
        setTitle("定时任务");
        setNaView(R.drawable.left_back, "", 0, "", 0, "", R.drawable.right_finsh_selector, "");
        assignViews();
        initData();
        handler.sendEmptyMessage(0);
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            for (int i = 0; i < ischose.length; i++) {
                if (ischose[i]) {
                    String type = "";
                    switch (i) {
                        case 0:
                            type = "仅此一次";
                            break;
                        case 1:
                            type = "每小时";
                            break;
                        case 2:
                            type = "每天";
                            break;
                        case 3:
                            type = "每月";
                            break;
                        case 4:
                            type = "每年";
                            break;
                        case 5:
                            type = getWeekstr(week_str);
                            break;
                    }
                    Intent intent = new Intent();
                    intent.putExtra("timeCycle", type);
                    setResult(Activity.RESULT_OK, intent);
                    back();
                }

            }


        } else {
            back();
        }
    }

    private String getWeekstr(String str) {
        String spt = str.substring(0, str.length() - 1);
        String week = "week[" + spt + "]";
        return week;

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == timeTaskCycleOnce.getId()) {
            for (int i = 0; i < ischose.length; i++) {
                if (i == 0) {
                    ischose[i] = true;
                } else
                    ischose[i] = false;
            }
            handler.sendEmptyMessage(0);
        } else if (v.getId() == timeTaskCycleHours.getId()) {
            for (int i = 0; i < ischose.length; i++) {
                if (i == 1) {
                    ischose[i] = true;
                } else
                    ischose[i] = false;
            }
            handler.sendEmptyMessage(0);
        } else if (v.getId() == timeTaskCycleDay.getId()) {
            for (int i = 0; i < ischose.length; i++) {
                if (i == 2) {
                    ischose[i] = true;
                } else
                    ischose[i] = false;
            }
            handler.sendEmptyMessage(0);
        } else if (v.getId() == timeTaskCycleMouth.getId()) {
            for (int i = 0; i < ischose.length; i++) {
                if (i == 3) {
                    ischose[i] = true;
                } else
                    ischose[i] = false;
            }
            handler.sendEmptyMessage(0);
        } else if (v.getId() == timeTaskCycleYear.getId()) {
            for (int i = 0; i < ischose.length; i++) {
                if (i == 4) {
                    ischose[i] = true;
                } else
                    ischose[i] = false;
            }
            handler.sendEmptyMessage(0);
        } else if (v.getId() == timeTaskCycleWeek.getId()) {
            week_str = "";
            dialog.show();

        }

    }

    public void alertInit() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        ListView listView = new ListView(this);
        WeekAdpter adpter = new WeekAdpter();
        listView.setAdapter(adpter);
        builder.setView(listView);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String wkday = "";
                for (int i = 0; i < ischose.length; i++) {
                    if (i == 5) {
                        ischose[i] = true;
                    } else
                        ischose[i] = false;
                }

                for (int i = 0; i < list.size(); i++) {
                    HashMap<String, Object> map = list.get(i);
                    String wk = (String) map.get("week") + ",";
                    boolean ck = (boolean) map.get("check");
                    if (ck) {
                        week_str += i + ",";
                        wkday += wk;
                    }
                }
                Message msg = handler.obtainMessage();
                msg.obj = wkday;
                msg.what = 2;
                handler.sendMessage(msg);


            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();

    }


    class WeekAdpter extends BaseAdapter {

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
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(TimeTaskSettingCycleActivity.this).inflate(R.layout.week_item, null);
            TextView text = (TextView) convertView.findViewById(R.id.item_week_text);
            CheckBox box = (CheckBox) convertView.findViewById(R.id.item_week_checkbox);
            final HashMap<String, Object> map = list.get(position);
            String st = (String) map.get("week");
            final boolean check = (boolean) map.get("check");
            text.setText(st);
            box.setChecked(check);

            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        map.put("check", true);
                        list.set(position, map);
                    } else {
                        map.put("check", false);
                        list.set(position, map);
                    }
                }
            });

            return convertView;
        }
    }
}
