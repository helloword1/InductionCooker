package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.MyEditText;
import com.goockr.ui.view.helper.HudHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/5.
 */

public class SmsCodeFragment extends Fragment {

    View contentView;

    private int fragmentContent;

    String phone;

    int state;

    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_sms_code_et)
    MyEditText code_et;
    @BindView(R.id.fragment_sms_code_tv)
    TextView phone_tv;
    @BindView(R.id.fragment_sms_code_bt)
    Button phone_bt;
    HudHelper hud = new HudHelper();
    private int sum = 60;
    private ScheduledExecutorService service;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment_sms_code, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();


        initEvent();

        return contentView;
    }

    private void initData() {

        Bundle bundle = getArguments();

        if (bundle != null) {
            phone = bundle.getString(Common.VERIFIED_PHONE_NUM_FRAGMENT_PHONE_KEY);
            phone_tv.setText("手机号码" + phone);
            fragmentContent = bundle.getInt("content");
            state = bundle.getInt("state");
        }

        service = new ScheduledThreadPoolExecutor(10, new ThreadFactory() {
            @Override
            public Thread newThread(@NonNull Runnable runnable) {
                return new Thread(runnable);
            }
        });
        phone_bt.setEnabled(false);
        service.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        phone_bt.setText(sum-- + " 秒");
                        if (sum < 0) {
                            service.shutdownNow();
                            phone_bt.setEnabled(true);
                            phone_bt.setText(R.string.sms_getCode);
                        }
                    }
                });
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void initUI() {
        title_tv.setText("填写验证码");
        code_et.setHint("验证码");
        code_et.setBgImageVisibility(false);
        code_et.setInputType(InputType.TYPE_CLASS_PHONE);


    }

    private void initEvent() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        service.shutdown();
    }

    private void nextStep() {
        if (code_et.getText().length() == 0) {
            hud.hudShowTip(getActivity(), getResources().getString(R.string.sms_code_null), Common.KHUDTIPSHORTTIME);
            return;
        }
        SetPwdFragment fragment = new SetPwdFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("content", fragmentContent);
        bundle.putString("phoneNumber", phone);
        bundle.putInt("state", state);
        bundle.putString("smsCode", code_et.getText().toString());
        fragment.setArguments(bundle);
        FragmentHelper.addFragmentToBackStack(getActivity(), fragmentContent, this, fragment, Common.SET_PWD_FRAGMENT);

    }


    @OnClick({R.id.navbar_right_bt, R.id.navbar_left_bt, R.id.fragment_sms_code_bt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case (R.id.navbar_left_bt):
                FragmentHelper.pop(getActivity());
                break;
            case (R.id.navbar_right_bt):
                nextStep();
                break;
            case R.id.fragment_sms_code_bt:
                //再次獲取驗證碼
                getCode();
                break;
            default:
                break;
        }
    }

    private void getCode() {
        Map<String, Object> map = new HashMap<>();
        map.put("functype", "vc");
        map.put("mobile", phone);

        hud.hudShow(getActivity(), getResources().getString(R.string.get_sms_code));

        HttpHelper.getForgetSmmCode(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                hud.hudUpdateAndHid(error.msg, Common.KHUDFINISHTIME);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                hud.hudUpdateAndHid("驗證碼已發送到手機", Common.KHUDFINISHTIME);
                sum = 60;
                phone_bt.setEnabled(false);
                boolean shutdown = service.isShutdown();

                Log.d("", "onSuccess: "+shutdown);
                if (shutdown) {
                    service = new ScheduledThreadPoolExecutor(10, new ThreadFactory() {
                        @Override
                        public Thread newThread(@NonNull Runnable runnable) {
                            return new Thread(runnable);
                        }
                    });
                    service.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    phone_bt.setText(sum-- + " 秒");
                                    if (sum < 0) {
                                        service.shutdownNow();
                                        phone_bt.setEnabled(true);
                                        phone_bt.setText(R.string.sms_getCode);
                                    }
                                }
                            });
                        }
                    }, 0, 1000, TimeUnit.MILLISECONDS);
                }

            }
        });
    }


}
