package com.enqos.atc.ui.register;

import android.text.TextUtils;
import android.util.Log;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.RegisterResponse;

import javax.inject.Inject;

public class RegisterPresenter extends BasePresenter implements NetworkApiResponse {


    private RegisterView registerView;
    private CreateApiRequest createApiRequest;


    @Inject
    public RegisterPresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

    public void registerUser(String email, String password, RegisterView registerView) {
        this.registerView = registerView;
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            registerView.showMessage("Please enter required inputs");
        } else {
            try {
                getmMvpView().showLoading();
                if (createApiRequest == null)
                    createApiRequest = new CreateApiRequest(this);
                createApiRequest.createRegisterRequest(email, password);

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

    public void showDialog() {
        getmMvpView().showLoading();
    }

    public void hideDialog() {
        getmMvpView().hideLoading();
    }

    public void registerSocialUser(String email, String externalId, String provider, RegisterView registerView) {
        this.registerView = registerView;
        if (TextUtils.isEmpty(email)) {
            registerView.showMessage("Please enter required inputs");
        } else {
            try {
                getmMvpView().showLoading();
                if (createApiRequest == null)
                    createApiRequest = new CreateApiRequest(this);
                createApiRequest.createSocialRegisterRequest(email, externalId, provider);

            } catch (Exception e) {

                e.printStackTrace();
            }

        }

    }

    @Override
    public void onSuccess(BaseResponse response) {
        getmMvpView().hideLoading();
        RegisterResponse registerResponse = (RegisterResponse) response;
        registerView.onRegisterUser(registerResponse);
        Log.i("*****", registerResponse.getId());
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {
        getmMvpView().hideLoading();
        registerView.showMessage(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        getmMvpView().hideLoading();
        registerView.showMessage("Time out");
    }

    @Override
    public void onNetworkError(int requestCode) {
        getmMvpView().hideLoading();
        registerView.showMessage("Network Error");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        getmMvpView().hideLoading();
        registerView.showMessage("Unknown Error");
    }
}
