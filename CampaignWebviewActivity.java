package com.sp.android_studio_project;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CampaignWebviewActivity extends AppCompatActivity {

    private WebView campWebView;
    String url = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_webview);
        campWebView = findViewById(R.id.campaignwebview);

        url = getIntent().getStringExtra("URL_NAME");
        campWebView.loadUrl(url);
        campWebView.getSettings().setJavaScriptEnabled(true);
        campWebView.setWebViewClient(new WebViewClient());
        campWebView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(campWebView.canGoBack()){
            campWebView.goBack();
        } else {
            finish();
        }
    }
}