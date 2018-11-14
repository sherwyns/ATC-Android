package com.enqos.atc.storeList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.home.HomeActivity;
import com.enqos.atc.myaccount.MyAccountActivity;
import com.enqos.atc.utils.SharedPreferenceManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreListActivity extends BaseActivity {

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
    @Inject
    StoreListPresenter storeListPresenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeListPresenter.attachView(this);
        ButterKnife.bind(this);
        initToolbar();
        replaceFragment(R.id.content_frame, ShopListFragment.newInstance(), false);

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
        if (isAnim)
            fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

    }

    private void menuSelection(int id) {
        switch (id) {
            case R.id.img_fav:
                break;
            case R.id.img_search:
                break;
            case R.id.img_home:
                break;
        }
    }

    @OnClick({R.id.image_right, R.id.image_left, R.id.menu_my_account, R.id.img_fav, R.id.img_search, R.id.img_home})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_left:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.image_right:
                break;
            case R.id.img_fav:
                title.setText(R.string.favourites);
                replaceFragment(R.id.content_frame, FavouriteFragment.newInstance(), false);
                break;
            case R.id.img_home:
                title.setText(R.string.store);
                replaceFragment(R.id.content_frame, ShopListFragment.newInstance(), false);
                break;
            case R.id.img_search:
                title.setText(R.string.search);
                replaceFragment(R.id.content_frame, SearchFragment.newInstance(), false);
                break;
            case R.id.menu_my_account:
                drawerLayout.closeDrawer(GravityCompat.START);
                boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
                if (isLogin) {
                    Intent intent = new Intent(this, MyAccountActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;

        }
    }


}
