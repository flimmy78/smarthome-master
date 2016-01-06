package com.kqt.smarthome.view;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.kqt.smarthome.R;

public class TimeTaskDialog extends Dialog {
    private LinearLayout update, delect, close;

    public void setUpdateListener(View.OnClickListener listener) {
        update.setOnClickListener(listener);
    }

    public void setDelectListener(View.OnClickListener listener) {
        delect.setOnClickListener(listener);
    }

    public void setCloseListener(View.OnClickListener listener) {
        close.setOnClickListener(listener);
    }


    public TimeTaskDialog(Context context) {
        super(context, R.style.CustomDialog);
        View view = LayoutInflater.from(context).inflate(R.layout.time_task_dialog,
                null);
        view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));
        update = (LinearLayout) view.findViewById(R.id.update_task_item);
        delect = (LinearLayout) view.findViewById(R.id.delect_task_item);
        close = (LinearLayout) view.findViewById(R.id.close_task_item);
        this.setCanceledOnTouchOutside(true);
        setContentView(view);

    }
}
