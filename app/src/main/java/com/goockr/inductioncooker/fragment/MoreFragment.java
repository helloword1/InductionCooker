package com.goockr.inductioncooker.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.DeviceManageActivity;
import com.goockr.inductioncooker.activity.LoginActivity;
import com.goockr.inductioncooker.activity.UpdatePwdActivity;
import com.goockr.inductioncooker.adapter.MoreAdapter;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
import com.goockr.inductioncooker.models.MoreAdapterModel;
import com.goockr.inductioncooker.models.MyAbStractSection;
import com.goockr.inductioncooker.utils.FileCache;
import com.goockr.inductioncooker.utils.MyToast;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.inductioncooker.view.DialogView;
import com.goockr.ui.view.activity.CompanyIntroduceActivity;
import com.jinlin.zxing.CaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static android.app.Activity.RESULT_OK;

/**
 * Created by CMQ on 2017/6/21.
 */

public class MoreFragment extends Fragment {

    private static final String TAG = "";
    View view;
    @BindView(R.id.fragment_more_rv)
    RecyclerView mRecyclerView;

    private List<MyAbStractSection> mData;
    private MoreAdapter sectionAdapter;

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_more, container, false);
        mData = new ArrayList<>();


        mRecyclerView = (RecyclerView) view.findViewById(R.id.fragment_more_rv);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));




        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 120 && NotNull.isNotNull(data)) {
            String result_data = data.getStringExtra("RESULT_DATA");
            if (TextUtils.equals(result_data, "")) {
                return;
            }
            bindDevice(result_data);

        }
    }

    private void bindDevice(String device) {
        if (device.contains("@_@")) {
            String[] split = device.split("@_@");
            device = split[1];
        } else {
            MyToast.showToastCustomerStyleText(getActivity(), "二维码不正确");
            return;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("mobile", SharePreferencesUtils.getMobile());
        map.put("token", SharePreferencesUtils.getToken());
        map.put("devcode", device);

        HttpHelper.addDevice(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                Log.d(TAG, "onFailure: " + error.msg);
                MyToast.showToastCustomerStyleText(getActivity(), error.msg);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                int result = 1;
                Log.d(TAG, "onSuccess: " + jsonObject);
                try {
                    result = jsonObject.getInt("result");
                    String msg = jsonObject.getString("msg");
                    if (result == 0) {//成功
                        MyToast.showToastCustomerStyleText(getActivity(), msg);
                        myInitData();
                    } else {
                        MyToast.showToastCustomerStyleText(getActivity(), msg);
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
    private void myInitData() {
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
                int result = 1;
                Log.d(TAG, "onSuccess: " + jsonObject);
                try {
                    result = jsonObject.getInt("result");
                    if (result == 0) {//成功
                        JSONArray list = jsonObject.getJSONArray("list");
                        FileCache.get(getActivity()).put("DEVICE_LIST", list);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initData() {
        if (mData.size() != 0) {
            mData.clear();
        }
        List<MyAbStractSection> list = new ArrayList<>();
        list.add(new MyAbStractSection(true, "Section 1", false));
        list.add(new MyAbStractSection(new MoreAdapterModel("添加设备", "", true).setCenter(false)));
        list.add(new MyAbStractSection(true, "Section 2", true));
        String userName = SharePreferencesUtils.getUserName();
        if (!NotNull.isNotNull(userName)) {
            userName = SharePreferencesUtils.getMobile();
        }
        list.add(new MyAbStractSection(new MoreAdapterModel("权限转移", "", true).setCenter(false)));
        list.add(new MyAbStractSection(new MoreAdapterModel("用户名", userName, false).setCenter(false)));
        list.add(new MyAbStractSection(new MoreAdapterModel("修改登录密码", "", true).setCenter(false)));
        list.add(new MyAbStractSection(new MoreAdapterModel("版本", "V1.0", false).setCenter(false)));
        list.add(new MyAbStractSection(new MoreAdapterModel("厂家信息", "", true).setCenter(false)));
        list.add(new MyAbStractSection(true, "Section 2", true));
        list.add(new MyAbStractSection(new MoreAdapterModel("退出登录", "", false).setCenter(true)));
        mData.addAll(list);
        sectionAdapter = new MoreAdapter(R.layout.item_section_content, R.layout.def_section_head, mData);
        mRecyclerView.setAdapter(sectionAdapter);
        sectionAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemClick: " + position);
                switch (position) {
                    case 1: // 添加设备
                        // initPermissions();
                        MoreFragment.this.startActivityForResult(new Intent(getActivity(), CaptureActivity.class), 120);
//                        getActivity().startActivity(new Intent(getActivity(), DeviceManageActivity.class));
                        break;
                    case 3://设备管理
                        getActivity().startActivity(new Intent(getActivity(), DeviceManageActivity.class));
                        break;
                    case 4://用户名
//                        getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                        break;
                    case 5://修改密码
                        getActivity().startActivity(new Intent(getActivity(), UpdatePwdActivity.class));
                        break;
                    case 6://版本
                        break;
                    case 7://厂家信息
                        getActivity().startActivity(new Intent(getActivity(), CompanyIntroduceActivity.class));
                        break;
                    case 9://退出登陆
                        //调用
                        showTurnOn("");
                        break;
                    default:
                        break;

                }
            }
        });
        sectionAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
    }
    /**
     * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory
     */
    private  void deleteFilesByDirectory(File directory) {
        if (directory != null && directory.exists() && directory.isDirectory()) {
            for (File item : directory.listFiles()) {
                item.delete();
            }
        }
    }
    private void showTurnOn(String msg) {
        final DialogView dialogView = DialogView.getSingleton();
        dialogView.setContext(getActivity());
        View view = dialogView.showCustomDialong(R.layout.dialog_power_change);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        TextView alert_title = (TextView) view.findViewById(R.id.alert_title);
        alert_title.setText("是否退出登陆");
        tvContent.setText(msg);
        TextView tvCommit = (TextView) view.findViewById(R.id.tvCommit);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismissDialong();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {

                deleteFilesByDirectory(new File("/data/data/" + getContext().getPackageName() + "/shared_prefs"));
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        });

    }

}
