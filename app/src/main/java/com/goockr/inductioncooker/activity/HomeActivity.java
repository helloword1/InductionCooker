package com.goockr.inductioncooker.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goockr.inductioncooker.R;
import com.goockr.inductioncooker.fragment.HomeFragment;
import com.goockr.inductioncooker.fragment.MoreFragment;
import com.goockr.inductioncooker.fragment.NoticeFragment;
import com.goockr.inductioncooker.view.Tabbar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends FragmentActivity {


    @BindView(R.id.tabbar)
    Tabbar tabbar;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        setContentView(R.layout.activity_home);

        initPermissions();

        ButterKnife.bind(this);

        initUI();

        initListener();


    }

    private void initListener() {

        tabbar.setSelectChangeListener(new Tabbar.TabbarCallback() {
            @Override
            public void tabbarItenChange(int selectIndex) {
                FragmentManager fragmentManager= getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);


                switch (selectIndex){
                    case (0):

                        fragmentTransaction.replace(R.id.maincontent,new HomeFragment(),"HomeFragment");

                        break;
                    case (1):
                        fragmentTransaction.replace(R.id.maincontent,new NoticeFragment(),"HomeFragment");

                        break;
                    case (2):
                        fragmentTransaction.replace(R.id.maincontent,new MoreFragment(),"HomeFragment");

                        break;
                }

                fragmentTransaction.commit();
            }
        });

    }

    private void initUI() {
        FragmentManager fragmentManager= getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.maincontent,new HomeFragment(),"HomeFragment");
        fragmentTransaction.commit();



    }


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    //初始化权限
    private void initPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //添加需要申请的权限
            ArrayList<String> PerList = new ArrayList<>();
            //PerList.add(Manifest.permission.ACCESS_WIFI_STATE);
            PerList.add(Manifest.permission.CAMERA);
            //PerList.add(Manifest.permission.READ_PHONE_STATE);
            //PerList.add(Manifest.permission.ACCESS_NETWORK_STATE);
            //	PerList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            //	PerList.add(Manifest.permission.RECORD_AUDIO);

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


    private  int backCount=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK)
        {
            backCount++;

            if (backCount<2)
            {
                Toast.makeText(this, "5秒内按返回键退出应用", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        backCount=0;
                    }
                },5000);
                return false;
            }



        }

        return super.onKeyDown(keyCode, event);
    }
}
