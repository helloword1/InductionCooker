package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.view.DialongView;
import com.goockr.inductioncooker.view.ImageTopButton;
import com.goockr.inductioncooker.view.SegmentController;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CMQ on 2017/6/21.
 */

public class HomeFragment1 extends Fragment implements SegmentController.SegmentControllerCallback, LeftDeviceFragment1.LeftDeviceFragmentCallback{
    View contentView;
    private LeftDeviceFragment1 leftFragment;
    private RightDeviceFragment rightFragment;
    private boolean isShowAdjustFragment = false;
    @BindView(R.id.fragment_home_moden_ll)
    LinearLayout moden_ll;
    @BindView(R.id.fragment_home_segment)
    SegmentController segmentController;
    private FragmentTransaction fragmentTransaction;
    private static String effectStr0 = "";
    private static String effectStr1 = "";
    public static int code1;
    public static int error = -1;
    public static int success = -1;
    public static int reverMode = -1;
    public int code;
    private DialongView dialongView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, contentView);
        initUI();
        initEvent();
        return contentView;
    }

    private void initEvent() {
        segmentController.addListener(this);
        dialongView = new DialongView(getActivity());

    }

    private void showTurnOn() {
        View view = dialongView.showCustomDialong(R.layout.dialong_view);
        ImageView deleteView = (ImageView) view.findViewById(R.id.dialong_delete);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialongView.dismissDialong();
            }
        });
        TextView dialongText = (TextView) view.findViewById(R.id.dialongText);
        dialongText.setText("设备与电磁炉断已开连接");
    }

    private void initUI() {
        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (NotNull.isNotNull(leftFragment) && NotNull.isNotNull(rightFragment)) {
            fragmentTransaction.remove(leftFragment);
            fragmentTransaction.remove(rightFragment);
        }

        if (leftFragment == null) {
            leftFragment = new LeftDeviceFragment1();
        }
        if (rightFragment == null) {
            rightFragment = new RightDeviceFragment();
        }
        fragmentTransaction.add(R.id.fragment_home_moden_ll, leftFragment, "LeftDeviceFragment");
        fragmentTransaction.add(R.id.fragment_home_moden_ll, rightFragment, "RightDeviceFragment");

        fragmentTransaction.show(leftFragment).hide(rightFragment);
        fragmentTransaction.commit();


    }

    @Override
    public void selectIndexChange(int index) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        if (index == 0) {
            fragmentTransaction.show(leftFragment).hide(rightFragment);
        } else {
            fragmentTransaction.show(rightFragment).hide(leftFragment);
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

    public void setMProtocol(String read) {
        if (NotNull.isNotNull(read)) {
            JSONObject order = null;
            //错误码
            code = 0;
            try {
                JSONObject object = new JSONObject(read);
                order = object.getJSONObject("order");
                if (NotNull.isNotNull(order)) {
                    int LRID = Integer.valueOf(order.getString("deviceId"));//左右炉
                    code = Integer.valueOf(order.getString("code"));// 指令码
                    code1 = code;
                    if (code == 6) { // 设置/取消预约时间
                        error = Integer.valueOf(order.getString("error"));
                        success = Integer.valueOf(order.getString("success"));
                    }
                    if (LRID == 0) { // 0是左炉
                        if (NotNull.isNotNull(leftFragment)) {
                            leftFragment.setCode(code);
                            leftFragment. setMProtocol(read);
                        }
                    } else if (LRID == 1) { // 1是右炉
                        if (NotNull.isNotNull(rightFragment)) {
                            rightFragment.setCode(code);
                            rightFragment.setMProtocol(read);
                        }
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    code = Integer.valueOf(order.getString("code"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (code == -1) {
                    if (dialongView.getAlertDialog() == null) {
                        showTurnOn();
                    }
                }
                leftFragment.setCode(code);
                rightFragment.setCode(code);
            }

        }
    }

}