package com.kqt.smarthome.adpter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACTimerTask;
import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.BoxSettingActivity;
import com.kqt.smarthome.activity.TimeTaskDeviceActivity;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.view.TimeTaskDialog;

import java.util.List;

public class TimeTaskListAdpter extends BaseAdapter {
    private Context context;
    private List<ACTimerTask> list;

    public TimeTaskListAdpter(List<ACTimerTask> list, Context context) {
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
        Viewholder viewholder;
        if (convertView == null) {
            viewholder = new Viewholder();
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.time_task_list_item, null);
            viewholder.time_task_adminname = (TextView) convertView
                    .findViewById(R.id.time_task_adminname);
            viewholder.time_task_do = (TextView) convertView
                    .findViewById(R.id.time_task_do);
            viewholder.time_task_mode_time = (TextView) convertView
                    .findViewById(R.id.time_task_mode_time);
            viewholder.time_task_dotime = (TextView) convertView
                    .findViewById(R.id.time_task_dotime);
            viewholder.time_task_period = (TextView) convertView
                    .findViewById(R.id.time_task_period);
            viewholder.time_task_content = (TextView) convertView
                    .findViewById(R.id.time_task_content);
            viewholder.edit = (LinearLayout) convertView.findViewById(R.id.edit_img);
            convertView.setTag(viewholder);

        } else
            viewholder = (Viewholder) convertView.getTag();

        final ACTimerTask acTimerTask = list.get(position);
        String adminname = acTimerTask.getNickName();
        String dotime = acTimerTask.getTimePoint();
        String perod = acTimerTask.getTimeCycle();
        String creatime = acTimerTask.getCreateTime();
        String content = acTimerTask.getDescription();
        int stuts = acTimerTask.getStatus();

        viewholder.time_task_adminname.setText(adminname);
        viewholder.time_task_dotime.setText(dotime);
        viewholder.time_task_period.setText(perod);
        viewholder.time_task_content.setText(content);
        viewholder.time_task_do.setText(stuts > 1 ? "执行中" : "停止");

        viewholder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimeTaskDialog dialog = new TimeTaskDialog(context);
                setListenner(dialog, acTimerTask);
                dialog.show();

            }
        });

        return convertView;

    }

    public void setListenner(final TimeTaskDialog dialog, final ACTimerTask acTimerTask) {
        final long deviceId = BoxSettingActivity.device.getDeviceid();
        dialog.setUpdateListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(context, TimeTaskDeviceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("timePoint", acTimerTask.getTimePoint());
                bundle.putString("timeCycle", acTimerTask.getTimeCycle());
                bundle.putString("description", acTimerTask.getDescription());
                bundle.putLong("taskId", acTimerTask.getTaskId());

                intent.putExtra("bundle", bundle);
                context.startActivity(intent);
            }
        });
        dialog.setDelectListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AC.timerMgr().deleteTask(deviceId, acTimerTask.getTaskId(), new VoidCallback() {
                    @Override
                    public void success() {
                        Ttoast.show(context, "删除成功");
                    }

                    @Override
                    public void error(ACException e) {
                        Ttoast.show(context, "删除失败");
                    }
                });
            }
        });
        dialog.setCloseListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AC.timerMgr().closeTask(deviceId, acTimerTask.getTaskId(), new VoidCallback() {
                    @Override
                    public void success() {
                        Ttoast.show(context, "关闭成功");
                    }

                    @Override
                    public void error(ACException e) {
                        Ttoast.show(context, "关闭失败");
                    }
                });
            }
        });


    }

    class Viewholder {
        TextView time_task_adminname, time_task_do, time_task_mode_time, time_task_dotime, time_task_period, time_task_content;
        LinearLayout edit;
    }


}
