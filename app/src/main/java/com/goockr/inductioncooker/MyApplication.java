package com.goockr.inductioncooker;

import android.app.Application;
import android.content.Context;

/**
 * Created by CMQ on 2017/6/23.
 */

public class MyApplication extends Application {

    static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化ontext
        context = getApplicationContext();
        instances=this;

    }

    //获取context
    public static final Context getContext(){
        return context;
    }

    public static MyApplication instances;

    public static MyApplication getInstances(){
        return instances;
    }


}
