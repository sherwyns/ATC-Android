package com.enqos.atc.register;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity implements RegisterView {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.coordinate_layout)
    CoordinatorLayout coordinatorLayout;


    @Inject
    RegisterPresenter registerPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerPresenter.attachView(this);

    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.register)
    public void onRegister(View v) {

        registerPresenter.registerUser(etEmail.getText().toString(), etPassword.getText().toString(), this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerPresenter.detachView();
    }

    @Override
    public void showMessage(String message) {
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
        Log.i("******", message);
    }

    @Override
    public void onRegisterUser() {

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
