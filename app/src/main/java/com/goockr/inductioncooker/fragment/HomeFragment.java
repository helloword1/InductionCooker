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
import com.goockr.inductioncooker.view.SegmentController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/21.
 */

public class HomeFragment extends Fragment implements SegmentController.SegmentControllerCallback, LeftDeviceFragment.LeftDeviceFragmentCallback {


    View contentView;

    private LeftDeviceFragment leftFragment;
    private RightDeviceFragment rightFragment;


    @BindView(R.id.fragment_home_power_bt)
    ImageTopButton power_bt;
    @BindView(R.id.fragment_home_reservation_bt)
    ImageTopButton reservation_bt;
    @BindView(R.id.fragment_home_unreservation_bt)
    ImageTopButton unreservation_bt;
    @BindView(R.id.fragment_home_moden_ll)
    LinearLayout moden_ll;
    @BindView(R.id.fragment_home_segment)
    SegmentController segmentController;



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

        segmentController.addListener(this);

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
        if (leftFragment==null)
        {
            leftFragment=new LeftDeviceFragment();
            leftFragment.buttonOnClickListener(this);
            fragmentTransaction.add(R.id.fragment_home_moden_ll,leftFragment,"LeftDeviceFragment");
        }

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


    @Override
    public void selectIndexChange(int index) {

        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (index==0)
        {

            if (leftFragment==null)
            {
                leftFragment=new LeftDeviceFragment();
                leftFragment.buttonOnClickListener(this);
                fragmentTransaction.add(R.id.fragment_home_moden_ll,leftFragment,"LeftDeviceFragment");
            }

            //隐藏所有fragment
            hideFragment(fragmentTransaction);
            //显示需要显示的fragment
            fragmentTransaction.show(leftFragment);
        }else {

            if (rightFragment==null)
            {
                rightFragment=new RightDeviceFragment();
                fragmentTransaction.add(R.id.fragment_home_moden_ll,rightFragment,"RightDeviceFragment");
            }
            //隐藏所有fragment
            hideFragment(fragmentTransaction);
            //显示需要显示的fragment
            fragmentTransaction.show(rightFragment);
        }



        fragmentTransaction.commit();

    }

    @Override
    public void leftDeviceFragmentButtonClick(ImageTopButton button) {


        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideFragment(fragmentTransaction);

        AdjustFragment fragment=new AdjustFragment();
        fragmentTransaction.add(R.id.fragment_home_moden_ll,fragment,"AdjustFragment");

        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();




    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction){
        if(leftFragment != null){
            transaction.hide(leftFragment);
        }
        if(rightFragment != null){
            transaction.hide(rightFragment);
        }

    }

}
