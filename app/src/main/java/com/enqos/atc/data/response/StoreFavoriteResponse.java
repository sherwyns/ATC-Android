package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StoreFavoriteResponse extends BaseResponse {

    @SerializedName("data")
    private List<NewStoreFavouriteEntity> data;

    public List<NewStoreFavouriteEntity> getData() {
        return data;
    }

    public void setData(List<NewStoreFavouriteEntity> data) {
        this.data = data;
    }
}
