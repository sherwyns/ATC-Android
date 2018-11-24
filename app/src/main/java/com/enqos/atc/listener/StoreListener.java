package com.enqos.atc.listener;

import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;

public interface StoreListener {

    void onSaveStoreFavorite(StoreEntity storeEntity,boolean isFav,int pos);

    void onSaveProductFavorite(ProductFavoriteEntity productFavoriteEntity);

    void onRemoveFav(int index);


}
