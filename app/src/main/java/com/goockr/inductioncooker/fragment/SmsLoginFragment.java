package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
import com.goockr.inductioncooker.models.User;
import com.goockr.inductioncooker.utils.CountDownButtonHelper;
import com.goockr.inductioncooker.utils.DensityUtil;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.inductioncooker.view.MyEditText;
import com.goockr.ui.view.helper.HudHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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

    //sms控件
    @BindView(R.id.fragment_login_sms_ll)
    LinearLayout sms_ll;
    @BindView(R.id.fragment_sms_phone_et)
    MyEditText sms_phone_et;
    @BindView(R.id.fragment_login_sms_et)
    MyEditText sms_et;
    @BindView(R.id.fragment_pwd_smscode_bt)
    Button sms_bt;
    @BindView(R.id.fragment_smslogin_bt)
    Button smslogin_bt;
    @BindView(R.id.fragment_login_moden_bt)
    Button login_moden_bt;

    //pwd控件
    @BindView(R.id.fragment_login_pwd_ll)
    LinearLayout pwd_ll;
    @BindView(R.id.fragment_pwd_phone_et)
    MyEditText phone_et;
    @BindView(R.id.fragment_login_pwd_et)
    MyEditText pwd_et;
    @BindView(R.id.fragment_pwd_forget_bt)
    Button forget_bt;
    @BindView(R.id.fragment_login_pwd_bt)
    Button pwd_bt;
    @BindView(R.id.fragment_login_smsmoden_bt)
    Button smsmoden_bt;



    public HudHelper hud=new HudHelper();

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
        sms_phone_et.setHint("手机号码");

        pwd_et.setHint("用户密码");
        //设置不可见密码
        pwd_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);//TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
        pwd_et.setBgImageVisibility(false);

    }


    private void initEvent() {


    }


    /**
     * @param v
     */
    @OnClick({R.id.navbar_left_bt,R.id.navbar_right_bt,R.id.fragment_pwd_smscode_bt,R.id.fragment_login_moden_bt,R.id.fragment_pwd_forget_bt,
    R.id.fragment_smslogin_bt,R.id.fragment_login_pwd_bt,R.id.fragment_login_smsmoden_bt})
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

            case (R.id.fragment_pwd_smscode_bt)://获取验证码
                getSmsCode();
                break;

            case (R.id.fragment_login_moden_bt)://转到密码登录页面

                pwdModen();
                break;

            case (R.id.fragment_pwd_forget_bt)://忘记密码

                forgetButtonClick();
                break;
            case (R.id.fragment_smslogin_bt)://验证码登录
                smsLogin();
                break;
            case (R.id.fragment_login_smsmoden_bt)://转到验证码登录
                smsModen();
                break;
            case (R.id.fragment_login_pwd_bt):
                pwdLogin();

                break;
        }
    }

    /**
     * 密码登录
     */
    private void pwdLogin() {

        if (moden==PwdModen&&(phone_et.getText().length()==0||pwd_et.getText().length()==0))
        {
            hud.hudShowTip(getActivity(),"请填写手机号码和密码",1500);
            return;
        }
        hud.hudShow(getActivity(),getResources().getString(R.string.logining));
        //functype=pl&mobile=13763085121&pwd=1234
        Map<String,Object> map=new HashMap<>();
        map.put("functype","pl");
        map.put("mobile",phone_et.getText().toString());
        map.put("pwd",pwd_et.getText().toString());

        HttpHelper.loginPwd(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                hud.hudUpdateAndHid(error.msg,Common.KHUDFINISHTIME);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                loginSuccess(jsonObject);
            }
        });


    }

    private void smsLogin() {

        if (moden == SmsModen &&(sms_phone_et.getText().length()==0||sms_et.getText().length()==0)) {
            hud.hudShowTip(getActivity(),"请填写手机号码和验证码",1500);
            return;
        }

        //functype=tl&mobile=13763085121&vcode=&token=64f62f5341b6455981ed456bdb95eb80&pwd=123

        hud.hudShow(getActivity(),getResources().getString(R.string.logining));
        Map<String,Object> map=new HashMap<>();
        map.put("functype","tl");
        map.put("mobile",sms_phone_et.getText().toString());
        map.put("vcode",sms_et.getText().toString());
        map.put("token","");
        map.put("pwd","");
        HttpHelper.loginSmmCode(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                hud.hudUpdateAndHid(error.msg,Common.KHUDFINISHTIME);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
              loginSuccess(jsonObject);
            }
        });
    }

    private void loginSuccess(JSONObject jsonObject) {

        dataSave(jsonObject);

        hud.hudUpdateAndHid(getResources().getString(R.string.login_success), Common.KHUDFINISHTIME, new HudHelper.SuccessCallBack() {
            @Override
            public void success() {
                getActivity().finish();
            }
        });
    }

    private void dataSave(JSONObject json) {

        JSONObject object=new JSONObject();

        String name="";
        String token="";
        String mobile="";
        String userId="";

        try {
            object=json.getJSONObject("userInfo");
            name=object.getString("name");
            token=object.getString("token");
            mobile=object.getString("mobile");
            userId=object.getString("id");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        User.getInstance().updateUserInfoWithdict(object);
        SharePreferencesUtils.setToken(token);
        SharePreferencesUtils.setUserName(name);
        SharePreferencesUtils.setMobile(mobile);
        SharePreferencesUtils.setUserID(userId);

    }


    private void pwdModen() {
        moden=PwdModen;
        title_tv.setText(R.string.login_pwd_title);
        sms_ll.setVisibility(View.INVISIBLE);
        pwd_ll.setVisibility(View.VISIBLE);


    }


    private void smsModen() {
        moden=SmsModen;
        title_tv.setText(R.string.login_sms_title);
        pwd_ll.setVisibility(View.INVISIBLE);
        sms_ll.setVisibility(View.VISIBLE);
    }


    private void forgetButtonClick() {

        VerifiedPhoneNumFragment fragment=new VerifiedPhoneNumFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("state",1);
        bundle.putInt("content",R.id.activity_login_content_fl);
        fragment.setArguments(bundle);
        FragmentHelper.addFragmentToBackStack(getActivity(),R.id.activity_login_content_fl,this,fragment,Common.VerifiedPhoneNumFragment);

    }

    private void registerButtonClick() {

        VerifiedPhoneNumFragment fragment=new VerifiedPhoneNumFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("state",0);
        bundle.putInt("content",R.id.activity_login_content_fl);
        fragment.setArguments(bundle);
        FragmentHelper.addFragmentToBackStack(getActivity(),R.id.activity_login_content_fl,this,fragment,Common.VerifiedPhoneNumFragment);


    }

    private void  getSmsCode()
    {
        if (sms_phone_et.getText().length()==0)
        {
            hud.hudShowTip(getActivity(),getResources().getString(R.string.phone_null),Common.KHUDTIPSHORTTIME);
            return;
        }


        hud.hudShow(getActivity(),getResources().getString(R.string.get_sms_code));
       // functype=vc&mobile=13763085121
        Map<String,Object> map=new HashMap<>();
        map.put("functype","vc");
        map.put("mobile",sms_phone_et.getText().toString());

        HttpHelper.getLoginSmmCode(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
               hud.hudUpdateAndHid(error.msg,Common.KHUDFINISHTIME);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {

                hud.hudHide();
                CountDownButtonHelper timer = new CountDownButtonHelper(sms_bt,
                        getResources().getString(R.string.sms_getCode), "重新获取", 60, 1);
                timer.setOnFinishListener(new CountDownButtonHelper.OnFinishListener(){
                    @Override
                    public void finish() {
                    }
                });
                timer.start();
            }
        });


    }

    private void changeModen(int moden)
    {
        sms_et.setText("");
        if (moden==SmsModen)
        {
            title_tv.setText(R.string.login_sms_title);
           // moden_bt.setText(R.string.login_sms_moden);
            forget_bt.setVisibility(View.INVISIBLE);
            sms_bt.setVisibility(View.VISIBLE);

        }else {
            title_tv.setText(R.string.login_pwd_title);
          //  moden_bt.setText(R.string.login_pwd_moden);
            sms_bt.setVisibility(View.INVISIBLE);
            forget_bt.setVisibility(View.VISIBLE);
        }
        AnimationSet animationSet = new AnimationSet(true);
        Animation alphaAnimation = new AlphaAnimation(1.0f, 0.1f);
        alphaAnimation.setDuration(500);//设置动画持续时间为500毫秒
        Animation scaleAnimation = new ScaleAnimation(0.0f, 1.5f, 0.0f, 1.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(500);//设置动画持续时间为500毫秒
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setStartOffset(0);
        scaleAnimation.setInterpolator(getActivity(), android.R.anim.decelerate_interpolator);//设置动画插入器
        contentView.startAnimation(animationSet);

    }


}
