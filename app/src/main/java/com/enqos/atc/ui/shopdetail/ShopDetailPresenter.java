package com.enqos.atc.ui.shopdetail;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.StoreDetailResponse;

import javax.inject.Inject;

public class ShopDetailPresenter extends BasePresenter implements NetworkApiResponse {

    private CreateApiRequest createApiRequest;
    private ShopDetailView shopDetailView;

    @Inject
    ShopDetailPresenter() {

        AtcApplication.getAppComponents().inject(this);
    }

    void callStoreDetail(ShopDetailView shopDetailView, String storeId) {
        this.shopDetailView = shopDetailView;
        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this);
        createApiRequest.createStoreDetailRequest(storeId);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof StoreDetailResponse) {
            StoreDetailResponse storeDetailResponse = (StoreDetailResponse) response;
            shopDetailView.shopDetailResponse(storeDetailResponse);
        }
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {

        shopDetailView.showMessage(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        shopDetailView.showMessage("Time out");
    }

    @Override
    public void onNetworkError(int requestCode) {
        shopDetailView.showMessage("No internet connnection");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        shopDetailView.showMessage("Unknown Error");
    }
}
