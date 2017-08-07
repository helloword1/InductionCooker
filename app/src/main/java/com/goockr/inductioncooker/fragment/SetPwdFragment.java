package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.MyEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/5.
 */

public class SetPwdFragment extends Fragment {

    View contentView;



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



    }

    private void initUI() {

        title_tv.setText("设置密码");

    }

    private void initEvent() {

        newpwd_et.setBgImageVisibility(false);
        newpwd_et.setHint("请输入密码");

        again_et.setBgImageVisibility(false);
        again_et.setHint("请确认密码");


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

              //  getActivity().finish();

                FragmentHelper.popRootFragment(getActivity());

                break;
        }
    }


}
