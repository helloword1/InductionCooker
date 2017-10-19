package com.goockr.inductioncooker.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.fragment.HomeFragment1;
import com.goockr.inductioncooker.lib.socket.Protocol2;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.ui.view.helper.HudHelper;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private int lrIndex;
    private Thread thread;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_time);
        ButterKnife.bind(this);
        HandlerMSG();
    }

    private void HandlerMSG() {
        Intent intent = getIntent();
        //查询预约返回
        mMode = intent.getIntExtra("moden", -1);
        lrIndex = intent.getIntExtra("LRIndex", -1);
        long bootTime = intent.getLongExtra("bootTime", 0L);//开机时间
        long appointment = intent.getLongExtra("appointment", 0L);//工作时间
        Log.d("copycat", "日期：" + appointment);

        Date mBeginTime = new Date(bootTime);
        int hours = mBeginTime.getHours();
        int minutes = mBeginTime.getMinutes();
        String minutesStr = "" + minutes;
        if (minutes < 10) {
            minutesStr = "0" + minutes;
        }
        String time = hours + ":" + minutesStr;

        switch (mMode) {//可定时
            case 4:
            case 5:
            case 6:
            case 7:
            case 11:
            case 12:
            case 13:
                if (appointment > 0) {
                    long aHour = appointment / 3600 / 1000;
                    long aMutes = (appointment % 3600000) / 60000;
                    if (aHour > 0 && aMutes > 0) { // 说明时分都有
                        tvOrderCompleteInfo.setText(String.format("模式:  %1$s\n开机时间：%2$s\n工作时长：%3$s小时%4$s分钟", modeStr[mMode], time, String.valueOf(aHour), aMutes));
                    } else if (aHour <= 0 && aMutes > 0) { // 说明只有分
                        tvOrderCompleteInfo.setText(String.format("模式:  %1$s\n开机时间：%2$s\n工作时长：%3$s分钟", modeStr[mMode], time, aMutes));
                    }
                }
                break;
            case 0:
            case 1:
            case 2:
            case 3:
            case 8:
            case 9:
                tvOrderCompleteInfo.setText(String.format("模式:  %1$s\n开机时间：%2$s\n工作时长：自动", modeStr[mMode], time));
                break;
        }
    }

    @OnClick((R.id.order_title_back_iv))
    public void onClick() {
        finish();
    }

    @OnClick((R.id.order_title_back_iv))
    public void onClick(View v) {
        finish();
    }

    //取消预约
    @OnClick(R.id.tv_cancel_order)//
    public void onclick() {
        if (lrIndex == -1) return;
        if (bsaeHudHelper == null)
            bsaeHudHelper = new HudHelper();
        bsaeHudHelper.hudShow(this, "正在加载...");
        if (thread == null) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    final Handler handler = new Handler(Looper.getMainLooper());
                    while (!thread.isInterrupted()) {
                        SystemClock.sleep(500);
                        TcpSocket.getInstance().write(Protocol2.setReservation(lrIndex, mMode, 0, 0, 0));
                        if (HomeFragment1.code1 == 6) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    thread.interrupt();
                                    SystemClock.sleep(500);
                                    bsaeHudHelper.hudHide();
                                    if (HomeFragment1.success == 1) {
                                        Toast.makeText(OrderTimeActivity.this, "已取消预约", Toast.LENGTH_SHORT).show();
                                        handler.removeCallbacksAndMessages(null);
                                        finish();
                                    } else {
                                        Toast.makeText(OrderTimeActivity.this, "取消失败", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                    }
                }
            });
        }
        if (!thread.isAlive())
            thread.start();
    }
}
