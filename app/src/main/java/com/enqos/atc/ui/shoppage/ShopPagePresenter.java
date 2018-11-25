package com.enqos.atc.ui.shoppage;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.StorePageResponse;

import javax.inject.Inject;

public class ShopPagePresenter extends BasePresenter implements NetworkApiResponse {

    private CreateApiRequest createApiRequest;
    private ShopPageView shopPageView;


    @Inject
    ShopPagePresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

    public void callShopPageDetailApi(ShopPageView shopPageView, String storeId) {
        this.shopPageView = shopPageView;
        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this);
        createApiRequest.createStorePageRequest(storeId);

    }

    @Override
    public void onSuccess(BaseResponse response) {

        if (response instanceof StorePageResponse)
            shopPageView.onSuccess((StorePageResponse) response);
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {

        shopPageView.showMessage(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        shopPageView.showMessage("Time out");
    }

    @Override
    public void onNetworkError(int requestCode) {
        shopPageView.showMessage("No internet connection");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        shopPageView.showMessage("Unknown error");
    }
}
