package com.goockr.inductioncooker.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.lib.http.HttpError;
import com.goockr.inductioncooker.lib.http.HttpHelper;
import com.goockr.inductioncooker.lib.http.OKHttp;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.inductioncooker.utils.StringUtils;
import com.goockr.inductioncooker.view.DialogView;
import com.goockr.ui.view.helper.HudHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangePowerActivity extends BaseActivity {
    private android.widget.TextView powerleft;
    private android.widget.TextView powerright;
    private android.widget.RelativeLayout capturenarbar;
    private android.widget.EditText tvPhone;
    private android.widget.ImageView ivDelete;
    public static final String TAG = "";
    public HudHelper hud = new HudHelper();
    private DialogView dialogView;
    private String mphone = "phone";
    private int finish = 112;
    private String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(R.layout.activity_power_change);
        this.ivDelete = (ImageView) findViewById(R.id.ivDelete);
        this.tvPhone = (EditText) findViewById(R.id.tvPhone);
        this.capturenarbar = (RelativeLayout) findViewById(R.id.capture_narbar);
        this.powerright = (TextView) findViewById(R.id.power_right);
        this.powerleft = (TextView) findViewById(R.id.power_left);
        dialogView = DialogView.getSingleton();
        dialogView.setContext(this);
        Intent intent = getIntent();
        if (NotNull.isNotNull(intent)) {
            deviceId = intent.getStringExtra("DEVICE_ID");
        }

        initDatas();
    }

    private void initDatas() {
        powerright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneStr = tvPhone.getText().toString();
                if (!NotNull.isNotNull(phoneStr) || !StringUtils.isPhone(phoneStr)) {
                    hud.hudShowTip(ChangePowerActivity.this, "请写正确的手机号码", 1500);
                    return;
                }
//                if (NotNull.isNotNull(list) && list.length() > 0) {
                    initNet(phoneStr);
//                } else {
//                    hud.hudShowTip(ChangePowerActivity.this, "暂无可用设备", 1500);
//                }

            }


        });
        powerleft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tvPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    ivDelete.setVisibility(View.VISIBLE);
                } else {
                    ivDelete.setVisibility(View.GONE);
                }
            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvPhone.setText("");
            }
        });
    }

    private void showTurnOn(String msg) {
        View view = dialogView.showCustomDialong(R.layout.dialog_power_change);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);

        tvContent.setText(msg);
        TextView tvCommit = (TextView) view.findViewById(R.id.tvCommit);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismissDialong();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogView.dismissDialong();
            }
        });

    }

    //访问网络
    private void initNet(final String phone) {
        Map<String, Object> map = new HashMap<>();
        map.put("functype", "pt");
        map.put("curruser", SharePreferencesUtils.getMobile());
        map.put("transfer", phone);
        map.put("devcode", deviceId);
        map.put("token", SharePreferencesUtils.getToken());

        HttpHelper.tranferRightReady(map, new OKHttp.HttpCallback() {
            @Override
            public void onFailure(HttpError error) {
                Log.d(TAG, "onFailure: " + error.msg);
                showTurnOn(error.msg);

            }

            @Override
            public void onSuccess(JSONObject jsonObject) {
                hud.hudHide();
                int result = 1;
                Log.d(TAG, "onSuccess: " + jsonObject);
                try {
                    result = jsonObject.getInt("result");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (result == 0) {//成功
                    startActivityForResult(new Intent(ChangePowerActivity.this, ChangePowerNextActivity.class).putExtra(mphone, phone).putExtra("DEVICE_ID", deviceId), 111);

                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == finish) {
            finish();
        }
    }
}
