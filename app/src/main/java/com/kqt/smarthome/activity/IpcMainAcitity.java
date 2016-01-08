package com.kqt.smarthome.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kqt.smarthome.R;
import com.kqt.smarthome.adpter.IpcListAdpter;
import com.kqt.smarthome.db.DeviceManager;
import com.kqt.smarthome.entity.IpcDevice;
import com.kqt.smarthome.listenner.DeviceStatusListener;
import com.kqt.smarthome.listenner.PictureListener;
import com.kqt.smarthome.service.BridgeService;
import com.kqt.smarthome.util.Ttoast;
import com.kqt.smarthome.util.Util;
import com.kqt.smarthome.view.CustomDialog;

import java.io.UnsupportedEncodingException;
import java.util.List;

import hsl.p2pipcam.nativecaller.DeviceSDK;

public class IpcMainAcitity extends BaseActivity implements
        DeviceStatusListener, PictureListener {
    private ListView listView;
    private List<IpcDevice> ipcList;
    private IpcListAdpter adpter;
    private TextView textView;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (ipcList == null || ipcList.size() == 0) {
                textView.setVisibility(View.VISIBLE);
            }else
                textView.setVisibility(View.GONE);
            if (msg.what == 0) {
                adpter.notifyDataSetChanged();
            } else if (msg.what == 2) {
                initData();
            }
        }
    };

    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(1);
        handler.removeMessages(2);
        handler.removeMessages(0);
        handler.removeMessages(5);
        if (ipcList != null) {
            for (IpcDevice dvice : ipcList) {
                DeviceSDK.closeDevice(dvice.getUserid());
            }
        }
    }

    ;

    // protected void onResume() {
    // System.out.println(".0.00.0.0.");
    // super.onResume();
    // handler.sendEmptyMessage(2);
    // };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ipc_main);
        BridgeService.setDeviceStatusListener(this);
        BridgeService.setPictureListener(this);
        setNaView(R.drawable.left_back, "", 0, "", 0, "",
                R.drawable.right_add_select, "");
        setTitle("摄像头列表");
        listView = (ListView) findViewById(R.id.device_ipc_listview);
        textView = (TextView) findViewById(R.id.ipc_main_text);
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                final int index = arg2;

                CustomDialog.Builder customDialog = new CustomDialog.Builder(
                        IpcMainAcitity.this);
                customDialog.setTitle("提示");
                customDialog.setMessage("是否删除设备");
                customDialog.setPositiveButton("是",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                delect(index, ipcList.get(index));
                                dialog.dismiss();
                                initData();
                                handler.sendEmptyMessage(0);
                            }
                        });
                customDialog.setNegativeButton("否",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // TODO Auto-generated method stub
                                dialog.dismiss();
                            }
                        });

                customDialog.create().show();
                return true;
            }
        });

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                if (ipcList.get(arg2).getConnect_state() != 100) {
                    Ttoast.show(IpcMainAcitity.this, "设备必须在线!");
                    return;
                }
                Intent intent = new Intent(IpcMainAcitity.this,
                        PlayDeviceActivity.class);
                intent.putExtra("userid", ipcList.get(arg2).getUserid());
                startActivity(intent);
            }
        });
        initData();
        ReopenDevice();
        handler.sendEmptyMessage(5);
        // myTask task = new myTask();
        // task.execute(null);

    }

    public void delect(int index, IpcDevice device) {
        DeviceSDK.closeDevice(device.getUserid());
        DeviceSDK.destoryDevice(device.getUserid());
        int id = DeviceManager.getInstence(IpcMainAcitity.this).delectDevice(
                device.getDeviceid());
        DeviceManager.getInstence(this).DelectMsg(device.getDeviceid());
        if (id > 0) {
            Toast.makeText(IpcMainAcitity.this, "删除成功", Toast.LENGTH_SHORT)
                    .show();
        } else {
            Toast.makeText(IpcMainAcitity.this, "删除失败", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    public void initData() {
        ipcList = DeviceManager.getInstence(this).GetIPCListDevice();
        System.out.println(ipcList.size() + " ipc数量");
        adpter = new IpcListAdpter(this, ipcList, handler);
        listView.setAdapter(adpter);
    }

    @SuppressLint("NewApi")
    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        if (titleBar == TitleBar.LIEFT) {
            back();
        } else { // 完成
            Intent intent = new Intent(IpcMainAcitity.this,
                    IpcAddDeviceActivity.class);
            startActivityForResult(intent, 0);
        }
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        for (int i = 0; i < ipcList.size(); i++) {
            IpcDevice device = ipcList.get(i);
            DeviceManager.getInstence(this).updateIPC_pic(device);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        initData();

    }

    @Override
    public void CallBack_RecordPicture(long userid, byte[] buff, int len) {
        // TODO Auto-generated method stub
        String imgstr = "";
        try {
            imgstr = new String(buff, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (ipcList != null) {
            for (IpcDevice l : ipcList) {
                if (l.getUserid() == userid) {
                    l.setPic(imgstr);
                }
            }
        }
        handler.sendEmptyMessage(0);
    }

    @Override
    public void receiveDeviceStatus(long userid, int status) {
        Log.d("zjm", "初始化摄像头信息--" + "userid-" + userid + "status-" + status);
        // TODO Auto-generated method stub
        if (ipcList != null) {
            for (IpcDevice id : ipcList) {
                long uid = id.getUserid();
                if (uid == userid) {
                    id.setConnect_state(status);
                    handler.sendEmptyMessage(0);
                }
            }

        }
    }

    // class myTask extends AsyncTask<Void, Void, Void> {
    //
    // @Override
    // protected Void doInBackground(Void... params) {
    // // TODO Auto-generated method stub
    // if (ipcList != null) {
    // for (IpcDevice dvice : ipcList) {
    // DeviceSDK.closeDevice(dvice.getUserid());
    // DeviceSDK.openDevice(dvice.getUserid());
    // }
    // }
    // return null;
    // }
    //
    // }


    /**
     * 初始化摄像头
     */
    public void initDevice() {

        for (IpcDevice dvice : ipcList) {
            DeviceSDK.closeDevice(dvice.getUserid());
            DeviceSDK.openDevice(dvice.getUserid());
        }

    }


    /*
     * 重启摄像头
     */
    public void ReopenDevice() {
        if (Util.isipcF) {

            for (IpcDevice dvice : ipcList) {
                long userid = DeviceSDK.createDevice("admin", "", "", 0,
                        dvice.getDeviceid(), 1);
                int temp = DeviceSDK.openDevice(userid);
                dvice.setUserid(userid);
            }
            Util.isipcF = false;
        } else {
            initDevice();
        }
    }


}
