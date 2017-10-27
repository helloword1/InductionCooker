package com.goockr.inductioncooker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
import com.goockr.inductioncooker.utils.FileCache;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);

    }

    @Override
    protected void onResume() {
        super.onResume();
        myInitData();
    }

    /**
     * 获取手机关联的信息
     */
    private void myInitData() {
        Map<String, Object> map = new HashMap<>();
        String mobile = SharePreferencesUtils.getMobile();
        if (!NotNull.isNotNull(mobile)) {
            startActivity(new Intent(this, LoginActivity.class).putExtra("FROM_GUIDE",true));
            finish();
            return;
        }
        map.put("mobile", mobile);
        map.put("token", SharePreferencesUtils.getToken());

        HttpHelper.checkDevice(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                Log.d(TAG, "onFailure: " + error.msg);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                int result = 1;
                Log.d(TAG, "onSuccess: " + jsonObject);
                try {
                    result = jsonObject.getInt("result");
                    if (result == 0) {//成功
                        JSONArray list = jsonObject.getJSONArray("list");
                        FileCache.get(LauncherActivity.this).put("DEVICE_LIST", list);
                        String deviceId = SharePreferencesUtils.getDeviceId();
                        if (!NotNull.isNotNull(deviceId)) {
                            SharePreferencesUtils.setDeviceId(list.get(0).toString());
                        }
                        Intent intent = new Intent();
                        intent.setClass(LauncherActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
