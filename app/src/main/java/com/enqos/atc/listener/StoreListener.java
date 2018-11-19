package com.enqos.atc.listener;

import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;

public interface StoreListener {

    void onSaveStoreFavorite(StoreFavoriteEntity storeFavoriteEntity);

    void onSaveProductFavorite(ProductFavoriteEntity productFavoriteEntity);

    void onRemoveFav(int index);
}
