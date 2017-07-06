package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.BarProgress;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/7/3.
 */

public class ReservationBootFragment extends Fragment {

    View  contentView;

    //private FragmentManager fragmentManager;

    private TimePickerView pvCustomTime;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        contentView = inflater.inflate(R.layout.fragment_reservationroot, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();

        initEvent();

        return contentView;
    }

    private void initData() {

       // fragmentManager= getFragmentManager();

    }

    private void initUI() {

        List<String> tips=new ArrayList<String>();
        tips.add("1.选择功能模式");
        tips.add("2.预约开机时间");
        tips.add("3.预约定时时间");
        bar_pv.setTips(tips);
        bar_pv.setMaxCount(3);
        bar_pv.setProgress(2);

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
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);

        //时间选择器 ，自定义布局
        pvCustomTime = new TimePickerView.Builder(getActivity(), new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/



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

                                /*pvTime.dismiss();*/
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                /*pvTime.dismiss();*/
                            }
                        });
                    }
                })
                .setType(new boolean[]{false, false, false, true, true, false})
                .setLabel("", "", "", "时", "分", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setContentSize(20)
                .setDate(selectedDate)
                .setRangDate(startDate,selectedDate)
                .setDecorView(date_fl)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .setBackgroundId(0x00000000)
                .setOutSideCancelable(false)
                .isCenterLabel(false)
                .build();


        pvCustomTime.show(null,false);
        pvCustomTime.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
    }

    private void initEvent() {

    }

    @OnClick({R.id.navbar_right_bt,R.id.navbar_left_bt})
    public void OnClick(View v)
    {
        switch (v.getId())
        {
            case (R.id.navbar_left_bt):
                //fragmentManager.popBackStack();
                FragmentHelper.pop(getActivity());
                break;
            case (R.id.navbar_right_bt):
                rightButtonClick();
                break;
        }

    }


    private void rightButtonClick() {

        TimeReservationFragment fragment=new TimeReservationFragment();

//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        fragmentTransaction.add(R.id.activity_reservation,fragment, Common.ReservationBootFragment);
//
//        fragmentTransaction.hide(this);
//
//        fragmentTransaction.show(fragment);
//
//        fragmentTransaction.addToBackStack(Common.ReservationBootFragment);
//
//        fragmentTransaction.commit();

        FragmentHelper.addFragmentToBackStack(getActivity(),R.id.activity_reservation,this,fragment,Common.TimeReservationFragment);

    }




}
