package com.enqos.atc.ui.storeList;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.listener.FavoriteListener;
import com.enqos.atc.listener.StoreActivityListener;
import com.enqos.atc.ui.filter.FilterActivity;
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

public class StoreListActivity extends BaseActivity implements FavoriteListener, StoreActivityListener {

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

    private String selectedCategories;
    private String selectedNeighbourhoods;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeListPresenter.attachView(this);
        ButterKnife.bind(this);


        initToolbar();
        isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        replaceFragment(R.id.content_frame, ShopListFragment.newInstance(""), false);

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
    public int getLayoutId() {
        return R.layout.activity_store_list;
    }

    public void replaceFragment(int id, Fragment fragment, boolean isAnim) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.addToBackStack(fragment.getClass().getName());
        fragmentTransaction.commit();
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
        if (toolbar.getVisibility() == View.GONE)
            toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                assert data != null;
                selectedCategories = data.getStringExtra("categories");
                selectedNeighbourhoods = data.getStringExtra("neighbourhoods");
                Constants.CATEGORIES = selectedCategories;
                Constants.NEIGHBOURHOODS = selectedNeighbourhoods;

                if (TextUtils.isEmpty(selectedCategories) && TextUtils.isEmpty(selectedNeighbourhoods))
                    filterCount.setVisibility(View.GONE);
                else {
                    int count;
                    filterCount.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(selectedNeighbourhoods) && !TextUtils.isEmpty(selectedCategories)) {
                        String[] categories = selectedCategories.split(",");
                        String[] neighbours = selectedNeighbourhoods.split(",");
                        count = categories.length + neighbours.length;
                    } else if (!TextUtils.isEmpty(selectedCategories)) {
                        String[] categories = selectedCategories.split(",");
                        count = categories.length;
                    } else {
                        String[] categories = selectedNeighbourhoods.split(",");
                        count = categories.length;

                    }
                    filterCount.setText(String.valueOf(count));
                }

                replaceFragment(ShopListFragment.newInstance(""));
            }
        } else {
            filterCount.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.image_right, R.id.image_left, R.id.menu_my_account, R.id.terms_condition, R.id.help, R.id.privacy, R.id.about, R.id.img_fav, R.id.img_search, R.id.img_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_left:
                if (title.getText().toString().equals(getString(R.string.store)) || title.getText().toString().equals(getString(R.string.favourites)) || title.getText().toString().equals(getString(R.string.search)) || title.getText().toString().equals(getString(R.string.filter_by_category)))
                    drawerLayout.openDrawer(GravityCompat.START);
                else
                    onBackPressed();
                break;
            case R.id.image_right:
                startActivityForResult(new Intent(this, FilterActivity.class), 1);
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
                    toolbar.setVisibility(View.VISIBLE);
                    replaceFragment(R.id.content_frame, ShopListFragment.newInstance(""), false);
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
    public String getCategories() {
        return selectedCategories;
    }

    @Override
    public String getNeighbourhoods() {
        return selectedNeighbourhoods;
    }

}
