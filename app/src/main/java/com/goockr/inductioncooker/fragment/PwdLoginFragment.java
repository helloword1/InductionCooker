package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.utils.CountDownButtonHelper;
import com.goockr.inductioncooker.utils.DensityUtil;
import com.goockr.inductioncooker.view.MyEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/4.
 */

public class PwdLoginFragment extends Fragment {


    View contentView;

    private FragmentManager fragmentManager;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment_pwd_login, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();


        initEvent();

        return contentView;
    }

    private void initData() {

        fragmentManager= getFragmentManager();

    }

    private void initUI() {



    }

    private void initEvent() {



    }

}
