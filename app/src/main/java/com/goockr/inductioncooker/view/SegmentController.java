package com.goockr.inductioncooker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.goockr.inductioncooker.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/26.
 */

public class SegmentController extends LinearLayout {

    View contentView;

    private int selectIndex;

    private SegmentControllerCallback callback;

    @BindView(R.id.segment_controller_left_rl)
    RelativeLayout left_rl;
    @BindView(R.id.segment_controller_right_rl)
    RelativeLayout right_rl;
    @BindView(R.id.segment_controller_leftbg_ll)
    LinearLayout leftbg_ll;
    @BindView(R.id.segment_controller_rightbg_ll)
    LinearLayout rightbg_ll;

    public SegmentController(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView=inflater.inflate(R.layout.segment_controller,this);

        ButterKnife.bind(this,contentView);

        initData();

    }

    public void addListener(SegmentControllerCallback call)
    {
        callback=call;
    }

    private void initData() {

        selectIndex=0;
    }

    @OnClick({R.id.segment_controller_left_rl,R.id.segment_controller_right_rl})
    public void onClick(View v) {

        switch (v.getId())
        {
            case (R.id.segment_controller_left_rl):

                if (selectIndex==0)return;
                selectIndex=0;
                rightbg_ll.setBackgroundDrawable(getResources().getDrawable(R.mipmap.segment_controls_right_normal));
                leftbg_ll.setBackgroundDrawable(getResources().getDrawable(R.mipmap.segment_controls_left_selected));
                callback.selectIndexChange(selectIndex);

                break;
            case (R.id.segment_controller_right_rl):
                if (selectIndex==1)return;
                selectIndex=1;
                leftbg_ll.setBackgroundDrawable(getResources().getDrawable(R.mipmap.segment_controls_left_normal));
                rightbg_ll.setBackgroundDrawable(getResources().getDrawable(R.mipmap.segment_controls_right_selected));
                callback.selectIndexChange(selectIndex);
                break;
        }

    }

    public  interface SegmentControllerCallback
    {
        public void selectIndexChange(int index);
    }


}
