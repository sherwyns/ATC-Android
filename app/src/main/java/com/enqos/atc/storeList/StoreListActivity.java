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

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.myaccount.MyAccountActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreListActivity extends BaseActivity implements StoreListView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @Inject
    StoreListPresenter storeListPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        storeListPresenter.attachView(this);
        ButterKnife.bind(this);
        initToolbar();
        replaceFragment(R.id.content_frame, ShopListFragment.newInstance());

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

    public void replaceFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

    }

    @OnClick({R.id.image_right, R.id.image_left, R.id.menu_my_account})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_left:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.image_right:

                break;
            case R.id.menu_my_account:
                drawerLayout.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(this, MyAccountActivity.class);
                startActivity(intent);
                break;

        }
    }


}
