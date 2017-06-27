package com.goockr.inductioncooker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.goockr.inductioncooker.R;

/**
 * Created by CMQ on 2017/6/26.
 */

public class SegmentController extends LinearLayout {

    public SegmentController(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.segment_controller,this);

    }


}
