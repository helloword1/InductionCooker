package com.goockr.inductioncooker.activity;

import android.Manifest;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.widget.Toast;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.common.Common;
import com.goockr.inductioncooker.fragment.HomeFragment1;
import com.goockr.inductioncooker.fragment.MoreFragment;
import com.goockr.inductioncooker.fragment.NoticeFragment;
import com.goockr.inductioncooker.lib.socket.TcpSocket;
import com.goockr.inductioncooker.utils.NotNull;
import com.goockr.inductioncooker.utils.PreferencesUitls;
import com.goockr.inductioncooker.view.Tabbar;
import com.goockr.ui.view.helper.HudHelper;
import com.goockr.ui.view.view.BadgeView;
import com.goockr.ui.view.view.TipDialog;
import com.google.zxing.common.Runnable;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends BaseActivity implements TcpSocket.TcpSocketCallBack {

    private static final int MAX_CONNECT_COUNT = 10;
    @BindView(R.id.tabbar)
    Tabbar tabbar;
    private int connectCount;
    private HomeFragment1 fragment;
    private NoticeFragment notifragment;
    private MoreFragment morefragment;
    private BadgeView mBvNotice;
    private int count = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(R.layout.activity_home);
        initPermissions();
        ButterKnife.bind(this);
        initData();
        initUI();
        initListener();

    }

    @Override
    protected void handleMsg(String mProtocol) {
        fragment.setMProtocol(mProtocol);
    }

    private void initData() {
        connectCount = 0;
    }

    /**
     * fragment切换
     */
    private void initListener() {
        tabbar.setSelectChangeListener(new Tabbar.TabbarCallback() {

            @Override
            public void tabbarItenChange(int selectIndex) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

                switch (selectIndex) {
                    case (0):
                        fragmentTransaction.show(fragment).hide(notifragment).hide(morefragment);
                        break;
                    case (1):
                        fragmentTransaction.show(notifragment).hide(fragment).hide(morefragment);
                        notifragment.set2Zero();
                        break;
                    case (2):
                        fragmentTransaction.show(morefragment).hide(fragment).hide(notifragment);
                        break;
                    default:
                        break;
                }

                fragmentTransaction.commit();
            }

            @Override
            public void setTabbarCount(BadgeView bvNotice) {
                mBvNotice = bvNotice;
            }
        });

    }

    private void initUI() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragment = new HomeFragment1();
        notifragment = new NoticeFragment();
        notifragment.setObservable(fragment);//关联观察者
        morefragment = new MoreFragment();
        fragmentTransaction.add(R.id.maincontent, fragment, "HomeFragment"); // 首页
        fragmentTransaction.add(R.id.maincontent, notifragment, "HomeFragment");// 通知
        fragmentTransaction.add(R.id.maincontent, morefragment, "HomeFragment");// 更多
        fragmentTransaction.show(fragment).hide(notifragment).hide(morefragment);
        fragmentTransaction.commit();

        //监听通知
        notifragment.setOnAlertListener(new NoticeFragment.onAlertListener() {
            @Override
            public void alertListener(int length) {
                if (NotNull.isNotNull(mBvNotice)) {
                    mBvNotice.setBadgeCount(length);
                }
            }
        });
    }


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    //初始化权限
    private void initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //添加需要申请的权限
            ArrayList<String> PerList = new ArrayList<>();
            PerList.add(Manifest.permission.CAMERA);
            if (!checkSelfPermissions(PerList)) {
                requestPermissions(PerList.toArray(new String[PerList.size()]), REQUEST_CODE_ASK_PERMISSIONS);
            }
        }
    }

    //检查权限是否有效
    private boolean checkSelfPermissions(ArrayList<String> PerList) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (String per : PerList) {
                if (checkSelfPermission(per) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    /**
     * 返回键退出
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (count < 2) {
                finishActivities();
            } else {
                count=1;
                Toast.makeText(this, "3秒内按返回键退出应用", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new java.lang.Runnable() {
                    @Override
                    public void run() {
                        count = 3;
                    }
                }, 3000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /****************************TcpSocketCallBack*******************************************/
    @Override
    public void onConnect() {

    }

    @Override
    public void onFailConnect() {
        if (connectCount < MAX_CONNECT_COUNT) {
            connectCount++;
            TcpSocket.getInstance().connect(Common.KIP, Common.KPORT, this);
        } else {
            connectCount = 0;
            TipDialog tipDialog = new TipDialog(HomeActivity.this, getResources().getString(R.string.dialog_tip), getResources().getString(R.string.dialog_failconnect), false, false);
            tipDialog.setActionButtonClick(new TipDialog.TipDialogCallBack() {
                @Override
                public void buttonClick(TipDialog dialog) {
                    TcpSocket.getInstance().connect(Common.KIP, Common.KPORT, HomeActivity.this);
                    dialog.dismiss();
                }
            });
            tipDialog.show();
        }
    }

    @Override
    public void onDisconnect() {

        if (connectCount < MAX_CONNECT_COUNT) {
            connectCount++;
            TcpSocket.getInstance().connect(Common.KIP, Common.KPORT, this);
        } else {
            connectCount = 0;
            TipDialog tipDialog = new TipDialog(HomeActivity.this, getResources().getString(R.string.dialog_tip), getResources().getString(R.string.dialog_disconnect), false, false);
            tipDialog.setActionButtonClick(new TipDialog.TipDialogCallBack() {
                @Override
                public void buttonClick(TipDialog dialog) {
                    TcpSocket.getInstance().connect(Common.KIP, Common.KPORT, HomeActivity.this);
                    dialog.dismiss();
                }
            });
            tipDialog.show();

        }


    }

    @Override
    public void onRead(String read) {

    }
}
