package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.OrderTimeActivity;
import com.goockr.inductioncooker.lib.socket.Protocol2;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.BarProgress;
import com.goockr.inductioncooker.view.OptionsPickView0;
import com.goockr.ui.view.helper.HudHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.chad.library.adapter.base.listener.SimpleClickListener.TAG;

/**
 * Created by CMQ on 2017/7/4.
 * 设置预约工作长度
 */

public class TimeReservationFragment extends Fragment {

    View contentView;

    // private FragmentManager fragmentManager;

    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_timereservation_bar_pv)
    BarProgress bar_pv;
    @BindView(R.id.fl_date)
    FrameLayout flDate;
    private OptionsPickView0 pvNoLinkOptions;
    private int hour;
    private int second;
    private Date time;
    private String mode;
    private int mMode;
    private int deviceId;
    private final String[] modeStr = {"煲粥", "煲汤", "煮饭", "烧水", "火锅", "煎炒", "烤炸", "保温", "煎焗", "闷烧","保温", "爆炒", "油炸", "文火"};
    private HudHelper bsaeHudHelper;
    private Thread thread;
    private long time1;

    public static final TimeReservationFragment newinstance(Date data, String mode) {
        TimeReservationFragment fragment = new TimeReservationFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("TIME", data);
        bundle.putString("MODE", mode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle arguments = getArguments();
        time = (Date) arguments.getSerializable("TIME");
        mode = arguments.getString("MODE");
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


        contentView = inflater.inflate(R.layout.fragment_time_reservation, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();


        initEvent();

        return contentView;
    }

    private void initUI() {

        List<String> tips = new ArrayList<String>();
        tips.add("1.选择功能模式");
        tips.add("2.预约开机时间");
        tips.add("3.预约定时时间");
        bar_pv.setTips(tips);
        bar_pv.setMaxCount(3);
        bar_pv.setProgress(3);
    }

    private void initEvent() {
    }

    @OnClick({R.id.navbar_left_bt, R.id.navbar_right_bt})
    public void OnClick(View v) {
        switch (v.getId()) {
            case (R.id.navbar_right_bt):

                break;
            case (R.id.navbar_left_bt):
                // fragmentManager.popBackStack();
                FragmentHelper.pop(getActivity());
                break;
        }
    }

    private void initData() {

        /**
         * 注意 ：如果是三级联动的数据(省市区等)，请参照 JsonDataActivity 类里面的写法。
         */
        final ArrayList<String> options1Items = new ArrayList<>();
        final ArrayList<String> options2Items = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            options1Items.add(String.valueOf(i + " 小时"));

        }
        for (int i = 0; i < 60; i++) {
            options2Items.add(String.valueOf(i + 1 + " 分"));
        }

        pvNoLinkOptions = new OptionsPickView0.Builder(getActivity(), new OptionsPickView0.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                hour = options1;
                second = options2 + 1;
                Log.d("12313 ", "onOptionsSelect: " + options1 + "   " + options2);
            }
        }).setOutSideCancelable(false).setDecorView(flDate).setBackgroundId(0x00FFFFFF).build();
        pvNoLinkOptions.setNPicker(options1Items, options2Items, null);
        //取消
        left_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentHelper.pop(getActivity());
            }
        });
        //确定
        right_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bsaeHudHelper = new HudHelper();
                pvNoLinkOptions.getData();
                time1 = TimeReservationFragment.this.time.getTime();

                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                String format = df.format(new Date(time1));// new Date()为获取当前系统时间
                Log.d(TAG, "onClick: " + format);
                bsaeHudHelper.hudShow(getActivity(), "正在加载...");
                if (thread == null) {
                    thread = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                SystemClock.sleep(500);
                                TcpSocket.getInstance().write(Protocol2.setReservation(deviceId, mMode, 1, time1 - System.currentTimeMillis(),(hour * 3600 + second * 60) * 1000));
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
                                                intent.putExtra("bootTime", time1);
                                                intent.putExtra("LRIndex", deviceId);
                                                long time = (hour * 3600 + second * 60) * 1000;
                                                intent.putExtra("appointment", time);
                                                startActivity(intent);
                                                getActivity().setResult(11, new Intent().putExtra("MODE", mMode));
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


            }
        });
        pvNoLinkOptions.show(null, false);
        pvNoLinkOptions.setKeyBackCancelable(false);
    }

}
