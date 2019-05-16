package com.enqos.atc.data.request;

import com.google.gson.annotations.SerializedName;

public class GetFavouriteRequest extends BaseRequest {


    @SerializedName("user_id")
    private String userId;
    @SerializedName("type")
    private String type;

    public GetFavouriteRequest(String userId, String type) {
        this.userId = userId;
        this.type = type;
    }

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
}
