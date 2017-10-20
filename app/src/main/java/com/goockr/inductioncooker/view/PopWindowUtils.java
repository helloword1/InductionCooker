package com.goockr.inductioncooker.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.adapter.PopuViewAdapter;

import java.util.List;

public class PopWindowUtils {

    private static PopWindowUtils popWindowUtils = null;

    public PopupWindow popWindow;


    public static PopWindowUtils getPopWindow() {
        if (popWindowUtils == null) {
            popWindowUtils = new PopWindowUtils();
        }
        return popWindowUtils;

    }

    public void showButtonPopwindow(Context context,final List<String> datas) {
        View popView = LayoutInflater.from(context).inflate(R.layout.pop_list_item, null);

        RecyclerView recycleView = (RecyclerView) popView.findViewById(R.id.recycleView);
        View line1 = (View) popView.findViewById(R.id.line1);
        RelativeLayout rl1 = (RelativeLayout) popView.findViewById(R.id.rl1);
        TextView close = (TextView) popView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popWindow.dismiss();
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(context);
        recycleView.setLayoutManager(manager);

        final PopuViewAdapter adapter = new PopuViewAdapter(context, datas);
        recycleView.setAdapter(adapter);
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int height = display.getHeight();
        int mheight = height / 3;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mheight);
        recycleView.setLayoutParams(params);
        this.popWindow = new PopupWindow(popView, LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        this.popWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        this.popWindow.setOutsideTouchable(false);// 设置允许在外点击消失
        this.popWindow.setAnimationStyle(R.style.popuAnima);
        this.popWindow.showAsDropDown(new View(context));
    }

}
