package com.enqos.atc.data.response;

public interface NetworkApiResponse {

    void onSuccess(BaseResponse response);

    void onFailure(String errorMessage, int requestCode, int statusCode);

    void onTimeOut(int requestCode);

    void onNetworkError(int requestCode);

    void onUnknownError(int requestCode, int statusCode, String errorMessage);
}
