package com.kqt.smarthome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.accloud.service.ACTimerTask;
import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.TimeTaskListAdpter;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.XListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/1/4.
 */
public class TimeTaskListActivity extends BaseActivity implements XListView.IXListViewListener {
    private List<ACTimerTask> timerTasks=new ArrayList<>();
    private TimeTaskListAdpter adpter;
    private XListView listview;
    private int RESOULT = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listview.stopRefresh();
            listview.stopLoadMore();
            listview.setRefreshTime(Util.getNowTime());
            adpter.notifyDataSetChanged();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetask_list);
        setTitle(getString(R.string.time_task_list_title));
        setNaView(R.drawable.left_back, "", 0, "", 0, "", 0, "添加");
        listview = (XListView) findViewById(R.id.time_task_list_listview);
        adpter = new TimeTaskListAdpter(timerTasks, this);
        listview.setPullLoadEnable(false);
        listview.setIXListViewListener(this);
        listview.setAdapter(adpter);

        queryTimeTaskList();
    }

    /**
     * 查询定时任务列表
     */
    private void queryTimeTaskList() {
        timerTasks.clear();
        ACTimerTask acTimerTask = new ACTimerTask(1, "每天", 2500, "momo", "YY", "2012", "2012", "2012-01-02", "20120102", "20130305", "20612012", 1);
        timerTasks.add(acTimerTask);
//
//        AC.timerMgr().listTasks(BoxSettingActivity.device.getDeviceid(), new PayloadCallback<List<ACTimerTask>>() {
//            @Override
//            public void success(List<ACTimerTask> timerTasks) {
//                //通过logcat查看获取到的定时任务列表进行显示或下一步操作
//                TimeTaskListActivity.this.timerTasks = timerTasks;
//                handler.sendEmptyMessage(0);
//            }
//
//            @Override
//            public void error(ACException e) {
//                //参数无误下一般为网络错误
//                Ttoast.show(TimeTaskListActivity.this, "网路错误");
//            }
//        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            queryTimeTaskList();

        }

    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            Intent Intent = new Intent(this, TimeTaskDeviceActivity.class);
            startActivityForResult(Intent, RESOULT);

        } else
            back();
    }

    @Override
    public void onRefresh() {
        queryTimeTaskList();
    }

    @Override
    public void onLoadMore() {

    }
}
