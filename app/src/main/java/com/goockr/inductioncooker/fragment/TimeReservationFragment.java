package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.BarProgress;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/4.
 */

public class TimeReservationFragment extends Fragment {

    View  contentView;

   // private FragmentManager fragmentManager;

    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_timereservation_bar_pv)
    BarProgress bar_pv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment_time_reservation, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();


        initEvent();

        return contentView;
    }

    private void initUI() {

        List<String> tips=new ArrayList<String>();
        tips.add("1.选择功能模式");
        tips.add("2.预约开机时间");
        tips.add("3.预约定时时间");
        bar_pv.setTips(tips);
        bar_pv.setMaxCount(3);
        bar_pv.setProgress(3);

    }

    private void initData() {
        //fragmentManager= getFragmentManager();
    }

    private void initEvent() {


    }

    @OnClick({R.id.navbar_left_bt,R.id.navbar_right_bt})
    public void  OnClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.navbar_right_bt):



                break;
            case (R.id.navbar_left_bt):
               // fragmentManager.popBackStack();
                FragmentHelper.pop(getActivity());
                break;
        }
    }

}
