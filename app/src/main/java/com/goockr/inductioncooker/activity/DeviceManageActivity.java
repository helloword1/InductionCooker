package com.goockr.inductioncooker.activity;

import android.os.Bundle;

import com.goockr.inductioncooker.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 设备管理Activity
 */
public class DeviceManageActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);

        myInitData();

    }

    /**
     * 获取手机关联的信息
     */
    private void myInitData() {

    }

    @Override
    protected void handleMsg(String mProtocol) {
        try {
            JSONObject jsonObject = new JSONObject(mProtocol);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
