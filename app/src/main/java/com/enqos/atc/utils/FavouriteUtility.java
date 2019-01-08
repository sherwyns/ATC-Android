package com.enqos.atc.utils;

import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;

import java.util.ArrayList;
import java.util.List;

public class FavouriteUtility {

    public static void saveFavourite(String userId, List<StoreFavoriteEntity> store, List<ProductFavoriteEntity> product) {
        CreateApiRequest createApiRequest = new CreateApiRequest(networkApiResponse);
        createApiRequest.createSaveFavoriteRequest(userId, store, product);
    }

    public static List<StoreFavoriteEntity> getStoreFavourites(List<StoreEntity> stores) {
        List<StoreFavoriteEntity> favStores = new ArrayList<>();
        if (stores != null) {
            for (StoreEntity store : stores) {
                StoreFavoriteEntity storeFavoriteEntity = new StoreFavoriteEntity(store.getId(), "1");
                favStores.add(storeFavoriteEntity);
            }
        }

        return favStores;
    }

    public static List<ProductFavoriteEntity> getProductFavourites(List<ProductEntity> products) {
        List<ProductFavoriteEntity> favStores = new ArrayList<>();

        if (products != null) {
            for (ProductEntity product : products) {
                ProductFavoriteEntity productFavoriteEntity = new ProductFavoriteEntity(product.getId(), "1");
                favStores.add(productFavoriteEntity);
            }
        }
        return favStores;
    }

    private static NetworkApiResponse networkApiResponse = new NetworkApiResponse() {
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
    };

}
