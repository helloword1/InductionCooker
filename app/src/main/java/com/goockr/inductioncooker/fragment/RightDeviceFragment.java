package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.ChoiceCookTimeActivity;
import com.goockr.inductioncooker.activity.OrderTimeActivity;
import com.goockr.inductioncooker.activity.ReservationActivity;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.common.CommonBean;
import com.goockr.inductioncooker.lib.socket.Protocol2;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.inductioncooker.models.BaseProtocol;
import com.goockr.inductioncooker.models.Moden;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.ReadAdssetsJson;
import com.goockr.inductioncooker.view.DialongView;
import com.goockr.inductioncooker.view.ImageTopButton;
import com.goockr.inductioncooker.view.ProgressView;
import com.goockr.ui.view.RingRoundProgressView;
import com.goockr.ui.view.helper.HudHelper;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_FIRST_USER;
import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;
import static com.goockr.inductioncooker.R.id.fragment_rightdevice_bt0;

/**
 * Created by CMQ on 2017/6/27.
 */

public class RightDeviceFragment extends Fragment implements ImageTopButton.ImageTopButtonOnClickListener,View.OnClickListener {
    private static final int REVER_DEX = 123;
    FragmentManager fragmentManager;
    @BindView(R.id.flAdjust)
    RelativeLayout flAdjust;
    View contentView;
    private int mode = -1;
    List<ImageTopButton> buttons;

