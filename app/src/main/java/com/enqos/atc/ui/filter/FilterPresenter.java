package com.enqos.atc.ui.filter;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

import javax.inject.Inject;

public class FilterPresenter extends BasePresenter implements NetworkApiResponse {
    private FilterView filterView;
    private CreateApiRequest createApiRequest;

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

    @Override
    public void onSuccess(BaseResponse response) {

        if (filterView != null && response instanceof CategoryResponse)
            filterView.onSuccess((CategoryResponse) response);

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
