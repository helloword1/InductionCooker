package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.view.ImageTopButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/27.
 */

public class LeftDeviceFragment extends Fragment implements View.OnClickListener, ImageTopButton.ImageTopButtonOnClickListener {

    View contentView;

    List<ImageTopButton> buttons;

    private LeftDeviceFragmentCallback callback;

    @BindView(R.id.fragment_leftdevice_bt0)
    ImageTopButton bt_0;
    @BindView(R.id.fragment_leftdevice_bt1)
    ImageTopButton bt_1;
    @BindView(R.id.fragment_leftdevice_bt2)
    ImageTopButton bt_2;
    @BindView(R.id.fragment_leftdevice_bt3)
    ImageTopButton bt_3;
    @BindView(R.id.fragment_leftdevice_bt4)
    ImageTopButton bt_4;
    @BindView(R.id.fragment_leftdevice_bt5)
    ImageTopButton bt_5;
    @BindView(R.id.fragment_leftdevice_bt6)
    ImageTopButton bt_6;
    @BindView(R.id.fragment_leftdevice_bt7)
    ImageTopButton bt_7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_leftdevice, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();

        initEvent();

        return contentView;
    }

    private void initData() {

        buttons=new ArrayList<ImageTopButton>();

        buttons.add(bt_0);
        buttons.add(bt_1);
        buttons.add(bt_2);
        buttons.add(bt_3);
        buttons.add(bt_4);
        buttons.add(bt_5);
        buttons.add(bt_6);
        buttons.add(bt_7);

    }

    private void initUI() {

        setButtonType(bt_0,R.mipmap.btn_soup_normal,R.mipmap.btn_soup_pressed,R.mipmap.btn_soup_disabled,R.color.colorGrayText,"煲粥");
        setButtonType(bt_1,R.mipmap.btn_porridge_normal,R.mipmap.btn_porridge_selected,R.mipmap.btn_porridge_disabled,R.color.colorGrayText,"煲汤");
        setButtonType(bt_2,R.mipmap.btn_rice_normal,R.mipmap.btn_rice__selected,R.mipmap.btn_rice_disabled,R.color.colorGrayText,"煮饭");
        setButtonType(bt_3,R.mipmap.btn_water_normal,R.mipmap.btn_water__selected,R.mipmap.btn_water_disabled,R.color.colorGrayText,"烧水");
        setButtonType(bt_4,R.mipmap.brn_hotpot__normal,R.mipmap.brn_hotpot_selected,R.mipmap.brn_hotpot_disabled,R.color.colorGrayText,"火锅");
        setButtonType(bt_5,R.mipmap.btn_fry_normal,R.mipmap.btn_fry_selected,R.mipmap.btn_fry_disabled,R.color.colorGrayText,"煎炒");
        setButtonType(bt_6,R.mipmap.btn_baked_fried_normal,R.mipmap.btn_baked_fried__selected,R.mipmap.btn_baked_fried_disabled,R.color.colorGrayText,"烤炸");
        setButtonType(bt_7,R.mipmap.btn_temperature_normal,R.mipmap.btn_temperature__selected,R.mipmap.btn_temperature_disabled,R.color.colorGrayText,"保温");

    }

    private void setButtonType(ImageTopButton bt,int normImageId,int selImageId,int disableImageId,int normTextColor,String text)
    {
        bt.setNormImageId(normImageId);
        bt.setSelImageId(selImageId);
        bt.setDisabledImageId(disableImageId);
        bt.setNormTextCoclor(normTextColor);
        bt.setText(text);
    }


    private void  initEvent()
    {

        for (ImageTopButton bt:buttons) {

            bt.setClickable(true);
            bt.buttonOnClickListener(this);

          //  bt.setOnClickListener();

        }

    }

    public void buttonOnClickListener(LeftDeviceFragmentCallback callback)
    {
        this.callback=callback;
    }

    @Override
    public void onClick(View v) {



    }

    @Override
    public void imageTopButtonOnClickListener(ImageTopButton button) {

        button.setSelect(!button.isSelect());

        if (this.callback!=null)
        {
            this.callback.leftDeviceFragmentButtonClick(button);
        }



    }


//    public void onClick(View v) {
//
//        switch (v.getId()){
//
//            default:
//
//                ImageTopButton bt=(ImageTopButton)v;
//                bt.setSelect(!bt.isSelect());
//
//                break;
//
//        }
//
//    }

    public interface LeftDeviceFragmentCallback{

         void leftDeviceFragmentButtonClick(ImageTopButton button);

    }




}
