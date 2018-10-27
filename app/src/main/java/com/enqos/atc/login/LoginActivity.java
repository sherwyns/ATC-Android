package com.enqos.atc.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.storeList.StoreListActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.coordinate_layout)
    CoordinatorLayout coordinatorLayout;


    @Inject
    LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginPresenter.attachView(this);


    }

    @OnClick(R.id.login)
    public void onClick(View view) {

        loginPresenter.authenticateUser(etEmail.getText().toString(), etPassword.getText().toString(), this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }


    @Override
    public void showMessage(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onValidUser() {
        Intent intent = new Intent(this, StoreListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public String getStringMessage(int id) {
        return null;
    }

    @Override
    public int getInt(int id) {
        return 0;
    }
}
