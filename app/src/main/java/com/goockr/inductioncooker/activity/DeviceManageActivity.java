package com.goockr.inductioncooker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.adapter.DeviceAdapter;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
import com.goockr.inductioncooker.models.BaseDevice;
import com.goockr.inductioncooker.utils.FileCache;
import com.goockr.inductioncooker.utils.MyToast;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.inductioncooker.view.DialogView;
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
    private TextView textView;
    private DialogView dialogView;
    private PercentRelativeLayout notiPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_manage);
        this.slideListView = (RecyclerView) findViewById(R.id.slideListView);
        notiPercent = findViewById(R.id.notiPercent);
        this.back = (TextView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialogView = DialogView.getSingleton();
        dialogView.setContext(this);
        codes = new ArrayList<>();
        slideListView.setLayoutManager(new LinearLayoutManager(DeviceManageActivity.this));
        slideListView.setAdapter(adapter = new DeviceAdapter(this, codes));
        myInitData();
        adapter.setOnItemClickListener(new DeviceAdapter.OnItemClickListener() {
            @Override
            public void itemClickListener(int position) {
                String deviceId = codes.get(position).getDeviceId();
                startActivity(new Intent(DeviceManageActivity.this, ChangePowerActivity.class).putExtra("DEVICE_ID", deviceId));
            }

            @Override
            public void itemLongClickListener(int position) {
                showTurnOn("确定解除绑定？",position);
            }
        });
    }

    private void showTurnOn(String msg,final int position) {
        View view = dialogView.showCustomDialong(R.layout.dialog_power_change);
        TextView tvCancel = view.findViewById(R.id.tvCancel);
        TextView tvContent = view.findViewById(R.id.tvContent);

        tvContent.setText(msg);
        TextView tvCommit = view.findViewById(R.id.tvCommit);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismissDialong();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCancel(position);
            }
        });

    }

    //解除绑定
    private void setCancel(final int position) {
        hud.hudShow(this, "正在加载。。。");
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", SharePreferencesUtils.getMobile());
        map.put("token", SharePreferencesUtils.getToken());
        map.put("devcode", codes.get(position).getDeviceId());

        HttpHelper.unBindDevice(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                Log.d(TAG, "onFailure: " + error.msg);
                dialogView.dismissDialong();
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                hud.hudHide();
                int result = 1;
                Log.d(TAG, "onSuccess: " + jsonObject);
                try {
                    result = jsonObject.getInt("result");
                    if (result == 0) {//成功
                        codes.remove(position);
                        adapter.notifyDataSetChanged();
                        myInitData1();
                        dialogView.dismissDialong();
                        MyToast.showToastCustomerStyleText(DeviceManageActivity.this,"解绑成功！");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setEmptyView() {
        textView = new TextView(this);
        textView.setText("暂时还没有内容哦");
        textView.setTextSize(15);
        textView.setTextColor(getResources().getColor(R.color.colormain));
        textView.setGravity(Gravity.CENTER);
        notiPercent.addView(textView);
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
                        if (codes.size() == 0) {
                            setEmptyView();
                        } else if (NotNull.isNotNull(textView)) {
                            textView.setVisibility(View.GONE);
                        }
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    /**
     * 获取手机关联的信息
     */
    private void myInitData1() {
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", SharePreferencesUtils.getMobile());
        map.put("token", SharePreferencesUtils.getToken());

        HttpHelper.checkDevice(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                int result = 1;
                try {
                    result = jsonObject.getInt("result");
                    if (result == 0) {//成功
                        JSONArray list = jsonObject.getJSONArray("list");
                        FileCache.get(DeviceManageActivity.this).put("DEVICE_LIST",list);
                        SharePreferencesUtils.setDeviceId(list.get(0).toString());
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
