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
import com.goockr.inductioncooker.models.User;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.inductioncooker.view.MyEditText;
import com.goockr.ui.view.helper.HudHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/5.
 */

public class SetPwdFragment extends Fragment {

    View contentView;

    private int fragmentContent;
    String phone;
    String smsCode;
    int state;

    HudHelper hud = new HudHelper();

    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_set_pwd_again_et)
    MyEditText again_et;
    @BindView(R.id.fragment_set_pwd_newpwd_et)
    MyEditText newpwd_et;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment_set_pwd, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();

        initEvent();

        return contentView;
    }

    private void initData() {

        Bundle bundle = getArguments();

        if (bundle != null) {
            phone = bundle.getString("phoneNumber");
            fragmentContent = bundle.getInt("content");
            smsCode = bundle.getString("smsCode");
            state = bundle.getInt("state");
        }

    }

    private void initUI() {

        title_tv.setText("设置密码");

    }

    private void initEvent() {

        newpwd_et.setBgImageVisibility(false);
        newpwd_et.setHint("请输入密码");
        newpwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        again_et.setBgImageVisibility(false);
        again_et.setHint("请确认密码");
        again_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//TYPE_TEXT_VARIATION_VISIBLE_PASSWORD

    }

    @OnClick({R.id.navbar_left_bt, R.id.navbar_right_bt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case (R.id.navbar_left_bt):
                FragmentHelper.pop(getActivity());
                break;
            case (R.id.navbar_right_bt):

                //  getActivity().finish();


                action();


                break;
            default:
                break;
        }
    }

    private void action() {

        if (newpwd_et.getText().length() == 0 || again_et.getText().length() == 0) {
            hud.hudShowTip(getActivity(), getResources().getString(R.string.pwd_againpwd_null), Common.KHUDTIPSHORTTIME);
            return;
        }

        if (!(newpwd_et.getText().toString().equals(again_et.getText().toString()))) {
            hud.hudShowTip(getActivity(), getResources().getString(R.string.pwd_againpwd_unequal), Common.KHUDTIPSHORTTIME);
            return;
        }

        if (state == 0) {

            hud.hudShow(getActivity(), getResources().getString(R.string.registing));

//functype=cu&mobile=13763085121&vcode=6000
            Map<String, Object> map = new HashMap<>();
            map.put("functype", "cu");
            map.put("mobile", phone);
            map.put("vcode", smsCode);
            map.put("pwd", newpwd_et.getText().toString());

            HttpHelper.regist(map, new OKHttp.HttpCallback() {
                @Override
                public void onFailure(HttpError error) {
                    hud.hudUpdateAndHid(error.msg, Common.KHUDFINISHTIME);
                }

                @Override
                public void onSuccess(final JSONObject jsonObject) {

                    hud.hudUpdateAndHid(getResources().getString(R.string.registed), Common.KHUDFINISHTIME, new HudHelper.SuccessCallBack() {
                        @Override
                        public void success() {
                            // FragmentHelper.popRootFragment(getActivity());

                            dataSave(jsonObject);

                            getActivity().finish();

                        }
                    });

                }
            });

        } else {

            hud.hudShow(getActivity(), getResources().getString(R.string.chage_pwd_tip));

            Map<String, Object> map = new HashMap<>();
            map.put("functype", "rp");
            map.put("mobile", phone);
            map.put("vcode", smsCode);
            map.put("new", newpwd_et.getText().toString());
            HttpHelper.forgetPwd(map, new OKHttp.HttpCallback() {
                @Override
                public void onFailure(HttpError error) {

                    hud.hudUpdateAndHid(error.msg, Common.KHUDFINISHTIME);

                }

                @Override
                public void onSuccess(JSONObject jsonObject) {

                    hud.hudUpdateAndHid(getResources().getString(R.string.chage_pwd_success), Common.KHUDFINISHTIME, new HudHelper.SuccessCallBack() {
                        @Override
                        public void success() {
                            FragmentHelper.popRootFragment(getActivity());
                        }
                    });

                }
            });
        }


    }

    private void dataSave(JSONObject json) {

        JSONObject object = new JSONObject();

        String name = "";
        String token = "";
        String mobile = "";
        String userId = "";

        try {
            object = json.getJSONObject("userInfo");
            name = object.getString("name");
            token = object.getString("token");
            mobile = object.getString("mobile");
            userId = object.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        User.getInstance().updateUserInfoWithdict(object);

        SharePreferencesUtils.setToken(token);
        SharePreferencesUtils.setUserName(name);
        SharePreferencesUtils.setMobile(mobile);
        SharePreferencesUtils.setUserID(userId);

    }


}
