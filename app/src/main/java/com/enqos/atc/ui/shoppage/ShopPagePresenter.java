package com.enqos.atc.ui.shoppage;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.StorePageResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class ShopPagePresenter extends BasePresenter implements NetworkApiResponse {

    private CreateApiRequest createApiRequest;
    private ShopPageView shopPageView;
    public Map<String, List<ProductEntity>> groupProducts;
    public List<String> categories;


    @Inject
    ShopPagePresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

    void callShopPageDetailApi(ShopPageView shopPageView, String storeId) {
        this.shopPageView = shopPageView;
        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this);
        createApiRequest.createStorePageRequest(storeId);

    }

    private void groupProducts(StorePageResponse storePageResponse) {
        groupProducts = new HashMap<>();
        categories = new ArrayList<>();
        categories.add(0, "All");
        for (ProductEntity product :
                storePageResponse.getData()) {
            if (!groupProducts.containsKey(product.getCategory_name())) {
                categories.add(product.getCategory_name());
                List<ProductEntity> productEntities = new ArrayList<>();
                productEntities.add(product);
                groupProducts.put(product.getCategory_name(), productEntities);
            } else {
                groupProducts.get(product.getCategory_name()).add(product);
            }

        }
    }

    @Override
    public void onSuccess(BaseResponse response) {

        if (response instanceof StorePageResponse) {
            StorePageResponse storePageResponse = (StorePageResponse) response;
            groupProducts(storePageResponse);
            shopPageView.onSuccess(storePageResponse);
        }
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
