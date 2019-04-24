package com.enqos.atc.ui.launcher;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.storeList.StoreListActivity;
import com.enqos.atc.ui.tutorial.TutorialActivity;
import com.enqos.atc.utils.Constants;

import javax.inject.Inject;

public class SplashActivity extends BaseActivity implements SplashView, LocationListener {

    @Inject
    SplashPresenter splashPresenter;
    private LocationManager locationManager;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        splashPresenter.attachView(this);
        logo = findViewById(R.id.splash_logo);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 1.0f, this);
            new Handler().postDelayed(() -> splashPresenter.navigate(SplashActivity.this), 2000);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    Toast.makeText(this, "Please enable location to get nearby stores.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Please enable location to get nearby stores.", Toast.LENGTH_LONG).show();
                }
            }
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 1.0f, this);
        }
        splashPresenter.navigate(SplashActivity.this);
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

    @Override
    public void navigateToTutorial() {
        Intent intent = new Intent(SplashActivity.this, TutorialActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {


    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        Log.i("Coordinates", location.getLatitude() + ":" + location.getLongitude());
    }
}
