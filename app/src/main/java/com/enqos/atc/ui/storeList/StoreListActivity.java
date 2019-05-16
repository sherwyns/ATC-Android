package com.enqos.atc.ui.storeList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.Neighbourhood;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.listener.FavoriteListener;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.ui.filter.multifilter.MultiFilterActivity;
import com.enqos.atc.ui.home.HomeActivity;
import com.enqos.atc.ui.login.LoginActivity;
import com.enqos.atc.ui.myaccount.MyAccountActivity;
import com.enqos.atc.ui.search.SearchFragment;
import com.enqos.atc.utils.Constants;
import com.enqos.atc.utils.SharedPreferenceManager;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreListActivity extends BaseActivity implements FavoriteListener, StoreActivityListener, LocationListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.img_search)
    ImageView menuSearch;
    @BindView(R.id.img_fav)
    ImageView menuFav;
    @BindView(R.id.img_home)
    ImageView menuHome;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.image_left)
    ImageView leftImg;
    @BindView(R.id.image_right)
    ImageView rightImg;
    @BindView(R.id.filter_count)
    TextView filterCount;
    @Inject
    StoreListPresenter storeListPresenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private boolean isLogin;
    private LocationManager locationManager;
    private ShopListFragment shopListFragment;
    private boolean isLocationDetected = false;
    private int totalFilterCount;
    private boolean isBackPressed;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeListPresenter.attachView(this);
        ButterKnife.bind(this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 1.0f, this);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }
        initToolbar();
        isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        storeListPresenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopListFragment = ShopListFragment.newInstance("");
        replaceFragment(R.id.content_frame, shopListFragment, false);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_store_list;
    }

    public void replaceFragment(int id, Fragment fragment, boolean isAnim) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isBackPressed = true;
        if (toolbar.getVisibility() == View.GONE)
            toolbar.setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.image_right, R.id.image_left, R.id.menu_my_account, R.id.terms_condition, R.id.help, R.id.privacy, R.id.about, R.id.img_fav, R.id.img_search, R.id.img_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_left:
                if (title.getText().toString().equals(getString(R.string.store)) || title.getText().toString().equals(getString(R.string.products)) || title.getText().toString().equals(getString(R.string.store_near_you)) || title.getText().toString().equals(getString(R.string.products_near_you)) || title.getText().toString().equals(getString(R.string.favourites)) || title.getText().toString().equals(getString(R.string.search)) || title.getText().toString().equals(getString(R.string.filter_by_category)))
                    drawerLayout.openDrawer(GravityCompat.START);
                else
                    onBackPressed();
                break;
            case R.id.image_right:
                if (shopListFragment != null)
                    onFilterClick(ShopListFragment.isProductSelected);
                break;
            case R.id.img_fav:
                if (!isLogin) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                } else
                    onMenuClick(view, getString(R.string.favourites));
                break;
            case R.id.img_home:
                onMenuClick(view, getString(R.string.store));
                break;
            case R.id.img_search:
                onMenuClick(view, getString(R.string.search));
                break;
            case R.id.terms_condition:
                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_POLICY_LINK));
                startActivity(browserIntent1);
                break;
            case R.id.help:
                Intent browserIntent2 = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.HELP_LINK));
                startActivity(browserIntent2);
                break;
            case R.id.privacy:
                Intent browserIntent3 = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PRIVACY_POLICY_LINK));
                startActivity(browserIntent3);
                break;
            case R.id.about:
                Intent browserIntent4 = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.ABOUT_US_LINK));
                startActivity(browserIntent4);
                break;
            case R.id.menu_my_account:
                drawerLayout.closeDrawer(GravityCompat.START);
                if (isLogin) {
                    Intent intent = new Intent(this, MyAccountActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.putExtra(Constants.IS_FAV, false);
                    startActivity(intent);
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    private void onMenuClick(View view, String titleStr) {
        if (!title.getText().toString().equals(titleStr)) {
            title.setText(titleStr);
            switch (view.getId()) {
                case R.id.img_home:
                    isBackPressed = true;
                    toolbar.setVisibility(View.VISIBLE);
                    replaceFragment(R.id.content_frame, shopListFragment, false);
                    break;
                case R.id.img_search:
                    toolbar.setVisibility(View.GONE);
                    replaceFragment(R.id.content_frame, SearchFragment.newInstance(), false);
                    break;
                case R.id.img_fav:
                    toolbar.setVisibility(View.VISIBLE);
                    replaceFragment(R.id.content_frame, FavouriteFragment.newInstance(), false);
                    break;
            }
        }
    }

    @Override
    public List<StoreEntity> getFavoriteStores() {
        return null;
    }

    @Override
    public void replaceFragment(Fragment fragment) {
        replaceFragment(R.id.content_frame, fragment, false);
    }

    @Override
    public void changeHeader(int leftResId, String text, int rightResId) {
        leftImg.setImageResource(leftResId);
        title.setText(text);
        rightImg.setImageResource(rightResId);
    }

    @Override
    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public String getCategories(List<CategoryEntity> categoryEntities) {
        StringBuilder selectedCategories = new StringBuilder();
        String result = "";
        for (CategoryEntity categoryEntity : categoryEntities) {
            if (categoryEntity.isSelected()) {
                selectedCategories.append(categoryEntity.getId()).append(",");
            } else if (categoryEntity.getSelectedCount() > 0) {
                for (CategoryEntity childCategory : categoryEntity.getAllChildren()) {
                    if (childCategory.isSelected())
                        selectedCategories.append(childCategory.getId()).append(",");
                }
            }
        }
        if (selectedCategories.length() > 0)
            result = selectedCategories.deleteCharAt(selectedCategories.length() - 1).toString();
        Log.i("*******", "CATEGORIES---->" + result);
        if (!result.isEmpty()) {
            String[] count = result.split(",");
            totalFilterCount += count.length;
        }
        if (totalFilterCount > 0)
            filterCount.setVisibility(View.VISIBLE);
        else
            filterCount.setVisibility(View.GONE);
        filterCount.setText(String.valueOf(totalFilterCount));
        return result;
    }

    @Override
    public String getNeighbourhoods(List<Neighbourhood> categoryEntities) {
        StringBuilder selectedCategories = new StringBuilder();
        String result = "";
        for (Neighbourhood categoryEntity : categoryEntities) {
            if (categoryEntity.isSelected()) {
                selectedCategories.append(categoryEntity.getNeighbourhood()).append(",");
            }
        }
        if (selectedCategories.length() > 0)
            result = selectedCategories.deleteCharAt(selectedCategories.length() - 1).toString();
        Log.i("*******", "NEIGHBOURHOOD---->" + result);
        if (!result.isEmpty()) {
            String[] count = result.split(",");
            totalFilterCount += count.length;
        }
        return result;
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
            } else
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100000, 1.0f, this);
        }
    }

    @Override
    public void onFilterClick(boolean isProduct) {
        totalFilterCount = 0;
        Intent intent = new Intent(this, MultiFilterActivity.class);
        intent.putExtra("isProduct", isProduct);
        startActivity(intent);
    }

    @Override
    public void setCount(String text) {
        if (TextUtils.isEmpty(text)) {
            totalFilterCount = 0;
            filterCount.setVisibility(View.GONE);
        } else
            filterCount.setVisibility(View.VISIBLE);
        filterCount.setText(text);
    }

    @Override
    public boolean isOnBackPressed() {
        return isBackPressed;
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        if (!isLocationDetected) {
            title.setText(R.string.store_near_you);
            isLocationDetected = true;
            shopListFragment = ShopListFragment.newInstance("");
            replaceFragment(R.id.content_frame, shopListFragment, false);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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
}
