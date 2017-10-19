package com.goockr.inductioncooker.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.adapter.DeviceAdapter;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
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
    private android.widget.TextView addDevice;
    private android.widget.RelativeLayout capturenarbar;
    private RecyclerView slideListView;
    public HudHelper hud = new HudHelper();
    private List<String> codes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        this.slideListView = (RecyclerView) findViewById(R.id.slideListView);
        this.capturenarbar = (RelativeLayout) findViewById(R.id.capture_narbar);
        this.addDevice = (TextView) findViewById(R.id.addDevice);
        this.back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        myInitData();

    }

    /**
     * 获取手机关联的信息
     */
    private void myInitData() {
        hud.hudShow(this, "正在加载。。。");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", SharePreferencesUtils.getMobile());
        map.put("token", SharePreferencesUtils.getToken());
        codes = new ArrayList<>();
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
                            codes.add(devicecode);
                        }
                        slideListView.setLayoutManager(new LinearLayoutManager(DeviceManageActivity.this));
                        slideListView.setAdapter(new DeviceAdapter(DeviceManageActivity.this, codes));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
