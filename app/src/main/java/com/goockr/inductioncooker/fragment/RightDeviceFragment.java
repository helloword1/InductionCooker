package com.goockr.inductioncooker.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.ChoiceCookTimeActivity;
import com.goockr.inductioncooker.activity.LoginActivity;
import com.goockr.inductioncooker.activity.OrderTimeActivity;
import com.goockr.inductioncooker.activity.ReservationActivity;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.common.CommonBean;
import com.goockr.inductioncooker.lib.observer.PowerObserval;
import com.goockr.inductioncooker.lib.observer.PowerObserver;
import com.goockr.inductioncooker.lib.socket.Protocol2;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.inductioncooker.models.BaseDevice;
import com.goockr.inductioncooker.models.BaseProtocol;
import com.goockr.inductioncooker.models.Moden;
import com.goockr.inductioncooker.utils.FileCache;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.ReadAdssetsJson;
import com.goockr.inductioncooker.utils.SharePreferencesUtils;
import com.goockr.inductioncooker.view.DialogView;
import com.goockr.inductioncooker.view.ImageTopButton;
import com.goockr.inductioncooker.view.PopWindowUtils;
import com.goockr.inductioncooker.view.ProgressView;
import com.goockr.ui.view.RingRoundProgressView;
import com.goockr.ui.view.helper.HudHelper;
import com.google.gson.Gson;
import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_FIRST_USER;
import static com.chad.library.adapter.base.listener.AbstractSimpleClickListener.TAG;
import static com.goockr.inductioncooker.R.id.fragment_rightdevice_bt0;

/**
 * Created by CMQ on 2017/6/27.
 */

