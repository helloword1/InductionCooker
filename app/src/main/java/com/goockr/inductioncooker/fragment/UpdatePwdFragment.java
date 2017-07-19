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
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.MyEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/19.
 */

public class UpdatePwdFragment extends Fragment {

    View contentView;

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

                break;

            case (R.id.navbar_left_bt):
             //   FragmentHelper.clearBackStack(getActivity());
                getActivity().finish();
                break;

            case (R.id.fragment_update_pwd_forget_tv):



                break;
        }
    }






}
