package com.enqos.atc.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.enqos.atc.R;
import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BaseActivity;
import com.enqos.atc.data.response.RegisterResponse;
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

public class RegisterActivity extends BaseActivity implements RegisterView {

    @BindView(R.id.et_email)
    EditText etEmail;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.coordinate_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    @Inject
    RegisterPresenter registerPresenter;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;
    private boolean isSocialRegister = false;
    private GoogleSignInClient mGoogleSignInClient;
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
    private static final int RC_SIGN_IN = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        registerPresenter.attachView(this);
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
                        Utility.getUserProfile(loginResult.getAccessToken(), RegisterActivity.this);
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

    @Override
    public void injectDependency() {
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @OnClick({R.id.register, R.id.tv_google_register, R.id.tv_fb_register, R.id.iv_back})
    public void onRegister(View v) {
        switch (v.getId()) {
            case R.id.register:
                registerPresenter.registerUser(etEmail.getText().toString(), etPassword.getText().toString(), this);
                break;
            case R.id.tv_google_register:
                registerPresenter.showDialog();
                googleSignIn();
                break;
            case R.id.tv_fb_register:
                loginButton.performClick();
                break;
            case R.id.iv_back:
                onBackPressed();
                break;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void googleSignIn() {
        isSocialRegister = true;
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
            registerPresenter.registerSocialUser(account.getEmail(), account.getId(), "google", this);
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            registerPresenter.hideDialog();
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Google Sign In", "signInResult:failed code=" + e.getStatusCode());
            updateUI(null);
        }
    }

    private void updateUI(GoogleSignInAccount account) {

        if (account != null) {
            Log.i("*****", account.getEmail());
        }
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
    public void onRegisterUser(RegisterResponse registerResponse) {
        sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.IS_LOGIN, true);
        sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.EMAIL, registerResponse.getEmail());
        if (isSocialRegister) {
            sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.TOKEN, registerResponse.getId());
            sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.USER_ID, registerResponse.getUserId());
        } else {
            sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.TOKEN, registerResponse.getId());
            sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.USER_ID, registerResponse.getId());
        }
        Intent intent = new Intent(this, StoreListActivity.class);
        startActivity(intent);
        finish();

    }

    @Override
    public void fbLogin(String email, String accessToken) {
        isSocialRegister = true;
        registerPresenter.registerSocialUser(email, accessToken, "facebook", this);
    }

}
