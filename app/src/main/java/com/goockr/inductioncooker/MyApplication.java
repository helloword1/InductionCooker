package com.goockr.inductioncooker;

import android.app.Application;
import android.content.Context;

import com.goockr.inductioncooker.models.User;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.ui.view.view.TipDialog;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by CMQ on 2017/6/23.
 */

public class MyApplication extends Application {
    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化ontext
        context = getApplicationContext();
        instances = this;
        autoLogin();
    }

    private void autoLogin() {

        JSONObject json=new JSONObject();

        try {
            json.put("name",SharePreferencesUtils.getUserName());
            json.put("token",SharePreferencesUtils.getToken());
            json.put("mobile",SharePreferencesUtils.getMobile());
            json.put("id",SharePreferencesUtils.getUserID());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        User.getInstance().updateUserInfoWithdict(json);

    }
    //获取context
    public static final Context getContext() {
        return context;
    }

    public static MyApplication instances;

    public static MyApplication getInstances() {
        return instances;
    }


}
