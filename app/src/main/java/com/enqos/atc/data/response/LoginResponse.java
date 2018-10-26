package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginResponse extends BaseResponse implements Serializable{

    public LoginResponse(String userId, String id) {
        this.userId = userId;
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @SerializedName("userId")
    private String userId;
    @SerializedName("id")
    private String id;

}
