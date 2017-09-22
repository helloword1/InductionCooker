package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.goockr.inductioncooker.view.ImageTopButton;
import com.goockr.inductioncooker.view.ProgressView;
import com.goockr.inductioncooker.view.SegmentController;
import com.goockr.ui.view.RingRoundProgressView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_FIRST_USER;
import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;
import static com.goockr.inductioncooker.R.id.fragment_leftdevice_power_bt;
import static com.goockr.inductioncooker.R.id.fragment_rightdevice_bt0;

/**
 * Created by CMQ on 2017/6/27.
 */

public class HomeFragment extends Fragment implements ImageTopButton.ImageTopButtonOnClickListener, View.OnClickListener {
    private static final int REVER_DEX = 123;
    View contentView;
    List<ImageTopButton> buttons;
    FragmentManager fragmentManager;
    @BindView(R.id.flAdjust)
    RelativeLayout flAdjust;
    @BindView(R.id.fragment_leftdevice_content)
    RelativeLayout fragmentLeftdeviceContent;
    @BindView(R.id.fragment_rightdevice_content)
    RelativeLayout fragmentRightdeviceContent;
    @BindView(fragment_rightdevice_bt0)
    ImageTopButton bt_8;
    @BindView(R.id.fragment_rightdevice_bt1)
    ImageTopButton bt_9;
    @BindView(R.id.fragment_rightdevice_bt2)
    ImageTopButton bt_10;
    @BindView(R.id.fragment_rightdevice_bt3)
    ImageTopButton bt_11;
    @BindView(R.id.fragment_rightdevice_bt4)
    ImageTopButton bt_12;
    @BindView(R.id.fragment_rightdevice_bt5)
    ImageTopButton bt_13;
    @BindView(R.id.fragment_rightdevice_bottomview)
    LinearLayout bottom_ll1;
    private int mode = -1;
    @BindView(fragment_leftdevice_power_bt)
    ImageTopButton power_bt;
    @BindView(R.id.fragment_leftdevice_reservation_bt)
    ImageTopButton reservation_bt;
    @BindView(R.id.fragment_leftdevice_unreservation_bt)
    ImageTopButton unreservation_bt;
    @BindView(R.id.fragment_leftdevice_bt0)
    ImageTopButton bt_0;
    @BindView(R.id.fragment_leftdevice_bt1)
    ImageTopButton bt_1;
    @BindView(R.id.fragment_leftdevice_bt2)
    ImageTopButton bt_2;
    @BindView(R.id.fragment_leftdevice_bt3)
    ImageTopButton bt_3;
    @BindView(R.id.fragment_leftdevice_bt4)
    ImageTopButton bt_4;
    @BindView(R.id.fragment_leftdevice_bt5)
    ImageTopButton bt_5;
    @BindView(R.id.fragment_leftdevice_bt6)
    ImageTopButton bt_6;
    @BindView(R.id.fragment_leftdevice_bt7)
    ImageTopButton bt_7;
    @BindView(R.id.fragment_leftdevice_bottomview)
    LinearLayout bottom_ll;
    ImageTopButton select_bt;
    ImageTopButton select_bt_r;
    @BindView(R.id.fragment_home_segment)
    SegmentController segmentController;

