package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FavoriteResponse extends BaseResponse{

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
    private String user_id;
    @SerializedName("store")
    private List<StoreFavoriteEntity> store;
    @SerializedName("product")
    private List<ProductFavoriteEntity> product;
}
