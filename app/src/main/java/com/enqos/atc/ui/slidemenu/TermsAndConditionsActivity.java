package com.enqos.atc.ui.slidemenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.enqos.atc.R;

public class TermsAndConditionsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        WebView webView = findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setJavaScriptEnabled(true);
        webView.loadUrl("https://app.aroundthecorner.store/termsofservice");

    }

    public void onLeftClick(View view) {
        finish();
    }
}