    private int leftTime;
    private int rightTime;
    private final String[] modeStr = {"煲粥", "煲汤", "煮饭", "烧水", "火锅", "煎炒", "烤炸", "保温", "煎焗", "闷烧", "保温", "爆炒", "油炸", "文火"};
    private final int TIME_COOK = 1;
    private final int TIME_OUT = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_COOK:
                    if (LRindex == 0) {
                        tvData.setText(hourToTime(leftTime));
                        int currentCount = (leftTime * 100) / leftSumTime;
                        progressView.setCurrentCount(currentCount);

                    } else if (LRindex == 1) {
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
    private boolean leftPowerOnOff;
    private boolean RightPowerOnOff;
    private int leftSumTime;
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
    private Thread leftThread;
    private Thread rightThread;
    private int LRindex = 0;//表示左右炉
    private List<Moden> modenList;
    private com.goockr.inductioncooker.models.BaseProtocol MProtocol;
    private ImageView reservationPon;//红点
    private final int POWER_OFF = 0;
    private final int POWER_ON = 1;
    private String lPower;
    private String RPower;
    private int stall;
    private int revermode=-1;
    private boolean intent2Rever;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_leftdevice, container, false);
        reservationPon = ((ImageView) contentView.findViewById(R.id.reservationPon));
        ButterKnife.bind(this, contentView);
        initData();
        initUI();
        initEvent();
        return contentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        leftTime = 0;
        rightTime = 0;
    }

    private void initData() {

        fragmentManager = getFragmentManager();

        modenList = new ArrayList<>();

        JSONArray jsonArray;
        try {
            jsonArray = ReadAdssetsJson.readJson("leftdevice").getJSONArray("value");
            for (int i = 0; i < jsonArray.length(); i++) {
                Moden moden = Moden.moden(jsonArray.get(i));
                modenList.add(moden);
            }
            JSONArray right = ReadAdssetsJson.readJson("rightdevice").getJSONArray("value");
            for (int i = 0; i < right.length(); i++) {
                Moden moden = Moden.moden(right.get(i));
                modenList.add(moden);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        buttons = new ArrayList<>();
        buttons.add(bt_0);
        buttons.add(bt_1);
        buttons.add(bt_2);
        buttons.add(bt_3);
        buttons.add(bt_4);
        buttons.add(bt_5);
        buttons.add(bt_6);
        buttons.add(bt_7);
        buttons.add(bt_8);
        buttons.add(bt_9);
        buttons.add(bt_10);
        buttons.add(bt_11);
        buttons.add(bt_12);
        buttons.add(bt_13);
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
        reservation_bt.setHightLightImageId(R.mipmap.btn_reservation_pressed);
        reservation_bt.setDisabledImageId(R.mipmap.btn_reservation_disabled);
        reservation_bt.setNormTextCoclor(R.color.colorBlack);
        reservation_bt.setText("预约");

        unreservation_bt.setNormImageId(R.mipmap.btn_cancel_normal);
        unreservation_bt.setHightLightImageId(R.mipmap.btn_cancel_pressed);
        unreservation_bt.setDisabledImageId(R.mipmap.btn_cancel_disabled);
        unreservation_bt.setNormTextCoclor(R.color.colorBlack);
        unreservation_bt.setText("取消预约");

        reservation_bt.buttonOnClickListener(this);
        unreservation_bt.buttonOnClickListener(this);
        power_bt.buttonOnClickListener(this);

        setButtonType(bt_0, R.mipmap.btn_soup_normal, R.mipmap.btn_soup_pressed, R.mipmap.btn_soup_disabled, R.color.colorGrayText, "煲粥");
        setButtonType(bt_1, R.mipmap.btn_porridge_normal, R.mipmap.btn_porridge_selected, R.mipmap.btn_porridge_disabled, R.color.colorGrayText, "煲汤");
        setButtonType(bt_2, R.mipmap.btn_rice_normal, R.mipmap.btn_rice__selected, R.mipmap.btn_rice_disabled, R.color.colorGrayText, "煮饭");
        setButtonType(bt_3, R.mipmap.btn_water_normal, R.mipmap.btn_water__selected, R.mipmap.btn_water_disabled, R.color.colorGrayText, "烧水");
        setButtonType(bt_4, R.mipmap.brn_hotpot__normal, R.mipmap.brn_hotpot_selected, R.mipmap.brn_hotpot_disabled, R.color.colorGrayText, "火锅");
        setButtonType(bt_5, R.mipmap.btn_fry_normal, R.mipmap.btn_fry_selected, R.mipmap.btn_fry_disabled, R.color.colorGrayText, "煎炒");
        setButtonType(bt_6, R.mipmap.btn_baked_fried_normal, R.mipmap.btn_baked_fried__selected, R.mipmap.btn_baked_fried_disabled, R.color.colorGrayText, "烤炸");
        setButtonType(bt_7, R.mipmap.btn_temperature_normal, R.mipmap.btn_temperature__selected, R.mipmap.btn_temperature_disabled, R.color.colorGrayText, "保温");
        setButtonType(bt_8, R.mipmap.btn_baked_normal, R.mipmap.btn_baked_selected, R.mipmap.btn_baked_selected, R.color.colorGrayText, "煎焗");
        setButtonType(bt_9, R.mipmap.btn_stew_normal, R.mipmap.btn_stew_selected, R.mipmap.btn_stew_disabled, R.color.colorGrayText, "焖烧");
        setButtonType(bt_10, R.mipmap.btn_r_temperature_normal, R.mipmap.btn_r_temperature_selected, R.mipmap.btn_r_temperature_disabled, R.color.colorGrayText, "保温");
        setButtonType(bt_11, R.mipmap.btn_quicyfry_normal, R.mipmap.btn_quicyfry_selected, R.mipmap.btn_quicyfry_disabled, R.color.colorGrayText, "爆炒");
        setButtonType(bt_12, R.mipmap.btn_fried_normal, R.mipmap.btn_fried_selected, R.mipmap.btn_fried_disabled, R.color.colorGrayText, "油炸");
        setButtonType(bt_13, R.mipmap.btn_slowfire_normal, R.mipmap.btn_slowfire_selected, R.mipmap.btn_slowfire_disabled, R.color.colorGrayText, "文火");
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
        flAdjust.addView(adjustView);
        bottom_ll.setVisibility(View.INVISIBLE);
        bottom_ll1.setVisibility(View.INVISIBLE);
    }

    private void adjustLeftData() {
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
            case 0://煲粥
            case 1://煲汤
                cookMode1();
                break;
            case 2://煮饭
                cookMode2();
                break;
            case 3://烧水
                cookMode3();
                break;
            case 4://火锅
                cookMode4();
                break;
            case 5://煎炒
                cookMode5();
                tvTemperature.setText("260℃");
                tvPower.setText("1600W");
                leftTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(leftTime));
                leftSumTime = leftTime;
                break;
            case 6://烤炒
                cookMode6();
                tvTemperature.setText("260℃");
                tvRightTemperature.setText("80℃");
                tvPower.setText("1600W");
                leftTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(leftTime));
                leftSumTime = leftTime;

                break;
            case 7://保温
                cookMode7();
                tvTemperature.setText("80℃");
                tvPower.setText("100W");
                leftTime = 2 * 3600;
                tvData.setText(hourToTime(leftTime));
                leftSumTime = leftTime;
                break;
        }
        if (!NotNull.isNotNull(leftThread) || !leftThread.isAlive()) {
            leftThread = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (leftTime > 0) {
                        SystemClock.sleep(1000);
                        leftTime--;
                        handler.sendEmptyMessage(TIME_COOK);
                    }
                    handler.sendEmptyMessage(TIME_OUT);
                }
            });
            leftThread.start();
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
                cookMode8();
                tvPower.setText("1000W");
                leftTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(leftTime));
                rightSumTime = rightTime;
                break;
            case 9://闷烧
                cookMode9();
                break;
            case 10://保温
                cookMode10();
                break;
            case 11://爆炒
                cookMode11();
                tvTemperature.setText("280℃");
                tvPower.setText("2000W");
                leftTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(leftTime));
                rightSumTime = rightTime;
                break;
            case 12://爆炒
                cookMode12();
                tvTemperature.setText("260℃");
                tvRightTemperature.setText("80℃");
                tvPower.setText("1800W");
                leftTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(leftTime));
                rightSumTime = rightTime;
                break;
            case 13://文火
                cookMode13();
                tvPower.setText("1200W");
                leftTime = 3600 + 20 * 60;
                tvData.setText(hourToTime(leftTime));
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
    private void cookMode13() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(6);
        ring_pv.setMaxCount(6);
    }

    //爆炒
    private void cookMode12() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvRightTemperature.setVisibility(View.VISIBLE);
        ring_pv.setProgress(4);
        ring_pv.setMaxCount(5);
    }

    //爆炒
    private void cookMode11() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(11);
        ring_pv.setMaxCount(12);
    }

    //保温
    private void cookMode10() {
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
        ring_pv.setProgress(5);
        ring_pv.setMaxCount(6);
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
        ring_pv.setMaxCount(2);
        ring_pv.setProgress(2);
    }

    //烤炒
    private void cookMode6() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvRightTemperature.setVisibility(View.VISIBLE);
        ring_pv.setProgress(4);
        ring_pv.setMaxCount(5);
    }

    //煎炒
    private void cookMode5() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(9);
    }

    //火锅
    private void cookMode4() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(9);
        ring_pv.setMaxCount(12);
        tvPower.setText("1600w");
        leftTime = 3600 + 20 * 60;
        tvData.setText(hourToTime(leftTime));
        leftSumTime = leftTime;

    }

    //烧水
    private void cookMode3() {
        unreservation_ib.setVisibility(View.VISIBLE);
        reduce_ib.setVisibility(View.GONE);
        plus_ib.setVisibility(View.GONE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(9);
        tvPower.setText("1600w");
        leftTime = 3600 + 20 * 60;
        tvData.setText(hourToTime(leftTime));
        leftSumTime = leftTime;
    }


    //煮饭
    private void cookMode2() {
        unreservation_ib.setVisibility(View.GONE);
        reduce_ib.setVisibility(View.GONE);
        plus_ib.setVisibility(View.GONE);
        reservation_btn.setVisibility(View.GONE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(7);
        tvPower.setText("1600w");
        leftTime = 3600 + 20 * 60;
        tvData.setText(hourToTime(leftTime));
        leftSumTime = leftTime;
    }

    //煲粥、煲汤
    private void cookMode1() {
        unreservation_ib.setVisibility(View.GONE);
        reduce_ib.setVisibility(View.GONE);
        plus_ib.setVisibility(View.GONE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        reservation_btn.setVisibility(View.VISIBLE);
        ring_pv.setProgress(7);
        tvPower.setText("1200w");
        leftTime = 3600 + 20 * 60;
        tvData.setText(hourToTime(leftTime));
        leftSumTime = leftTime;
    }

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

    private void setButtonType(ImageTopButton bt, int normImageId, int selImageId, int disableImageId, int normTextColor, String text) {
        bt.setNormImageId(normImageId);
        bt.setSelImageId(selImageId);
        bt.setDisabledImageId(disableImageId);
        bt.setNormTextCoclor(normTextColor);
        bt.setText(text);
    }

    private void initEvent() {
        for (ImageTopButton bt : buttons) {
            bt.setClickable(true);
            bt.buttonOnClickListener(this);
        }
        //左右切换隐藏
        segmentController.addListener(new SegmentController.SegmentControllerCallback() {
            @Override
            public void selectIndexChange(int index) {
                LRindex = index;
                flAdjust.setVisibility(View.GONE);
                if (index == 0) {//左炉
                    fragmentLeftdeviceContent.setVisibility(View.VISIBLE);
                    fragmentRightdeviceContent.setVisibility(View.GONE);
                    if (leftPowerOnOff) {
                        power_bt.setSelect(true);
                    } else {
                        power_bt.setSelect(false);
                    }
                } else {//右炉
                    fragmentLeftdeviceContent.setVisibility(View.GONE);
                    fragmentRightdeviceContent.setVisibility(View.VISIBLE);
                    if (RightPowerOnOff) {
                        power_bt.setSelect(true);
                    } else {
                        power_bt.setSelect(false);
                    }
                }
            }
        });
    }

    @OnClick({R.id.fragment_leftdevice_bottomview, R.id.fragment_rightdevice_bottomview})
    public void OnClick(View v) {
        switch (v.getId()) {
            case (R.id.fragment_leftdevice_bottomview):
            case (R.id.fragment_rightdevice_bottomview):
                flAdjust.setVisibility(View.VISIBLE);
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
                break;
        }
    }


    @Override
    public void imageTopButtonOnClickListener(ImageTopButton button) {
        if (LRindex == 0) {//左炉
            int id = button.getId();
            switch (id) {
                case R.id.fragment_leftdevice_power_bt://电源按钮
                    if (leftPowerOnOff) {//关机
                        //指令
                        TcpSocket.getInstance().write(Protocol2.powerStatus(LRindex, POWER_OFF));
                    } else {
                        //开机
                        TcpSocket.getInstance().write(Protocol2.powerStatus(LRindex, POWER_ON));
                    }
                    return;
                case R.id.fragment_leftdevice_reservation_bt://预约
                    if (reservationPon.getVisibility()==View.GONE){
                        Intent intent = new Intent(getActivity(), ReservationActivity.class);
                        intent.putExtra(Common.HomeFragmentSelectIndexKey, 0);
                        startActivityForResult(intent,REVER_DEX);
                    }else{
                        intent2Rever =true;
                    }

                    return;
                case R.id.fragment_leftdevice_unreservation_bt://取消预约
                    TcpSocket.getInstance().write(Protocol2.setReservation(0, revermode, 0, 0, 0));
                    return;
            }


            if (leftPowerOnOff) {
                switch (id) {
                    case R.id.fragment_leftdevice_bt0://煲粥
                        mode = 0;

                        break;
                    case R.id.fragment_leftdevice_bt1://煲汤

                        mode = 1;
                        break;
                    case R.id.fragment_leftdevice_bt2://煮饭
                        mode = 2;
                        break;
                    case R.id.fragment_leftdevice_bt3://烧水
                        mode = 3;
                        break;
                    case R.id.fragment_leftdevice_bt4://火锅
                        mode = 4;
                        break;
                    case R.id.fragment_leftdevice_bt5://煎炒
                        mode = 5;
                        break;
                    case R.id.fragment_leftdevice_bt6://烤炸
                        mode = 6;
                        break;
                    case R.id.fragment_leftdevice_bt7://保温
                        mode = 7;
                        break;
                }
                TcpSocket.getInstance().write(Protocol2.moden(0, mode));
                if (select_bt != null) select_bt.setSelect(false);
                button.setSelect(true);
                select_bt = button;
                adjustLeftData();
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
                flAdjust.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "请先开机", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {//右炉
            int id = button.getId();
            switch (id) {
                case R.id.fragment_leftdevice_power_bt://电源按钮
                    if (RightPowerOnOff) {//关机
                        TcpSocket.getInstance().write(Protocol2.powerStatus(LRindex, POWER_OFF));
                    } else {//开机
                        TcpSocket.getInstance().write(Protocol2.powerStatus(LRindex, POWER_ON));
                    }
                    return;
                case R.id.fragment_leftdevice_reservation_bt://预约
                    Intent intent = new Intent(getActivity(), ReservationActivity.class);
                    intent.putExtra(Common.HomeFragmentSelectIndexKey, 1);
                    startActivityForResult(intent,REVER_DEX);
                    return;
                case R.id.fragment_leftdevice_unreservation_bt://取消预约
                    if (reservationPon.getVisibility()==View.GONE){
                        TcpSocket.getInstance().write(Protocol2.setReservation(1, revermode, 0, 0, 0));
                    }else{
                        intent2Rever=true;
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
                        mode = 10;
                        break;
                    case (R.id.fragment_rightdevice_bt3)://爆炒
                        mode = 11;
                        break;
                    case (R.id.fragment_rightdevice_bt4)://油炸
                        mode = 12;
                        break;
                    case (R.id.fragment_rightdevice_bt5)://文火
                        mode = 13;
                        break;
                }
                TcpSocket.getInstance().write(Protocol2.moden(1, mode));
                if (select_bt_r != null) select_bt_r.setSelect(false);
                button.setSelect(true);
                select_bt_r = button;
                adjustRightData();
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
                flAdjust.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(getActivity(), "请先开机", Toast.LENGTH_SHORT).show();
            }
        }

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_FIRST_USER) {
            if (data == null) return;
            int hour = data.getIntExtra("HOUR", -1);
            int second = data.getIntExtra("SECOND", -1);
            if (LRindex == 0) {
                leftTime = hour * 3600 + second * 60;
                leftSumTime = leftTime;
                tvData.setText(hourToTime(leftTime));
            } else if (LRindex == 1) {
                rightTime = hour * 3600 + second * 60;
                rightSumTime = rightTime;
                tvData.setText(hourToTime(rightTime));
            }

        }else if (requestCode==REVER_DEX){
            if (NotNull.isNotNull(data))
            revermode = data.getIntExtra("MODE", -1);
            TcpSocket.getInstance().write(Protocol2.getReservationStatus(LRindex));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.fragment_adjust_reservation_ib)://定时
                startActivityForResult(new Intent(getActivity(), ChoiceCookTimeActivity.class), RESULT_FIRST_USER);
                break;

            case (R.id.fragment_adjust_unreservation_ib)://取消定时，关闭
                if (LRindex == 0) {
                    leftTime = 1;
                } else {
                    rightTime = 1;
                }

                break;
            case (R.id.fragment_adjust_lower_ib):
//                if (NotNull.isNotNull(select_bt)) select_bt.setSelect(false);
//                if (NotNull.isNotNull(select_bt_r)) select_bt_r.setSelect(false);
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
            TcpSocket.getInstance().write(Protocol2.stall(LRindex, 0));
        else
            TcpSocket.getInstance().write(Protocol2.stall(LRindex, stall - 1));
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
            TcpSocket.getInstance().write(Protocol2.stall(LRindex, ring_pv.maxCount));
        else
            TcpSocket.getInstance().write(Protocol2.stall(LRindex, stall + 1));
    }

    public void setMProtocol(BaseProtocol MProtocol) {
        this.MProtocol = MProtocol;
        if (NotNull.isNotNull(MProtocol)) {
            JSONObject orderObject = MProtocol.getOrder();
            try {
                int code = Integer.valueOf(orderObject.getString("code"));
                if (code < 0) {//错误码
                    String msg = orderObject.getString("msg");
                    if (msg.contains("unconnected")) {
                        Toast.makeText(getActivity(), "请打开电磁炉", Toast.LENGTH_SHORT).show();
                    }
                    return;
                } else if (code == 9) {
                    TcpSocket.getInstance().write(Protocol2.stopErro(LRindex));//停止发送错误
                    return;
                }

                int LRID = Integer.valueOf(orderObject.getString("deviceId"));//左右炉
                String moden = null;

                if (leftPowerOnOff || RightPowerOnOff) {//表示打开电磁炉


                }

                switch (code) {
                    case 0://主机接收指令更新状态
                        TcpSocket.getInstance().write(Protocol2.statusCheck(0, LRindex));
                        break;
                    case 1://控制主机机返回
                        if (LRindex == 0) {//左炉
                            //左炉开关状态
                            String leftPower = orderObject.getString("power");
                            //判断开关是否一致 一致干掉
                            if (TextUtils.equals(leftPower, lPower)) {
                                return;
                            }
                            lPower = leftPower;
                            if (TextUtils.equals(leftPower, "1")) {//开机

                                leftPowerOnOff = true;
                                power_bt.setSelect(true);
                                bottom_ll.setVisibility(View.VISIBLE);

                                if (flAdjust.getVisibility() != View.VISIBLE) {
                                    flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
                                    flAdjust.setVisibility(View.VISIBLE);
                                }
                                LRindex = LRID;
                                moden = orderObject.getString("moden");
                                mode = Integer.valueOf(moden);
                                stall = Integer.valueOf(orderObject.getString("stall"));

                                ring_pv.setProgress(stall + 1);
                                if (mode == 4) {//火锅
                                    tvPower.setText(CommonBean.HOTPOTSTRS[stall]);
                                } else if (mode == 5) {//煎炒
                                    tvPower.setText(CommonBean.FRYOIlSTRS1[stall]);
                                    tvTemperature.setText(CommonBean.FRYOIlSTRS2[stall]);
                                } else if (mode == 6) {//烤炸
                                    tvPower.setText(CommonBean.KAOZA1[stall]);
                                    tvTemperature.setText(CommonBean.KAOZA2[stall]);
                                }

                                if (mode > 7)
                                    changeRightMode(moden, orderObject);
                                else
                                    changeLeftMode(moden, orderObject);

                            } else {

                                leftPowerOnOff = false;
                                power_bt.setSelect(false);
                                bottom_ll.setVisibility(View.INVISIBLE);

                                if (flAdjust.getVisibility() == View.VISIBLE) {
                                    if (NotNull.isNotNull(select_bt))
                                        select_bt.setSelect(false);
                                    flAdjust.setVisibility(View.INVISIBLE);
                                    flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
                                }
                            }


                        } else if (LRindex == 1) {//右炉
                            //左炉开关状态
                            String rightPower = orderObject.getString("power");
                            if (TextUtils.equals(rightPower, RPower)) {
                                return;
                            }
                            RPower = rightPower;
                            if (TextUtils.equals(rightPower, "1")) {//开机
                                RightPowerOnOff = true;
                                power_bt.setSelect(true);
                                bottom_ll1.setVisibility(View.VISIBLE);
                                LRindex = LRID;
                                if (flAdjust.getVisibility() != View.VISIBLE) {
                                    flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
                                    flAdjust.setVisibility(View.VISIBLE);
                                }

                                moden = orderObject.getString("moden");
                                mode = Integer.valueOf(moden);
                                stall = Integer.valueOf(orderObject.getString("stall"));

                                ring_pv.setProgress(stall + 1);

                                if (mode == 8) {//煎焗
                                    tvPower.setText(CommonBean.JIANJU[stall]);
                                } else if (mode == 11) {//爆炒
                                    tvPower.setText(CommonBean.BAOCHAO1[stall]);
                                    tvTemperature.setText(CommonBean.BAOCHAO2[stall]);
                                } else if (mode == 12) {//油炸
                                    tvPower.setText(CommonBean.YOUZA1[stall]);
                                    tvTemperature.setText(CommonBean.YOUZA2[stall]);
                                } else if (mode == 13) {//文火
                                    tvPower.setText(CommonBean.WENHUO[stall]);
                                }
                                changeRightMode(moden, orderObject);

                            } else {
                                RightPowerOnOff = false;
                                power_bt.setSelect(false);
                                bottom_ll1.setVisibility(View.INVISIBLE);

                                if (flAdjust.getVisibility() == View.VISIBLE) {
                                    if (NotNull.isNotNull(select_bt_r))
                                        select_bt_r.setSelect(false);
                                    flAdjust.setVisibility(View.INVISIBLE);
                                    flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
                                }
                            }

                        }
                        if (Boolean.valueOf(orderObject.getString("reservation"))){
                            reservationPon.setVisibility(View.VISIBLE);
                        }else{
                            reservationPon.setVisibility(View.GONE);
                        }

                        break;
                    case 2://开关机返回
                        Log.d(TAG, "setMProtocol: ");
                        String power = orderObject.getString("power");
                        if (NotNull.isNotNull(power) && TextUtils.equals(power, "1")) {//开机
                            String error = orderObject.getString("error");
                            if (!TextUtils.equals(error,"0")){
                                Toast.makeText(getActivity(), "连接异常", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (LRindex == 0) {
                                leftPowerOnOff = true;
                                power_bt.setSelect(true);
                                bottom_ll.setVisibility(View.VISIBLE);
                            } else if (LRindex == 1) {
                                RightPowerOnOff = true;
                                power_bt.setSelect(true);
                                bottom_ll1.setVisibility(View.VISIBLE);
                            }

                        } else if (NotNull.isNotNull(power) && TextUtils.equals(power, "0")) {//关机
                            String error = orderObject.getString("error");
                            if (!TextUtils.equals(error,"0")){
                                Toast.makeText(getActivity(), "连接异常", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            if (LRindex == 0) {
                                leftPowerOnOff = false;
                                power_bt.setSelect(false);
                                bottom_ll.setVisibility(View.INVISIBLE);
                                if (flAdjust.getVisibility() == View.INVISIBLE) {
                                    select_bt.setSelect(false);
                                    flAdjust.setVisibility(View.INVISIBLE);
                                    flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
                                }
                            } else if (LRindex == 1) {
                                RightPowerOnOff = false;
                                power_bt.setSelect(false);
                                bottom_ll1.setVisibility(View.INVISIBLE);
                                if (flAdjust.getVisibility() == View.INVISIBLE) {
                                    select_bt_r.setSelect(false);
                                    flAdjust.setVisibility(View.INVISIBLE);
                                    flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
                                }

                            }

                            //关机状态直接忽略
                            return;
                        }

                        break;
                    case 3://更改模式返回
                        moden = orderObject.getString("moden");
                        mode = Integer.valueOf(moden);
                        if (mode > 7)
                            changeRightMode(moden, orderObject);
                        else
                            changeLeftMode(moden, orderObject);
                        break;
                    case 4://档位设定返回
                        Log.d(TAG, "setMProtocol: ");
                        moden = orderObject.getString("moden");
                        stall = Integer.valueOf(orderObject.getString("stall"));
                        mode = Integer.valueOf(moden);

                        if (stall < 1) {
                            ring_pv.setProgress(1);
                        } else {
                            ring_pv.setProgress(stall + 1);
                        }
                        if (mode == 4) {//火锅
                            tvPower.setText(CommonBean.HOTPOTSTRS[stall]);
                        } else if (mode == 5) {//煎炒
                            tvPower.setText(CommonBean.FRYOIlSTRS1[stall]);
                            tvTemperature.setText(CommonBean.FRYOIlSTRS2[stall]);
                        } else if (mode == 6) {//烤炸
                            tvPower.setText(CommonBean.KAOZA1[stall]);
                            tvTemperature.setText(CommonBean.KAOZA2[stall]);
                        } else if (mode == 8) {//煎焗
                            tvPower.setText(CommonBean.JIANJU[stall]);
                        } else if (mode == 11) {//爆炒
                            tvPower.setText(CommonBean.BAOCHAO1[stall]);
                            tvTemperature.setText(CommonBean.BAOCHAO2[stall]);
                        } else if (mode == 12) {//油炸
                            tvPower.setText(CommonBean.YOUZA1[stall]);
                            tvTemperature.setText(CommonBean.YOUZA2[stall]);
                        } else if (mode == 13) {//文火
                            tvPower.setText(CommonBean.WENHUO[stall]);
                        }
                        if (mode > 7)
                            changeRightMode(moden, orderObject);
                        else
                            changeLeftMode(moden, orderObject);
                        break;
                    case 5://工作时间查询返回


                        break;
                    case 6://设置/取消预约时间返回

                        String error = orderObject.getString("error");
                        if (Integer.valueOf(error)!=0) return;
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
                        moden = orderObject.getString("moden");
                        mode = Integer.valueOf(moden);

                        String bootTime = orderObject.getString("bootTime");//开机时间
                        String appointment = orderObject.getString("appointment");//工作时间
                        Long aBootTime = Long.valueOf(bootTime);
                        Long aAppointment = Long.valueOf(appointment);
                        if (mode > 7)
                            changeRightMode(moden, orderObject);
                        else
                            changeLeftMode(moden, orderObject);
                        if (aAppointment != 0 && aBootTime != 0) {
                            Intent intent = new Intent(getActivity(), OrderTimeActivity.class);
                            intent.putExtra("HOUR", aAppointment / 3600000);
                            intent.putExtra("MINUTE", (aAppointment % 3600000) / 60000);
                            intent.putExtra("BEGIN_TIME", new Date(aBootTime));
                            intent.putExtra("MODE", mode);
                            startActivity(intent);
                        }

                        break;
                }
                if (NotNull.isNotNull(LRindex) && LRindex == 0) {//左炉
                    fragmentLeftdeviceContent.setVisibility(View.VISIBLE);
                    fragmentRightdeviceContent.setVisibility(View.GONE);
                    if (leftPowerOnOff) {
                        power_bt.setSelect(true);
                    } else {
                        power_bt.setSelect(false);
                    }
                    segmentController.setSelectIndexListener(0);
                } else if (NotNull.isNotNull(LRindex) && LRindex == 1) {//右炉
                    fragmentLeftdeviceContent.setVisibility(View.GONE);
                    fragmentRightdeviceContent.setVisibility(View.VISIBLE);
                    if (RightPowerOnOff) {
                        power_bt.setSelect(true);
                    } else {
                        power_bt.setSelect(false);
                    }
                    segmentController.setSelectIndexListener(1);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setModenButton(int Resin) {
        ImageTopButton button = (ImageTopButton) contentView.findViewById(Resin);

        if (mode < 7) {//表示左炉
            if (select_bt != null) {
                if (select_bt != button) {
                    select_bt.setSelect(false);
                }
            }
            select_bt = button;
        } else {
            if (select_bt_r != null) {
                if (select_bt_r != button) {
                    select_bt_r.setSelect(false);
                }
            }
            select_bt_r = button;
        }

        button.setSelect(true);

        if (flAdjust.getVisibility() != View.VISIBLE) {
            flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
            flAdjust.setVisibility(View.VISIBLE);
        }
    }

    //改变模式状态
    private void changeLeftMode(String moden, JSONObject orderObject) throws JSONException {
        progressView.setMaxCount(100.0f);
        progressView.setCurrentCount(100);
        if (NotNull.isNotNull(moden)) {
            mode = Integer.valueOf(moden);
            tvMode.setText(modeStr[mode]);
            //模式
            switch (mode) {
                case 0:
                    cookMode1();
                    setModenButton(R.id.fragment_leftdevice_bt0);
                    break;
                case 1:
                    cookMode1();
                    setModenButton(R.id.fragment_leftdevice_bt1);
                    break;
                case 2:
                    cookMode2();
                    setModenButton(R.id.fragment_leftdevice_bt2);
                    break;
                case 3:
                    cookMode3();
                    setModenButton(R.id.fragment_leftdevice_bt3);
                    break;
                case 4://火锅
                    cookMode4();
                    setModenButton(R.id.fragment_leftdevice_bt4);
                    String mStall = orderObject.getString("stall");
                    if (NotNull.isNotNull(mStall)) {
                        int progress = Integer.valueOf(mStall);
                        if (progress <= 1) {
                            ring_pv.setProgress(1);//当前档位
                        } else {
                            ring_pv.setProgress(progress);//当前档位
                        }
                        tvPower.setText(CommonBean.HOTPOTSTRS[progress]);
                    }

                    break;
                case 5://煎炒
                    cookMode5();
                    setModenButton(R.id.fragment_leftdevice_bt5);
                    String stall5 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall5)) {
                        ring_pv.setProgress(Integer.valueOf(stall5));//当前档位
                        int progress = Integer.valueOf(stall5);
                        if (progress != 0)
                            tvPower.setText(CommonBean.FRYOIlSTRS1[progress]);
                        tvTemperature.setText(CommonBean.FRYOIlSTRS2[progress]);
                    }
                    break;
                case 6://烤炸
                    cookMode6();
                    setModenButton(R.id.fragment_leftdevice_bt6);
                    String stall6 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall6)) {
                        ring_pv.setProgress(Integer.valueOf(stall6));//当前档位
                        int progress = Integer.valueOf(stall6);
                        if (progress != 0)
                            tvPower.setText(CommonBean.KAOZA1[progress]);
                        tvTemperature.setText(CommonBean.KAOZA2[progress]);
                    }
                    break;
                case 7:
                    cookMode7();
                    setModenButton(R.id.fragment_leftdevice_bt7);
                    break;
            }
            int worktime = Integer.valueOf(orderObject.getString("worktime"));
            if (worktime != 0) {
                leftTime = worktime / 1000;
                if (!NotNull.isNotNull(leftThread) || !leftThread.isAlive()) {
                    leftThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (leftTime > 0) {
                                SystemClock.sleep(1000);
                                leftTime--;
                                handler.sendEmptyMessage(TIME_COOK);
                            }
                            handler.sendEmptyMessage(TIME_OUT);
                        }
                    });
                    leftThread.start();
                }
            }
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
                        ring_pv.setProgress(Integer.valueOf(stall8));//当前档位
                        int progress = Integer.valueOf(stall8);
                        if (progress != 0)
                            tvPower.setText(CommonBean.JIANJU[progress]);
                    }
                    break;
                case 9:
                    cookMode9();
                    setModenButton(R.id.fragment_rightdevice_bt1);
                    break;
                case 10:
                    cookMode10();
                    setModenButton(R.id.fragment_rightdevice_bt2);
                    break;
                case 11://爆炒
                    cookMode11();
                    setModenButton(R.id.fragment_rightdevice_bt3);
                    String stall11 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall11)) {
                        ring_pv.setProgress(Integer.valueOf(stall11));//当前档位
                        int progress = Integer.valueOf(stall11);
                        if (progress != 0)
                            tvPower.setText(CommonBean.BAOCHAO1[progress]);
                        tvTemperature.setText(CommonBean.BAOCHAO2[progress]);
                    }
                    break;
                case 12:
                    cookMode12();
                    setModenButton(R.id.fragment_rightdevice_bt4);
                    String stall12 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall12)) {
                        ring_pv.setProgress(Integer.valueOf(stall12));//当前档位
                        int progress = Integer.valueOf(stall12);
                        if (progress != 0)
                            tvPower.setText(CommonBean.YOUZA1[progress]);
                        tvTemperature.setText(CommonBean.YOUZA2[progress]);
                    }
                    break;
                case 13:
                    cookMode13();
                    setModenButton(R.id.fragment_rightdevice_bt5);
                    String stall13 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall13)) {
                        ring_pv.setProgress(Integer.valueOf(stall13));//当前档位
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
}