    @BindView(R.id.fragment_rightdevice_power_bt)
    ImageTopButton power_bt;
    @BindView(R.id.fragment_rightdevice_reservation_bt)
    ImageTopButton reservation_bt;
    @BindView(R.id.fragment_rightdevice_unreservation_bt)
    ImageTopButton unreservation_bt;
    @BindView(fragment_rightdevice_bt0)
    ImageTopButton bt_0;
    @BindView(R.id.fragment_rightdevice_bt1)
    ImageTopButton bt_1;
    @BindView(R.id.fragment_rightdevice_bt2)
    ImageTopButton bt_2;
    @BindView(R.id.fragment_rightdevice_bt3)
    ImageTopButton bt_3;
    @BindView(R.id.fragment_rightdevice_bt4)
    ImageTopButton bt_4;
    @BindView(R.id.fragment_rightdevice_bt5)
    ImageTopButton bt_5;
    @BindView(R.id.fragment_rightdevice_bottomview)
    LinearLayout bottom_ll;
    private int rightTime;
    private final String[] modeStr = {"煲粥", "煲汤", "煮饭", "烧水", "火锅", "煎炒", "烤炸", "保温", "煎焗", "闷烧", "爆炒", "油炸", "文火"};
    private final int TIME_COOK = 1;
    private final int TIME_OUT = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_COOK:
                    tvData.setText(hourToTime(rightTime));
                    if (rightSumTime != 0) {
                        int currentCount = (rightTime * 100) / rightSumTime;
                        progressView.setCurrentCount(currentCount);
                    }
                    //表示闷烧
                    if (mode == 9) {
                        int minute = rightTime / 60;
                        if (minute < 3) {
                            tvPower.setText("1400W");
                        } else if (minute < 18) {
                            tvPower.setText("1200W");
                        } else if (minute < 28) {
                            tvPower.setText("200W");
                        }
                    }
                    break;
                case TIME_OUT:
                    try {
                        Toast.makeText(getActivity(), "时间到", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                    }
                    break;
            }
        }
    };
    private boolean RightPowerOnOff;
    private int rightSumTime;
    private ImageButton lower_ib;
    private TextView tvMode;
    private TextView tvRightTemperature;
    private TextView tvData;
    private ProgressView progressView;
    private ImageView reservation_btn;
    private ImageView unreservation_ib;
    private TextView tvPower;
    private ImageView tvALine;
    private TextView tvTemperature;
    private RingRoundProgressView ring_pv;
    private ImageView reduce_ib;
    private ImageView plus_ib;
    private Thread rightThread;
    private List<Moden> modenList;
    private com.goockr.inductioncooker.models.BaseProtocol MProtocol;
    private ImageView reservationPon;//红点
    private final int POWER_OFF = 0;
    private final int POWER_ON = 1;
    private String RPower;
    private int stall;
    ImageTopButton select_bt_r;
    private boolean isReverBl = false;
    private static String effectStr0;
    private int code;
    private boolean clickRever = false;
    private Thread thread;
    private HudHelper bsaeHudHelper;
    private Runnable target;
    private Runnable runnable;
    private int Sum = 0;
    private int sum1 = 0;
    private int mMode = -1;
    private int touTime = 0;
    private int touTime1 = 0;
    private DialongView dialongView;

    private String hourToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        if (time <= 0)
            return "00:00";
        else {
            hour = time / 3600;
            if (hour == 0) {
                minute = time / 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute);
            } else {
                if (hour > 99)
                    return "99:59";
                minute = (time - hour * 3600) / 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute);
            }
        }
        return timeStr;
    }

    private String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // Inflate the layout for this fragment
        contentView = inflater.inflate(R.layout.fragment_rightdevice, container, false);
        reservationPon = ((ImageView) contentView.findViewById(R.id.reservationPon));
        ButterKnife.bind(this, contentView);
        initData();
        initUI();
        initEvent();
        TcpSocket.getInstance().write(Protocol2.getReservationStatus(1));
        return contentView;
    }

    private void initData() {

        List<Moden> modenList = new ArrayList<Moden>();

        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = ReadAdssetsJson.readJson("rightdevice").getJSONArray("value");
            for (int i = 0; i < jsonArray.length(); i++) {
                Moden moden = Moden.moden(jsonArray.get(i));
                modenList.add(moden);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        buttons = new ArrayList<ImageTopButton>();

        buttons.add(bt_0);
        buttons.add(bt_1);
        buttons.add(bt_2);
        buttons.add(bt_3);
        buttons.add(bt_4);
        buttons.add(bt_5);

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).moden = modenList.get(i);
        }

    }

    private void initUI() {

        power_bt.setNormImageId(R.mipmap.btn_openkey_normal);
        power_bt.setHightLightImageId(R.mipmap.btn_openkey_pressed);
        power_bt.setSelImageId(R.mipmap.btn_openkey_selected);
        power_bt.setNormTextCoclor(R.color.colorBlack);
        power_bt.setText("开关机");

        reservation_bt.setNormImageId(R.mipmap.btn_reservation_normal);
        reservation_bt.setSelImageId(R.mipmap.btn_reservation_pressed);
        reservation_bt.setDisabledImageId(R.mipmap.btn_reservation_disabled);
        reservation_bt.setNormTextCoclor(R.color.colorBlack);
        reservation_bt.setText("预约");

        unreservation_bt.setNormImageId(R.mipmap.btn_cancel_normal);
        unreservation_bt.setSelImageId(R.mipmap.btn_cancel_pressed);
        unreservation_bt.setDisabledImageId(R.mipmap.btn_cancel_disabled);
        unreservation_bt.setNormTextCoclor(R.color.colorBlack);
        unreservation_bt.setText("取消预约");

        reservation_bt.buttonOnClickListener(this);
        unreservation_bt.buttonOnClickListener(this);
        power_bt.buttonOnClickListener(this);

        setButtonType(bt_0, R.mipmap.btn_baked_normal, R.mipmap.btn_baked_selected, R.mipmap.btn_baked_disabled, R.color.colorGrayText, "煎焗");
        setButtonType(bt_1, R.mipmap.btn_stew_normal, R.mipmap.btn_stew_selected, R.mipmap.btn_stew_disabled, R.color.colorGrayText, "焖烧");
        setButtonType(bt_2, R.mipmap.btn_r_temperature_normal, R.mipmap.btn_r_temperature_selected, R.mipmap.btn_r_temperature_disabled, R.color.colorGrayText, "保温");
        setButtonType(bt_3, R.mipmap.btn_quicyfry_normal, R.mipmap.btn_quicyfry_selected, R.mipmap.btn_quicyfry_disabled, R.color.colorGrayText, "爆炒");
        setButtonType(bt_4, R.mipmap.btn_fried_normal, R.mipmap.btn_fried_selected, R.mipmap.btn_fried_disabled, R.color.colorGrayText, "油炸");
        setButtonType(bt_5, R.mipmap.btn_slowfire_normal, R.mipmap.btn_slowfire_selected, R.mipmap.btn_slowfire_disabled, R.color.colorGrayText, "文火");
        flAdjust.addView(getAdView());
        dialongView = new DialongView(getActivity());
        setButtonStatus(false);//关机状态
    }
    private void setButtonStatus(boolean bl){
        bt_0.setEnabledStatus(bl);
        bt_1.setEnabledStatus(bl);
        bt_2.setEnabledStatus(bl);
        bt_3.setEnabledStatus(bl);
        bt_4.setEnabledStatus(bl);
        bt_5.setEnabledStatus(bl);
    }
    private View getAdView() {
        View adjustView = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_adjust, null);
        plus_ib = (ImageView) adjustView.findViewById(R.id.fragment_adjust_plus_ib);
        reduce_ib = (ImageView) adjustView.findViewById(R.id.fragment_adjust_reduce_ib);
        ring_pv = (RingRoundProgressView) adjustView.findViewById(R.id.fragment_adjust_round_pv);
        tvTemperature = (TextView) adjustView.findViewById(R.id.fragment_adjust_temperature_tv);
        tvALine = (ImageView) adjustView.findViewById(R.id.fragment_adjust_line_iv);
        tvPower = (TextView) adjustView.findViewById(R.id.fragment_adjust_power_tv);
        unreservation_ib = (ImageView) adjustView.findViewById(R.id.fragment_adjust_unreservation_ib);
        reservation_btn = (ImageView) adjustView.findViewById(R.id.fragment_adjust_reservation_ib);
        progressView = (ProgressView) adjustView.findViewById(R.id.fragment_adjust_pv);
        tvData = (TextView) adjustView.findViewById(R.id.fragment_adjust_date_tv);
        tvRightTemperature = (TextView) adjustView.findViewById(R.id.right_temperature_tv);
        tvMode = (TextView) adjustView.findViewById(R.id.fragment_adjust_moden_tv);
        lower_ib = (ImageButton) adjustView.findViewById(R.id.fragment_adjust_lower_ib);
        reservation_btn.setOnClickListener(this);
        unreservation_ib.setOnClickListener(this);
        lower_ib.setOnClickListener(this);
        reduce_ib.setOnClickListener(this);
        plus_ib.setOnClickListener(this);
        return adjustView;
    }

    private void initEvent() {

        for (ImageTopButton bt : buttons) {

            bt.setClickable(true);
            bt.buttonOnClickListener(this);

        }

    }

    private void setButtonType(ImageTopButton bt, int normImageId, int selImageId, int disableImageId, int normTextColor, String text) {
        bt.setNormImageId(normImageId);
        bt.setSelImageId(selImageId);
        bt.setDisabledImageId(disableImageId);
        bt.setNormTextCoclor(normTextColor);
        bt.setText(text);
    }


    @OnClick(R.id.fragment_rightdevice_bottomview)
    public void OnClick(View v) {
        switch (v.getId()) {
            case (R.id.fragment_rightdevice_bottomview):
                flAdjust.setVisibility(View.VISIBLE);
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
                adjustRightData();
                break;
        }
    }


    @Override
    public void imageTopButtonOnClickListener(ImageTopButton button) {
        if (code < 0) {
            button.setEnabledStatus(false);
//            Toast.makeText(getActivity(), "请打开电磁炉", Toast.LENGTH_SHORT).show();
            showTurnOn();
            return;
        }
        int id = button.getId();
        switch (id) {
            case R.id.fragment_rightdevice_power_bt://电源按钮
                if (RightPowerOnOff) {//关机
                    TcpSocket.getInstance().write(Protocol2.powerStatus(1, POWER_OFF));
                } else {//开机
                    TcpSocket.getInstance().write(Protocol2.powerStatus(1, POWER_ON));
                }
                return;
            case R.id.fragment_rightdevice_reservation_bt://预约
                if (!isReverBl) {
                    Intent intent = new Intent(getActivity(), ReservationActivity.class);
                    intent.putExtra(Common.HomeFragmentSelectIndexKey, 1);
                    startActivityForResult(intent, REVER_DEX);
                } else {
                    if (bsaeHudHelper == null)
                        bsaeHudHelper = new HudHelper();
                    bsaeHudHelper.hudShow(getActivity(), "正在加载...");
                    clickRever = true;
                    if (thread == null) {
                        //右边返回还有错
                        runnable = new Runnable() {
                            @Override
                            public void run() {
                                while (!Thread.currentThread().isInterrupted() && touTime < 10) {
                                    SystemClock.sleep(500);//右边返回还有错
                                    touTime++;
                                    TcpSocket.getInstance().write(Protocol2.getReservationStatus(1));
                                }
                            }
                        };
                        thread = new Thread(runnable);
                        thread.start();
                    }
                    if (!thread.isAlive() && Sum < 4) {
                        clickRever = false;
                        Sum++;
                        if (runnable != null) {
                            thread = new Thread(runnable);
                            thread.start();
                        }
                    }
//                    Intent intent = new Intent(getActivity(), OrderTimeActivity.class);
//                    intent.putExtra("LRIndex", 1);
//                    startActivity(intent);
                }
                return;
            case R.id.fragment_rightdevice_unreservation_bt://取消预约
                if (!isReverBl) return;
                if (bsaeHudHelper == null)
                    bsaeHudHelper = new HudHelper();
                bsaeHudHelper.hudShow(getActivity(), "正在加载...");
                if (thread == null) {
                    target = new Runnable() {
                        @Override
                        public void run() {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            while (!thread.isInterrupted() && touTime1 < 10) {
                                SystemClock.sleep(500);
                                touTime1++;
                                TcpSocket.getInstance().write(Protocol2.setReservation(1, mode, 0, 0, 0));
                                if (HomeFragment1.code1 == 6) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            thread.interrupt();
                                            SystemClock.sleep(500);
                                            bsaeHudHelper.hudHide();
                                            if (HomeFragment1.success == 1) {
                                                Toast.makeText(getActivity(), "已取消预约", Toast.LENGTH_SHORT).show();
                                                handler.removeCallbacksAndMessages(null);
                                            } else {
                                                Toast.makeText(getActivity(), "取消失败", Toast.LENGTH_SHORT).show();
                                            }
                                            if (thread != null) {//清空线程
                                                thread.interrupt();
                                                thread = null;
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    };
                    thread = new Thread(target);
                    thread.start();
                }
                if (!thread.isAlive() && sum1 < 4) {
                    sum1++;
                    if (target != null) {
                        thread = new Thread(target);
                        thread.start();
                    }
                }
                return;
        }
        if (RightPowerOnOff) {
            switch (id) {
                case (fragment_rightdevice_bt0)://煎焗
                    mode = 8;
                    break;
                case (R.id.fragment_rightdevice_bt1)://闷烧
                    mode = 9;
                    break;
                case (R.id.fragment_rightdevice_bt2)://保温
                    mode = 7;
                    break;
                case (R.id.fragment_rightdevice_bt3)://爆炒
                    mode = 10;
                    break;
                case (R.id.fragment_rightdevice_bt4)://油炸
                    mode = 11;
                    break;
                case (R.id.fragment_rightdevice_bt5)://文火
                    mode = 12;
                    break;
            }
            if (mMode != mode) {
                TcpSocket.getInstance().write(Protocol2.moden(1, mode));
//                if (NotNull.isNotNull(bsaeHudHelper)){
//                    bsaeHudHelper.hudHide();
//                }
//                bsaeHudHelper = new HudHelper();
//                bsaeHudHelper.hudShow(getActivity(),"正在加载...");
            } else {
//                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
//                flAdjust.setVisibility(View.VISIBLE);
            }

            if (select_bt_r != null) select_bt_r.setSelect(false);
            button.setSelect(true);
            select_bt_r = button;
//            adjustRightData();

//            flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
//            flAdjust.setVisibility(View.VISIBLE);
        } else {
//            Toast.makeText(getActivity(), "请先开机", Toast.LENGTH_SHORT).show();
            button.setEnabledStatus(false);
            showTurnOn();
        }

    }

    private void adjustRightData() {
        progressView.setMaxCount(100.0f);
        progressView.setCurrentCount(100);
        ring_pv.setProgress(1);
        ring_pv.setMaxCount(12);
        ring_pv.setStartAngle(120);
        ring_pv.setSweepAngle(300);
        ring_pv.reload();
        if (mode != -1) {
            tvMode.setText(modeStr[mode]);
        } else {
            tvMode.setText("");
        }
        switch (mode) {
            case 8://煎焗
                stall=4;
                cookMode8();
                tvPower.setText("1000W");
                rightTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(rightTime));
                rightSumTime = rightTime;
                break;
            case 9://闷烧
                cookMode9();
                break;
            case 7://保温
                cookMode7();
                break;
            case 10://爆炒
                stall=10;
                cookMode10();
                tvTemperature.setText("280℃");
                tvPower.setText("2000W");
                rightTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(rightTime));
                rightSumTime = rightTime;
                break;
            case 11://油炸
                stall=3;
                cookMode11();
                tvTemperature.setText("260℃");
                tvRightTemperature.setText("80℃");
                tvPower.setText("1800W");
                rightTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(rightTime));
                rightSumTime = rightTime;
                break;
            case 12://文火
                stall=5;
                cookMode12();
                tvPower.setText("1200W");
                rightTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(rightTime));
                rightSumTime = rightTime;
                break;
        }
        if (!NotNull.isNotNull(rightThread) || !rightThread.isAlive()) {
            rightThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (rightTime > 0) {
                        SystemClock.sleep(1000);
                        rightTime--;
                        handler.sendEmptyMessage(TIME_COOK);
                    }
                    handler.sendEmptyMessage(TIME_OUT);
                }
            });
            rightThread.start();
        }
    }

    //文火
    private void cookMode12() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(stall+1);
        ring_pv.setMaxCount(6);
    }

    //油炸
    private void cookMode11() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvRightTemperature.setVisibility(View.VISIBLE);
        ring_pv.setProgress(stall+1);
        ring_pv.setMaxCount(5);
    }

    //爆炒
    private void cookMode10() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(stall+1);
        ring_pv.setMaxCount(12);
    }

    //保温
    private void cookMode7() {
        unreservation_ib.setVisibility(View.GONE);
        reduce_ib.setVisibility(View.GONE);
        plus_ib.setVisibility(View.GONE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(2);
        ring_pv.setMaxCount(2);
        tvTemperature.setText("80℃");
        tvPower.setText("100w");
        rightTime = 2 * 3600;
        tvData.setText(hourToTime(rightTime));
        rightSumTime = rightTime;
    }

    //闷烧
    private void cookMode9() {
        unreservation_ib.setVisibility(View.GONE);
        reduce_ib.setVisibility(View.GONE);
        plus_ib.setVisibility(View.GONE);
        reservation_btn.setVisibility(View.GONE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(5);
        ring_pv.setMaxCount(6);
        tvPower.setText("1400w");
        rightTime = 28 * 60;
        tvData.setText(hourToTime(rightTime));
        rightSumTime = rightTime;
    }

    //煎焗
    private void cookMode8() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(stall+1);
        ring_pv.setMaxCount(6);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_FIRST_USER) {
            if (data == null) return;
            int hour = data.getIntExtra("HOUR", -1);
            int second = data.getIntExtra("SECOND", -1);
            rightTime = hour * 3600 + second * 60;
            rightSumTime = rightTime;
            tvData.setText(hourToTime(rightTime));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.fragment_adjust_reservation_ib)://定时
                startActivityForResult(new Intent(getActivity(), ChoiceCookTimeActivity.class), RESULT_FIRST_USER);
                break;

            case (R.id.fragment_adjust_unreservation_ib)://取消定时，关闭
                rightTime = 1;
                break;
            case (R.id.fragment_adjust_lower_ib):
                flAdjust.setVisibility(View.INVISIBLE);
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));


                break;
            case (R.id.fragment_adjust_reduce_ib):
                reducePower();
                break;
            case (R.id.fragment_adjust_plus_ib):
                plusPower();
                break;

        }

    }

    private void reducePower() {
        /*if (ring_pv.progress <= 1) {
            ring_pv.setProgress(1);
        } else {
            ring_pv.setProgress(ring_pv.progress - 1);
        }
        if (mode == 4) {//火锅
            tvPower.setText(CommonBean.HOTPOTSTRS[ring_pv.progress - 1]);
        } else if (mode == 5) {//煎炒
            tvPower.setText(CommonBean.FRYOIlSTRS1[ring_pv.progress - 1]);
            tvTemperature.setText(CommonBean.FRYOIlSTRS2[ring_pv.progress - 1]);
        } else if (mode == 6) {//烤炸
            tvPower.setText(CommonBean.KAOZA1[ring_pv.progress - 1]);
            tvTemperature.setText(CommonBean.KAOZA2[ring_pv.progress - 1]);
        } else if (mode == 8) {//煎焗
            tvPower.setText(CommonBean.JIANJU[ring_pv.progress - 1]);
        } else if (mode == 11) {//爆炒
            tvPower.setText(CommonBean.BAOCHAO1[ring_pv.progress - 1]);
            tvTemperature.setText(CommonBean.BAOCHAO2[ring_pv.progress - 1]);
        } else if (mode == 12) {//油炸
            tvPower.setText(CommonBean.YOUZA1[ring_pv.progress - 1]);
            tvTemperature.setText(CommonBean.YOUZA2[ring_pv.progress - 1]);
        } else if (mode == 13) {//文火
            tvPower.setText(CommonBean.WENHUO[ring_pv.progress - 1]);
        }*/
        if (stall < 2)
            TcpSocket.getInstance().write(Protocol2.stall(1, 0));
        else
            TcpSocket.getInstance().write(Protocol2.stall(1, stall - 1));
    }

    private void plusPower() {
        /*if (ring_pv.progress > ring_pv.maxCount) {
            ring_pv.setProgress(ring_pv.maxCount);
        } else {
            ring_pv.setProgress(ring_pv.progress + 1);
        }
        if (mode == 4) {//火锅
            tvPower.setText(CommonBean.HOTPOTSTRS[ring_pv.progress - 1]);
        } else if (mode == 5) {//煎炒
            tvPower.setText(CommonBean.FRYOIlSTRS1[ring_pv.progress - 1]);
            tvTemperature.setText(CommonBean.FRYOIlSTRS2[ring_pv.progress - 1]);
        } else if (mode == 6) {//烤炸
            tvPower.setText(CommonBean.KAOZA1[ring_pv.progress - 1]);
            tvTemperature.setText(CommonBean.KAOZA2[ring_pv.progress - 1]);
        } else if (mode == 8) {//煎焗
            tvPower.setText(CommonBean.JIANJU[ring_pv.progress - 1]);
        } else if (mode == 11) {//爆炒
            tvPower.setText(CommonBean.BAOCHAO1[ring_pv.progress - 1]);
            tvTemperature.setText(CommonBean.BAOCHAO2[ring_pv.progress - 1]);
        } else if (mode == 12) {//油炸
            tvPower.setText(CommonBean.YOUZA1[ring_pv.progress - 1]);
            tvTemperature.setText(CommonBean.YOUZA2[ring_pv.progress - 1]);
        } else if (mode == 13) {//文火
            tvPower.setText(CommonBean.WENHUO[ring_pv.progress - 1]);
        }*/
        if (stall > ring_pv.maxCount)
            TcpSocket.getInstance().write(Protocol2.stall(1, ring_pv.maxCount));
        else
            TcpSocket.getInstance().write(Protocol2.stall(1, stall + 1));
    }

    public void setMProtocol(String read) {

        //过滤重复字符串
        if (TextUtils.equals(effectStr0, read)) {
            return;
        }
        effectStr0 = read;

        try {
            JSONObject object = new JSONObject(read);
            JSONObject orderObject = object.getJSONObject("order");
            MProtocol = new Gson().fromJson(read, BaseProtocol.class);
            if (NotNull.isNotNull(orderObject))
                MProtocol.setOrder(orderObject);
            if (NotNull.isNotNull(MProtocol)) {
                int code = Integer.valueOf(orderObject.getString("code"));
                String moden = null;
                switch (code) {
                    case 0://主机接收指令更新状态
                        TcpSocket.getInstance().write(Protocol2.statusCheck(0, 1));
                        break;
                    case 1://控制主机机返回
                        //右炉开关状态
                        String rightPower = orderObject.getString("power");
                       /* if (TextUtils.equals(rightPower, RPower)) {
                            return;
                        }*/
                        RPower = rightPower;
                        if (TextUtils.equals(rightPower, "1")) {//开机
                            RightPowerOnOff = true;
                            power_bt.setSelect(true);
                            bottom_ll.setVisibility(View.VISIBLE);
                            setButtonStatus(true);//关机状态
//                            if (flAdjust.getVisibility() != View.VISIBLE) {
//                                if (NotNull.isNotNull(bsaeHudHelper)){
//                                    bsaeHudHelper.hudHide();
//                                }
//                                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
//                                flAdjust.setVisibility(View.VISIBLE);
//                            }

                            moden = orderObject.getString("moden");
                            mode = Integer.valueOf(moden);
                            mMode = mode;
                            stall = Integer.valueOf(orderObject.getString("stall"));
                            ring_pv.setProgress(Integer.valueOf(orderObject.getString("stall")) + 1);

                            if (mode == 8) {//煎焗
                                tvPower.setText(CommonBean.JIANJU[stall]);
                            } else if (mode == 10) {//爆炒
                                tvPower.setText(CommonBean.BAOCHAO1[stall]);
                                tvTemperature.setText(CommonBean.BAOCHAO2[stall]);
                            } else if (mode == 11) {//油炸
                                tvPower.setText(CommonBean.YOUZA1[stall]);
                                tvTemperature.setText(CommonBean.YOUZA2[stall]);
                            } else if (mode == 12) {//文火
                                tvPower.setText(CommonBean.WENHUO[stall]);
                            }
                            changeRightMode(moden, orderObject);

                        } else {
                            RightPowerOnOff = false;
                            power_bt.setSelect(false);
                            setButtonStatus(false);//关机状态
                            bottom_ll.setVisibility(View.INVISIBLE);
                            if (NotNull.isNotNull(select_bt_r))
                                select_bt_r.setSelect(false);
                            if (flAdjust.getVisibility() == View.VISIBLE) {
                                flAdjust.setVisibility(View.INVISIBLE);
                                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
                            }
                        }

                        if (Boolean.valueOf(orderObject.getString("reservation"))) {
                            isReverBl = true;
                            reservationPon.setVisibility(View.VISIBLE);
                            reservation_bt.setSelect(true);
                        } else {
                            reservationPon.setVisibility(View.GONE);
                            reservation_bt.setSelect(false);
                            isReverBl = false;
                        }

                        break;
                    case 2://开关机返回
                        String power = orderObject.getString("power");
                        if (NotNull.isNotNull(power) && TextUtils.equals(power, "1")) {//开机
                            String error = orderObject.getString("error");
                            if (!TextUtils.equals(error, "0")) {
                                Toast.makeText(getActivity(), "连接异常", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            RightPowerOnOff = true;
                            power_bt.setSelect(true);
                            bottom_ll.setVisibility(View.VISIBLE);

                        } else if (NotNull.isNotNull(power) && TextUtils.equals(power, "0")) {//关机
                            String error = orderObject.getString("error");
                            if (!TextUtils.equals(error, "0")) {
                                Toast.makeText(getActivity(), "连接异常", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            RightPowerOnOff = false;
                            power_bt.setSelect(false);
                            bottom_ll.setVisibility(View.INVISIBLE);
                            select_bt_r.setSelect(false);
                            if (flAdjust.getVisibility() == View.INVISIBLE) {
                                flAdjust.setVisibility(View.INVISIBLE);
                                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
                            }
                            //关机状态直接忽略
                            return;
                        }

                        break;
                    case 3://更改模式返回
                        moden = orderObject.getString("moden");
                        mode = Integer.valueOf(moden);
//                        changeRightMode(moden, orderObject);

                        break;
                    case 4://档位设定返回
                        Log.d(TAG, "setMProtocol: ");
                        moden = orderObject.getString("moden");
//                        stall = Integer.valueOf(orderObject.getString("stall"));
                        mode = Integer.valueOf(moden);
//                        if (stall < 0) {
//                            ring_pv.setProgress(Integer.valueOf(orderObject.getString("stall"))+1);
//                        } else {
//                            ring_pv.setProgress(stall + 1);
//                        }
                       /* if (mode == 4) {//火锅
                            tvPower.setText(CommonBean.HOTPOTSTRS[stall]);
                        } else if (mode == 5) {//煎炒
                            tvPower.setText(CommonBean.FRYOIlSTRS1[stall]);
                            tvTemperature.setText(CommonBean.FRYOIlSTRS2[stall]);
                        } else if (mode == 6) {//烤炸
                            tvPower.setText(CommonBean.KAOZA1[stall]);
                            tvTemperature.setText(CommonBean.KAOZA2[stall]);
                        } else if (mode == 8) {//煎焗
                            tvPower.setText(CommonBean.JIANJU[stall]);
                        } else if (mode == 10) {//爆炒
                            tvPower.setText(CommonBean.BAOCHAO1[stall]);
                            tvTemperature.setText(CommonBean.BAOCHAO2[stall]);
                        } else if (mode == 11) {//油炸
                            tvPower.setText(CommonBean.YOUZA1[stall]);
                            tvTemperature.setText(CommonBean.YOUZA2[stall]);
                        } else if (mode == 12) {//文火
                            tvPower.setText(CommonBean.WENHUO[stall]);
                        }*/
//                        changeRightMode(moden, orderObject);
                        break;
                    case 5://工作时间查询返回


                        break;
                    case 6://设置/取消预约时间返回
                        String error = orderObject.getString("error");
                        if (Integer.valueOf(error) != 0) return;
                        String reser = orderObject.getString("setting");
                        String success = orderObject.getString("success");
                        if (Boolean.valueOf(reser)) {
                            reservationPon.setVisibility(View.VISIBLE);
                        } else {
                            reservationPon.setVisibility(View.GONE);
                        }
                        moden = orderObject.getString("moden");
                        mode = Integer.valueOf(moden);
                        break;
                    case 7://查询预约返回
                        if (!clickRever) return;//点击按钮才进去
                        clickRever = false;
//                        if (NotNull.isNotNull(bsaeHudHelper))
//                            bsaeHudHelper.hudHide();
                        if (thread != null) thread.interrupt();
                        moden = orderObject.getString("moden");
                        mode = Integer.valueOf(moden);
                        String bootTime = orderObject.getString("bootTime");//开机时间
                        String appointment = orderObject.getString("appointment");//工作时间
                        long aBootTime = Long.valueOf(bootTime);
                        long aAppointment = Long.valueOf(appointment);

                        Intent intent = new Intent(getActivity(), OrderTimeActivity.class);
                        intent.putExtra("LRIndex", 1);
                        intent.putExtra("moden", mode);
                        intent.putExtra("bootTime", aBootTime);
                        intent.putExtra("appointment", aAppointment);
                        startActivity(intent);
                        if (thread != null) {//清空线程
                            thread = null;
                        }
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //改变模式状态
    private void changeRightMode(String moden, JSONObject orderObject) throws JSONException {
        progressView.setMaxCount(100.0f);
        progressView.setCurrentCount(100);

        if (NotNull.isNotNull(moden)) {
            mode = Integer.valueOf(moden);
            tvMode.setText(modeStr[mode]);
            //模式
            switch (this.mode) {
                case 8://煎焗
                    cookMode8();
                    setModenButton(R.id.fragment_rightdevice_bt0);
                    String stall8 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall8)) {
//                        ring_pv.setProgress(Integer.valueOf(stall8));//当前档位
                        int progress = Integer.valueOf(stall8);
                        if (progress != 0)
                            tvPower.setText(CommonBean.JIANJU[progress]);
                    }
                    break;
                case 9://闷烧
                    cookMode9();
                    setModenButton(R.id.fragment_rightdevice_bt1);
                    break;
                case 7://保温
                    cookMode7();
                    setModenButton(R.id.fragment_rightdevice_bt2);
                    break;
                case 10://爆炒
                    cookMode10();
                    setModenButton(R.id.fragment_rightdevice_bt3);
                    String stall11 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall11)) {
//                        ring_pv.setProgress(Integer.valueOf(stall + 1));//当前档位
                        int progress = Integer.valueOf(stall + 1);
                        tvPower.setText(CommonBean.BAOCHAO1[progress]);
                        tvTemperature.setText(CommonBean.BAOCHAO2[progress]);
                    }
                    break;
                case 11://文火
                    cookMode11();
                    setModenButton(R.id.fragment_rightdevice_bt4);
                    String stall12 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall12)) {
//                        ring_pv.setProgress(Integer.valueOf(stall12));//当前档位
                        int progress = Integer.valueOf(stall12);
                        if (progress != 0)
                            tvPower.setText(CommonBean.YOUZA1[progress]);
                        tvTemperature.setText(CommonBean.YOUZA2[progress]);
                    }
                    break;
                case 12:
                    cookMode12();
                    setModenButton(R.id.fragment_rightdevice_bt5);
                    String stall13 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall13)) {
//                        ring_pv.setProgress(Integer.valueOf(stall13));//当前档位
                        int progress = Integer.valueOf(stall13);
                        if (progress != 0)
                            tvPower.setText(CommonBean.WENHUO[progress]);
                    }
                    break;
            }

            int worktime = Integer.valueOf(orderObject.getString("worktime"));
            if (worktime != 0) {
                rightTime = worktime / 1000;
                if (!NotNull.isNotNull(rightThread) || !rightThread.isAlive()) {
                    rightThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (rightTime > 0) {
                                SystemClock.sleep(1000);
                                rightTime--;
                                handler.sendEmptyMessage(TIME_COOK);
                            }
                            handler.sendEmptyMessage(TIME_OUT);
                        }
                    });
                    rightThread.start();
                }
            }
        }
    }

    private void setModenButton(int Resin) {
        ImageTopButton button = (ImageTopButton) contentView.findViewById(Resin);
        if (select_bt_r != null) {
            if (select_bt_r != button) {
                select_bt_r.setSelect(false);
            }
        }
        select_bt_r = button;
        button.setSelect(true);
//        if (flAdjust.getVisibility() != View.VISIBLE) {
//            if (bsaeHudHelper != null){
//                bsaeHudHelper.hudHide();
//            }
//            flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
//            flAdjust.setVisibility(View.VISIBLE);
//        }
    }
    public void setCode(int code) {
        this.code = code;
        if (code == -1) {
            RightPowerOnOff = false;
            power_bt.setSelect(false);
            bottom_ll.setVisibility(View.INVISIBLE);
            if (select_bt_r != null)
                select_bt_r.setSelect(false);
            if (flAdjust.getVisibility() == View.VISIBLE) {
                flAdjust.setVisibility(View.INVISIBLE);
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
            }
        }
    }

    private void showTurnOn() {
        View view = dialongView.showCustomDialong(R.layout.dialong_view);
        ImageView deleteView = (ImageView) view.findViewById(R.id.dialong_delete);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialongView.dismissDialong();
            }
        });
        TextView dialongText = (TextView) view.findViewById(R.id.dialongText);
        dialongText.setText("请先打开电磁炉");
    }
}