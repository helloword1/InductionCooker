package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.INotificationSideChannel;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.goockr.inductioncooker.MyApplication;
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

public class VerifiedPhoneNumFragment extends Fragment {


    private  final int STATE_REGIST = 0;

    private  final int STATE_FORGET = 1;

    View contentView;

    private HudHelper hudHelper;

    private int state=0;


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



    }

    private void initData() {



    }

    private void initUI() {

        hudHelper=new HudHelper();

        title_tv.setText("验证手机号码");
        phone_et.setInputType(InputType.TYPE_CLASS_PHONE);
        phone_et.setHint("手机号码");

    }

    private void initEvent() {



    }

    private void nextStep()
    {


        SmsCodeFragment fragment=new SmsCodeFragment();
        Bundle bundle=new Bundle();
        bundle.putString(Common.VerifiedPhoneNumFragmentPhoneKey,phone_et.getText());
        fragment.setArguments(bundle);

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.add(R.id.activity_login_content_fl,fragment, Common.SmsCodeFragment);
//
//
//
//        fragmentTransaction.hide(this);
//
//        fragmentTransaction.show(fragment);
//
//        fragmentTransaction.addToBackStack(Common.SmsCodeFragment);
//
//        fragmentTransaction.commit();

        FragmentHelper.addFragmentToBackStack(getActivity(),R.id.activity_login_content_fl,this,fragment,Common.SmsCodeFragment);
    }

    @OnClick({R.id.navbar_left_bt,R.id.navbar_right_bt})
    public void OnClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.navbar_left_bt):
                FragmentHelper.pop(getActivity());
                break;

            case (R.id.navbar_right_bt):

                if (phone_et.getText().length()==0)
                {
                    hudHelper.hudShowTip(getActivity(),"请填写手机号码！",1000);
                    return;
                }
                nextStep();
                break;

        }
    }


}
