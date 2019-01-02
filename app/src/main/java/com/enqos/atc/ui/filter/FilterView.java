package com.enqos.atc.ui.filter;

import com.enqos.atc.data.response.CategoryResponse;

public interface FilterView {

    void onSuccess(CategoryResponse categoryResponse);

    void onError(String message);

    void onItemClick(String id);
}
