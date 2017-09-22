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
import com.goockr.inductioncooker.view.MyEditText;
import com.goockr.ui.view.helper.HudHelper;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/19.
 */

public class UpdatePwdFragment extends Fragment {

    View contentView;

    HudHelper hud=new HudHelper();

    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_update_pwd_oldpwd_et)
    MyEditText oldpwd_et;
    @BindView(R.id.fragment_update_pwd_newpwd_et)
    MyEditText newpwd_et;
    @BindView(R.id.fragment_update_pwd_again_et)
    MyEditText againpwd_et;
    @BindView(R.id.fragment_update_pwd_forget_tv)
    TextView forget_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment_update_pwd, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();

        initEvent();

        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }

    private void initData() {
    }

    private void initUI() {

        title_tv.setText("修改密码");

        right_bt.setText("确定");

        oldpwd_et.setBgImageVisibility(false);
        newpwd_et.setBgImageVisibility(false);
        againpwd_et.setBgImageVisibility(false);

        oldpwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        newpwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        againpwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

    }

    private void initEvent() {
    }



    @OnClick({R.id.navbar_right_bt,R.id.navbar_left_bt,R.id.fragment_update_pwd_forget_tv})
    public void OnClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.navbar_right_bt):

                updatePwd();

                break;

            case (R.id.navbar_left_bt):
             //   FragmentHelper.clearBackStack(getActivity());

                break;

            case (R.id.fragment_update_pwd_forget_tv):

                forgetButtonClick();

                break;
        }
    }

    private void updatePwd() {

        if (oldpwd_et.getText().length()==0||newpwd_et.getText().length()==0||againpwd_et.getText().length()==0)
        {
            hud.hudShowTip(getActivity(),getResources().getString(R.string.update_pwd_null),Common.KHUDTIPSHORTTIME);
            return;
        }

        if (! (newpwd_et.getText().toString().equals(againpwd_et.getText().toString())))
        {
            hud.hudShowTip(getActivity(),getResources().getString(R.string.pwd_againpwd_unequal),Common.KHUDTIPSHORTTIME);
            return;
        }

        hud.hudShow(getActivity(),getResources().getString(R.string.chage_pwd_tip));

        Map<String,Object> map=new HashMap<>();
//functype=ch&mobile=13763085121&token=64f62f5341b6455981ed456bdb95eb80&old=123&new=456
        map.put("functype","ch");
        map.put("mobile", User.getInstance().mobile);
        map.put("token",User.getInstance().token);
        map.put("old",oldpwd_et.getText().toString());
        map.put("new",newpwd_et.getText().toString());
        HttpHelper.updatePwd(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {

                hud.hudUpdateAndHid(error.msg,Common.KHUDFINISHTIME);

            }

            @Override
            public void onSuccess(JSONObject jsonObject) {

                hud.hudUpdateAndHid(getResources().getString(R.string.chage_pwd_success), Common.KHUDFINISHTIME, new HudHelper.SuccessCallBack() {
                    @Override
                    public void success() {
                        getActivity().finish();
                    }
                });

            }
        });

    }

    private void forgetButtonClick() {

        VerifiedPhoneNumFragment fragment=new VerifiedPhoneNumFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("state",1);
        bundle.putInt("content",R.id.activity_update_pwd);
        fragment.setArguments(bundle);
        FragmentHelper.addFragmentToBackStack(getActivity(),R.id.activity_update_pwd,this,fragment, Common.VerifiedPhoneNumFragment);

    }


}
