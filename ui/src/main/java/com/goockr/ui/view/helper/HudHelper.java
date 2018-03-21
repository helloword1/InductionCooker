package com.goockr.ui.view.helper;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.widget.TextView;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Created by CMQ on 2017/6/12.
 */

public class HudHelper {

    KProgressHUD hud;
    private ScheduledExecutorService service;

    public void hudShowTip(Context context, String tip, int delay) {
        service = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r);
            }
        });
        final TextView tv_Reset = new TextView(context);
        tv_Reset.setTextColor(Color.WHITE);
        tv_Reset.setTextSize(13);
        tv_Reset.setText(tip);
        tv_Reset.setPadding(0, 0, 0, 0);
        hud = KProgressHUD.create(context)
                .setCustomView(tv_Reset)
                .show();
        service.schedule(new Runnable() {
            @Override
            public void run() {
                if (hud != null) {
                    hud.dismiss();
                    hud = null;
                }
            }
        }, delay, TimeUnit.MILLISECONDS);

    }


    public void hudShow(final Context context, String tip) {
        if (hud != null) {
            return;
        }
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(tip)
                .setCancellable(false);

        hud.show();
        service = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r);
            }
        });
        service.schedule(new Runnable() {
            @Override
            public void run() {
                if (hud != null) {
                    hud.dismiss();
                }
            }
        }, 5, TimeUnit.SECONDS);
    }

    public void hudShowChange(final Context context, String tip) {
        if (hud != null) {
            return;
        }
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(tip)
                .setCancellable(false);

        hud.show();
        service = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r);
            }
        });
        service.schedule(new Runnable() {
            @Override
            public void run() {
                if (hud != null) {
                    hud.dismiss();
                }
            }
        }, 3, TimeUnit.SECONDS);
    }

    public void hudShowNoText(final Context context) {
        if (hud != null) {
            return;
        }
        hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.ANNULAR_DETERMINATE)
                .setCancellable(false);
        hud.show();
        service = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable r) {
                return new Thread(r);
            }
        });
        service.schedule(new Runnable() {
            @Override
            public void run() {
                if (hud != null) {
                    hud.dismiss();
                }
            }
        }, 5, TimeUnit.SECONDS);
    }

    public void hudUpdate(String tip) {
        if (hud != null) {
            hud.setLabel(tip);
        }
    }

    public void hudUpdateAndHid(String tip, double delay) {
        if (hud!=null){
            hud.setLabel(tip);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hudHide();
                }
            }, (int) delay * 1000);
        }
    }

    public void hudUpdateAndHid(String tip, double delay, final SuccessCallBack callBack) {
        if (hud != null) {
            hud.setLabel(tip);
        }
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hudHide();
                callBack.success();
            }
        }, (int) delay * 1000);
    }

    public void hudHide() {
        if (hud != null) {
            hud.dismiss();
            hud = null;
        }
    }

    public interface SuccessCallBack {
        void success();
    }
}
