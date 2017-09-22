package com.goockr.inductioncooker.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.lib.socket.Protocol2;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.inductioncooker.models.BaseProtocol;
import com.goockr.inductioncooker.utils.NotNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.mode;

/**
 * Created by LJN on 2017/8/30.
 * 预约成功
 */

public class OrderTimeActivity extends BaseActivity {
    @BindView(R.id.order_title_back_iv)
    ImageView orderTitleBackIv;
    @BindView(R.id.tv_order_complete_info)
    TextView tvOrderCompleteInfo;
    @BindView(R.id.tv_cancel_order)
    TextView tvCancelOrder;
    private int mMode;
    private int deviceId;
    private final String[] modeStr = {"煲粥", "煲汤", "煮饭", "烧水", "火锅", "煎炒", "烤炸", "保温", "煎焗", "闷烧", "保温", "爆炒", "油炸", "文火"};
    private Intent intent;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_time);
        ButterKnife.bind(this);
        intent = getIntent();
        if (NotNull.isNotNull(intent))
            initData();
    }

    @OnClick((R.id.order_title_back_iv))
    public void onClick() {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void initData() {
        int hour = intent.getIntExtra("HOUR", 0);
        int minute = intent.getIntExtra("MINUTE", 0);
        String mode = intent.getStringExtra("MODE");
        Date beginTime = (Date) intent.getSerializableExtra("BEGIN_TIME");
        int hours = beginTime.getHours();
        int minutes = beginTime.getMinutes();
        String time = hours + ":" + minutes;
        if (minute != 0) {
            if (hour != 0)
                tvOrderCompleteInfo.setText(String.format("选择功能模式:  %1$s\n开机时间：%2$s\n定时：%3$s小时%4$s分钟", mode, time, String.valueOf(hour), minute));
            else
                tvOrderCompleteInfo.setText(String.format("选择功能模式:  %1$s\n开机时间：%2$s\n定时：%3$s分钟", mode, time, minute));
        } else {
            tvOrderCompleteInfo.setText(String.format("选择功能模式:  %1$s\n开机时间：%2$s", mode, time));
        }
        for (int i = 0; i < modeStr.length; i++) {
            if (TextUtils.equals(mode, modeStr[i])) {
                mMode = i;
                deviceId = i > 7 ? 1 : 0;
            }
        }
    }

    //取消预约
    @OnClick(R.id.tv_cancel_order)//
    public void onclick() {
        TcpSocket.getInstance().write(Protocol2.setReservation(deviceId, mMode, 0, 0, 0));
    }

    @Override
    protected void handleMsg(BaseProtocol mProtocol) {
        if (NotNull.isNotNull(mProtocol)) {
            JSONObject orderObject = mProtocol.getOrder();
            try {
                int code = Integer.valueOf(orderObject.getString("code"));
                if (code == 7) {
                    //查询预约返回
                    String moden = orderObject.getString("moden");
                    mMode = Integer.valueOf(moden);

                    String bootTime = orderObject.getString("bootTime");//开机时间
                    String appointment = orderObject.getString("appointment");//工作时间
                    Long aBootTime = Long.valueOf(bootTime);
                    Long aAppointment = Long.valueOf(appointment);

                    if (aBootTime != 0) {
                        Date beginTime = new Date(aAppointment);
                        int hours = beginTime.getHours();
                        int minutes = beginTime.getMinutes();
                        String time = hours + ":" + minutes;
                        if (minutes != 0) {
                            if (hours != 0)
                                tvOrderCompleteInfo.setText(String.format("选择功能模式:  %1$s\n开机时间：%2$s\n定时：%3$s小时%4$s分钟", mode, time, String.valueOf(hours), minutes));
                            else
                                tvOrderCompleteInfo.setText(String.format("选择功能模式:  %1$s\n开机时间：%2$s\n定时：%3$s分钟", mode, time, minutes));
                        } else {
                            tvOrderCompleteInfo.setText(String.format("选择功能模式:  %1$s\n开机时间：%2$s", mode, time));
                        }
                        for (int i = 0; i < modeStr.length; i++) {
                            if (TextUtils.equals(moden, modeStr[i])) {
                                mMode = i;
                                deviceId = i > 7 ? 1 : 0;
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
