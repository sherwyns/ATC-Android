package com.enqos.atc.data.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest extends BaseRequest {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;

    public LoginRequest(String email, String externalid, String provider) {
        this.email = email;
        this.externalid = externalid;
        this.provider = provider;
    }

    public String getExternalid() {
        return externalid;
    }

    public void setExternalid(String externalid) {
        this.externalid = externalid;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    @SerializedName("externalid")
    private String externalid;
    @SerializedName("provider")
    private String provider;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
