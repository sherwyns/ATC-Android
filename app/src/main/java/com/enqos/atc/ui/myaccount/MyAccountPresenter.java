package com.enqos.atc.ui.myaccount;

import android.text.TextUtils;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

import javax.inject.Inject;

public class MyAccountPresenter extends BasePresenter implements NetworkApiResponse {

    private CreateApiRequest createApiRequest;
    private MyAccountView accountView;

    @Inject
    MyAccountPresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

    public void changePassword(MyAccountView accountView, String accessToken, String oldPassword, String newPassword) {
        this.accountView = accountView;
        createApiRequest = new CreateApiRequest(this);
        createApiRequest.createChangePasswordRequest(accessToken, oldPassword, newPassword);


    }

    @Override
    public void onSuccess(BaseResponse response) {
        accountView.onSuccess();
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {
        accountView.errorMessage(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        accountView.errorMessage("Time Out");
    }

    @Override
    public void onNetworkError(int requestCode) {
        accountView.errorMessage("Network error");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        accountView.errorMessage(errorMessage);
    }
}
