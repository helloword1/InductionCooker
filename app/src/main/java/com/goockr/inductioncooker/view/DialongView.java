package com.goockr.inductioncooker.view;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.goockr.inductioncooker.R;

/**
 * Created by LJN on 2017/9/27.
 */

public class DialongView {
    private Context context;
    private AlertDialog.Builder dialong;
    private AlertDialog alertDialog;

    public DialongView(Context context) {
        this.context = context;
        dialong = new AlertDialog.Builder(context, R.style.AlertDialog);
    }

    public View showDialong(int layout, float Width, float Height) {
        final View inflateDialong = LayoutInflater.from(context)
                .inflate(layout, null);
        alertDialog = dialong.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setContentView(inflateDialong);
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
        float dWidth = Width;
        float DHeight = Height;
        lp.width = (int) dWidth;// 定义宽度
        lp.height = (int) DHeight;// 定义高度
        alertDialog.getWindow().setAttributes(lp);
        return inflateDialong;
    }

    public View showCustomDialong(int layout) {
        final View inflateDialong = LayoutInflater.from(context)
                .inflate(layout, null);
        alertDialog = dialong.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setContentView(inflateDialong);
        WindowManager.LayoutParams lp = alertDialog.getWindow().getAttributes();
//        float dWidth = Width;
//        float DHeight = Height;
//        lp.width = (int) dWidth;// 定义宽度
//        lp.height = (int) DHeight;// 定义高度
//        alertDialog.getWindow().setAttributes(lp);
        return inflateDialong;
    }

    public void dismissDialong() {
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }

    public AlertDialog getAlertDialog() {
        if (alertDialog != null) {
            return alertDialog;
        }
        return null;
    }
}
