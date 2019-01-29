package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

public class SaveFavouriteResponse extends BaseResponse {

    @SerializedName("data")
    private SaveFavourite data;

    public SaveFavourite getData() {
        return data;
    }

    public void setData(SaveFavourite data) {
        this.data = data;
    }
}
