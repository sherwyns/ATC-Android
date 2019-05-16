package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

public class NewStoreFavouriteEntity {

    @SerializedName("id")
    private String id;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("store_id")
    private String store_id;
    @SerializedName("favorite")
    private String favorite;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getFavorite() {
        return favorite;
    }

    public void setFavorite(String favorite) {
        this.favorite = favorite;
    }
}
