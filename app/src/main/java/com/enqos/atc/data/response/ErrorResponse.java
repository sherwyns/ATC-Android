package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class ErrorResponse implements Serializable {

    @SerializedName("statusCode")
    private Integer statusCode;

    @SerializedName("message")
    private String message;

    @SerializedName("requestCode")
    private Integer requestCode;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(Integer requestCode) {
        this.requestCode = requestCode;
    }

}
