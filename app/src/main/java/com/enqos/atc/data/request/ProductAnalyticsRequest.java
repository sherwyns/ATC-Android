package com.enqos.atc.data.request;

public class ProductAnalyticsRequest extends BaseRequest{

    public ProductAnalyticsRequest(String store_id, String product_id, String name) {
        this.store_id = store_id;
        this.product_id = product_id;
        this.name = name;
    }

    private String store_id;
    private String product_id;
    private String name;
    private String category_id;

    public ProductAnalyticsRequest(String store_id) {
        this.store_id = store_id;
    }

    public ProductAnalyticsRequest(String store_id, String category_id) {
        this.store_id = store_id;
        this.category_id = category_id;
    }


    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }
}
