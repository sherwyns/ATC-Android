package com.enqos.atc.ui.storeList;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.FavoriteResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.data.response.UpdateFavoriteResponse;
import com.enqos.atc.utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

public class StoreListPresenter extends BasePresenter implements NetworkApiResponse {

    private CreateApiRequest createApiRequest;
    private StoreListView storeListView;
    private StoreResponse storeResponse;
    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    @Inject
    StoreListPresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

    void getStore(StoreListView storeListView) {
        this.storeListView = storeListView;
        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this);
        try {
            createApiRequest.createStoreRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof StoreResponse) {
            storeResponse = (StoreResponse) response;
            String id = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
        }
        storeListView.storeResponse(storeResponse);
    }


    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {
        storeListView.showMessage(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        storeListView.showMessage("Time Out");
    }

    @Override
    public void onNetworkError(int requestCode) {
        storeListView.showMessage("Network error");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        storeListView.showMessage(errorMessage);
    }
}
