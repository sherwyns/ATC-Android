package com.enqos.atc.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.ErrorResponse;
import com.enqos.atc.data.response.LoginResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

import javax.inject.Inject;

public class LoginPresenter extends BasePresenter implements NetworkApiResponse {
    private LoginView loginView;
    private String email;
    private String password;
    private CreateApiRequest createApiRequest;

    @Inject
    public LoginPresenter() {

        AtcApplication.getAppComponents().inject(this);
    }


    public void authenticateUser(String email, String password, LoginView loginView) {

        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this, (Context) getmMvpView());

        try {
            this.loginView = loginView;
            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {

                this.email = email;
                this.password = password;
                getmMvpView().showLoading();
                createApiRequest.createLoginRequest(email, password);
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        getmMvpView().hideLoading();
        LoginResponse loginResponse = (LoginResponse) response;

        Log.i("*****", loginResponse.getUserId());

    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {
        getmMvpView().hideLoading();
        Log.i("*****", errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        getmMvpView().hideLoading();
        Log.i("*****", "TimeOut");
    }

    @Override
    public void onNetworkError(int requestCode) {
        getmMvpView().hideLoading();
        Log.i("*****", "NetworkError");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        getmMvpView().hideLoading();
        Log.i("*****", "UnknownError");
    }
}
