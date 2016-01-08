package com.kqt.smarthome.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.cloudservice.VoidCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.kqt.smarthome.R;
import com.kqt.smarthome.activity.AddDeviceActivity;
import com.kqt.smarthome.activity.BoxMainActivity;
import com.kqt.smarthome.activity.IpcMainAcitity;
import com.kqt.smarthome.adpter.DeviceListAdpter;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.util.Config;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.CustomDialog;
import com.kqt.smarthome.view.XListView;
import com.kqt.smarthome.view.XListView.IXListViewListener;
import com.shizhefei.fragment.LazyFragment;

public class DeviceLayerFragment extends LazyFragment implements
        OnClickListener, IXListViewListener {
    private ProgressBar loading;
    private LinearLayout body;
    private ImageButton imgbtn, addDevice;
    ;
    private List<Device> list = new ArrayList<Device>();
    private XListView listview;
    private DeviceListAdpter adpter;
    private LinearLayout ipc_layout;
    private TextView timeout;
    private boolean hasdata = false;
    private Timer timer;
    private TimerTask task;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            listview.stopRefresh();
            listview.stopLoadMore();
            listview.setRefreshTime(Util.getNowTime());
            loading.setVisibility(View.GONE);
            if (msg.what == Util.TIMEOUT_WHAT) {
                timeout.setText("获取超时,请重试!");
                timeout.setVisibility(View.VISIBLE);
                timer = null;
                task = null;
            }
            if (msg.what == 0) {
                body.setVisibility(View.VISIBLE);
                timeout.setVisibility(View.GONE);
                adpter.notifyDataSetChanged();
            }

        }
    };

    protected void onResumeLazy() {
        super.onResumeLazy();
        loading.setVisibility(View.VISIBLE);
        body.setVisibility(View.GONE);
        initData();

    }

    ;

    @Override
    protected void onCreateViewLazy(Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        setContentView(R.layout.fragment_tabmain);
        loading = (ProgressBar) findViewById(R.id.loading);
        body = (LinearLayout) findViewById(R.id.body_msg);
        ipc_layout = (LinearLayout) findViewById(R.id.ipc_mannery);
        imgbtn = (ImageButton) findViewById(R.id.device_nodata_add_btn);
        addDevice = (ImageButton) findViewById(R.id.add_device_btn);
        listview = (XListView) findViewById(R.id.device_listview);
        timeout = (TextView) findViewById(R.id.timeout_text);
        listview.setPullLoadEnable(false);
        listview.setIXListViewListener(this);
        imgbtn.setOnClickListener(this);
        addDevice.setOnClickListener(this);
        ipc_layout.setOnClickListener(this);
        adpter = new DeviceListAdpter(list,
                DeviceLayerFragment.this.getActivity());
        listview.setAdapter(adpter);
        listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (arg2 == 0) {
                    return;
                }
                Device device = list.get(arg2 - 1);
                int type = device.getdType();
                if (type == Config.BOX_TYPE) {
                    Intent intent = new Intent(DeviceLayerFragment.this
                            .getActivity(), BoxMainActivity.class);
                    intent.putExtra(Config.BOX_D, device);
                    startActivity(intent);

                }
            }
        });

        listview.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) { // 长按删除
                if (arg2== 0) {
                    return true;
                }
                final int index = arg2 - 1;
                final Device device = list.get(index);
                CustomDialog.Builder customDialog = new CustomDialog.Builder(
                        DeviceLayerFragment.this.getActivity());
                customDialog.setTitle("提示");
                customDialog.setMessage("是否删除设备");
                customDialog.setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                DelectDevice(index, device);
                                dialog.dismiss();
                            }
                        });
                customDialog.setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method
                                // stub
                                dialog.dismiss();
                            }
                        });
                customDialog.create().show();

                return true;
            }
        });
        // initData();

    }

    /**
     * 设备删除
     *
     * @param type
     * @param userid
     */
    public void DelectDevice(int index, Device device) {
        boolean flg = false;

        if (device.getdType() == Config.BOX_TYPE) {


            AC.bindMgr().unbindGateway(Config.SUBDOMAIN, device.getDeviceid(),
                    new VoidCallback() {

                        @Override
                        public void success() {
                            initData();
                            Toast.makeText(
                                    DeviceLayerFragment.this.getActivity(),
                                    "删除成功", Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void error(ACException arg0) {
                            Toast.makeText(
                                    DeviceLayerFragment.this.getActivity(),
                                    "删除失败,请重试!", 0).show();
                        }
                    });
        }

    }

    public void initData() {
        hasdata = false;
        timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
                if (!hasdata)
                    handler.sendEmptyMessage(Util.TIMEOUT_WHAT);
            }
        };
        timer.schedule(task, 5000);
        list.clear();
        AC.bindMgr().listDevices(new PayloadCallback<List<ACUserDevice>>() {
            @Override
            public void success(List<ACUserDevice> arg0) {
                hasdata = true;

                for (int i = 0; i < arg0.size(); i++) {
                    ACUserDevice acUserDevice = arg0.get(i);
                    String uuid = acUserDevice.getPhysicalDeviceId();
                    String name = acUserDevice.getName();
                    long deviceid = acUserDevice.getDeviceId();
                    long submainID = acUserDevice.getSubDomainId(); // 子域id
                    long getwata = acUserDevice.getGatewayDeviceId();
                    int status = acUserDevice.getStatus();
                    long ownerid = acUserDevice.getOwner();
                    Device device = new Device(Config.BOX_TYPE, name, uuid, -1,
                            status, deviceid, getwata, submainID, ownerid);
                    list.add(device);
                }

                if (list == null && list.size() == 0) {
                    imgbtn.setVisibility(View.VISIBLE);
                } else {
                    imgbtn.setVisibility(View.GONE);
                }

                handler.sendEmptyMessage(0); // 初始化列表

            }

            @Override
            public void error(ACException arg0) {
                Log.d("zjm", arg0.getErrorCode() + "");
            }
        });

    }

    @Override
    protected void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeMessages(0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == addDevice.getId()) { // 添加设备
            Intent intent = new Intent(getActivity(), AddDeviceActivity.class);
            startActivity(intent);

        } else if (id == ipc_layout.getId()) {
            Intent intent = new Intent(getActivity(), IpcMainAcitity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onPauseLazy() {
        // TODO Auto-generated method stub
        super.onPauseLazy();

    }

    @Override
    public void onRefresh() {
        timeout.setVisibility(View.GONE);
        initData();
    }

    @Override
    public void onLoadMore() {
        // TODO Auto-generated method stub

    }

}
