package com.enqos.atc.ui.storeList;

import android.text.TextUtils;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreFavoriteResponse;
import com.enqos.atc.data.response.StoreResponse;
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
    static HashMap<String, List<StoreEntity>> groupStores;

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

    private void groupStoreByCategory(StoreResponse storeResponse) {
        groupStores = new HashMap<>();
        for (StoreEntity store :
                storeResponse.getData()) {
            if (store.getCategory() != null) {
                if (!groupStores.containsKey(store.getCategory().get(0).getId())) {
                    List<StoreEntity> storeEntities = new ArrayList<>();
                    storeEntities.add(store);
                    groupStores.put(store.getCategory().get(0).getId(), storeEntities);
                } else {
                    groupStores.get(store.getCategory().get(0).getId()).add(store);
                }

            }
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof StoreResponse) {
            storeResponse = (StoreResponse) response;
            groupStoreByCategory(storeResponse);
            String userId = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
            if (!TextUtils.isEmpty(userId))
                createApiRequest.createGetStoreFavorites(userId);
            else
                storeListView.storeResponse(storeResponse, null);
        } else if (response instanceof StoreFavoriteResponse) {
            StoreFavoriteResponse storeFavoriteResponse = (StoreFavoriteResponse) response;
            storeListView.storeResponse(storeResponse, storeFavoriteResponse.getData());
        }

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
