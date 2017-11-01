package com.goockr.ui.view.view;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.goockr.ui.R;

/**
 * Created by CMQ on 2017/7/17.
 * 老黄到此一游
 */

public class TipDialog extends Dialog implements View.OnClickListener {

    View contentView;

    private String tip;

    private String msg;

    private boolean showCancel;

    private boolean touchCancel;

    private TipDialogCallBack callBack;


    private TextView tip_tv;

    private TextView msg_tv;

    private Button cancel_bt;

    private Button action_bt;

    private Context mContext;

    public TipDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public TipDialog(Context context) {
        super(context);
    }

    public TipDialog(Context context, String tip, String msg, boolean touchCancel, boolean showCancel) {
        super(context);

        this.tip = tip;

        this.msg = msg;

        this.touchCancel = touchCancel;

        this.showCancel = showCancel;

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        mContext = context;

    }

    public TipDialog(Context context, String tip, String msg, boolean touchCancel) {
        super(context);

        this.tip = tip;

        this.msg = msg;

        this.touchCancel = touchCancel;

        this.showCancel = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //以view来引入布局
        setContentView(R.layout.dialog_tip);

        tip_tv = (TextView) findViewById(R.id.dialog_tip_tip_tv);

        msg_tv = (TextView) findViewById(R.id.dialog_tip_text_tv);

        cancel_bt = (Button) findViewById(R.id.dialog_tip_cancle_bt);

        action_bt = (Button) findViewById(R.id.dialog_tip_action_bt);

        TextView margin_tv = (TextView) findViewById(R.id.dialog_tip_btmargin_tv);

        tip_tv.setText(tip);

        msg_tv.setText(msg);

        setCanceledOnTouchOutside(touchCancel);


        cancel_bt.setOnClickListener(this);

        action_bt.setOnClickListener(this);

        if (!this.showCancel) {
            cancel_bt.setVisibility(View.GONE);
            margin_tv.setVisibility(View.GONE);
        }


        /*
         * 将对话框的大小按屏幕大小的百分比设置
         */
        WindowManager m = getWindow().getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = getWindow().getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.4); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.65
        getWindow().setAttributes(p);

        getWindow().setBackgroundDrawable(new ColorDrawable(0));


    }

    public void setActionButtonClick(TipDialogCallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.dialog_tip_cancle_bt) {
            this.dismiss();

        } else if (v.getId() == R.id.dialog_tip_action_bt) {
            this.callBack.buttonClick(this);
        }

    }


    public interface TipDialogCallBack {
        void buttonClick(TipDialog dialog);
    }

    public void dialogShow() {

    }


}
