package com.enqos.atc.listener;

import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;

public interface StoreListener {

    void onSaveStoreFavorite(StoreEntity storeEntity,boolean isFav,int pos);

    void onSaveProductFavorite(ProductEntity productEntity, boolean isFav, int pos);

    void onRemoveFav(int index,boolean isStore);


}
