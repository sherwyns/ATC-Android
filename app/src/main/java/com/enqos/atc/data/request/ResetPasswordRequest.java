package com.enqos.atc.data.request;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest extends BaseRequest{

    @SerializedName("email")
    private String email;
    @SerializedName("url")
    private String url;

    public ResetPasswordRequest(String email, String url) {
        this.email = email;
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
