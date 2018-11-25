package com.enqos.atc.ui.shoppage;

import com.enqos.atc.data.response.StorePageResponse;

public interface ShopPageView {

    void showMessage(String message);
    void onSuccess(StorePageResponse response);

}
