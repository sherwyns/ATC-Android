package com.enqos.atc.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.response.LoginResponse;
import com.enqos.atc.ui.storeList.StoreListActivity;
import com.enqos.atc.utils.SharedPreferenceManager;
import com.enqos.atc.utils.Utility;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;

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
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @Inject
    LoginPresenter loginPresenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 123;
    private String email;
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        loginPresenter.attachView(this);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        loginButton.setReadPermissions(Arrays.asList(EMAIL));
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Utility.getUserProfile(loginResult.getAccessToken(), LoginActivity.this);
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });

    }

    @OnClick({R.id.login, R.id.tv_google_login, R.id.tv_fb_login, R.id.back,R.id.tv_forgot_pass})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                loginPresenter.authenticateUser(etEmail.getText().toString(), etPassword.getText().toString(), this);
                break;
            case R.id.tv_google_login:
                loginPresenter.showDialog();
                googleSignIn();
                break;
            case R.id.tv_fb_login:
                loginButton.performClick();
                break;
            case R.id.tv_forgot_pass:
                startActivity(new Intent(this,ForgotPasswordActivity.class));
                break;
            case R.id.back:
                onBackPressed();
                break;
        }


    }

    private void googleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onStart() {
        super.onStart();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {

            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            loginPresenter.authenticateSocialUser(account.getEmail(), account.getId(), "google", this);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            loginPresenter.hideDialog();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            email = account.getEmail();
            Log.i("*****", account.getEmail());
        }
    }

    @Override
    public void onValidUser(LoginResponse loginResponse) {
        sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.IS_LOGIN, true);
        if (TextUtils.isEmpty(email))
            sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.EMAIL, etEmail.getText().toString());
        else
            sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.EMAIL, email);
        sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.TOKEN, loginResponse.getId());
        sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.USER_ID,loginResponse.getUserId());
        Intent intent = new Intent(this, StoreListActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void fbLogin(String email, String password) {
        this.email = email;
        loginPresenter.authenticateSocialUser(email, password, "facebook", this);
    }

}
