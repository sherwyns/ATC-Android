package com.enqos.atc.storeList;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.enqos.atc.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class StoreListActivity extends AppCompatActivity implements ShopListFragment.OnFragmentInteractionListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list);
        ButterKnife.bind(this);
        initViews();
        replaceFragment(R.id.content_frame, ShopListFragment.newInstance("", ""));

    }

    public void replaceFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    private void initViews() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);

    }

    @OnClick({R.id.image_right, R.id.image_left})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_left:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.image_right:

                break;

        }
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
