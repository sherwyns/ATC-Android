package com.enqos.atc.ui.login;

import android.text.TextUtils;
import android.util.Log;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.LoginResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

import javax.inject.Inject;

public class LoginPresenter extends BasePresenter implements NetworkApiResponse {
    private LoginView loginView;
    private CreateApiRequest createApiRequest;

    @Inject
    public LoginPresenter() {

        AtcApplication.getAppComponents().inject(this);
    }


    void authenticateUser(String email, String password, LoginView loginView) {

        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this);

        try {
            this.loginView = loginView;
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                getmMvpView().showLoading();
                createApiRequest.createLoginRequest(email, password);
            } else {
                loginView.showMessage("Please enter valid credential");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void authenticateSocialUser(String email, String externalId, String provider, LoginView loginView) {

        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this);

        try {
            this.loginView = loginView;
            if (!TextUtils.isEmpty(email)) {
                getmMvpView().showLoading();
                createApiRequest.createSocialLoginRequest(email, externalId, provider);
            } else {
                loginView.showMessage("Please enter valid credential");
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    void showDialog() {
        getmMvpView().showLoading();
    }

    void hideDialog() {
        getmMvpView().hideLoading();
    }

    @Override
    public void onSuccess(BaseResponse response) {
        getmMvpView().hideLoading();
        LoginResponse loginResponse = (LoginResponse) response;
        loginView.onValidUser(loginResponse);

    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {
        getmMvpView().hideLoading();
        loginView.showMessage(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        getmMvpView().hideLoading();
        loginView.showMessage("TimeOut");
        Log.i("*****", "TimeOut");
    }

    @Override
    public void onNetworkError(int requestCode) {
        getmMvpView().hideLoading();
        loginView.showMessage("Network Error");
        Log.i("*****", "NetworkError");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        getmMvpView().hideLoading();
        loginView.showMessage("Unknown error");
        Log.i("*****", "UnknownError");
    }
}
