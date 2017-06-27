package com.goockr.inductioncooker.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.goockr.inductioncooker.R;

/**
 * Created by CMQ on 2017/6/22.
 */

public class CustomView  extends RelativeLayout {
    public CustomView(Context context) {
        super(context);

        LayoutInflater.from(context).inflate(R.layout.custom_view, this, true);

    }

    //自定义view必须实现这个重载方法
    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.custom_view,this);

    }

}
