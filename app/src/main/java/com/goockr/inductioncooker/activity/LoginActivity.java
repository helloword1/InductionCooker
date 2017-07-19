package com.goockr.inductioncooker.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.fragment.PwdLoginFragment;
import com.goockr.inductioncooker.fragment.ReservationFragment;
import com.goockr.inductioncooker.fragment.SmsLoginFragment;
import com.goockr.inductioncooker.utils.FragmentHelper;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends FragmentActivity {

    FragmentManager fragmentManager;

//    @BindView(R.id.activity_login_content_fl)
//    FrameLayout conten_fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        fragmentManager = getFragmentManager();

        initUI();

    }

    private void initUI() {

        SmsLoginFragment fragment=new SmsLoginFragment();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        fragmentTransaction.add(R.id.activity_login_content_fl,fragment, Common.PwdLoginFragment);
//        fragmentTransaction.show(fragment);
//        fragmentTransaction.addToBackStack(Common.PwdLoginFragment);
//        fragmentTransaction.commit();


        FragmentHelper.addFirstFragmentToBackStack(this,R.id.activity_login_content_fl,fragment,Common.PwdLoginFragment);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            if (fragmentManager.getBackStackEntryCount()==1)
            {
                finish();
            }
        }

        return super.onKeyDown(keyCode, event);
    }
}
