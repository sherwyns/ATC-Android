package com.enqos.atc.ui.filter.multifilter;

import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.CategoryResponse;

import java.util.List;

public interface MultiFilterView {
    void onSuccess(CategoryResponse categoryResponse);

    void onError(String message);

    void setFinalCategories(List<CategoryEntity> parentCategories);
}
