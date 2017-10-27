package com.goockr.inductioncooker.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;

/**
 * Created by CMQ on 2017/6/26.
 */

public class SegmentController extends LinearLayout implements View.OnClickListener {

    View contentView;

    private int selectIndex;
    private TextView LTv;
    private TextView RTv;
    private TextView LTv1;
    private TextView RTv1;

    public int getSelectIndex() {
        return selectIndex;
    }

    private SegmentControllerCallback callback;
    RelativeLayout leftbg_ll;
    RelativeLayout rightbg_ll;

    public SegmentController(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.segment_controller, this);
        leftbg_ll = (RelativeLayout) contentView.findViewById(R.id.segment_controller_leftbg_ll);
        rightbg_ll = (RelativeLayout) contentView.findViewById(R.id.segment_controller_rightbg_ll);
        LTv = (TextView) contentView.findViewById(R.id.LTv);
        LTv1 = (TextView) contentView.findViewById(R.id.LTv1);
        RTv = (TextView) contentView.findViewById(R.id.RTv);
        RTv1 = (TextView) contentView.findViewById(R.id.RTv1);
        leftbg_ll.setOnClickListener(this);
        rightbg_ll.setOnClickListener(this);
        initData();
    }

    public void addListener(SegmentControllerCallback call) {
        callback = call;
    }

    private void initData() {
        selectIndex = 0;
    }
        @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.segment_controller_leftbg_ll):
                if (selectIndex == 0) {
                    return;
                }
                selectIndex = 0;
                LTv1.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                LTv.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                RTv.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                RTv1.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                rightbg_ll.setBackgroundResource(R.mipmap.segment_controls_right_normal);
                leftbg_ll.setBackgroundResource(R.mipmap.segment_controls_left_selected);
                callback.selectIndexChange(selectIndex);
                break;
            case (R.id.segment_controller_rightbg_ll):
                if (selectIndex == 1) {
                    return;
                }
                selectIndex = 1;
                LTv1.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                LTv.setTextColor(ContextCompat.getColor(getContext(),R.color.colorPrimary));
                RTv.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                RTv1.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                leftbg_ll.setBackgroundResource(R.mipmap.segment_controls_left_normal);
                rightbg_ll.setBackgroundResource(R.mipmap.segment_controls_right_selected);
                callback.selectIndexChange(selectIndex);
                break;
            default:
                break;
        }
    }


    public interface SegmentControllerCallback {
        void selectIndexChange(int index);
    }

    public void setSelectLeft(String text) {
        LTv.setText(text);
    }

    public void setSelectRight(String text) {
        RTv.setText(text);
    }
}
