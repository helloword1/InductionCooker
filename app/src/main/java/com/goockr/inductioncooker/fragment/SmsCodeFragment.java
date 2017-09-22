package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.MyEditText;
import com.goockr.ui.view.helper.HudHelper;

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

    HudHelper hud=new HudHelper();

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

        Bundle bundle= getArguments();

        if (bundle!=null)
        {
            phone=bundle.getString(Common.VerifiedPhoneNumFragmentPhoneKey);
            phone_tv.setText("手机号码"+phone);
            fragmentContent=bundle.getInt("content");
            state=bundle.getInt("state");
        }





    }

    private void initUI() {

        title_tv.setText("填写验证码");

        code_et.setHint("验证码");
        code_et.setBgImageVisibility(false);
        code_et.setInputType(InputType.TYPE_CLASS_PHONE);



    }

    private void initEvent() {
    }

    private void nextStep()
    {
        if (code_et.getText().length()==0)
        {
            hud.hudShowTip(getActivity(),getResources().getString(R.string.sms_code_null),Common.KHUDTIPSHORTTIME);
            return;
        }

        SetPwdFragment fragment=new SetPwdFragment();

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.add(R.id.activity_login_content_fl,fragment, Common.SetPwdFragment);
//
//        fragmentTransaction.hide(this);
//
//        fragmentTransaction.show(fragment);
//
//        fragmentTransaction.addToBackStack(Common.SetPwdFragment);
//
//        fragmentTransaction.commit();

        Bundle bundle=new Bundle();
        bundle.putInt("content",fragmentContent);
        bundle.putString("phoneNumber",phone);
        bundle.putInt("state",state);
        bundle.putString("smsCode",code_et.getText().toString());
        fragment.setArguments(bundle);
        FragmentHelper.addFragmentToBackStack(getActivity(),fragmentContent,this,fragment,Common.SetPwdFragment);

    }


    @OnClick({R.id.navbar_right_bt,R.id.navbar_left_bt})
    public void OnClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.navbar_left_bt):
                FragmentHelper.pop(getActivity());
                break;
            case (R.id.navbar_right_bt):

                nextStep();
                break;
        }
    }


}
