package com.kqt.smarthome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.util.Ttoast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TimeTaskSettingTypeActivity extends BaseActivity implements
        OnClickListener {
    private ListView listView;
    private boolean[] itemchose;
    private FrameLayout lb, fl;
    private LinearLayout lc, fc;
    private boolean lb_sel = true, fl_sel = false;
    private CustomAdpter adpter;
    private List<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
    private Handler hanlder = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            lc.setVisibility(lb_sel ? View.VISIBLE : View.GONE);
            fc.setVisibility(fl_sel ? View.VISIBLE : View.GONE);
            if (msg.what == 1) {
                adpter.notifyDataSetChanged();
            } else if (msg.what == 2) {
                queryLeakPower();
            } else if (msg.what == 3) {
                queryChannelPower();

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetask_setting_type);
        setTitle("设备选择");
        setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", R.drawable.right_finsh_selector, "");
        lb = (FrameLayout) findViewById(R.id.timetask_lb_layout);
        fl = (FrameLayout) findViewById(R.id.timetask_fl_layout);
        lc = (LinearLayout) findViewById(R.id.timetask_lb_ischose);
        fc = (LinearLayout) findViewById(R.id.timetask_fl_ischose);
        listView = (ListView) findViewById(R.id.timetask_bottom_listview);
        adpter = new CustomAdpter();
        listView.setAdapter(adpter);
        lb.setOnClickListener(this);
        fl.setOnClickListener(this);
        hanlder.sendEmptyMessage(0);
        queryChannelPower();
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            String linname = "";
            boolean check = true;

            for (int i = 0; i < itemchose.length; i++) {
                if (itemchose[i]) {
                    linname = (String) list.get(i).get("name");
                    check = false;
                }
            }

            Intent intent = new Intent();
            if (lb_sel) {
                intent.putExtra("infomation", "漏电保护器-" + linname);
            } else
                intent.putExtra("infomation", "智能分路器-" + linname);
            if (check) {
                Ttoast.show(this, "请选择线路");
                return;
            }
            setResult(Activity.RESULT_OK, intent);
            back();
        } else {
            setResult(Activity.RESULT_CANCELED);
            back();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == lb.getId()) {
            if (lb_sel == true) {
                lb_sel = true;
                fl_sel = false;
            } else {
                lb_sel = true;
                fl_sel = false;
                hanlder.sendEmptyMessage(2);
            }
        } else if (v.getId() == fl.getId()) {

            if (fl_sel == true) {
                lb_sel = false;
                fl_sel = true;
            } else {
                lb_sel = false;
                fl_sel = true;

                hanlder.sendEmptyMessage(3);
            }
        }
    }

    class CustomAdpter extends BaseAdapter {

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
            // TODO Auto-generated method stub
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView = LayoutInflater.from(TimeTaskSettingTypeActivity.this)
                    .inflate(R.layout.timetask_type_item, null);
            TextView view = (TextView) convertView.findViewById(R.id.timetask_item_text);
            LinearLayout imglayout = (LinearLayout) convertView.findViewById(R.id.timetask_item_ischose);
            HashMap<String, Object> map = list.get(position);
            String linename = (String) map.get("name");
            view.setText(linename);
            imglayout.setVisibility(itemchose[position] ? View.VISIBLE : View.GONE);
            view.setText(linename);

            convertView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemchose[position] = true;
                    for (int i = 0; i < itemchose.length; i++) {
                        if (i == position) {
                            itemchose[i] = true;
                        } else
                            itemchose[i] = false;
                    }
                    hanlder.sendEmptyMessage(1);
                }
            });
            return convertView;
        }

    }

    /**
     * query leakchannel infomation
     */

    private void queryLeakPower() {
        list.clear();
        itemchose = new boolean[5];
        for (int i = 0; i < 6; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", "线路" + i);
            itemchose[i] = false;
            list.add(map);
        }
        hanlder.sendEmptyMessage(1);
//************************************下面为远端获取的线路列表*****************************//
//        BoxManager.getintence().queryLeakProtectStateInfo(BoxSettingActivity.device.getDeviceid(), 0,
//                new PayloadCallback<ACMsg>() {
//                    @Override
//                    public void error(ACException arg0) {
//                        Log.d("error", arg0.getErrorCode() + "");
//                    }
//
//                    @Override
//                    public void success(ACMsg arg0) {
//                        ACObject acObject = arg0.get("stateInfo");
//                        long deviceId = acObject.getLong("deviceId");
//                        List<ACObject> list_ac = acObject.getList("value");
//                        itemchose = new boolean[list_ac.size()];
//                        for (int i = 0; i < list_ac.size(); i++) {
//                            HashMap<String, Object> map = new HashMap<String, Object>();
//                            ACObject li = list_ac.get(i);
//                            String lineName = li.getString("lineName");
//                            map.put("name", lineName);
//                            itemchose[i] = false;
//                            list.add(map);
//                        }
//                        hanlder.sendEmptyMessage(1);
//
//                    }
//                });

    }


    /**
     * query channel infomation
     */
    private void queryChannelPower() {
        list.clear();
        itemchose = new boolean[5];
        for (int i = 0; i < 5; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", "线路" + i);
            itemchose[i] = false;
            list.add(map);
        }
        hanlder.sendEmptyMessage(1);


//
//        BoxManager.getintence().queryChannelDivideStateInfo(BoxSettingActivity.device.getDeviceid(), 0,
//                new PayloadCallback<ACMsg>() {
//                    @Override
//                    public void error(ACException arg0) {
//                        Log.d("error", arg0.getErrorCode() + "");
//                    }
//
//                    @Override
//                    public void success(ACMsg arg0) {
//                        ACObject acObject = arg0.get("stateInfo");
//                        long deviceId = acObject.getLong("deviceId");
//                        List<ACObject> list_ac = acObject.getList("value");
//                        itemchose = new boolean[list_ac.size()];
//                        for (int i = 0; i < list_ac.size(); i++) {
//                            HashMap<String, Object> map = new HashMap<String, Object>();
//                            ACObject li = list_ac.get(i);
//                            String lineName = li.getString("lineName");
//                            map.put("name", lineName);
//                            itemchose[i] = false;
//                            list.add(map);
//                        }
//                        hanlder.sendEmptyMessage(1);
//                    }
//                });
    }


}
