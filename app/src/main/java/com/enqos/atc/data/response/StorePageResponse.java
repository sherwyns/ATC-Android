package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StorePageResponse extends BaseResponse{

    @SerializedName("data")
    private List<ProductEntity> data;

    public List<ProductEntity> getData() {
        return data;
    }

    public void setData(List<ProductEntity> data) {
        this.data = data;
    }
}
