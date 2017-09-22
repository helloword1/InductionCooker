package com.goockr.inductioncooker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.goockr.inductioncooker.R;

/**
 * Created by CMQ on 2017/6/26.
 */

public class SegmentController extends LinearLayout implements View.OnClickListener{

    View contentView;

    private int selectIndex;

    public int getSelectIndex() {
        return selectIndex;
    }

    private SegmentControllerCallback callback;
    LinearLayout leftbg_ll;
    LinearLayout rightbg_ll;

    public SegmentController(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.segment_controller, this);
        leftbg_ll= (LinearLayout) contentView.findViewById(R.id.segment_controller_leftbg_ll);
        rightbg_ll= (LinearLayout) contentView.findViewById(R.id.segment_controller_rightbg_ll);
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

    public void onClick(View v) {
        switch (v.getId()) {
            case (R.id.segment_controller_leftbg_ll):
                if (selectIndex == 0) return;
                selectIndex = 0;
                rightbg_ll.setBackgroundResource(R.mipmap.segment_controls_right_normal);
                leftbg_ll.setBackgroundResource(R.mipmap.segment_controls_left_selected);
                callback.selectIndexChange(selectIndex);
                break;
            case (R.id.segment_controller_rightbg_ll):
                if (selectIndex == 1) return;
                selectIndex = 1;
                leftbg_ll.setBackgroundResource(R.mipmap.segment_controls_left_normal);
                rightbg_ll.setBackgroundResource(R.mipmap.segment_controls_right_selected);
                callback.selectIndexChange(selectIndex);
                break;
        }

    }

    public interface SegmentControllerCallback {
         void selectIndexChange(int index);
    }

    public void setSelectIndexListener(int index) {
        if (index == 0) {//左炉
            if (selectIndex == 0) return;
            selectIndex = 0;
            rightbg_ll.setBackgroundResource(R.mipmap.segment_controls_right_normal);
            leftbg_ll.setBackgroundResource(R.mipmap.segment_controls_left_selected);
        } else if (index == 1) {//右炉
            if (selectIndex == 1) return;
            selectIndex = 1;
            leftbg_ll.setBackgroundResource(R.mipmap.segment_controls_left_normal);
            rightbg_ll.setBackgroundResource(R.mipmap.segment_controls_right_selected);
        }
        callback.selectIndexChange(index);
    }
}
