package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.lib.observer.NoticeObserval;
import com.goockr.inductioncooker.lib.observer.NoticeObserver;
import com.goockr.inductioncooker.lib.observer.PowerObserver;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.view.DialogView;
import com.goockr.inductioncooker.view.ImageTopButton;
import com.goockr.inductioncooker.view.SegmentController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by CMQ on 2017/6/21.
 */

public class HomeFragment1 extends Fragment implements SegmentController.SegmentControllerCallback, LeftDeviceFragment1.LeftDeviceFragmentCallback, PowerObserver, NoticeObserval {
    View contentView;
    private LeftDeviceFragment1 leftFragment;
    private RightDeviceFragment rightFragment;
    @BindView(R.id.fragment_home_moden_ll)
    LinearLayout moden_ll;
    @BindView(R.id.tvConnect)
    TextView tvConnect;
    @BindView(R.id.imagetop_button_title_tv)
    TextView title_tv;
    @BindView(R.id.fragment_home_segment)
    SegmentController segmentController;
    private FragmentTransaction fragmentTransaction;
    public static int code1;
    public static int error = -1;
    public static int success = -1;
    public int code;
    private DialogView dialogView;
    private CharSequence mId;
    private List<NoticeObserver> observers = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, contentView);
        initUI();
        initEvent();
        return contentView;
    }

    private void initEvent() {
        segmentController.addListener(this);
        dialogView = DialogView.getSingleton();
        dialogView.setContext(getActivity());
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

        dialongText.setText("设备与电磁炉断已开连接");
    }

    private void initUI() {
        Button addNotice = (Button)contentView.findViewById(R.id.addNotice);
        addNotice.setVisibility(View.GONE);
        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try {
                    object.put("code", 9);
                    object.put("deviceId", "0");
                    Random random = new Random();
                    int i = random.nextInt(7);
                    object.put("warm", String.format("E%s__%s", i + 1, String.valueOf(System.currentTimeMillis())));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                notifyObservers(object.toString());
            }
        });

        FragmentManager fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (NotNull.isNotNull(leftFragment) && NotNull.isNotNull(rightFragment)) {
            fragmentTransaction.remove(leftFragment);
            fragmentTransaction.remove(rightFragment);
        }

        if (leftFragment == null) {
            leftFragment = new LeftDeviceFragment1();
            leftFragment.registerObserver(this);
        }
        if (rightFragment == null) {
            rightFragment = new RightDeviceFragment();
            rightFragment.registerObserver(this);
        }
        fragmentTransaction.add(R.id.fragment_home_moden_ll, leftFragment, "LeftDeviceFragment");
        fragmentTransaction.add(R.id.fragment_home_moden_ll, rightFragment, "RightDeviceFragment");

        fragmentTransaction.show(leftFragment).hide(rightFragment);
        fragmentTransaction.commit();
        leftFragment.setOnDeviceListener(new LeftDeviceFragment1.OnDeviceListener() {
            @Override
            public void deviceListener(String deviceName) {
                title_tv.setText(deviceName);
            }
        });
        rightFragment.setOnDeviceListener(new RightDeviceFragment.OnDeviceListener() {
            @Override
            public void deviceListener(String deviceName) {
                title_tv.setText(deviceName);
            }
        });

    }

    @Override
    public void selectIndexChange(int index) {
        fragmentTransaction = getFragmentManager().beginTransaction();
        if (index == 0) {
            fragmentTransaction.show(leftFragment).hide(rightFragment);
        } else {
            fragmentTransaction.show(rightFragment).hide(leftFragment);
        }

        fragmentTransaction.commit();
    }

    @Override
    public void leftDeviceFragmentButtonClick(ImageTopButton button) {

    }

    public void setMProtocol(String read) {
        if (NotNull.isNotNull(read)) {
            JSONObject order = null;
            //错误码
            code = 0;
            try {
                JSONObject object = new JSONObject(read);
                String id = object.getString("id");
                if (!TextUtils.equals(id, mId)) {
                    title_tv.setText(id);
                    mId = id;
                }
                order = object.getJSONObject("order");
                if (NotNull.isNotNull(order)) {
                    tvConnect.setText("已连接");
                    int LRID = Integer.valueOf(order.getString("deviceId"));//左右炉
                    code = Integer.valueOf(order.getString("code"));// 指令码
                    if (code == 9) {
                        notifyObservers(order.toString());
                        return;
                    }
                    code1 = code;
                    if (code == 6) { // 设置/取消预约时间
                        error = Integer.valueOf(order.getString("error"));
                        success = Integer.valueOf(order.getString("success"));
                    }
                    if (LRID == 0) { // 0是左炉
                        if (NotNull.isNotNull(leftFragment)) {
                            leftFragment.setCode(code);
                            leftFragment.setMProtocol(read);
                        }
                    } else if (LRID == 1) { // 1是右炉
                        if (NotNull.isNotNull(rightFragment)) {
                            rightFragment.setCode(code);
                            rightFragment.setMProtocol(read);
                        }
                    }

                } else {
                    tvConnect.setText("未连接");
                }

            } catch (Exception e) {
                e.printStackTrace();
                try {
                    code = Integer.valueOf(order.getString("code"));
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                if (code == -1) {
                    if (dialogView.getAlertDialog() == null) {
                        showTurnOn();
                    }
                }
                tvConnect.setText("未连接");
                leftFragment.setCode(code);
                rightFragment.setCode(code);
            }

        }
    }

    @Override
    public void updateLeft(String succeedStr) {
        if (NotNull.isNotNull(segmentController)) {
            segmentController.setSelectLeft(succeedStr);
        }
    }

    @Override
    public void updateRight(String succeedStr) {
        if (NotNull.isNotNull(segmentController)) {
            segmentController.setSelectRight(succeedStr);
        }
    }

    @Override
    public void registerObserver(NoticeObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(NoticeObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String succeedStr) {
        for (NoticeObserver observer :
                observers) {
            observer.update(succeedStr);
        }
    }

}
