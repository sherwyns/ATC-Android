package com.enqos.atc.ui.login;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.ResetPasswordResponse;

import javax.inject.Inject;

public class ForgotPassPresenter extends BasePresenter implements NetworkApiResponse {

    CreateApiRequest createApiRequest;
    ForgotPassView forgotPassView;

    @Inject
    ForgotPassPresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

    public void resetPassword(ForgotPassView forgotPassView, String email, String url) {
        this.forgotPassView = forgotPassView;
        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this);
        createApiRequest.createResetPasswordRequest(email, url);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        forgotPassView.onSuccess();
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {

        forgotPassView.onMessage(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        forgotPassView.onMessage("Timeout");
    }

    @Override
    public void onNetworkError(int requestCode) {
        forgotPassView.onMessage("Network Error");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        forgotPassView.onMessage(errorMessage);
    }
}
