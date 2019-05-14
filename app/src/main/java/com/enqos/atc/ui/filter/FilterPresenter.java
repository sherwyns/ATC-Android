package com.enqos.atc.ui.filter;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.NeighbourhoodResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class FilterPresenter extends BasePresenter implements NetworkApiResponse {
    private FilterView filterView;
    private CreateApiRequest createApiRequest;
    private String[] neighbourhoods = {"Ballard", "Belltown", "Capitol Hill", "Downtown", "Fremont", "Greenwood", "Magnolia", "Phinney Ridge", "Pioneer Square", "Queen Anne", "South Lake Union", "Wallingford", "West Seattle"};

    @Inject
    FilterPresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

    void getCategories(FilterView filterView) {
        this.filterView = filterView;
        try {
            if (createApiRequest == null)
                createApiRequest = new CreateApiRequest(this);
            createApiRequest.createCategoriesRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void getProductCategories(FilterView filterView) {
        this.filterView = filterView;
        try {
            if (createApiRequest == null)
                createApiRequest = new CreateApiRequest(this);
            createApiRequest.createProductCategoriesRequest("0");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void getProductCategories(String id) {
        try {
            if (createApiRequest == null)
                createApiRequest = new CreateApiRequest(this);
            createApiRequest.createProductCategoriesRequest(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addAllOptionInNeighbourhood(List<CategoryEntity> categories) {
        if (categories != null) {
            CategoryEntity allCategory = new CategoryEntity();
            allCategory.setId("0");
            allCategory.setName("All");
            categories.add(0, allCategory);
        }
    }

    public void getNeibhourhoods() {
        try {
            if (createApiRequest == null)
                createApiRequest = new CreateApiRequest(this);
            createApiRequest.createNeighbourhoodRequest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (filterView != null && response instanceof CategoryResponse) {
            CategoryResponse categoryResponse = (CategoryResponse) response;
            filterView.onSuccess(categoryResponse);
        } else if (filterView != null && response instanceof NeighbourhoodResponse)
            filterView.onSuccess((NeighbourhoodResponse) response);
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {
        filterView.onError(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        filterView.onError("Time out");
    }

    @Override
    public void onNetworkError(int requestCode) {
        filterView.onError("Network error");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {

    }
}
