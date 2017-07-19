package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.ReservationActivity;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.view.ImageTopButton;
import com.goockr.inductioncooker.view.SegmentController;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/21.
 */

public class HomeFragment extends Fragment implements SegmentController.SegmentControllerCallback, LeftDeviceFragment.LeftDeviceFragmentCallback, AdjustFragment.AdjustFragmentCallback {


    View contentView;

    private LeftDeviceFragment leftFragment;
    private RightDeviceFragment rightFragment;
    private AdjustFragment adjustFragment;

    private boolean isShowAdjustFragment=false;

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

    @OnClick({})
    public void onClick(View v) {

        switch (v.getId())
        {

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


//        FragmentManager fragmentManager= getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//
//
//
//        if (adjustFragment==null)
//        {
//            adjustFragment=new AdjustFragment();
//
//        }
//
//        adjustFragment.setCallback(this);
//
//
//        fragmentTransaction.add(R.id.fragment_home_moden_ll,adjustFragment,"AdjustFragment");
//        hideFragment(fragmentTransaction);
//        fragmentTransaction.show(adjustFragment);
//        fragmentTransaction.commit();




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

    @Override
    public void removeAdjustFragment() {

        if (adjustFragment!=null)
        {
            FragmentManager fragmentManager= getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.remove(adjustFragment);
            fragmentTransaction.commit();
            adjustFragment=null;
        }

    }


}
