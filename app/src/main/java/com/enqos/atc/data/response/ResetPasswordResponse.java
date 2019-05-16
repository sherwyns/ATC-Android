package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordResponse extends BaseResponse {

    @SerializedName("data")
    private ResetPasswordEntity data;

    public ResetPasswordEntity getData() {
        return data;
    }

    public void setData(ResetPasswordEntity data) {
        this.data = data;

    }
}
