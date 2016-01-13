package com.kqt.smarthome.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.zxing.WriterException;
import com.kqt.smarthome.R;
import com.kqt.smarthome.entity.Device;
import com.kqt.smarthome.entity.IpcDevice;
import com.zxing.encoding.EncodingHandler;

/**
 * 分享
 *
 * @author Administrator
 */
public class SettingIpcSharpActivity extends BaseActivity {
    private Device device;
    private ImageView box_sharp_code_img;
    private LinearLayout error, cont;
    private Bitmap bitmap;
    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                cont.setVisibility(View.VISIBLE);
                error.setVisibility(View.GONE);
                if (bitmap != null)
                    box_sharp_code_img.setImageBitmap(bitmap);
            }

        }

    };

    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(0);
        handler.removeMessages(1);
    }

    ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_sharp_activity);
        device = BoxSettingActivity.device;
        setNaView(R.drawable.left_back_selector, "", 0, "", 0, "", R.drawable.right_finsh_selector, "");
        setTitle("设备分享");
        box_sharp_code_img = (ImageView) findViewById(R.id.box_sharp_code_img);
        error = (LinearLayout) findViewById(R.id.error_layout);
        cont = (LinearLayout) findViewById(R.id.content_layout);
        creatBitmap();
    }

    public void creatBitmap() {
        IpcDevice device = (IpcDevice) getIntent().getSerializableExtra("device");
        try {
            bitmap = EncodingHandler.createQRCode(device.getDeviceid(), 250);

            handler.sendEmptyMessage(0);
        } catch (WriterException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @Override
    public void viewEvent(TitleBar titleBar, View v) {
        // TODO Auto-generated method stub
        if (titleBar == TitleBar.LIEFT) {
            finish();
        }
    }
}
