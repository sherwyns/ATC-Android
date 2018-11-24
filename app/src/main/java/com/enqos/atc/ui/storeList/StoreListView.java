package com.enqos.atc.ui.storeList;

import com.enqos.atc.data.response.StoreResponse;

public interface StoreListView {

    void showMessage(String message);

    void storeResponse(StoreResponse storeResponse);

    void navigateLogin();

}
