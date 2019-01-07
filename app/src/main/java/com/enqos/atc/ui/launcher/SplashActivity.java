package com.enqos.atc.ui.launcher;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.widget.ImageView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.storeList.StoreListActivity;
import com.enqos.atc.utils.Constants;
import com.enqos.atc.utils.SharedPreferenceManager;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashView {

    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    @Inject
    SplashPresenter splashPresenter;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashPresenter.attachView(this);
        logo = findViewById(R.id.splash_logo);
        new Handler().postDelayed(() -> {
            splashPresenter.navigate(SplashActivity.this);
        }, 2000);
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        splashPresenter.detachView();
    }

    @Override
    public void navigateHome() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra(Constants.IS_FAV, false);
        ActivityOptions activityOptions =
                ActivityOptions.makeSceneTransitionAnimation(
                        this, logo, "logo");
        startActivity(intent, activityOptions.toBundle());
        finish();
    }

    @Override
    public void navigateStore() {
        Intent intent = new Intent(SplashActivity.this, StoreListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
