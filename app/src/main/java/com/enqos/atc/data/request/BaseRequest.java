package com.enqos.atc.data.request;

import com.google.gson.annotations.SerializedName;

public class BaseRequest {
    @SerializedName("requestCode")
    private int requestCode;

    public int getRequestCode() {
        return requestCode;
    }

    public void setRequestCode(int requestCode) {
        this.requestCode = requestCode;
    }


}
