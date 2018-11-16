package com.enqos.atc.listener;

import com.enqos.atc.data.response.StoreEntity;

import java.util.List;

public interface FavoriteListener {

    List<StoreEntity> getFavoriteStores();
}
