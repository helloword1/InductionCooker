package com.goockr.inductioncooker.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.fragment.HomeFragment;
import com.goockr.inductioncooker.fragment.MoreFragment;
import com.goockr.inductioncooker.fragment.NoticeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by CMQ on 2017/6/22.
 */

public class Tabbar extends LinearLayout {

    @BindView(R.id.ll_home)LinearLayout ll_home;
    @BindView(R.id.ll_notise)LinearLayout ll_notise;
    @BindView(R.id.ll_more)LinearLayout ll_more;

    // 底部菜单4个ImageView
    @BindView(R.id.iv_home)
    ImageView iv_home;
    @BindView(R.id.iv_notise) ImageView iv_notice;
    @BindView(R.id.iv_more) ImageView iv_more;

    // 底部菜单4个菜单标题
    @BindView(R.id.tv_home)
    TextView tv_home;
    @BindView(R.id.tv_notise) TextView tv_notice;
    @BindView(R.id.tv_more) TextView tv_more;

    LinearLayout selLinearLayout;

    int selectIndex;

    private TabbarCallback tabbarCallback;

   public void setSelectChangeListener(TabbarCallback callback)
   {
       this.tabbarCallback=callback;
   }

    public Tabbar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.tabbar, this, true);
        ButterKnife.bind(this);
        selLinearLayout=ll_home;
        selectIndex=0;



    }

    public Tabbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
//        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        inflater.inflate(R.layout.tabbar,this);
        LayoutInflater.from(context).inflate(R.layout.tabbar, this, true);

        ButterKnife.bind(this);

        selLinearLayout=ll_home;

    }




    @OnClick({R.id.ll_home, R.id.ll_notise,R.id.ll_more})
    public void onClick(View v) {

        if  (selLinearLayout==v)
        { return;}
        else if (v==ll_home||v==ll_notise||v==ll_more){
            selLinearLayout=(LinearLayout) v;
        }
        // 在每次点击后将所有的底部按钮(ImageView,TextView)颜色改为灰色，然后根据点击着色
        restartBotton();

//        FragmentManager fragmentManager= getFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.ll_home:
                iv_home.setImageResource(R.mipmap.tab_icon_home_selected);
                tv_home.setTextColor(getResources().getColor(R.color.white));
                selectIndex=0;

//                fragmentTransaction.replace(R.id.maincontent,new HomeFragment(),"HomeFragment");
//                fragmentTransaction.commit();
                break;
            case R.id.ll_notise:
                iv_notice.setImageResource(R.mipmap.tab_icon_notise_selected);
                tv_notice.setTextColor(getResources().getColor(R.color.white));
                selectIndex=1;

//                fragmentTransaction.replace(R.id.maincontent,new NoticeFragment(),"NoticeFragment");
//                fragmentTransaction.commit();
                break;
            case R.id.ll_more:
                iv_more.setImageResource(R.mipmap.tab_icon_more_selected);
                tv_more.setTextColor(getResources().getColor(R.color.white));
                selectIndex=2;
//                fragmentTransaction.replace(R.id.maincontent,new MoreFragment(),"MoreFragment");
//                fragmentTransaction.commit();
                break;


            default:
                break;
        }

        tabbarCallback.tabbarItenChange(selectIndex);

    }

    private void restartBotton() {
        // ImageView置为灰色
        iv_home.setImageResource(R.mipmap.tab_icon_home_normal);
        iv_notice.setImageResource(R.mipmap.tab_icon_notise_normal);
        iv_more.setImageResource(R.mipmap.tab_icon_more_normal);
        // TextView置为白色
        tv_home.setTextColor(getResources().getColor(R.color.colorlightgray));
        tv_notice.setTextColor(getResources().getColor(R.color.colorlightgray));
        tv_more.setTextColor(getResources().getColor(R.color.colorlightgray));

    }

    public interface TabbarCallback
    {
        public void tabbarItenChange(int selectIndex);
    }


}
