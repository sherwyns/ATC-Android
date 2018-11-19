package com.enqos.atc.storeList;

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
import com.enqos.atc.utils.SharedPreferenceManager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class StoreListPresenter extends BasePresenter implements NetworkApiResponse {

    private CreateApiRequest createApiRequest;
    private StoreListView storeListView;
    private FavoriteResponse favoriteResponse;
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

    void saveFavorite(String userId, StoreFavoriteEntity favoriteEntity) {
        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        if (isLogin) {
            List<StoreFavoriteEntity> store = new ArrayList<>();
            List<ProductFavoriteEntity> product = new ArrayList<>();
            if (favoriteResponse != null && favoriteResponse.getStore() != null) {
                store = favoriteResponse.getStore();
                store.add(favoriteEntity);
            } else {
                store.add(favoriteEntity);
            }
            createApiRequest.createSaveFavoriteRequest(userId, store, product);
        } else {
            storeListView.navigateLogin();
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response instanceof StoreResponse) {
            storeResponse = (StoreResponse) response;
            String id = (String) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.STRING, SharedPreferenceManager.USER_ID);
            boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
            if (isLogin)
                createApiRequest.createFavoriteRequest("{\"where\":{\"user_id\":" + id + "}}");
        } else {
            favoriteResponse = (FavoriteResponse) response;
        }
        if (favoriteResponse != null && favoriteResponse.getStore() != null && storeResponse != null && storeResponse.getData() != null) {
            sharedPreferenceManager.saveFavourites(getFavoriteStores());
            storeListView.storeResponse(storeResponse);
        } else
            storeListView.storeResponse(storeResponse);
    }

    List<StoreEntity> getFavoriteStores() {
        List<StoreEntity> allStoreFavorites = new ArrayList<>();
        for (StoreFavoriteEntity favStore :
                favoriteResponse.getStore()) {
            for (StoreEntity store : storeResponse.getData()) {
                if (store.getId().equals(favStore.getStoreid())) {
                    store.setFavourite(true);
                    allStoreFavorites.add(store);

                }
            }
        }
        return allStoreFavorites;


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
