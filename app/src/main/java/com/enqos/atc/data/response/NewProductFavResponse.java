package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewProductFavResponse extends BaseResponse {

    @SerializedName("data")
    private List<ProductFavEntity> data;

    public List<ProductFavEntity> getData() {
        return data;
    }

    public void setData(List<ProductFavEntity> data) {
        this.data = data;
    }
}
