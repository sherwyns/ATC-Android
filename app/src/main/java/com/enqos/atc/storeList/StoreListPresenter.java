package com.enqos.atc.storeList;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

import javax.inject.Inject;

public class StoreListPresenter extends BasePresenter implements NetworkApiResponse{

    @Inject
    StoreListPresenter(){
        AtcApplication.getAppComponents().inject(this);
    }

    @Override
    public void onSuccess(BaseResponse response) {

    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {

    }

    @Override
    public void onTimeOut(int requestCode) {

    }

    @Override
    public void onNetworkError(int requestCode) {

    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {

    }
}
