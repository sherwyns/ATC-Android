package com.enqos.atc.data.request;

import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.StoreFavoriteEntity;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SaveFavoriteRequest extends BaseRequest {

    public SaveFavoriteRequest(String userId, List<StoreFavoriteEntity> store, List<ProductFavoriteEntity> product) {
        this.userId = userId;
        this.store = store;
        this.product = product;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<StoreFavoriteEntity> getStore() {
        return store;
    }

    public void setStore(List<StoreFavoriteEntity> store) {
        this.store = store;
    }

    public List<ProductFavoriteEntity> getProduct() {
        return product;
    }

    public void setProduct(List<ProductFavoriteEntity> product) {
        this.product = product;
    }

    @SerializedName("user_id")
    private String userId;
    @SerializedName("store")
    private List<StoreFavoriteEntity> store;
    @SerializedName("product")
    private List<ProductFavoriteEntity> product;
}
