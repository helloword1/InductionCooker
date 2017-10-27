package com.goockr.inductioncooker.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.listener.CustomListener;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.OrderTimeActivity;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.lib.socket.Protocol2;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.BarProgress;
import com.goockr.inductioncooker.view.TimePickerView0;
import com.goockr.ui.view.helper.HudHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chad.library.adapter.base.listener.AbstractSimpleClickListener.TAG;

/**
 * Created by CMQ on 2017/7/3.
 * 设置开机时间
 */

public class ReservationBootFragment extends Fragment {

    View contentView;
    private TimePickerView0 pvCustomTime;

    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_reservationroot_bar_pv)
    BarProgress bar_pv;
    @BindView(R.id.fragment_reservationroot_date_fl)
    FrameLayout date_fl;
    private String mode;
    private Date time;
    private int mMode;
    private int deviceId;
    private final String[] modeStr = {"煲粥", "煲汤", "煮饭", "烧水", "火锅", "煎炒", "烤炸", "保温", "煎焗", "闷烧", "保温","爆炒", "油炸", "文火"};
    private Thread thread;
    private HudHelper bsaeHudHelper;
    private boolean canRever;

    public static final ReservationBootFragment newInstance(String mode) {
        ReservationBootFragment fragment = new ReservationBootFragment();
        Bundle bundle = new Bundle();
        bundle.putString("MODE", mode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getArguments().getString("MODE");
        for (int i = 0; i < modeStr.length; i++) {
            if (TextUtils.equals(mode, modeStr[i])) {
                mMode = i;
                deviceId = i > 7 ? 1 : 0;
                if (TextUtils.equals(mode,modeStr[7])){
                    mMode=7;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment_reservationroot, container, false);

        ButterKnife.bind(this, contentView);
        initUI();
        return contentView;
    }

    private void initUI() {
        List<String> tips = new ArrayList<String>();
       switch (mMode){
           case 4:
           case 5:
           case 6:
           case 7:
           case 11:
           case 12:
           case 13:
               tips.add("1.选择功能模式");
               tips.add("2.预约开机时间");
               tips.add("3.预约定时时间");
               bar_pv.setTips(tips);
               bar_pv.setMaxCount(3);
               bar_pv.setProgress(2);
               canRever =true;
               break;
           case 0:
           case 1:
           case 2:
           case 3:
           case 8:
           case 9:
               tips.add("1.选择功能模式");
               tips.add("2.预约定时时间");
               bar_pv.setTips(tips);
               bar_pv.setMaxCount(2);
               bar_pv.setProgress(2);
               canRever =false;
               break;
           default:
               break;
       }

        initDatePickerView();
    }

    private void initDatePickerView() {
        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        int hours = selectedDate.getTime().getHours();
        Log.d(TAG, "onTimeSelect: " + hours);
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);

        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView0.Builder(getActivity(), new TimePickerView0.OnTimeSelectListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                time = date;
            }
        })
                .setLayoutRes(R.layout.pickerview_custom_time, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = (ImageView) v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                            }
                        });
                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("", "", "", "时", "分", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate, selectedDate)
                .setDecorView(date_fl)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .setBackgroundId(0x00000000)
                .setOutSideCancelable(false)
                .isCenterLabel(false)
                .build();
        pvCustomTime.show(null, false);
        pvCustomTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
    }

    @OnClick({R.id.navbar_right_bt, R.id.navbar_left_bt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case (R.id.navbar_left_bt):
                FragmentHelper.pop(getActivity());
                break;
            case (R.id.navbar_right_bt):
                rightButtonClick();
                break;
            default:
                break;
        }

    }

    private void rightButtonClick() {
        pvCustomTime.getData();
        long currentTimeMillis = System.currentTimeMillis();
        long time = this.time.getTime();
        long l = time - currentTimeMillis;
        if (l < 60000) {
            Toast.makeText(getActivity(), "开机时间要大于等于一分钟", Toast.LENGTH_SHORT).show();
            return;
        }
        bsaeHudHelper = new HudHelper();
        if (!canRever) {//不可定时
            bsaeHudHelper.hudShow(getActivity(), "正在连接...");
            if (thread == null) {
                thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            SystemClock.sleep(500);
                            TcpSocket.getInstance().write(Protocol2.setReservation(deviceId, mMode, 1, ReservationBootFragment.this.time.getTime()- System.currentTimeMillis(), 0L));
                            if (HomeFragment1.code1 == 6) {
                                thread.interrupt();
                                SystemClock.sleep(500);
                                bsaeHudHelper.hudHide();
                                if (HomeFragment1.error == 0) {
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(getActivity(), OrderTimeActivity.class);
                                            intent.putExtra("moden", mMode);
                                            intent.putExtra("LRIndex", deviceId);
                                            intent.putExtra("bootTime", ReservationBootFragment.this.time.getTime());
                                            intent.putExtra("appointment", 0);
                                            startActivity(intent);
                                            FragmentHelper.clearBackStack(getActivity());
                                            getActivity().finish();
                                        }
                                    });
                                } else {
                                    Toast.makeText(getActivity(), "设置失败", Toast.LENGTH_SHORT).show();
                                }

                            }


                        }
                    }
                });
                thread.start();
            }
        } else {
            TimeReservationFragment fragment = TimeReservationFragment.newinstance(this.time, mode);
            FragmentHelper.addFragmentToBackStack(getActivity(), R.id.activity_reservation, this, fragment, Common.TIME_RESERVATION_FRAGMENT);
        }
    }
}
