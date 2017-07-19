package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.ReservationActivity;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.ImageTopButton;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/27.
 */

public class RightDeviceFragment extends Fragment implements ImageTopButton.ImageTopButtonOnClickListener, AdjustFragment.AdjustFragmentCallback {

    View contentView;

    List<ImageTopButton> buttons;

    @BindView(R.id.fragment_rightdevice_power_bt)
    ImageTopButton power_bt;
    @BindView(R.id.fragment_rightdevice_reservation_bt)
    ImageTopButton reservation_bt;
    @BindView(R.id.fragment_rightdevice_unreservation_bt)
    ImageTopButton unreservation_bt;
    @BindView(R.id.fragment_rightdevice_bt0)
    ImageTopButton bt_0;
    @BindView(R.id.fragment_rightdevice_bt1)
    ImageTopButton bt_1;
    @BindView(R.id.fragment_rightdevice_bt2)
    ImageTopButton bt_2;
    @BindView(R.id.fragment_rightdevice_bt3)
    ImageTopButton bt_3;
    @BindView(R.id.fragment_rightdevice_bt4)
    ImageTopButton bt_4;
    @BindView(R.id.fragment_rightdevice_bt5)
    ImageTopButton bt_5;
    @BindView(R.id.fragment_rightdevice_bottomview)
    LinearLayout bottom_ll;

    private AdjustFragment adjustFragment;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_rightdevice, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();

        initEvent();

        return contentView;
    }

    private void initData() {

        buttons= new ArrayList<ImageTopButton>();

        buttons.add(bt_0);
        buttons.add(bt_1);
        buttons.add(bt_2);
        buttons.add(bt_3);
        buttons.add(bt_4);
        buttons.add(bt_5);


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

        reservation_bt.buttonOnClickListener(this);
        unreservation_bt.buttonOnClickListener(this);
        power_bt.buttonOnClickListener(this);

        setButtonType(bt_0,R.mipmap.btn_baked_normal,R.mipmap.btn_baked_selected,R.mipmap.btn_baked_selected,R.color.colorGrayText,"煎焗");
        setButtonType(bt_1,R.mipmap.btn_stew_normal,R.mipmap.btn_stew_selected,R.mipmap.btn_stew_disabled,R.color.colorGrayText,"焖烧");
        setButtonType(bt_2,R.mipmap.btn_r_temperature_normal,R.mipmap.btn_r_temperature_selected,R.mipmap.btn_r_temperature_disabled,R.color.colorGrayText,"保温");
        setButtonType(bt_3,R.mipmap.btn_quicyfry_normal,R.mipmap.btn_quicyfry_selected,R.mipmap.btn_quicyfry_disabled,R.color.colorGrayText,"爆炒");
        setButtonType(bt_4,R.mipmap.btn_fried_normal,R.mipmap.btn_fried_selected,R.mipmap.btn_fried_disabled,R.color.colorGrayText,"油炸");
        setButtonType(bt_5,R.mipmap.btn_slowfire_normal,R.mipmap.btn_slowfire_selected,R.mipmap.btn_slowfire_disabled,R.color.colorGrayText,"文火");

    }

    private void initEvent() {

        for (ImageTopButton bt:buttons) {

            bt.setClickable(true);
            bt.buttonOnClickListener(this);

        }

    }

    private void setButtonType(ImageTopButton bt,int normImageId,int selImageId,int disableImageId,int normTextColor,String text)
    {
        bt.setNormImageId(normImageId);
        bt.setSelImageId(selImageId);
        bt.setDisabledImageId(disableImageId);
        bt.setNormTextCoclor(normTextColor);
        bt.setText(text);
    }


    @OnClick({R.id.fragment_rightdevice_bottomview})
    public void OnClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.fragment_rightdevice_bottomview):

                if (adjustFragment==null)
                {
                    adjustFragment=new AdjustFragment();

                }
                adjustFragment.setCallback(this);
                FragmentHelper.addFragmentToBackStack(getActivity(),R.id.fragment_rightdevice_content,null,adjustFragment,Common.AdjustFragment,Common.FragmentAnimationBottom);

                break;
        }
    }


    @Override
    public void imageTopButtonOnClickListener(ImageTopButton button) {

        switch (button.getId())
        {
            case (R.id.fragment_rightdevice_reservation_bt):

                Intent intent=new Intent(getActivity(),ReservationActivity.class);
                intent.putExtra(Common.HomeFragmentSelectIndexKey,0);
                getActivity().startActivity(intent);

                return;

            case (R.id.fragment_rightdevice_unreservation_bt):
                return;
            case (R.id.fragment_rightdevice_power_bt):
                return;
        }

      //  button.setSelect(!button.isSelect());

        if (adjustFragment==null)
        {
            adjustFragment=new AdjustFragment();

        }
        adjustFragment.setCallback(this);
        FragmentHelper.addFragmentToBackStack(getActivity(),R.id.fragment_rightdevice_content,null,adjustFragment,Common.AdjustFragment,Common.FragmentAnimationBottom);


    }

    @Override
    public void removeAdjustFragment() {
        adjustFragment=null;
    }
}
