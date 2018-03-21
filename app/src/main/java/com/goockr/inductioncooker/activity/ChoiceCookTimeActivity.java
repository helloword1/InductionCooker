package com.goockr.inductioncooker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.view.OptionsPickView0;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChoiceCookTimeActivity extends BaseActivity {

    @BindView(R.id.navbar_title_tv)
    TextView navbarTitleTv;
    @BindView(R.id.navbar_left_bt)
    Button navbarLeftBt;
    @BindView(R.id.navbar_right_bt)
    Button navbarRightBt;
    @BindView(R.id.fl_date)
    FrameLayout flDate;
    private OptionsPickView0 pvNoLinkOptions;
    private int hour;
    private int second;
    private int mMode;
    private int mDeviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice_time);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        mMode = intent.getIntExtra("mode", 0);
        mDeviceId = intent.getIntExtra("deviceId",0);
        initUI();
        initData();
    }


    private void initUI() {
        navbarRightBt.setText(R.string.confir);
        navbarTitleTv.setText(R.string.onetime);
    }
    private void initData() {

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */
        final ArrayList<String> options1Items = new ArrayList<>();
        final ArrayList<String> options2Items = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            options1Items.add(String.valueOf(i+" 小时"));

        }
        for (int i = 0; i < 60; i++) {
            options2Items.add(String.valueOf(i+1+" 分"));
        }

        pvNoLinkOptions = new OptionsPickView0.Builder(this, new OptionsPickView0.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                hour =options1;
                second =options2+1;
                Log.d("12313 ", "onOptionsSelect: "+options1+"   "+options2);
            }
        }).setOutSideCancelable(false).setDecorView(flDate).setBackgroundId(0x00FFFFFF).build();
        pvNoLinkOptions.setNPicker(options1Items, options2Items, null);
        //取消
        navbarLeftBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //确定
        navbarRightBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvNoLinkOptions.getData();
                Intent intent=new Intent();
                intent.putExtra("HOUR",hour);
                intent.putExtra("SECOND",second);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
        pvNoLinkOptions.show(null, false);
        pvNoLinkOptions.setKeyBackCancelable(false);
    }

}
