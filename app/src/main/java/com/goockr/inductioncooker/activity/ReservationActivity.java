package com.goockr.inductioncooker.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.fragment.ReservationFragment;
import com.goockr.inductioncooker.utils.FragmentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReservationActivity extends BaseActivity {
    private static final String TAG = "ReservationActivity";
    public static final String KModenKey = "KModenKey";
    @BindView(R.id.activity_reservation)
    FrameLayout activityReservation;

    private int moden;
    private ReservationFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        setContentView(R.layout.activity_reservation);
        ButterKnife.bind(this);

        initData();

        initUI();


    }

    private void initData() {
        Intent intent = getIntent();
        moden = intent.getIntExtra(Common.HomeFragmentSelectIndexKey, 0);
    }

    private void initUI() {
        fragment = new ReservationFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KModenKey, moden);
        fragment.setArguments(bundle);
        FragmentHelper.addFirstFragmentToBackStack(this, R.id.activity_reservation, fragment, Common.ReservationFragment);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (activityReservation.getChildCount()==0) finish();
    }
}
