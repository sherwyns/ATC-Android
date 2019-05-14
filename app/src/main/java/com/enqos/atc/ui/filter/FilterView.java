package com.enqos.atc.ui.filter;

import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.NeighbourhoodResponse;

public interface FilterView {

    void onSuccess(CategoryResponse categoryResponse);

    void onError(String message);

    void onSuccess(NeighbourhoodResponse neighbourhoodResponse);
}
