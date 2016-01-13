package com.kqt.smarthome.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.accloud.cloudservice.AC;
import com.accloud.cloudservice.PayloadCallback;
import com.accloud.service.ACException;
import com.accloud.service.ACUserDevice;
import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.DeviceTypeAdpter;
import com.kqt.smarthome.decoding.CaptureActivity;
import com.kqt.smarthome.entity.TypeDevice;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AddDeviceActivity extends BaseActivity {

    private ListView listview;
    private DeviceTypeAdpter adpter;
    private List<TypeDevice> list;
    public static List<Activity> listAC = new ArrayList<Activity>();
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            if (msg.what == 0) {
                String shareCode = (String) msg.obj;
                BindSharp(shareCode);
            } else if (msg.what == 1) {
                AddDeviceActivity.this.showToast("绑定失败");
                finish();
            } else if (msg.what == 2) {
                AddDeviceActivity.this.showToast("绑定成功");
            }
            AddDeviceActivity.this.hideProgressDialog();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        listAC.add(this);
        setTitle("设备类型");
        setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", R.drawable.right_search_selector, "");
        initdata();
        listview = (ListView) findViewById(R.id.device_type_listview);
        adpter = new DeviceTypeAdpter(this, list);
        listview.setAdapter(adpter);
        listview.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                TypeDevice device = list.get(position);
                switch (device.getdType()) {
                    case 1: // 电箱
                        Intent intent = new Intent(AddDeviceActivity.this,
                                BoxDeviceAddActivity.class);
                        startActivity(intent);
                        break;
                    case 3: // ipc camer
                        Intent intent1 = new Intent(AddDeviceActivity.this,
                                IpcAddDeviceActivity.class);
                        startActivity(intent1);
                        break;
                    default:
                        break;
                }
            }

        });
    }

    public void BindSharp(String shareCode) {

        AC.bindMgr().bindDeviceWithShareCode(shareCode,
                new PayloadCallback<ACUserDevice>() {

                    @Override
                    public void error(ACException arg0) {
                        // TODO Auto-generated method stub
                        Log.d("zjm", "绑定失败");
                        Ttoast.show(AddDeviceActivity.this,
                                Util.GetEorrMsg(arg0.getErrorCode()));
                    }

                    @Override
                    public void success(ACUserDevice arg0) {
                        // TODO Auto-generated method stub
                        Log.d("zjm", "绑定成功");
                        handler.sendEmptyMessage(2);
                    }

                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void initdata() {
        TypeDevice device = new TypeDevice(2, "智能灯泡");
        TypeDevice device1 = new TypeDevice(1, "智能电箱");
        list = new ArrayList<TypeDevice>();
        list.add(device);
        list.add(device1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            String da = data.getStringExtra(CaptureActivity.CODE_RESULT);
            Log.d("zjm", da);
            Message message = handler.obtainMessage();
            message.what = 0;
            message.obj = da;
            handler.sendMessage(message);

        }
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.LIEFT) {
            finish();
        } else if (titleBar == TitleBar.RIGHT) {
            Intent openCameraIntent = new Intent(AddDeviceActivity.this,
                    CaptureActivity.class);
            startActivityForResult(openCameraIntent, 0); // 扫描
        }
    }

}
