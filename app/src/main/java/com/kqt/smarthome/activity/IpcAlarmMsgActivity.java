package com.kqt.smarthome.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.AlarmMsgAdpter;
import com.kqt.smarthome.db.DeviceManager;
import com.kqt.smarthome.entity.AlarmMsg;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.util.AlarmSortComparator;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.CustomDialog;
import com.kqt.smarthome.view.LoadingDialog;
import com.kqt.smarthome.view.XListView;
import com.kqt.smarthome.view.XListView.IXListViewListener;

import java.util.Collections;
import java.util.List;

/**
 * 消息中摄像头列表
 *
 * @author Administrator
 */
public class IpcAlarmMsgActivity extends BaseActivity implements
        IXListViewListener {
    private XListView listView;
    private List<AlarmMsg> list = null;
    private AlarmMsgAdpter adpter;
    private IpcDevice ipcDevice;
    private CustomDialog.Builder dialog;
    private LoadingDialog loadingDialog;
    private boolean gl_msg = true;
    private PopupWindow menupop;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                initView();
            } else if (msg.what == 2) {
                listView.stopRefresh();
                listView.stopLoadMore();
                listView.setRefreshTime(Util.getNowTime());
                initView();
            } else if (msg.what == 3) {
                if (gl_msg) {
                    adpter.setIsvisb(true);
                    setNaView(R.drawable.left_back_selector, "", 0, "", 0, "取消", 0, "");
                    gl_msg = false;
                    menupop.showAtLocation(listView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
                } else {
                    adpter.setIsvisb(false);
                    gl_msg = true;
                    setNaView(R.drawable.left_back_selector, "", 0, "", 0, "管理", 0, "");
                    menupop.dismiss();
                }
                adpter.notifyDataSetChanged();

            }

        }
    };

    public void menuPop() {

        View view = LayoutInflater.from(this).inflate(R.layout.msg_menu_pop, null);

        LinearLayout delect_lt = (LinearLayout) view.findViewById(R.id.delect_layout);
        LinearLayout all = (LinearLayout) view.findViewById(R.id.all_layout);


        final TextView delect = (TextView) view.findViewById(R.id.delect);
        TextView select_all = (TextView) view.findViewById(R.id.all_select);
        menupop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        delect_lt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.create().show();
            }
        });
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AlarmMsg msg : list) {
                    msg.setIscheck(true);
                }
                handler.sendEmptyMessage(0);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ipc_alarm_msg);
        setTitle("摄像头消息");
        setNaView(R.drawable.left_back_selector, "", 0, "", 0, "管理", 0, "");
        menuPop();
        initView();
    }

    private void initView() {
        ipcDevice = (IpcDevice) getIntent().getSerializableExtra("device");
        list = DeviceManager.getInstence(this).QueryMsg(ipcDevice.getDeviceid());

        if (list != null) {
            AlarmSortComparator comparator = new AlarmSortComparator();
            Collections.sort(list, comparator);
        }
        listView = (XListView) findViewById(R.id.alarm_listivew);
        adpter = new AlarmMsgAdpter(list, this);
        listView.setAdapter(adpter);
        listView.setPullLoadEnable(false);
        listView.setIXListViewListener(this);
        dialog = new CustomDialog.Builder(this);
        dialog.setTitle("提示");
        dialog.setMessage("确定删除所选中项目?");
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Loading();
            }
        });
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
    }

    public void Loading() {
        loadingDialog = new LoadingDialog(this);
        loadingDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (AlarmMsg msg : list) {
                    if (msg.ischeck()) {
                        DeviceManager.getInstence(IpcAlarmMsgActivity.this).DelectMsg_msgid(msg.getId());

                    }
                }
                loadingDialog.dismiss();
                handler.sendEmptyMessage(1);
            }
        }).start();

    }

    @Override
    public void onRefresh() {
        handler.sendEmptyMessageDelayed(2, 2000);
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.RIGHT) {
            handler.sendEmptyMessage(3);
        } else if (titleBar == TitleBar.LIEFT)
            back();
    }

}
