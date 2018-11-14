package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreResponse extends BaseResponse{

    public List<StoreEntity> getData() {
        return data;
    }

    public void setData(List<StoreEntity> data) {
        this.data = data;
    }

    @SerializedName("data")
    private List<StoreEntity> data;

}
