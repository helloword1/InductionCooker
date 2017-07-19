package com.goockr.inductioncooker.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.fragment.ReservationFragment;
import com.goockr.inductioncooker.utils.FragmentHelper;

public class ReservationActivity extends FragmentActivity {

    public static final String KModenKey  = "KModenKey";

    private int moden;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        setContentView(R.layout.activity_reservation);

        initData();

        initUI();


    }

    private void initData() {

        Intent intent=getIntent();
        moden=intent.getIntExtra(Common.HomeFragmentSelectIndexKey,0);


    }

    private void initUI() {

        ReservationFragment fragment=new ReservationFragment();

        Bundle bundle=new Bundle();
        bundle.putInt(KModenKey,moden);
        fragment.setArguments(bundle);

//        FragmentManager fragmentManager= getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.add(R.id.activity_reservation,fragment, Common.ReservationFragment);
//
//        fragmentTransaction.show(fragment);
//
//        fragmentTransaction.addToBackStack(Common.ReservationFragment);
//
//        fragmentTransaction.commit();

        FragmentHelper.addFirstFragmentToBackStack(this,R.id.activity_reservation,fragment,Common.ReservationFragment);

    }
}
