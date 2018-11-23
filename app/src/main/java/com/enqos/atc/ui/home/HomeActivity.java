package com.enqos.atc.ui.home;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.enqos.atc.R;
import com.enqos.atc.ui.login.LoginActivity;
import com.enqos.atc.ui.register.RegisterActivity;
import com.enqos.atc.ui.storeList.StoreListActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    /**
     * Clicked login
     * @param view
     */
    public void onLogin(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Clicked register
     * @param view
     */
    public void onRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    /**
     * Clicked start shopping
     * @param view
     */
    public void onStartShopping(View view) {
        Intent intent = new Intent(this, StoreListActivity.class);
        startActivity(intent);
    }
}
