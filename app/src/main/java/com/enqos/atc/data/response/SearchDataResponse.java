package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchDataResponse {

    @SerializedName("products")
    private List<ProductEntity> products;
    @SerializedName("stores")
    private List<StoreEntity> stores;

    public List<ProductEntity> getProducts() {
        return products;
    }

    public void setProducts(List<ProductEntity> products) {
        this.products = products;
    }

    public List<StoreEntity> getStores() {
        return stores;
    }

    public void setStores(List<StoreEntity> stores) {
        this.stores = stores;
    }
}
