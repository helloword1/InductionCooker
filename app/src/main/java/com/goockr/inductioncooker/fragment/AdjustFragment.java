package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.view.ProgressView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/28.
 */

public class AdjustFragment extends Fragment {


    View contentView;

    @BindView(R.id.fragment_adjust_pv)
    ProgressView progressView;
    @BindView(R.id.fragment_adjust_reservation_ib)
    ImageButton reservation_bt;
    @BindView(R.id.fragment_adjust_unreservation_ib)
    ImageButton unreservation_ib;
    @BindView(R.id.fragment_adjust_lower_ib)
    ImageButton lower_ib;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_adjust, container, false);

        ButterKnife.bind(this, contentView);

        initUI();

        initEvent();

        return contentView;
    }

    private void initUI() {

        progressView.setMaxCount(100.0f);

        progressView.setCurrentCount(50);


    }

    private void initEvent() {


    }

    @OnClick({R.id.fragment_adjust_reservation_ib,R.id.fragment_adjust_unreservation_ib,R.id.fragment_adjust_lower_ib})
    public void onClick(View v) {

        switch (v.getId())
        {
            case (R.id.fragment_adjust_reservation_ib):



                break;

            case (R.id.fragment_adjust_unreservation_ib):


                break;

            case (R.id.fragment_adjust_lower_ib):

                break;

        }

    }

}
