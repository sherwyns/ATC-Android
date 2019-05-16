package com.enqos.atc.ui.shopdetail;

import com.enqos.atc.data.response.StoreDetailResponse;

public interface ShopDetailView {

    void showMessage(String message);

    void shopDetailResponse(StoreDetailResponse response);

}
