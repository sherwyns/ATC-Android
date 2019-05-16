package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

public class UpdateFavoriteResponse extends BaseResponse{

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @SerializedName("count")
    private String count;

}
