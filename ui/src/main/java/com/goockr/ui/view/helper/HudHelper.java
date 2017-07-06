package com.goockr.ui.view.helper;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by CMQ on 2017/6/12.
 */

public class HudHelper {

     KProgressHUD hud;

    private Timer tipTimer;

    TimerTask tipTask;

    public void hudShowTip(Context context,String tip,int delay)
    {
        final TextView tv_Reset = new TextView(context);
        tv_Reset.setTextColor(Color.WHITE);
        tv_Reset.setTextSize(16);
        tv_Reset.setText(tip);
        tv_Reset.setPadding(0,0,0,0);
        hud = KProgressHUD.create(context)
                .setCustomView(tv_Reset)
                .show();


        if (tipTimer != null) {
            tipTimer.cancel();
            tipTimer = null;
        }
        if (tipTask != null) {
            tipTask.cancel();
            tipTask = null;
        }


        tipTask = new TimerTask(){
            public void run(){
                if (hud!=null)
                {
                    hud.dismiss();
                    hud=null;
                }
            }
        };
        tipTimer = new Timer();
        tipTimer.schedule(tipTask, delay);
    }

    public  void hudHide()
    {
        if (hud!=null)
        {
            hud.dismiss();
            hud=null;
        }
    }

}
