package com.goockr.ui.view.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;

import com.goockr.ui.R;


public class CompanyIntroduceActivity extends Activity {
    Button bt_ComIntroduceBack;
    ProgressBar pb_ProgressBar;
    WebView web_View;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_introduce);
        setupUI();
        eventHandle();
    }

    private void setupUI() {

        pb_ProgressBar = (ProgressBar) findViewById(R.id.pb_ProgressBar);
        web_View = (WebView) findViewById(R.id.web_View);
        //  web_View.getSettings().setJavaScriptEnabled(true);


//支持javascript
        //       web_View.getSettings().setJavaScriptEnabled(true);
// 设置可以支持缩放
        web_View.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        web_View.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
        web_View.getSettings().setUseWideViewPort(true);
//自适应屏幕
//        web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//        web.getSettings().setLoadWithOverviewMode(true);


        web_View.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        web_View.getSettings().setLoadWithOverviewMode(true);

        web_View.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pb_ProgressBar.setVisibility(View.GONE);
            }
        });

        web_View.loadUrl("http://www.goockr.com");

        bt_ComIntroduceBack = (Button) findViewById(R.id.bt_ComIntroduceBack);

    }

    private void eventHandle() {

        bt_ComIntroduceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (web_View.canGoBack()){
                    web_View.goBack();
                }else {
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (web_View.canGoBack()){
            web_View.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
