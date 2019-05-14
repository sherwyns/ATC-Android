package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NeighbourhoodResponse extends BaseResponse {

    @SerializedName("data")
    private List<Neighbourhood> categoryEntities;

    public List<Neighbourhood> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<Neighbourhood> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }
}