public class RightDeviceFragment extends Fragment implements ImageTopButton.ImageTopButtonOnClickListener, View.OnClickListener, PowerObserval {
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
    @BindView(R.id.fragment_leftdevice_change)
    ImageTopButton device_change;
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
    private int rightTime = 3600 * 2;
    private final String[] modeStr = {"煲粥", "煲汤", "煮饭", "烧水", "火锅", "煎炒", "烤炸", "保温", "煎焗", "闷烧", "爆炒", "油炸", "文火"};
    private final int TIME_COOK = 1;
    private final int TIME_OUT = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_COOK:
                    if (mode != 8 && mode != 9) {
                        tvData.setText(hourToTime(rightTime));
                        Log.d(TAG, "rightTime: " + rightTime);
                        if (rightSumTime != 0) {
                            float currentCount = (rightTime * 100) / rightSumTime;
                            progressView.setCurrentCount(100 - currentCount);
                        } else {
                            progressView.setCurrentCount(0);
                        }
                    } else {
                        progressView.setCurrentCount(100);
                    }
                    //表示闷烧
                    if (mode == 9) {
                        int minute = rightTime / 60;
                        if (minute < 3) {
                            tvPower.setText("1400W");
                            notifyObservers("1400W");
                        } else if (minute < 18) {
                            tvPower.setText("1200W");
                            notifyObservers("1200W");
                        } else if (minute < 28) {
                            tvPower.setText("200W");
                            notifyObservers("200W");
                        }
                    }
                    break;
                case TIME_OUT:
                    try {
//                        Toast.makeText(getActivity(), "时间到", Toast.LENGTH_SHORT).show();
                    } catch (NullPointerException e) {
                    }
                    break;
                default:
                    break;
            }
        }
    };
    // 取消倒计时用的
    private Handler cancelCountDownHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (countDown != null) {
                countDown.cancel();
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
    private int stall = -1;
    ImageTopButton select_bt_r; // 之前的按钮
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
    private DialogView dialogView;
    private TextView tvUnit;
    private double allTime = 0;
    private KProgressHUD hud;
    private int hadChangeMode = 2; // 0是切换失败，1是切换成功，2是切换中
    private boolean hadChangeModeBool;
    private int preMode; // 上一个模式
    private CountDownTimer countDown;
    private ImageTopButton currentButton; // 当前的按钮
    private boolean powerStateChange;
    private int globalPower;
    private int sumBack;
    private Thread timeThread;
    private List<PowerObserver> observers = new ArrayList<>();
    private OnDeviceListener listener;
    private Thread modeThread;
    //点击时间的间隔
    public final int UP_MIN_CLICK_DELAY_TIME = 700;
    public final int DOWN_MIN_CLICK_DELAY_TIME = 700;
    //最后一次点击时间
    private long UP_LAST_CLICK_TIME = 0;
    private long DOWN_LAST_CLICK_TIME = 0;
    private Thread plusThread;
    private Thread reduceThread;
    private int reduceIndex;
    private int plusIndex;
    private ScheduledThreadPoolExecutor service;

    @Override
    public void registerObserver(PowerObserver observer) {
        if (NotNull.isNotNull(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(PowerObserver observer) {
        if (NotNull.isNotNull(observer) && observers.contains(observers)) {
            observers.add(observer);
        }
    }

    @Override
    public void notifyObservers(String succeedStr) {
        for (PowerObserver observer :
                observers) {
            observer.updateRight(succeedStr);
        }
    }

    private String hourToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        if (time <= 0) {
            return "00:00";
        } else {
            hour = time / 3600;
            if (hour == 0) { // 不满一小时
                minute = time / 60 + 1; // 就换成分钟
                if (minute % 60 == 0) {
                    minute = 0;
                    hour = hour + 1;
                }
                timeStr = unitFormat(hour) + ":" + unitFormat(minute);
            } else {
                if (hour > 99) {
                    return "99:59";
                }
                minute = (time - hour * 3600) / 60 + 1;
                if (minute % 60 == 0) {
                    minute = 0;
                    hour = hour + 1;
                }
                timeStr = unitFormat(hour) + ":" + unitFormat(minute);
            }
        }
        return timeStr;
    }

    private String unitFormat(int i) {
        String retStr;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_rightdevice, container, false);
        reservationPon = ((ImageView) contentView.findViewById(R.id.reservationPon));
        ButterKnife.bind(this, contentView);
        initData();
        initUI();
        initEvent();
//        TcpSocket.getInstance().write(Protocol2.getReservationStatus(1));
        return contentView;
    }

    private void initData() {

        countDown = new CountDownTimer(4500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                hud.setLabel("切换超时");
                scheduleDismiss();
                if (NotNull.isNotNull(modeThread)) {
                    modeThread.interrupt();
                }
            }
        };

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

        buttons = new ArrayList<>();

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
        hud = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("切换中....").setCancellable(true).setAnimationSpeed(1).setDimAmount(0.5f);

        power_bt.setNormImageId(R.mipmap.btn_openkey_selected);
        power_bt.setDisabledImageId(R.mipmap.btn_openkey_normal);
        power_bt.setSelImageId(R.mipmap.btn_openkey_pressed);
        power_bt.setSelect(false);
        power_bt.setText("开关机");

        reservation_bt.setNormImageId(R.mipmap.btn_reservation_normal);
        reservation_bt.setSelImageId(R.mipmap.btn_reservation_pressed);
        reservation_bt.setDisabledImageId(R.mipmap.btn_reservation_disabled);
//        reservation_bt.setNormTextCoclor(R.color.colorBlack);
        reservation_bt.setText("预约");

        unreservation_bt.setNormImageId(R.mipmap.btn_cancel_normal);
        unreservation_bt.setSelImageId(R.mipmap.btn_cancel_pressed);
        unreservation_bt.setDisabledImageId(R.mipmap.btn_cancel_disabled);
//        unreservation_bt.setNormTextCoclor(R.color.colorBlack);
        unreservation_bt.setText("取消预约");

        device_change.setNormImageId(R.mipmap.btn_device_switching_normal);
        device_change.setSelImageId(R.mipmap.btn_device_switching_pressed);
        device_change.setDisabledImageId(R.mipmap.btn_device_switching_normal);
//        device_change.setNormTextCoclor(R.color.colorBlack);
        device_change.setText("设备切换");

        reservation_bt.buttonOnClickListener(this);
        unreservation_bt.buttonOnClickListener(this);
        power_bt.buttonOnClickListener(this);
        device_change.buttonOnClickListener(this);

        setButtonType(bt_0, R.mipmap.btn_baked_normal, R.mipmap.btn_baked_selected, R.mipmap.btn_baked_disabled, R.color.colorGrayText, "煎焗");
        setButtonType(bt_1, R.mipmap.btn_stew_normal, R.mipmap.btn_stew_selected, R.mipmap.btn_stew_disabled, R.color.colorGrayText, "焖烧");
        setButtonType(bt_2, R.mipmap.btn_r_temperature_normal, R.mipmap.btn_r_temperature_selected, R.mipmap.btn_r_temperature_disabled, R.color.colorGrayText, "保温");
        setButtonType(bt_3, R.mipmap.btn_quicyfry_normal, R.mipmap.btn_quicyfry_selected, R.mipmap.btn_quicyfry_disabled, R.color.colorGrayText, "爆炒");
        setButtonType(bt_4, R.mipmap.btn_fried_normal, R.mipmap.btn_fried_selected, R.mipmap.btn_fried_disabled, R.color.colorGrayText, "油炸");
        setButtonType(bt_5, R.mipmap.btn_slowfire_normal, R.mipmap.btn_slowfire_selected, R.mipmap.btn_slowfire_disabled, R.color.colorGrayText, "文火");
        flAdjust.addView(getAdView());
        dialogView = DialogView.getSingleton();
        dialogView.setContext(getActivity());
        setButtonStatus(false);//关机状态
    }

    private void setButtonStatus(boolean bl) {
        bt_0.setEnabledStatus(bl);
        bt_1.setEnabledStatus(bl);
        bt_2.setEnabledStatus(bl);
        bt_3.setEnabledStatus(bl);
        bt_4.setEnabledStatus(bl);
        bt_5.setEnabledStatus(bl);
        power_bt.setEnabledStatus(bl);
        reservation_bt.setEnabledStatus(bl);
        unreservation_bt.setEnabledStatus(bl);
        device_change.setEnabledStatus(bl);
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
        progressView.setMaxCount(100.0f);
        tvData = (TextView) adjustView.findViewById(R.id.fragment_adjust_date_tv);
        tvRightTemperature = (TextView) adjustView.findViewById(R.id.right_temperature_tv);
        tvMode = (TextView) adjustView.findViewById(R.id.fragment_adjust_moden_tv);
        lower_ib = (ImageButton) adjustView.findViewById(R.id.fragment_adjust_lower_ib);
        tvUnit = (TextView) adjustView.findViewById(R.id.unit);// 时间单位
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
        device_change.buttonOnClickListener(new ImageTopButton.ImageTopButtonOnClickListener() {
            @Override
            public void imageTopButtonOnClickListener(ImageTopButton button) {
                if (!NotNull.isNotNull(SharePreferencesUtils.getToken())) {
                    showTurnOn("请先登陆，才可操作");
                    return;
                }
                //设备切换
                JSONArray device_list = FileCache.get(getActivity()).getAsJSONArray("DEVICE_LIST");
                if (!NotNull.isNotNull(device_list)) {
                    return;
                }
                final List<BaseDevice> list = new ArrayList<>();
                for (int i = 0; i < device_list.length(); i++) {
                    String devicecode = null;
                    try {
                        devicecode = device_list.getString(i);
                        BaseDevice baseDevice = new BaseDevice();
                        baseDevice.setDeviceName("设备");
                        baseDevice.setDeviceId(devicecode);
                        list.add(baseDevice);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
//                if (list.size() == 1) {
//                    MyToast.showToastCustomerStyleText(getActivity(), "没有发现其他设备");
//                    return;
//                }
                PopWindowUtils popWindow = PopWindowUtils.getPopWindow();
                popWindow.showButtonPopwindow(getActivity(), list);
                popWindow.setOnItemclickListener(new PopWindowUtils.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        if (NotNull.isNotNull(listener)) {
                            listener.deviceListener(list.get(position).getDeviceId());
                            new HudHelper().hudShowChange(getActivity(), "正在切换中");
                            SharePreferencesUtils.setDeviceId(list.get(position).getDeviceId());
                        }
                    }
                });
            }
        });
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
            default:
                break;
        }
    }


    @Override
    public void imageTopButtonOnClickListener(ImageTopButton button) {
        if (NotNull.isNotNull(SharePreferencesUtils.getToken())) {
            if (code < 0) {
                button.setEnabledStatus(false);
                showTurnOn();
                return;
            }
        } else {
            showTurnOn("请先登陆，才可操作");
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
                    intent.putExtra(Common.HOME_FRAGMENT_SELECT_INDEX_KEY, 1);
                    startActivityForResult(intent, REVER_DEX);
                } else {
                    if (bsaeHudHelper == null) {
                        bsaeHudHelper = new HudHelper();
                    }
                    bsaeHudHelper.hudShow(getActivity(), "正在加载...");
                    clickRever = true;
                    if (thread == null || !thread.isAlive()) {
                        touTime = 0;
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
                }
                return;
            case R.id.fragment_rightdevice_unreservation_bt://取消预约
                if (!isReverBl) {
                    return;
                }
                if (bsaeHudHelper == null) {
                    bsaeHudHelper = new HudHelper();
                }
                bsaeHudHelper.hudShow(getActivity(), "正在加载...");
                if (thread == null || !thread.isAlive()) {
                    touTime1 = 0;
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
            default:
                break;
        }
        if (RightPowerOnOff) {

            hud.setLabel("切换中....");
            switch (id) {
                case (fragment_rightdevice_bt0)://煎焗
                    mode = 8;
                    preMode = 8;
                    break;
                case (R.id.fragment_rightdevice_bt1)://闷烧
                    mode = 9;
                    preMode = 9;
                    break;
                case (R.id.fragment_rightdevice_bt2)://保温
                    mode = 7;
                    preMode = 7;
                    break;
                case (R.id.fragment_rightdevice_bt3)://爆炒
                    mode = 10;
                    preMode = 10;
                    break;
                case (R.id.fragment_rightdevice_bt4)://油炸
                    mode = 11;
                    preMode = 11;
                    break;
                case (R.id.fragment_rightdevice_bt5)://文火
                    mode = 12;
                    preMode = 12;
                    break;
                default:
                    break;
            }
            if (mMode != mode) {
                if (!NotNull.isNotNull(modeThread) || !modeThread.isAlive()) {
                    modeThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                SystemClock.sleep(500);
                                TcpSocket.getInstance().write(Protocol2.moden(1, preMode));
                            }
                        }
                    });
                    modeThread.start();
                }

                TcpSocket.getInstance().write(Protocol2.timeStatus(1, mode));
                hadChangeModeBool = true;
            } else {
                hadChangeModeBool = false;
            }
            if (hadChangeModeBool) {
                hud.show();
            } else {
                hud.dismiss();
            }
            countDown.start();
            currentButton = button;
        } else {
            button.setEnabledStatus(false);
            if (NotNull.isNotNull(SharePreferencesUtils.getToken())) {
                showTurnOn();
            } else {
                showTurnOn("请先登陆，才可操作");
            }
        }

    }

    private void adjustRightData() {
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
                if (stall == -1) {
                    stall = 4;
                }
                cookMode8();
                tvPower.setText("1000W");
                notifyObservers("1000W");
                rightSumTime = rightTime;
                break;
            case 9://闷烧
                cookMode9();
                break;
            case 7://保温
                cookMode7();
                break;
            case 10://爆炒
                if (stall == -1) {
                    stall = 10;
                }
                cookMode10();
                tvTemperature.setText("280℃");
                tvPower.setText("2000W");
                notifyObservers("2000W");
                tvData.setText(hourToTime(rightTime));
                break;
            case 11://油炸
                if (stall == -1) {
                    stall = 3;
                }
                cookMode11();
                tvTemperature.setText("260℃");
                tvRightTemperature.setText("80℃");
                tvPower.setText("1800W");
                notifyObservers("1800W");
                tvData.setText(hourToTime(rightTime));
                break;
            case 12://文火
                if (stall == -1) {
                    stall = 5;
                }
                cookMode12();
                tvPower.setText("1200W");
                notifyObservers("1200W");
                tvData.setText(hourToTime(rightTime));
                break;
            default:
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
        ring_pv.setProgress(stall + 1);
        ring_pv.setMaxCount(4);
        tvUnit.setVisibility(View.VISIBLE);
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
        ring_pv.setProgress(stall + 1);
        ring_pv.setMaxCount(5);
        tvUnit.setVisibility(View.VISIBLE);
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
        ring_pv.setProgress(stall + 1);
        ring_pv.setMaxCount(12);
        tvUnit.setVisibility(View.VISIBLE);
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
        notifyObservers("100W");
//        rightTime = 2 * 3600;
        tvData.setText(hourToTime(rightTime));
//        rightSumTime = rightTime;
        tvUnit.setVisibility(View.VISIBLE);
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
        notifyObservers("1400W");
//        rightTime = 28 * 60;
        tvData.setText("Auto");
        tvUnit.setVisibility(View.GONE);
//        rightSumTime = rightTime;
    }

    //煎焗
    private void cookMode8() {
        unreservation_ib.setVisibility(View.GONE);
        reduce_ib.setVisibility(View.VISIBLE);
        plus_ib.setVisibility(View.VISIBLE);
        reservation_btn.setVisibility(View.GONE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(stall + 1);
        ring_pv.setMaxCount(6);
        tvData.setText("Auto");

        tvUnit.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_FIRST_USER) {
            if (data == null) {
                return;
            }
            final int hour = data.getIntExtra("HOUR", -1);
            final int second = data.getIntExtra("SECOND", -1);
//            rightTime = hour * 3600 + second * 60;
//            rightSumTime = rightTime;
//            tvData.setText(hourToTime(rightTime));
            sumBack = 5;
            if (bsaeHudHelper == null) {
                bsaeHudHelper = new HudHelper();
            }
            bsaeHudHelper.hudShow(getActivity(), "正在设置");
            if (timeThread == null || (timeThread != null && !timeThread.isAlive())) {
                timeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted() && sumBack > 0) {
                            sumBack--;
                            SystemClock.sleep(1000);
                            TcpSocket.getInstance().write(Protocol2.setCookTime(1, 1, mMode, (hour * 3600 + second * 60) * 1000));
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                bsaeHudHelper.hudHide();
                            }
                        });
                    }
                });
                timeThread.start();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.fragment_adjust_reservation_ib)://定时
                Intent intent = new Intent(getActivity(), ChoiceCookTimeActivity.class);
                intent.putExtra("mode", mMode);
                intent.putExtra("deviceId", 1);
                startActivityForResult(intent, RESULT_FIRST_USER);
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
            default:
                break;

        }

    }

    private void reducePower() {
        stall--;
        long currentTime = System.currentTimeMillis();
        if (currentTime - DOWN_LAST_CLICK_TIME > DOWN_MIN_CLICK_DELAY_TIME) {
            DOWN_LAST_CLICK_TIME = currentTime;
            if (stall < 2) {
                stall = 0;
            }
            reduceIndex = 5;
            if (bsaeHudHelper == null) {
                bsaeHudHelper = new HudHelper();
            }
            bsaeHudHelper.hudShow(getActivity(), "正在设置");
            if (!NotNull.isNotNull(reduceThread) || !reduceThread.isAlive()) {
                reduceThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted() && reduceIndex > 0) {
                            TcpSocket.getInstance().write(Protocol2.stall(1, stall));
                            reduceIndex--;
                            SystemClock.sleep(500);
                        }
                    }
                });
                reduceThread.start();
            }

        }
    }

    private void plusPower() {
        stall++;
        long currentTime = System.currentTimeMillis();
        if (currentTime - UP_LAST_CLICK_TIME > UP_MIN_CLICK_DELAY_TIME) {
            UP_LAST_CLICK_TIME = currentTime;
            if (stall > ring_pv.maxCount) {
                stall = ring_pv.maxCount;
            }
            plusIndex = 5;
            if (bsaeHudHelper == null) {
                bsaeHudHelper = new HudHelper();
            }
            bsaeHudHelper.hudShow(getActivity(), "正在设置");
            if (!NotNull.isNotNull(plusThread) || !plusThread.isAlive()) {
                plusThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted() && plusIndex > 0) {
                            TcpSocket.getInstance().write(Protocol2.stall(1, stall));
                            plusIndex--;
                            SystemClock.sleep(500);
                        }
                    }
                });
                plusThread.start();
            }

        }

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
            if (NotNull.isNotNull(orderObject)) {
                MProtocol.setOrder(orderObject);
            }
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
                        // 提示app会干扰弹窗
                        tipDialog(orderObject);
                        RPower = rightPower;
                        if (TextUtils.equals(rightPower, "1")) {//开机
                            RightPowerOnOff = true;
                            power_bt.setSelect(true);
                            bottom_ll.setVisibility(View.VISIBLE);
                            setButtonStatus(true);//关机状态
                            moden = orderObject.getString("moden");
                            mode = Integer.valueOf(moden);
                            mMode = mode;
                            if (!hadChangeModeBool) {
                                preMode = mode;
                            }
                            stall = Integer.valueOf(orderObject.getString("stall"));
                            ring_pv.setProgress(Integer.valueOf(orderObject.getString("stall")) + 1);

                            if (mode == 8) {//煎焗
                                tvPower.setText(CommonBean.JIANJU[stall]);
                                notifyObservers(CommonBean.JIANJU[stall]);
                            } else if (mode == 10) {//爆炒
                                tvPower.setText(CommonBean.BAOCHAO1[stall]);
                                notifyObservers(CommonBean.BAOCHAO1[stall]);
                                tvTemperature.setText(CommonBean.BAOCHAO2[stall]);
                            } else if (mode == 11) {//油炸
                                tvPower.setText(CommonBean.YOUZA1[stall]);
                                notifyObservers(CommonBean.YOUZA1[stall]);
                                tvTemperature.setText(CommonBean.YOUZA2[stall]);
                            } else if (mode == 12) {//文火
                                tvPower.setText(CommonBean.WENHUO[stall]);
                                notifyObservers(CommonBean.WENHUO[stall]);
                            }
                            changeRightMode(moden, orderObject);

                        } else {
                            RightPowerOnOff = false;
                            power_bt.setSelect(false);
                            notifyObservers("");
                            setButtonStatus(false);//关机状态
                            bottom_ll.setVisibility(View.INVISIBLE);
                            if (NotNull.isNotNull(select_bt_r)) {
                                select_bt_r.setEnabledStatus(false);
                            }
                            if (flAdjust.getVisibility() == View.VISIBLE) {
                                flAdjust.setVisibility(View.INVISIBLE);
                                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
                            }
                        }

                        if (Boolean.valueOf(orderObject.getString("reservation"))) {
                            isReverBl = true;
                            reservationPon.setVisibility(View.VISIBLE);
                            reservation_bt.setSelect(true);
                            unreservation_bt.setEnabledStatus(true);
                        } else {
                            reservationPon.setVisibility(View.GONE);
                            reservation_bt.setSelect(false);
                            isReverBl = false;
                            unreservation_bt.setEnabledStatus(false);
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
                            select_bt_r.setEnabledStatus(false);
                            notifyObservers("");
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
                        //----------------------------------------------------------------
                        if (preMode != mode) {
                            hadChangeMode = 1;
                        } else {
                            hadChangeMode = 0;
                        }
                        hud.setLabel("切换成功");
                        if (select_bt_r != null) {
                            select_bt_r.setSelect(false); // 更新之前的按钮显示图片
                        }
                        currentButton.setSelect(true); // 更新当前点击的按钮显示图片
                        select_bt_r = currentButton;
                        cancelCountDown();
                        scheduleDismiss();
                        if (NotNull.isNotNull(modeThread)) {
                            modeThread.interrupt();
                        }
                        //----------------------------------------------------------------
                        break;
                    case 4://档位设定返回
                        Log.d(TAG, "setMProtocol: ");
                        if (NotNull.isNotNull(plusThread)) {
                            plusThread.interrupt();
                            plusIndex = 0;
                        }
                        if (NotNull.isNotNull(reduceThread)) {
                            reduceThread.interrupt();
                            reduceIndex = 0;
                        }
                        bsaeHudHelper.hudHide();
                        moden = orderObject.getString("moden");
                        stall = Integer.valueOf(orderObject.getString("stall"));
                        mode = Integer.valueOf(moden);
                        changeRightMode(moden, orderObject);
                        break;
                    case 5://工作时间查询返回
                        double stopTime = orderObject.getDouble("stoptime");
                        if (allTime != stopTime) {
                            allTime = stopTime;
                        }
                        break;
                    case 6://设置/取消预约时间返回
                        String error = orderObject.getString("error");
                        if (Integer.valueOf(error) != 0) {
                            return;
                        }
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
                        if (!clickRever) {
                            return;//点击按钮才进去
                        }
                        clickRever = false;
                        if (thread != null) {
                            thread.interrupt();
                        }
                        moden = orderObject.getString("moden");
                        mode = Integer.valueOf(moden);
                        String bootTime = orderObject.getString("bootTime");//开机时间
                        String appointment = orderObject.getString("appointment");//工作时间
                        long aBootTime = Long.valueOf(bootTime);
                        long aAppointment = Long.valueOf(appointment);

                        Intent intent = new Intent(getActivity(), OrderTimeActivity.class);
                        intent.putExtra("LRIndex", 1);
                        intent.putExtra("moden", mode);
                        intent.putExtra("bootTime", aBootTime + System.currentTimeMillis());
                        intent.putExtra("appointment", aAppointment);
                        startActivity(intent);
                        if (thread != null) {//清空线程
                            thread = null;
                        }
                        break;
                    case 8://定时关机返回
                        String success8 = orderObject.getString("success");
                        if (TextUtils.equals("true", success8)) {
                            tvData.setText(hourToTime(rightTime));
                            if (timeThread != null) {
                                timeThread.interrupt();
                            }
                            if (bsaeHudHelper == null) {
                                bsaeHudHelper = new HudHelper();
                            }
                            bsaeHudHelper.hudShow(getActivity(), "设置成功");
                        }
                        bsaeHudHelper.hudHide();
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //改变模式状态
    private void changeRightMode(String moden, JSONObject orderObject) throws JSONException {
//        progressView.setMaxCount(100.0f);
//        progressView.setCurrentCount(100);

        int currentMode = Integer.valueOf(moden);
        if (currentMode != 4 && currentMode != 5) {
            TcpSocket.getInstance().write(Protocol2.timeStatus(1, currentMode));
        }
        rightSumTime = (int) allTime / 1000; // 毫秒转成秒

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
                        if (progress != 0) {
                            tvPower.setText(CommonBean.JIANJU[progress]);
                        }
                        notifyObservers(CommonBean.JIANJU[progress]);
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
                        int progress = Integer.valueOf(stall);
                        tvPower.setText(CommonBean.BAOCHAO1[progress]);
                        notifyObservers(CommonBean.BAOCHAO1[progress]);
                        tvTemperature.setText(CommonBean.BAOCHAO2[progress]);
                    }
                    break;
                case 11:
                    cookMode11();
                    setModenButton(R.id.fragment_rightdevice_bt4);
                    String stall12 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall12)) {
//                        ring_pv.setProgress(Integer.valueOf(stall12));//当前档位
                        int progress = Integer.valueOf(stall12);
                        if (progress != 0) {
                            tvPower.setText(CommonBean.YOUZA1[progress]);
                        }
                        notifyObservers(CommonBean.YOUZA1[progress]);
                        tvTemperature.setText(CommonBean.YOUZA2[progress]);
                    }
                    break;
                case 12://文火
                    cookMode12();
                    setModenButton(R.id.fragment_rightdevice_bt5);
                    String stall13 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall13)) {
//                        ring_pv.setProgress(Integer.valueOf(stall13));//当前档位
                        int progress = Integer.valueOf(stall13);
                        if (progress != 0) {
                            tvPower.setText(CommonBean.WENHUO[progress]);
                        }
                        notifyObservers(CommonBean.WENHUO[progress]);
                    }
                    break;
                default:
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
    }

    public void setCode(int code) {
        this.code = code;
        if (code == -1 || code == -3) {
            setButtonStatus(false);
            RightPowerOnOff = false;
            if (NotNull.isNotNull(power_bt)) {
                power_bt.setSelect(false);
                power_bt.setNormImageId(R.mipmap.btn_openkey_normal);
            }
            if (bottom_ll.getVisibility() == View.VISIBLE) {
                bottom_ll.setVisibility(View.INVISIBLE);
            }

            if (flAdjust.getVisibility() == View.VISIBLE) {
                if (NotNull.isNotNull(select_bt_r)) {
                    select_bt_r.setSelect(false);
                }
                flAdjust.setVisibility(View.INVISIBLE);
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
            }
        }
    }

    private void showTurnOn() {
        if (getActivity().isFinishing()) {
            return;
        }
        View view = dialogView.showCustomDialong(R.layout.dialong_view);
        ImageView deleteView = (ImageView) view.findViewById(R.id.dialong_delete);
        deleteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismissDialong();
            }
        });
        TextView dialongText = (TextView) view.findViewById(R.id.dialongText);
        dialongText.setText("请先打开电磁炉");
    }

    private void showTurnOn(String msg) {
        final DialogView dialogView = DialogView.getSingleton();
        dialogView.setContext(getActivity());
        View view = dialogView.showCustomDialong(R.layout.dialog_power_change);
        TextView tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        TextView tvContent = (TextView) view.findViewById(R.id.tvContent);
        TextView alert_title = (TextView) view.findViewById(R.id.alert_title);
        alert_title.setText("提示");
        tvContent.setText(msg);
        TextView tvCommit = (TextView) view.findViewById(R.id.tvCommit);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismissDialong();
            }
        });
        tvCommit.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                dialogView.dismissDialong();
            }
        });

    }

    /**
     * 隐藏hud
     */
    private void scheduleDismiss() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hud.dismiss();
            }
        }, 700);
    }

    /**
     * 在fragment页面按返回键也要cancel掉倒计时
     */
    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    if (countDown != null) {
                        cancelCountDown();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    /**
     * 结束倒计时
     */
    public void cancelCountDown() {
        cancelCountDownHandler.sendMessage(Message.obtain());
    }

    /**
     * 开启电磁炉时的APP干扰提示
     *
     * @param orderObject
     * @throws JSONException
     */
    private void tipDialog(JSONObject orderObject) throws JSONException {
        int currentPower = orderObject.getInt("power");
        if (globalPower != currentPower) {
            powerStateChange = true;
        } else {
            powerStateChange = false;
        }
        globalPower = currentPower;
        if (powerStateChange && globalPower == 1) {
            // 提示窗弹出
            final DialogView dialogView = DialogView.getSingleton();
            dialogView.setContext(getActivity());
            if (dialogView.isShow()) {
                return;
            }
            View view = dialogView.showCustomDialong(R.layout.dialong_tips);
            Button btnKnow = (Button) view.findViewById(R.id.btn_know);
            btnKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogView.dismissDialong();
                }
            });
        }
    }


    public interface OnDeviceListener {
        void deviceListener(String deviceName);
    }

    public void setOnDeviceListener(OnDeviceListener listener) {
        this.listener = listener;
    }
}
