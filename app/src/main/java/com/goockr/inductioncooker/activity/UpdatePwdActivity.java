package com.goockr.inductioncooker.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.fragment.ReservationFragment;
import com.goockr.inductioncooker.fragment.UpdatePwdFragment;
import com.goockr.inductioncooker.utils.FragmentHelper;

public class UpdatePwdActivity extends BaseActivity {

    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        setContentView(R.layout.activity_update_pwd);

        fragmentManager = getFragmentManager();

        UpdatePwdFragment fragment=new UpdatePwdFragment();

        FragmentHelper.addFirstFragmentToBackStack(this,R.id.activity_update_pwd,fragment, Common.ReservationFragment);

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
