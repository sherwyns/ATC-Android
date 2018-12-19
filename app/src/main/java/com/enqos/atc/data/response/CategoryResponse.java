package com.enqos.atc.data.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryResponse extends BaseResponse{

    @SerializedName("data")
    private List<CategoryEntity> categoryEntities;

    public List<CategoryEntity> getCategoryEntities() {
        return categoryEntities;
    }

    public void setCategoryEntities(List<CategoryEntity> categoryEntities) {
        this.categoryEntities = categoryEntities;
    }
}
