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
import com.goockr.inductioncooker.utils.NotNull;

/**
 * Created by LJN on 2017/9/27.
 */

public class DialogView {
    private Context context;
    private AlertDialog.Builder dialong;
    private AlertDialog alertDialog;
    private volatile static DialogView singleton;

    public static DialogView getSingleton() {
        if (singleton == null) {
            synchronized (DialogView.class) {
                if (singleton == null) {
                    singleton = new DialogView();
                }
            }
        }
        return singleton;
    }

    public void setContext(Context context) {
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

    public boolean isShow() {
        if (NotNull.isNotNull(alertDialog) && alertDialog.isShowing()) {
           return true;
        }else{
            return false;
        }
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
