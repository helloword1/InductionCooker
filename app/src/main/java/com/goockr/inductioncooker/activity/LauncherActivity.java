package com.goockr.inductioncooker.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.view.WindowManager;
import com.goockr.inductioncooker.R;

public class LauncherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_launcher);
    }

    @Override
    protected void onStart() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent =new Intent();
                intent.setClass(LauncherActivity.this, HomeActivity.class);

                startActivity(intent);
                finish();
                //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

            }
        }, 1000);super.onStart();


    }

}
