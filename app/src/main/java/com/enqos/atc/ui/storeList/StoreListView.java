package com.enqos.atc.ui.storeList;

import com.enqos.atc.data.response.NewStoreFavouriteEntity;
import com.enqos.atc.data.response.StoreResponse;

import java.util.List;

public interface StoreListView {

    void showMessage(String message);

    void storeResponse(StoreResponse storeResponse, List<NewStoreFavouriteEntity> data);

    void navigateLogin();

}
