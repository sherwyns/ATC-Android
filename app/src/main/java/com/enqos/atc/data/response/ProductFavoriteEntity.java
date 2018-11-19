package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

public class ProductFavoriteEntity {

    public ProductFavoriteEntity(String productid, String isfavorite) {
        this.productid = productid;
            this.isfavorite = isfavorite;
    }


    public String getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(String isfavorite) {
        this.isfavorite = isfavorite;
    }

    public String getProductid() {
        return productid;
    }

    public void setProductid(String productid) {
        this.productid = productid;
    }


    @SerializedName("isfavorite")
    private String isfavorite;
    @SerializedName("productid")
    private String productid;
}
