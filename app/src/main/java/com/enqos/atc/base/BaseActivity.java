package com.enqos.atc.base;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements MvpView {

    private AppProgressDialog dialog;
    public double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getLayoutId() != 0)
            setContentView(getLayoutId());
        injectDependency();
        ButterKnife.bind(this);
    }

    public abstract void injectDependency();

    public abstract int getLayoutId();

    @Override
    public void showLoading() {
        showProgressDialog();
    }

    @Override
    public void hideLoading() {
        hideProgressDialog();
    }

    /*showing the progress dialog*/
    public void showProgressDialog() {
        try {
            if (!isFinishing() && dialog != null) {
                if (dialog.isShowing()) {
                    return;
                }
            }
            dialog = new AppProgressDialog(getContext());
            dialog.setCancelable(false);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*hiding the progress dialog*/
    public void hideProgressDialog() {
        if (!isFinishing() && dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    public void replaceFragment(int id, Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    @NonNull
    public Context getContext() {
        return this;
    }
}
