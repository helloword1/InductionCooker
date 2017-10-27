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
    Button btComIntroduceBack;
    ProgressBar pbProgressBar;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_introduce);
        setupUI();
        eventHandle();
    }

    private void setupUI() {

        pbProgressBar = (ProgressBar) findViewById(R.id.pb_ProgressBar);
        webView = (WebView) findViewById(R.id.web_View);
        //  webView.getSettings().setJavaScriptEnabled(true);


//支持javascript
        //       webView.getSettings().setJavaScriptEnabled(true);
// 设置可以支持缩放
        webView.getSettings().setSupportZoom(true);
// 设置出现缩放工具
        webView.getSettings().setBuiltInZoomControls(true);
//扩大比例的缩放
        webView.getSettings().setUseWideViewPort(true);
//自适应屏幕
//        web.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//        web.getSettings().setLoadWithOverviewMode(true);


        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.getSettings().setLoadWithOverviewMode(true);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pbProgressBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl("http://www.goockr.com");

        btComIntroduceBack = (Button) findViewById(R.id.bt_ComIntroduceBack);

    }

    private void eventHandle() {

        btComIntroduceBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()){
                    webView.goBack();
                }else {
                    finish();
                }

            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack();
        }else {
            super.onBackPressed();
        }
    }
}
