package com.goockr.inductioncooker.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.activity.ReservationActivity;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.utils.FragmentHelper;
import com.goockr.inductioncooker.view.BarProgress;
import com.goockr.inductioncooker.view.ImageTopButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/30.
 * 设置预约模式
 */

public class ReservationFragment extends Fragment implements ImageTopButton.ImageTopButtonOnClickListener {

    View contentView;
    List<ImageTopButton> buttons;

    @BindView(R.id.navbar_title_tv)
    TextView title_tv;
    @BindView(R.id.navbar_left_bt)
    Button left_bt;
    @BindView(R.id.navbar_right_bt)
    Button right_bt;
    @BindView(R.id.fragment_reservation_content_rl)
    RelativeLayout content_rl;
    @BindView(R.id.fragment_reservation_left_ll)
    LinearLayout left_ll;
    @BindView(R.id.fragment_reservation_right_ll)
    LinearLayout right_ll;
    @BindView(R.id.fragment_reservation_soup_ib)
    ImageTopButton soup_ib;
    @BindView(R.id.fragment_reservation_porridge_ib)
    ImageTopButton porridge_ib;
    @BindView(R.id.fragment_reservation_rich_ib)
    ImageTopButton rich_ib;
    @BindView(R.id.fragment_reservation_water_ib)
    ImageTopButton water_ib;
    @BindView(R.id.fragment_reservation_baked_ib)
    ImageTopButton baked_ib;
    @BindView(R.id.fragment_reservation_stew_ib)
    ImageTopButton stew_ib;
    @BindView(R.id.fragment_reservation_temperature_ib)
    ImageTopButton temperature_ib;
    @BindView(R.id.fragment_reservation_bar_pv)
    BarProgress bar_pv;

    ImageTopButton select_bt;
    @BindView(R.id.fragment_reservation_huoguo_ib)
    ImageTopButton huoguo_ib;
    @BindView(R.id.fragment_reservation_jianchao_ib)
    ImageTopButton jianchao_ib;
    @BindView(R.id.fragment_reservation_kaozha_ib)
    ImageTopButton kaozha_ib;
    @BindView(R.id.fragment_reservation_baowen_ib)
    ImageTopButton baowen_ib;
    @BindView(R.id.fragment_reservation_left1_ll)
    LinearLayout left1_ll;
    @BindView(R.id.fragment_reservation_baochao_ib)
    ImageTopButton baochao_ib;
    @BindView(R.id.fragment_reservation_youzha_ib)
    ImageTopButton youzha_ib;
    @BindView(R.id.fragment_reservation_wenhuo_ib)
    ImageTopButton wenhuo_ib;
    @BindView(R.id.fragment_reservation_right1_ll)
    LinearLayout right1_ll;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_reservation, container, false);

        ButterKnife.bind(this, contentView);

        initData();

        initUI();

        return contentView;
    }

    private void initData() {

        buttons = new ArrayList<>();

    }

    private void initUI() {

        List<String> tips = new ArrayList<String>();
        tips.add("1.选择功能模式");
        tips.add("2.多久后启动");
        bar_pv.setTips(tips);
        bar_pv.setMaxCount(2);
        bar_pv.setProgress(1);

        //   fragmentManager= getFragmentManager();

        Bundle bundle = getArguments();
        int moden = bundle.getInt(ReservationActivity.KMODEN_KEY);

        if (moden == 0) {
            left_ll.setVisibility(View.VISIBLE);
            left1_ll.setVisibility(View.VISIBLE);
            buttons.add(soup_ib);
            buttons.add(porridge_ib);
            buttons.add(rich_ib);
            buttons.add(water_ib);
            buttons.add(huoguo_ib);
            buttons.add(jianchao_ib);
            buttons.add(kaozha_ib);
            buttons.add(baowen_ib);

        } else {
            right_ll.setVisibility(View.VISIBLE);
            right1_ll.setVisibility(View.VISIBLE);
            buttons.add(baked_ib);
            buttons.add(stew_ib);
            buttons.add(temperature_ib);
            buttons.add(baochao_ib);
            buttons.add(youzha_ib);
            buttons.add(wenhuo_ib);
        }

        select_bt = buttons.get(0);
        select_bt.setSelect(true);

        for (ImageTopButton button : buttons) {
            button.buttonOnClickListener(this);
        }


    }

    @OnClick({R.id.navbar_left_bt, R.id.navbar_right_bt})
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.navbar_left_bt):
                FragmentHelper.pop(getActivity());
                getActivity().finish();
                break;
            case (R.id.navbar_right_bt)://下一步
                rightButtonClick();
                break;
            default:
                break;
        }

    }

    private void rightButtonClick() {
        ReservationBootFragment fragment = ReservationBootFragment.newInstance(select_bt.getText());
        FragmentHelper.addFragmentToBackStack(getActivity(), R.id.activity_reservation, this, fragment, Common.RESERVATION_BOOT_FRAGMENT);
    }

    @Override
    public void imageTopButtonOnClickListener(ImageTopButton button) {
        int id = button.getId();
        if (select_bt == button) {
            return;
        }
        select_bt.setSelect(!select_bt.isSelect());
        button.setSelect(!button.isSelect());
        select_bt = button;
    }

}
