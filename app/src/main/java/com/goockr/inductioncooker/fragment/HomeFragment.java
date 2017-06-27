package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.view.ImageTopButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/21.
 */

public class HomeFragment extends Fragment {


    View contentView;

    @BindView(R.id.fragment_home_power_bt)
    ImageTopButton power_bt;
    @BindView(R.id.fragment_home_reservation_bt)
    ImageTopButton reservation_bt;
    @BindView(R.id.fragment_home_unreservation_bt)
    ImageTopButton unreservation_bt;
    @BindView(R.id.fragment_home_moden_ll)
    LinearLayout moden_ll;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_home, container, false);

        ButterKnife.bind(this, contentView);

        initUI();

        initEvent();



        return contentView;
    }

    private void initEvent() {
    }

    private void initUI() {

        power_bt.setNormImageId(R.mipmap.btn_openkey_normal);
        power_bt.setHightLightImageId(R.mipmap.btn_openkey_pressed);
        power_bt.setSelImageId(R.mipmap.btn_openkey_selected);
        power_bt.setNormTextCoclor(R.color.colorBlack);
        power_bt.setText("开关机");

        reservation_bt.setNormImageId(R.mipmap.btn_reservation_normal);
        reservation_bt.setHightLightImageId(R.mipmap.btn_reservation_pressed);
        reservation_bt.setDisabledImageId(R.mipmap.btn_reservation_disabled);
        reservation_bt.setNormTextCoclor(R.color.colorBlack);
        reservation_bt.setText("预约");

        unreservation_bt.setNormImageId(R.mipmap.btn_cancel_normal);
        unreservation_bt.setHightLightImageId(R.mipmap.btn_cancel_pressed);
        unreservation_bt.setDisabledImageId(R.mipmap.btn_cancel_disabled);
        unreservation_bt.setNormTextCoclor(R.color.colorBlack);
        unreservation_bt.setText("取消预约");

        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_home_moden_ll,new LeftDeviceFragment(),"LeftDeviceFragment");
        fragmentTransaction.commit();


    }

    @OnClick({R.id.fragment_home_power_bt, R.id.fragment_home_reservation_bt,R.id.fragment_home_unreservation_bt})
    public void onClick(View v) {

        switch (v.getId())
        {
            case (R.id.fragment_home_power_bt):
                break;
            case (R.id.fragment_home_reservation_bt):
                break;
            case (R.id.fragment_home_unreservation_bt):
                break;
        }

    }



}
