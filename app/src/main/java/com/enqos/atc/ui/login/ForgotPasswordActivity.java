package com.enqos.atc.ui.login;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgotPasswordActivity extends BaseActivity implements ForgotPassView {
    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.coordinate_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.progress_layout)
    LinearLayout progressLayout;
    @Inject
    ForgotPassPresenter presenter;

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forgot_password;
    }

    @OnClick({R.id.done, R.id.back})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.done:
                if (TextUtils.isEmpty(etEmail.getText().toString()))
                    Snackbar.make(coordinatorLayout, "Please enter valid email address", Snackbar.LENGTH_LONG).show();
                else {
                    progressLayout.setVisibility(View.VISIBLE);
                    presenter.resetPassword(this, etEmail.getText().toString(), "https://app.aroundthecorner.store/");
                }
                break;
            case R.id.back:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSuccess() {
        progressLayout.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Password sent to your registered email address.", Snackbar.LENGTH_LONG);
        snackbar.addCallback(new Snackbar.Callback() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                finish();
            }
        });

        snackbar.show();

    }

    @Override
    public void onMessage(String message) {
        progressLayout.setVisibility(View.GONE);
        Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
