package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
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
import java.util.concurrent.ScheduledExecutorService;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.app.Activity.RESULT_FIRST_USER;
import static com.chad.library.adapter.base.listener.AbstractSimpleClickListener.TAG;

/**
 * Created by CMQ on 2017/6/27.
 */

public class LeftDeviceFragment1 extends Fragment implements ImageTopButton.ImageTopButtonOnClickListener, View.OnClickListener, PowerObserval {
    private static final int REVER_DEX = 123;
    View contentView;
    List<ImageTopButton> buttons;
    FragmentManager fragmentManager;
    private static String effectStr0 = "";
    private static String effectStr1 = "";
    private LeftDeviceFragment1 adjustFragment;

    private int mode = -1;
    @BindView(R.id.flAdjust)
    RelativeLayout flAdjust;
    @BindView(R.id.fragment_leftdevice_power_bt)
    ImageTopButton power_bt;
    @BindView(R.id.fragment_leftdevice_reservation_bt)
    ImageTopButton reservation_bt;
    @BindView(R.id.fragment_leftdevice_change)
    ImageTopButton device_change;
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
    private int leftTime = 3600 * 2;
    private HudHelper bsaeHudHelper;
    private final String[] modeStr = {"煲粥", "煲汤", "煮饭", "烧水", "火锅", "煎炒", "烤炸", "保温", "煎焗", "闷烧", "保温", "爆炒", "油炸", "文火"};
    private final int TIME_COOK = 1;
    private final int TIME_OUT = 2;
    // 更新进度条用的
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME_COOK:
                    if (mode == 4 || mode == 5 || mode == 6 || mode == 7) {
                        tvData.setText(hourToTime(leftTime));
                        if (leftSumTime != 0) {
                            float currentCount = leftTime * 100 / leftSumTime;
                            progressView.setCurrentCount(100 - currentCount);
                        } else {
                            progressView.setCurrentCount(0);
                        }

                    } else {
                        progressView.setCurrentCount(100.0f);
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
    // 延迟发送检查时间指令用的
    private boolean leftPowerOnOff;
    private int leftSumTime;
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
    private List<Moden> modenList;
    private com.goockr.inductioncooker.models.BaseProtocol MProtocol;
    private ImageView reservationPon;//红点
    private final int POWER_OFF = 0;
    private final int POWER_ON = 1;
    private String lPower;
    private int stall;
    private int revermode = -1;
    private Long aBootTime = 0L;
    private Long aAppointment = 0L;
    private boolean isReverBl = false;
    private int code;
    private boolean clickRever = false;
    private Thread thread;
    private int mMode = 0;
    private int touTime = 0;
    private int touTime1 = 0;
    private DialogView dialogView;
    private double allTime = 0;
    private TextView tvUnit;
    private JSONObject globalJson;
    private int lastTime;
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
    private ScheduledExecutorService theadPool;
    private Thread modeThread;

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
            observer.updateLeft(succeedStr);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_leftdevice1, container, false);
        reservationPon = ((ImageView) contentView.findViewById(R.id.reservationPon));
        ButterKnife.bind(this, contentView);
        initData();
        initUI();
        initEvent();
        return contentView;
    }


    /**
     * 默认数据
     */
    private void initData() {
        countDown = new CountDownTimer(3000, 1000) {
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

        fragmentManager = getFragmentManager();

        List<Moden> modenList = new ArrayList<Moden>();

        JSONArray jsonArray;
        try {
            jsonArray = ReadAdssetsJson.readJson("leftdevice").getJSONArray("value");
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
        buttons.add(bt_6);
        buttons.add(bt_7);

        for (int i = 0; i < buttons.size(); i++) {
            buttons.get(i).moden = modenList.get(i);
        }


    }

    private void initUI() {
        hud = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setLabel("切换中....").setCancellable(true).setAnimationSpeed(1).setDimAmount(0.5f);

        power_bt.setNormImageId(R.mipmap.btn_openkey_normal);
        power_bt.setDisabledImageId(R.mipmap.btn_openkey_pressed);
        power_bt.setSelImageId(R.mipmap.btn_openkey_selected);
//        power_bt.setNormTextCoclor(R.color.colorBlack);
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

        setButtonType(bt_0, R.mipmap.btn_soup_normal, R.mipmap.btn_soup_pressed, R.mipmap.btn_soup_disabled, R.color.colorGrayText, "煲粥");
        setButtonType(bt_1, R.mipmap.btn_porridge_normal, R.mipmap.btn_porridge_selected, R.mipmap.btn_porridge_disabled, R.color.colorGrayText, "煲汤");
        setButtonType(bt_2, R.mipmap.btn_rice_normal, R.mipmap.btn_rice__selected, R.mipmap.btn_rice_disabled, R.color.colorGrayText, "煮饭");
        setButtonType(bt_3, R.mipmap.btn_water_normal, R.mipmap.btn_water__selected, R.mipmap.btn_water_disabled, R.color.colorGrayText, "烧水");
        setButtonType(bt_4, R.mipmap.brn_hotpot__normal, R.mipmap.brn_hotpot_selected, R.mipmap.brn_hotpot_disabled, R.color.colorGrayText, "火锅");
        setButtonType(bt_5, R.mipmap.btn_fry_normal, R.mipmap.btn_fry_selected, R.mipmap.btn_fry_disabled, R.color.colorGrayText, "煎炒");
        setButtonType(bt_6, R.mipmap.btn_baked_fried_normal, R.mipmap.btn_baked_fried__selected, R.mipmap.btn_baked_fried_disabled, R.color.colorGrayText, "烤炸");
        setButtonType(bt_7, R.mipmap.btn_temperature_normal, R.mipmap.btn_temperature__selected, R.mipmap.btn_temperature_disabled, R.color.colorGrayText, "保温");
        flAdjust.addView(getAdView());
        dialogView = DialogView.getSingleton();
        dialogView.setContext(getActivity());
        setButtonStatus(false);// 默认是关机状态，显示对应的图片
    }

    private void setButtonType(ImageTopButton bt, int normImageId, int selImageId, int disableImageId, int normTextColor, String text) {
        bt.setNormImageId(normImageId);
        bt.setSelImageId(selImageId);
        bt.setDisabledImageId(disableImageId);
        bt.setNormTextCoclor(normTextColor);
        bt.setText(text);
    }

    private void setButtonStatus(boolean bl) {
        bt_0.setEnabledStatus(bl);
        bt_1.setEnabledStatus(bl);
        bt_2.setEnabledStatus(bl);
        bt_3.setEnabledStatus(bl);
        bt_4.setEnabledStatus(bl);
        bt_5.setEnabledStatus(bl);
        bt_6.setEnabledStatus(bl);
        bt_7.setEnabledStatus(bl);
        power_bt.setEnabledStatus(bl);
        reservation_bt.setEnabledStatus(bl);
        unreservation_bt.setEnabledStatus(bl);
        device_change.setEnabledStatus(bl);
    }

    /**
     * 底部弹窗布局
     *
     * @return
     */
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
        progressView.setCurrentCount(100.0f);
        tvData = (TextView) adjustView.findViewById(R.id.fragment_adjust_date_tv);// 时间/自动
        tvUnit = (TextView) adjustView.findViewById(R.id.unit);// 单位
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

    private void initEvent() {
        for (ImageTopButton bt : buttons) {
            bt.setClickable(true);
            bt.buttonOnClickListener(this); // 每个按钮都添加一样的回调ImageTopButtonOnClickListener
        }
        device_change.buttonOnClickListener(new ImageTopButton.ImageTopButtonOnClickListener() {
            @Override
            public void imageTopButtonOnClickListener(ImageTopButton button) {
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

    /**
     * tab栏上放的橙色箭头按钮，点击会弹出进度框
     *
     * @param v
     */
    @OnClick(R.id.fragment_leftdevice_bottomview)
    public void OnClick(View v) {
        switch (v.getId()) {
            case (R.id.fragment_leftdevice_bottomview):
                flAdjust.setVisibility(View.VISIBLE);
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_open));
                adjustLeftData();
                break;
            default:
                break;
        }

    }

    /**
     * 所有按钮的点击事件
     *
     * @param button
     */
    @Override
    public void imageTopButtonOnClickListener(ImageTopButton button) {
        if (code < 0) {
            button.setEnabledStatus(false);
            showTurnOn();
            return;
        }
        int id = button.getId();

        // 开关机、预约、取消预约三个按钮点击事件
        switch (id) {
            case R.id.fragment_leftdevice_power_bt://电源按钮

                if (leftPowerOnOff) {//关机
                    //指令
                    TcpSocket.getInstance().write(Protocol2.powerStatus(0, POWER_OFF));
                } else {
                    //开机
                    TcpSocket.getInstance().write(Protocol2.powerStatus(0, POWER_ON));
                }
                return;
            case R.id.fragment_leftdevice_reservation_bt://预约
                if (!isReverBl) {
                    Intent intent = new Intent(getActivity(), ReservationActivity.class);
                    intent.putExtra(Common.HOME_FRAGMENT_SELECT_INDEX_KEY, 0);
                    startActivityForResult(intent, REVER_DEX);
                } else {
                    if (bsaeHudHelper == null) {
                        bsaeHudHelper = new HudHelper();
                    }
                    bsaeHudHelper.hudShow(getActivity(), "正在加载...");
                    clickRever = true;
                    if (thread == null||!thread.isAlive()) {
                        touTime=0;
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while (!Thread.currentThread().isInterrupted() && touTime < 10) {
                                    touTime++;
                                    SystemClock.sleep(500);
                                    TcpSocket.getInstance().write(Protocol2.getReservationStatus(0));
                                }
                            }
                        });
                        thread.start();
                    }
                }
                return;
            case R.id.fragment_leftdevice_unreservation_bt://取消预约
                if (!isReverBl) {
                    return;
                }
                if (bsaeHudHelper == null) {
                    bsaeHudHelper = new HudHelper();
                }
                bsaeHudHelper.hudShow(getActivity(), "正在加载...");
                if (thread == null||!thread.isAlive()) {
                    touTime1=0;
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            final Handler handler = new Handler(Looper.getMainLooper());
                            while (!thread.isInterrupted() && touTime1 < 10) {
                                SystemClock.sleep(500);
                                touTime1++;
                                TcpSocket.getInstance().write(Protocol2.setReservation(0, mode, 0, 0, 0));
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
                    });
                    thread.start();
                }
                return;
            default:
                break;
        }

        // 只有电源通了的情况下才可以执行
        if (leftPowerOnOff) {
            hud.setLabel("切换中....");
            switch (id) {
                case R.id.fragment_leftdevice_bt0://煲粥
                    mode = 0;
                    preMode = 0;
                    break;
                case R.id.fragment_leftdevice_bt1://煲汤
                    mode = 1;
                    preMode = 1;
                    break;
                case R.id.fragment_leftdevice_bt2://煮饭
                    mode = 2;
                    preMode = 2;
                    break;
                case R.id.fragment_leftdevice_bt3://烧水
                    mode = 3;
                    preMode = 3;
                    break;
                case R.id.fragment_leftdevice_bt4://火锅
                    mode = 4;
                    preMode = 4;
                    break;
                case R.id.fragment_leftdevice_bt5://煎炒
                    mode = 5;
                    preMode = 5;
                    break;
                case R.id.fragment_leftdevice_bt6://烤炸
                    mode = 6;
                    preMode = 6;
                    break;
                case R.id.fragment_leftdevice_bt7://保温
                    mode = 7;
                    preMode = 7;
                    break;
                default:
                    break;
            }
            // 当前模式发生变化时，才会用socket发送数据检查状态
            if (mMode != mode) {
                if (!NotNull.isNotNull(modeThread) || !modeThread.isAlive()) {
                    modeThread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                SystemClock.sleep(500);
                                TcpSocket.getInstance().write(Protocol2.moden(0, preMode));
                            }
                        }
                    });
                    modeThread.start();
                }

                TcpSocket.getInstance().write(Protocol2.timeStatus(0, mode));
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
            showTurnOn();
        }
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

    private void adjustLeftData() {
//        progressView.setMaxCount(100.0f);
//        progressView.setCurrentCount(100);
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
                stall = 8;
                cookMode4();
                break;
            case 5://煎炒
                stall = 8;
                cookMode5();
                tvTemperature.setText("260℃");
                tvPower.setText("1600W");
                notifyObservers("1600W");
                tvData.setText(hourToTime(leftTime));
                break;
            case 6://烤炒
                stall = 3;
                cookMode6();
                tvTemperature.setText("260℃");
                tvRightTemperature.setText("80℃");
                tvPower.setText("1600W");
                notifyObservers("1600W");
                tvData.setText(hourToTime(leftTime));
                break;
            case 7://保温
                cookMode7();
                tvTemperature.setText("80℃");
                tvPower.setText("100W");
                notifyObservers("100W");
                tvData.setText(hourToTime(leftTime));
                break;
            default:
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_FIRST_USER) {
            if (data == null) {
                return;
            }
            final int hour = data.getIntExtra("HOUR", -1);
            final int second = data.getIntExtra("SECOND", -1);
            sumBack = 5;
            hubShow("正在设置");
            if (timeThread == null || (timeThread != null && !timeThread.isAlive())) {
                timeThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted() && sumBack > 0) {
                            sumBack--;
                            SystemClock.sleep(1000);
                            TcpSocket.getInstance().write(Protocol2.setCookTime(1, 0, mMode, (hour * 3600 + second * 60) * 1000));
                        }
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                hubDismiss();
                            }
                        });
                    }
                });
                timeThread.start();
            }
        }
    }

    //保温
    private void cookMode7() {
        unreservation_ib.setVisibility(View.GONE);
        reduce_ib.setVisibility(View.GONE);
        plus_ib.setVisibility(View.GONE);
        reservation_btn.setVisibility(View.VISIBLE);
        tvALine.setVisibility(View.VISIBLE);
        tvTemperature.setVisibility(View.VISIBLE);
        tvUnit.setVisibility(View.VISIBLE);
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
        tvUnit.setVisibility(View.VISIBLE);
        ring_pv.setProgress(stall + 1);
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
        tvUnit.setVisibility(View.VISIBLE);
        ring_pv.setProgress(stall + 1);
        ring_pv.setMaxCount(12);
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
        tvUnit.setVisibility(View.VISIBLE);
        ring_pv.setProgress(stall + 1);
        ring_pv.setMaxCount(12);
        tvPower.setText("1600W");
        notifyObservers("1600W");
        tvData.setText(hourToTime(leftTime));
    }

    //烧水
    private void cookMode3() {
        unreservation_ib.setVisibility(View.GONE);
        reduce_ib.setVisibility(View.GONE);
        plus_ib.setVisibility(View.GONE);
        reservation_btn.setVisibility(View.GONE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(9);
        ring_pv.setMaxCount(12);
        tvPower.setText("Auto");
        notifyObservers("Auto");
        tvData.setText("Auto");
        tvUnit.setVisibility(View.GONE);
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
        ring_pv.setMaxCount(12);
        tvPower.setText("Auto");
        notifyObservers("Auto");
        tvData.setText("Auto");
        tvUnit.setVisibility(View.GONE);
    }

    //煲粥、煲汤
    private void cookMode1() {
        unreservation_ib.setVisibility(View.GONE);
        reservation_btn.setVisibility(View.GONE);
        reduce_ib.setVisibility(View.GONE);
        plus_ib.setVisibility(View.GONE);
        tvALine.setVisibility(View.GONE);
        tvTemperature.setVisibility(View.GONE);
        tvRightTemperature.setVisibility(View.GONE);
        ring_pv.setProgress(7);
        ring_pv.setMaxCount(12);
        tvPower.setText("Auto");
        tvData.setText("Auto");
        tvUnit.setVisibility(View.GONE);
    }

    private void reducePower() {
        if (stall < 2) {
            TcpSocket.getInstance().write(Protocol2.stall(0, 0));
        } else {
            TcpSocket.getInstance().write(Protocol2.stall(0, stall - 1));
        }
    }

    private void plusPower() {
        if (stall > ring_pv.maxCount) {
            TcpSocket.getInstance().write(Protocol2.stall(0, ring_pv.maxCount));
        } else {
            TcpSocket.getInstance().write(Protocol2.stall(0, stall + 1));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.fragment_adjust_reservation_ib)://定时
                Intent intent = new Intent(getActivity(), ChoiceCookTimeActivity.class);
                intent.putExtra("mode", mMode);
                intent.putExtra("deviceId", 0);
                startActivityForResult(intent, RESULT_FIRST_USER);
                break;
            case (R.id.fragment_adjust_unreservation_ib)://取消定时，关闭
                leftTime = 0;
                TcpSocket.getInstance().write(Protocol2.setCookTime(0, 0, mMode, 0));// 这里关闭定时
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

    public void setCode(int code) {
        this.code = code;
        if (code == -1 || code == -3) {//关机代码
            setButtonStatus(false);
            leftPowerOnOff = false;
            if (NotNull.isNotNull(power_bt)) {
                power_bt.setSelect(false);
            }
            if (bottom_ll.getVisibility() == View.VISIBLE) {
                bottom_ll.setVisibility(View.INVISIBLE);
            }
            if (flAdjust.getVisibility() == View.VISIBLE) {
                if (NotNull.isNotNull(select_bt)) {
                    select_bt.setSelect(false);
                }
                flAdjust.setVisibility(View.INVISIBLE);
                flAdjust.setAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.adujst_close));
            }
        }
    }


    public interface LeftDeviceFragmentCallback {
        void leftDeviceFragmentButtonClick(ImageTopButton button);
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
            BaseProtocol mProtocol = null;
            if (NotNull.isNotNull(orderObject)) {
                mProtocol = new Gson().fromJson(read, BaseProtocol.class);
                mProtocol.setOrder(orderObject);
            }
            int code = Integer.valueOf(orderObject.getString("code"));
            String moden = null;
            switch (code) {
                case 0://主机接收指令更新状态
                    TcpSocket.getInstance().write(Protocol2.statusCheck(0, 0));
                    break;
                case 1://控制主机机返回
                    //左炉开关状态
                    String leftPower = orderObject.getString("power");
                    // 提示app会干扰弹窗
                    tipDialog(orderObject);
                    lPower = leftPower;
                    if (TextUtils.equals(leftPower, "1")) {//开机
                        setButtonStatus(true);
                        leftPowerOnOff = true;
                        power_bt.setSelect(true);
                        bottom_ll.setVisibility(View.VISIBLE);

                        moden = orderObject.getString("moden");
                        mode = Integer.valueOf(moden);
                        mMode = mode;
                        if (!hadChangeModeBool) {
                            preMode = mode;
                        }
                        stall = Integer.valueOf(orderObject.getString("stall"));
                        if (mode == 4) {//火锅
                            tvPower.setText(CommonBean.HOTPOTSTRS[stall]);
                            notifyObservers(CommonBean.HOTPOTSTRS[stall]);
                        } else if (mode == 5) {//煎炒
                            tvPower.setText(CommonBean.FRYOIlSTRS1[stall]);
                            notifyObservers(CommonBean.FRYOIlSTRS1[stall]);
                            tvTemperature.setText(CommonBean.FRYOIlSTRS2[stall]);
                        } else if (mode == 6) {//烤炸
                            tvPower.setText(CommonBean.KAOZA1[stall]);
                            notifyObservers(CommonBean.KAOZA1[stall]);
                            tvTemperature.setText(CommonBean.KAOZA2[stall]);
                        }
                        globalJson = orderObject;
                        changeLeftMode(moden, orderObject);
                    } else {
                        setButtonStatus(false);
                        leftPowerOnOff = false;
                        power_bt.setSelect(false);
                        notifyObservers("");
                        bottom_ll.setVisibility(View.INVISIBLE);
                        if (flAdjust.getVisibility() == View.VISIBLE) {
                            if (NotNull.isNotNull(select_bt)) {
                                select_bt.setEnabledStatus(false);
                            }
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
                        isReverBl = false;
                        reservationPon.setVisibility(View.GONE);
                        reservation_bt.setSelect(false);
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
                        leftPowerOnOff = true;
                        power_bt.setSelect(true);
                        bottom_ll.setVisibility(View.VISIBLE);

                    } else if (NotNull.isNotNull(power) && TextUtils.equals(power, "0")) {//关机
                        String error = orderObject.getString("error");
                        if (!TextUtils.equals(error, "0")) {
                            Toast.makeText(getActivity(), "连接异常", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        notifyObservers("");
                        leftPowerOnOff = false;
                        power_bt.setSelect(false);
                        bottom_ll.setVisibility(View.INVISIBLE);
                        select_bt.setEnabledStatus(false);
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
                    if (preMode != mode) {
                        hadChangeMode = 1;
                    } else {
                        hadChangeMode = 0;
                    }
                    hud.setLabel("切换成功");
                    if (select_bt != null) {
                        select_bt.setSelect(false); // 更新之前的按钮显示图片
                        currentButton.setSelect(true); // 更新当前点击的按钮显示图片
                        select_bt = currentButton;
                    }
                    cancelCountDown();
                    scheduleDismiss();
                    if (NotNull.isNotNull(modeThread)) {
                        modeThread.interrupt();
                    }
                    break;
                case 4://档位设定返回
                    Log.d(TAG, "setMProtocol: ");
                    moden = orderObject.getString("moden");
                    stall = Integer.valueOf(orderObject.getString("stall"));
                    mode = Integer.valueOf(moden);
                    break;
                case 5://工作时间查询返回
                    Log.d(TAG, "setMProtocol: ");
                    double stoptime = orderObject.getDouble("stoptime");
                    if (allTime != stoptime) {
                        allTime = stoptime;
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
                    if (NotNull.isNotNull(bsaeHudHelper)) {
                        bsaeHudHelper.hudHide();
                    }
                    if (thread != null) {
                        thread.interrupt();
                    }
                    moden = orderObject.getString("moden");
                    mode = Integer.valueOf(moden);
                    String bootTime = orderObject.getString("bootTime");//开机时间
                    String appointment = orderObject.getString("appointment");//工作时间
                    aBootTime = Long.valueOf(bootTime);
                    aAppointment = Long.valueOf(appointment);
                    Intent intent = new Intent(getActivity(), OrderTimeActivity.class);
                    intent.putExtra("LRIndex", 0);
                    intent.putExtra("moden", mode);
                    intent.putExtra("bootTime", aBootTime + System.currentTimeMillis());
                    intent.putExtra("appointment", aAppointment);
                    startActivity(intent);
                    if (thread != null) {//清空线程
                        thread = null;
                    }
                    break;
                case 8://定时关机返回
                    handler.removeCallbacksAndMessages(null);
                    String success8 = orderObject.getString("success");
                    if (TextUtils.equals("true", success8)) {
                        tvData.setText(hourToTime(leftTime));
                        if (timeThread != null) {
                            timeThread.interrupt();
                        }
                    }
                    hud.dismiss();
                    break;
                default:
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            ImageView dialongDelete = (ImageView) view.findViewById(R.id.dialong_delete);
            dialongDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogView.dismissDialong();
                }
            });
            btnKnow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogView.dismissDialong();
                }
            });
        }
    }

    private void setModenButton(int Resin) {
        ImageTopButton button = (ImageTopButton) contentView.findViewById(Resin);
        if (select_bt != null) {
            if (select_bt != button) {
                select_bt.setSelect(false);
            }
        }
        select_bt = button;
        button.setSelect(true);
        if (flAdjust.getVisibility() != View.VISIBLE) {
            if (bsaeHudHelper != null) {
                bsaeHudHelper.hudHide();
            }
        }
    }

    //改变模式状态
    private void changeLeftMode(String moden, JSONObject orderObject) throws JSONException {
        int currentMode = Integer.valueOf(moden);
        if (currentMode == 4 || currentMode == 5 || currentMode == 6 || currentMode == 7) {
            TcpSocket.getInstance().write(Protocol2.timeStatus(0, currentMode));
        }

        leftSumTime = (int) allTime / 1000; // 毫秒转成秒
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
                        tvPower.setText(CommonBean.HOTPOTSTRS[progress]);
                        notifyObservers(CommonBean.HOTPOTSTRS[progress]);
                    }

                    break;
                case 5://煎炒
                    cookMode5();
                    setModenButton(R.id.fragment_leftdevice_bt5);
                    String stall5 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall5)) {
                        int progress = Integer.valueOf(stall5);
                        if (progress >= 0) {
                            tvPower.setText(CommonBean.FRYOIlSTRS1[progress]);
                        }
                        notifyObservers(CommonBean.FRYOIlSTRS1[progress]);
                        tvTemperature.setText(CommonBean.FRYOIlSTRS2[progress]);
                    }
                    break;
                case 6://烤炸
                    cookMode6();
                    setModenButton(R.id.fragment_leftdevice_bt6);
                    String stall6 = orderObject.getString("stall");
                    if (NotNull.isNotNull(stall6)) {
                        int progress = Integer.valueOf(stall6);
                        if (progress >= 0) {
                            tvPower.setText(CommonBean.KAOZA1[progress]);
                        }
                        notifyObservers(CommonBean.KAOZA1[progress]);
                        tvTemperature.setText(CommonBean.KAOZA2[progress]);
                    }
                    break;
                case 7:
                    cookMode7();
                    setModenButton(R.id.fragment_leftdevice_bt7);
                    break;
                default:
                    break;
            }
            int worktime = Integer.valueOf(orderObject.getString("worktime"));
            lastTime = worktime;
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

    private void showTurnOn() {
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
        if (select_bt != null) {
            select_bt.setEnabledStatus(false);
        }

    }

    public CountDownTimer getCountDowmTimer() {
        return countDown;
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

    private void hubDismiss() {
        if (NotNull.isNotNull(hud) && hud.isShowing()) {
            hud.dismiss();
        }
    }

    private void hubShow(String text) {
        if (!NotNull.isNotNull(hud)) {
            hud = KProgressHUD.create(getActivity()).setStyle(KProgressHUD.Style.SPIN_INDETERMINATE).setCancellable(true).setAnimationSpeed(1).setDimAmount(0.5f);
        }
        hud.setLabel(text);
        hud.show();
    }

    /**
     * 结束倒计时
     */
    public void cancelCountDown() {

        cancelCountDownHandler.sendMessage(Message.obtain());
    }

    public interface OnDeviceListener {
        void deviceListener(String deviceName);
    }

    public void setOnDeviceListener(OnDeviceListener listener) {
        this.listener = listener;
    }
}
