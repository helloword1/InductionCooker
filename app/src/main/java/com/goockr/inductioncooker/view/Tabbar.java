package com.goockr.inductioncooker.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.ui.view.view.BadgeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/22.
 */

public class Tabbar extends LinearLayout {

    @BindView(R.id.ll_home)
    LinearLayout ll_home;
    @BindView(R.id.ll_notise)
    LinearLayout ll_notise;
    @BindView(R.id.ll_more)
    LinearLayout ll_more;

    // 底部菜单4个ImageView
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.iv_notise)
    ImageView iv_notice;
    @BindView(R.id.iv_more)
    ImageView iv_more;

    // 底部菜单4个菜单标题
    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.tv_notise)
    TextView tv_notice;
    @BindView(R.id.tv_more)
    TextView tv_more;
    @BindView(R.id.bv_notise)
    BadgeView bv_notise;

    LinearLayout selLinearLayout;

    int selectIndex;

    private TabbarCallback tabbarCallback;

    public void setSelectChangeListener(TabbarCallback callback) {
        this.tabbarCallback = callback;
        if (NotNull.isNotNull(tabbarCallback)){
            tabbarCallback.setTabbarCount(bv_notise);
        }
    }

    public Tabbar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.tabbar, this, true);
        ButterKnife.bind(this);
        selLinearLayout = ll_home;
        selectIndex = 0;

    }

    public Tabbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        View view = LayoutInflater.from(context).inflate(R.layout.tabbar, this, true);
        ButterKnife.bind(this, view);
        selLinearLayout = ll_home;
        bv_notise.setBadgeCount(0);

    }


    @OnClick({R.id.ll_home, R.id.ll_notise, R.id.ll_more})
    public void onClick(View v) {

        if (selLinearLayout == v) {
            return;
        } else if (v == ll_home || v == ll_notise || v == ll_more) {
            selLinearLayout = (LinearLayout) v;
        }
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBotton();
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.ll_home:
                iv_home.setImageResource(R.mipmap.tab_icon_home_selected);
                tv_home.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                selectIndex = 0;
                break;
            case R.id.ll_notise:
                iv_notice.setImageResource(R.mipmap.tab_icon_notise_selected);
                tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                selectIndex = 1;
                bv_notise.setBadgeCount(0);
                break;
            case R.id.ll_more:
                iv_more.setImageResource(R.mipmap.tab_icon_more_selected);
                tv_more.setTextColor(ContextCompat.getColor(getContext(),R.color.white));
                selectIndex = 2;
                break;
            default:
                break;
        }

        tabbarCallback.tabbarItenChange(selectIndex);

    }

    private void restartBotton() {
        iv_home.setImageResource(R.mipmap.tab_icon_home_normal);
        iv_notice.setImageResource(R.mipmap.tab_icon_notise_normal);
        iv_more.setImageResource(R.mipmap.tab_icon_more_normal);
        tv_home.setTextColor(ContextCompat.getColor(getContext(),R.color.colorlightgray));
        tv_notice.setTextColor(ContextCompat.getColor(getContext(),R.color.colorlightgray));
        tv_more.setTextColor(ContextCompat.getColor(getContext(),R.color.colorlightgray));

    }

    public interface TabbarCallback {
        void tabbarItenChange(int selectIndex);
        void setTabbarCount(BadgeView bvNotice);
    }


}
