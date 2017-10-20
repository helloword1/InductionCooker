package com.goockr.inductioncooker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.adapter.DeviceAdapter;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
import com.goockr.inductioncooker.models.BaseDevice;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.ui.view.helper.HudHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 设备管理Activity
 */
public class DeviceManageActivity extends BaseActivity {
    private static final String TAG = "";
    private android.widget.TextView back;
    private RecyclerView slideListView;
    public HudHelper hud = new HudHelper();
    private List<BaseDevice> codes;
    private DeviceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        this.slideListView = (RecyclerView) findViewById(R.id.slideListView);
        this.back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        codes = new ArrayList<>();
        slideListView.setLayoutManager(new LinearLayoutManager(DeviceManageActivity.this));
        slideListView.setAdapter(adapter = new DeviceAdapter(this, codes));
        myInitData();
        adapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void itemClickListener(int position) {
                String deviceId = codes.get(position).getDeviceId();
                startActivity(new Intent(DeviceManageActivity.this,ChangePowerActivity.class).putExtra("DEVICE_ID",deviceId));
            }
        });
    }
    /**
     * 获取手机关联的信息
     */
    private void myInitData() {
        hud.hudShow(this, "正在加载。。。");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", SharePreferencesUtils.getMobile());
        map.put("token", SharePreferencesUtils.getToken());

        HttpHelper.checkDevice(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                Log.d(TAG, "onFailure: " + error.msg);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                hud.hudHide();
                int result = 1;
                Log.d(TAG, "onSuccess: " + jsonObject);
                try {
                    result = jsonObject.getInt("result");
                    if (result == 0) {//成功
                        JSONArray list = jsonObject.getJSONArray("list");
                        for (int i = 0; i < list.length(); i++) {
                            String devicecode = list.getString(i);
                            BaseDevice baseDevice = new BaseDevice();
                            baseDevice.setDeviceName("设备");
                            baseDevice.setDeviceId(devicecode);
                            codes.add(baseDevice);
                      }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
