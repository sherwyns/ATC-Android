package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

class ResetPasswordEntity {

    @SerializedName("status")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
