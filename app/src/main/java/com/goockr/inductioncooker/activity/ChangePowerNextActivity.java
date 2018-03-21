package com.goockr.inductioncooker.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
import com.goockr.inductioncooker.utils.CountDownButtonHelper;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.inductioncooker.view.DialogView;
import com.goockr.ui.view.helper.HudHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ChangePowerNextActivity extends BaseActivity implements View.OnClickListener {
    private android.widget.TextView powerleft;
    private android.widget.TextView powerright;
    private android.widget.TextView tvShowTips;
    private android.widget.EditText tvCode;
    private android.widget.Button tvGetCode;
    private DialogView dialogView;
    private String code;
    private String phone;
    private String tag = "";
    public HudHelper hud = new HudHelper();
    private int finish = 112;
    private String device;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(R.layout.activity_power_change_next);
        this.tvGetCode = (Button) findViewById(R.id.tvGetCode);
        this.tvCode = (EditText) findViewById(R.id.tvCode);
        this.tvShowTips = (TextView) findViewById(R.id.tvShowTips);
        this.powerright = (TextView) findViewById(R.id.power_right);
        this.powerleft = (TextView) findViewById(R.id.power_left);
        powerleft.setOnClickListener(this);
        powerright.setOnClickListener(this);
        tvGetCode.setOnClickListener(this);
        dialogView = DialogView.getSingleton();
        dialogView.setContext(this);
        initDatas();
    }

    @SuppressLint("SetTextI18n")
    @TargetApi(Build.VERSION_CODES.N)
    private void initDatas() {
        phone = getIntent().getStringExtra("phone");
        device = getIntent().getStringExtra("DEVICE_ID");
        if (NotNull.isNotNull(phone)){
            tvShowTips.setText(Html.fromHtml("请输入验证手机 <font color='#ff8212'>"+phone+"</font> 收到的短信验证码"));
        }
    }

    private void showTurnOn() {
        View view = dialogView.showCustomDialong(R.layout.dialog_power_change_succeed);
        TextView tvCommit = (TextView) view.findViewById(R.id.tvCommit);

        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismissDialong();
                ChangePowerNextActivity.this.setResult(finish);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.power_right://下一步
                if (TextUtils.equals(code, tvCode.getText().toString())) {
                    initNet();
                } else {
                    hud.hudShowTip(ChangePowerNextActivity.this, "验证码错误", 1500);
                }
                break;
            case R.id.power_left://取消
                finish();
                break;
            case R.id.tvGetCode://再次获取验证码
                initCode();
                break;
            default:
                break;
        }
    }

    //访问网络绑定
    private void initNet() {
        hud.hudShow(this, "正在加载。。。");
        Map<String, Object> map = new HashMap<>();
        map.put("functype", "tp");
        map.put("curruser", SharePreferencesUtils.getMobile());
        map.put("transfer", phone);
        map.put("devcode", device);
        map.put("token", SharePreferencesUtils.getToken());
        map.put("vcode", code);

        HttpHelper.tranferRight(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                Log.d(tag, "onFailure: " + error.msg);
            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                hud.hudHide();
                int result = 1;
                Log.d(tag, "onSuccess: " + jsonObject);
                try {
                    result = jsonObject.getInt("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 0) {//成功
                    showTurnOn();
                }

            }
        });
    }

    private void initCode() {

        hud.hudShow(this, getResources().getString(R.string.get_sms_code));
        Map<String, Object> map = new HashMap<>();
        map.put("functype", "vc");
        map.put("mobile", phone);

        HttpHelper.getLoginSmmCode(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                Log.d(tag, "onFailure: " + error.msg);
                hud.hudShow(ChangePowerNextActivity.this, error.msg);
            }
            @Override
            public void onSuccess(JSONObject jsonObject) {
                hud.hudHide();
                int result = 1;
                Log.d(tag, "onSuccess: " + jsonObject);
                try {
                    result = jsonObject.getInt("result");
                    if (result == 0) {
                        code = jsonObject.getString("code");
                        tvGetCode.setClickable(false);
                        CountDownButtonHelper timer = new CountDownButtonHelper(tvGetCode,
                                getResources().getString(R.string.sms_getCode), "重新获取", 60, 1);
                        timer.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
                            @Override
                            public void finish() {
                                tvGetCode.setClickable(true);
                            }
                        });
                        timer.start();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 0) {//成功
                    hud.hudShowTip(ChangePowerNextActivity.this, "验证码已发送到手机", 1500);
                }

            }
        });
    }
}
