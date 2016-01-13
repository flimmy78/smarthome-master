package com.kqt.smarthome.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.kqt.smarthome.R;

/**
 * Created by Administrator on 2016/1/13.
 */
public class BoxHomeActivity extends BaseActivity {
    private GridView home;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.box_home_activity);
        home= (GridView) findViewById(R.id.box_home_at_gridview);
    }

    @Override
    public void viewEvent(TitleBar titleBar, View v) {

    }
}
