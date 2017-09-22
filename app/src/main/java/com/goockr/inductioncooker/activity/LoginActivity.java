package com.goockr.inductioncooker.activity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.fragment.SmsLoginFragment;
import com.goockr.inductioncooker.utils.FragmentHelper;

import butterknife.ButterKnife;

public class LoginActivity extends BaseActivity {

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


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
            return super.dispatchTouchEvent(ev);
        } // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    public  boolean isShouldHideInput(View v, MotionEvent event) {

        if (v != null && (v instanceof EditText)) {//instanceof 判断对象是否是一个类
            int[] leftTop = { 0, 0 }; //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
          //  Log.v("isShouldHideInput","top:"+top+"\neventy:"+event.getY()+"\nbottom:"+bottom+"\nleft:"+left+"\neventx:"+event.getX()+"\nright:"+right);

            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }






}
