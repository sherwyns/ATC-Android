package com.enqos.atc.data.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegisterRequest extends BaseRequest {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("emailVerified")
    @Expose
    private boolean emailVerified;

    public RegisterRequest(String username, String email, String externalid, String provider) {
        this.username = username;
        this.email = email;
        this.provider = provider;
        this.externalid = externalid;
    }

    @SerializedName("provider")
    private String provider;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getExternalid() {
        return externalid;
    }

    public void setExternalid(String externalid) {
        this.externalid = externalid;
    }

    @SerializedName("externalid")
    private String externalid;


    public RegisterRequest(String username, String emaill, String password, boolean emailVerified) {
        this.username = username;
        this.email = emaill;
        this.password = password;
        this.emailVerified = emailVerified;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }
}
