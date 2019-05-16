package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponse extends BaseResponse{

    @SerializedName("data")
    private List<SearchDataResponse> data;

    public List<SearchDataResponse> getData() {
        return data;
    }

    public void setData(List<SearchDataResponse> data) {
        this.data = data;
    }
}
