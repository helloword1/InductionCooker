package com.goockr.inductioncooker;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.goockr.inductioncooker.activity.HomeActivity;
import com.goockr.inductioncooker.activity.LauncherActivity;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.utils.TcpSocket;
import com.goockr.inductioncooker.utils.WifiConnectHelper;
import com.goockr.ui.view.view.TipDialog;

/**
 * Created by CMQ on 2017/6/23.
 */

public class MyApplication extends Application {

    static Context context;

    private int activityCount;//activity的count数

    TipDialog tipDialog;


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化ontext
        context = getApplicationContext();
        instances = this;

        //registerActivityLifecycleCallbacks();

    }


//    private void registerActivityLifecycleCallbacks() {
//
//        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
//                                               @Override
//                                               public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
//
//                                               }
//
//                                               @Override
//                                               public void onActivityStarted(final Activity activity) {
//
//                                                   if (activity.getClass()!=LauncherActivity.class)
//                                                   {
//                                                       activityCount++;
//                                                   }
//
//
//                                                   if (!WifiConnectHelper.isConnectWifi()) {
//
//                                                       if (activityCount==1)
//                                                       {
//                                                           if (tipDialog!=null&&tipDialog.isShowing())
//                                                           {
//                                                               return;
//                                                           }
//
//                                                           String msg = "请连接名称以“" + Common.KWIFINameHead + "”开头的wifi";
//
//                                                           tipDialog = new TipDialog(activity, getResources().getString(R.string.dialog_tip), msg, false, true);
//                                                           tipDialog.setActionButtonClick(new TipDialog.TipDialogCallBack() {
//                                                               @Override
//                                                               public void buttonClick(TipDialog dialog) {
//                                                                   activity.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));//直接进入手机中的wifi网络设置界面
//                                                                   dialog.dismiss();
//                                                               }
//                                                           });
//                                                           tipDialog.show();
//                                                       }
//
//                                                   }else {
//                                                       if (tipDialog!=null&&tipDialog.isShowing())
//                                                       {
//                                                           tipDialog.dismiss();
//                                                       }
//                                                   }
//
//
//                                               }
//
//                                               @Override
//                                               public void onActivityResumed(Activity activity) {
//
//                                               }
//
//                                               @Override
//                                               public void onActivityPaused(Activity activity) {
//
//                                               }
//
//                                               @Override
//                                               public void onActivityStopped(Activity activity) {
//                                                   if (activity.getClass()!=LauncherActivity.class)
//                                                   {
//                                                       activityCount--;
//                                                   }
//                                               }
//
//                                               @Override
//                                               public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
//
//                                               }
//
//                                               @Override
//                                               public void onActivityDestroyed(Activity activity) {
//
//                                               }
//                                           }
//        );
//
//    }


    //获取context
    public static final Context getContext() {
        return context;
    }

    public static MyApplication instances;

    public static MyApplication getInstances() {
        return instances;
    }


}
