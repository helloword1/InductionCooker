package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.inductioncooker.MyApplication;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.utils.CountDownButtonHelper;
import com.goockr.inductioncooker.utils.DensityUtil;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.MyEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/4.
 */

public class SmsLoginFragment extends Fragment {

    private static final int SmsModen  = 0;

    private static final int PwdModen  = 1;

    View contentView;

    private int moden;




    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_pwd_phone_et)
    MyEditText phone_et;
    @BindView(R.id.fragment_pwd_sms_et)
    MyEditText sms_et;
    @BindView(R.id.fragment_pwd_smscode_bt)
    Button sms_bt;
    @BindView(R.id.fragment_login_moden_bt)
    Button moden_bt;

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

        moden=SmsModen;

    }

    private void initUI() {

        title_tv.setText(R.string.login_sms_title);
        left_bt.setText("");
        Drawable drawable= getResources().getDrawable(R.drawable.title_back);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(DensityUtil.px2dip(getActivity(),10), 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        left_bt.setCompoundDrawables(drawable,null,null,null);

        right_bt.setText("注册");

        phone_et.setInputType(InputType.TYPE_CLASS_PHONE);
        phone_et.setHint("手机号码");

        sms_et.setInputType(InputType.TYPE_CLASS_PHONE);
        sms_et.setBgImageVisibility(false);
        sms_et.setHint("动态密码");

    }


    private void initEvent() {


    }


    /**
     * @param v
     */
    @OnClick({R.id.navbar_left_bt,R.id.navbar_right_bt,R.id.fragment_pwd_smscode_bt,R.id.fragment_login_moden_bt})
    public void OnClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.navbar_left_bt):

                FragmentHelper.pop(getActivity());
                getActivity().finish();

                break;
            case (R.id.navbar_right_bt):
                registerButtonClick();
                break;

            case (R.id.fragment_pwd_smscode_bt):
                getSmsCode();
                break;

            case (R.id.fragment_login_moden_bt):

                moden=(moden==SmsModen)?PwdModen:SmsModen;
                changeModen(moden);

                break;
        }
    }

    private void registerButtonClick() {

        VerifiedPhoneNumFragment fragment=new VerifiedPhoneNumFragment();

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.add(R.id.activity_login_content_fl,fragment, Common.VerifiedPhoneNumFragment);
//
//        fragmentTransaction.hide(this);
//
//        fragmentTransaction.show(fragment);
//
//        fragmentTransaction.addToBackStack(Common.VerifiedPhoneNumFragment);
//
//        fragmentTransaction.commit();

        FragmentHelper.addFragmentToBackStack(getActivity(),R.id.activity_login_content_fl,this,fragment,Common.VerifiedPhoneNumFragment);


    }

    private void  getSmsCode()
    {
        CountDownButtonHelper timer = new CountDownButtonHelper(sms_bt,
                "获取验证码", "重新获取", 60, 1);
        timer.setOnFinishListener(new CountDownButtonHelper.OnFinishListener(){
            @Override
            public void finish() {

            }
        });
        timer.start();
    }

    private void changeModen(int moden)
    {

//        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
//
//        int width = wm.getDefaultDisplay().getWidth();
//        int height = wm.getDefaultDisplay().getHeight();
//        TranslateAnimation tAnim;
        if (moden==SmsModen)
        {
            title_tv.setText(R.string.login_sms_title);
            sms_bt.setText(R.string.login_sms_getCode);
            moden_bt.setText(R.string.login_sms_moden);
           // tAnim = new TranslateAnimation(0,-1*width, 0, 0);

        }else {
            title_tv.setText(R.string.login_pwd_title);
            sms_bt.setText(R.string.login_pwd_forgetPwd);
            moden_bt.setText(R.string.login_pwd_moden);
           // tAnim = new TranslateAnimation(0,1*width, 0, 0);

        }


        AnimationSet animationSet = new AnimationSet(true);

        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
        alphaAnimation.setDuration(500);//设置动画持续时间为500毫秒

        Animation scaleAnimation = new ScaleAnimation(0.0f, 1.5f, 0.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);//设置动画持续时间为500毫秒
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setStartOffset(0);
        scaleAnimation.setInterpolator(getActivity(), android.R.anim.decelerate_interpolator);//设置动画插入器



      //  animationSet.addAnimation(alphaAnimation);
      //  animationSet.addAnimation(scaleAnimation);

        contentView.startAnimation(animationSet);

//        tAnim.setDuration(2000);
//        contentView.startAnimation(tAnim);


    }


}
