package com.enqos.atc.data.request;

import com.google.gson.annotations.SerializedName;

public class SaveFavoriteRequest extends BaseRequest {

    public SaveFavoriteRequest(String userId, String id, String type, String isFavorite) {
        this.userId = userId;
        this.type = type;
        this.id = id;
        this.isFavorite = isFavorite;
    }


    @SerializedName("user_id")
    private String userId;
    @SerializedName("type")
    private String type;
    @SerializedName("isfavorite")
    private String isFavorite;
    @SerializedName("id")
    private String id;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
