package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/5.
 */

public class VerifiedPhoneNumFragment extends Fragment {


    private final int STATE_REGIST = 0;

    private final int STATE_FORGET = 1;

    View contentView;

    private HudHelper hudHelper;

    private int state = 0;

    private int fragmentContent;

    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_verified_phone_num_et)
    MyEditText phone_et;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment_verified_phone_num, container, false);

        ButterKnife.bind(this, contentView);

        getData();

        initData();

        initUI();


        initEvent();

        return contentView;
    }

    private void getData() {

        Bundle bundle = getArguments();
        state = bundle.getInt("state");
        fragmentContent = bundle.getInt("content");
    }

    private void initData() {


    }

    private void initUI() {
        hudHelper = new HudHelper();
        title_tv.setText("验证手机号码");
        phone_et.setInputType(InputType.TYPE_CLASS_PHONE);
        phone_et.setHint("手机号码");

    }

    private void initEvent() {


    }

    private void nextStep() {

        if (phone_et.getText().length() == 0) {
            hudHelper.hudShowTip(getActivity(), getResources().getString(R.string.phone_null), Common.KHUDTIPSHORTTIME);
            return;
        }
//functype=vc&mobile=13763085121
        Map<String, Object> map = new HashMap<>();
        map.put("functype", "vc");
        map.put("mobile", phone_et.getText().toString());

        hudHelper.hudShow(getActivity(), getResources().getString(R.string.get_sms_code));

        HttpHelper.getForgetSmmCode(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                hudHelper.hudUpdateAndHid(error.msg, Common.KHUDFINISHTIME);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {

                hudHelper.hudHide();
                SmsCodeFragment fragment = new SmsCodeFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Common.VERIFIED_PHONE_NUM_FRAGMENT_PHONE_KEY, phone_et.getText());
                bundle.putInt("content", fragmentContent);
                bundle.putInt("state", state);
                fragment.setArguments(bundle);
                FragmentHelper.addFragmentToBackStack(getActivity(), fragmentContent, VerifiedPhoneNumFragment.this, fragment, Common.SMS_CODE_FRAGMENT);
            }
        });


    }

    @OnClick({R.id.navbar_left_bt, R.id.navbar_right_bt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case (R.id.navbar_left_bt):
                FragmentHelper.pop(getActivity());
                break;

            case (R.id.navbar_right_bt):

                if (phone_et.getText().length() == 0) {
                    hudHelper.hudShowTip(getActivity(), "请填写手机号码！", 1000);
                    return;
                }
                nextStep();
                break;
            default:
                break;

        }
    }


}
