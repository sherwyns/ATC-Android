package com.enqos.atc.ui.slidemenu;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.utils.Constants;

public class TermsAndConditionsActivity extends AppCompatActivity {

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        TextView title = findViewById(R.id.title);
        ProgressBar progressBar = findViewById(R.id.progress);
        WebView webView = findViewById(R.id.web_view);

        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        Intent intent = getIntent();
        String link = intent.getStringExtra("link");

        String url = "";
        switch (link) {
            case "TC":
                url = Constants.PRIVACY_POLICY_LINK;
                title.setText("Terms & Conditions");
                break;
            case "PRIVACY":
                url = Constants.PRIVACY_POLICY_LINK;
                title.setText("Privacy and Policy");
                break;
            case "HELP":
                url = Constants.HELP_LINK;
                title.setText("Help");
                break;
            case "ABOUT":
                url = Constants.ABOUT_US_LINK;
                title.setText("About");
                break;
        }
        String finalUrl = url;
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                progressBar.setVisibility(View.VISIBLE);
                view.loadUrl(finalUrl);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);

            }
        });
        webView.loadUrl(url);

    }

    public void onLeftClick(View view) {
        finish();
    }
}
