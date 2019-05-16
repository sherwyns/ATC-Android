package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class BaseResponse implements Serializable {

    public ErrorResponse getError() {
        return error;
    }

    public void setError(ErrorResponse error) {
        this.error = error;
    }

    @SerializedName("error")
    private ErrorResponse error;

}
