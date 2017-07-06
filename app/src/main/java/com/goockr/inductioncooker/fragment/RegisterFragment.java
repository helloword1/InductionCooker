package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.goockr.inductioncooker.R;

import butterknife.ButterKnife;

/**
 * Created by CMQ on 2017/7/5.
 */

public class RegisterFragment extends Fragment {

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
