package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

public class StoreFavoriteEntity {

    @SerializedName("storeid")
    private String storeid;
    @SerializedName("isfavorite")
    private String isfavorite;

    public StoreFavoriteEntity(String storeid, String isfavorite) {
        this.storeid = storeid;
        this.isfavorite = isfavorite;
    }

    public String getStoreid() {
        return storeid;
    }

    public void setStoreid(String storeid) {
        this.storeid = storeid;
    }

    public String getIsfavorite() {
        return isfavorite;
    }

    public void setIsfavorite(String isfavorite) {
        this.isfavorite = isfavorite;
    }
}
