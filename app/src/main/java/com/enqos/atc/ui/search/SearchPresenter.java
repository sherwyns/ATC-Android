package com.enqos.atc.ui.search;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.SearchResponse;

import javax.inject.Inject;

public class SearchPresenter extends BasePresenter implements NetworkApiResponse {

    private CreateApiRequest createApiRequest;
    private SearchView searchView;

    @Inject
    SearchPresenter() {
        AtcApplication.getAppComponents().inject(this);
    }


    void getSearch(SearchView searchView, String key) {
        this.searchView = searchView;
        if (createApiRequest == null)
            createApiRequest = new CreateApiRequest(this);
        createApiRequest.createSearchRequest(key);

    }

    @Override
    public void onSuccess(BaseResponse response) {

        if (response instanceof SearchResponse)
            searchView.searchResponse((SearchResponse) response);
    }

    @Override
    public void onFailure(String errorMessage, int requestCode, int statusCode) {
        searchView.onError(errorMessage);
    }

    @Override
    public void onTimeOut(int requestCode) {
        searchView.onError("Time out");
    }

    @Override
    public void onNetworkError(int requestCode) {
        searchView.onError("Network error");
    }

    @Override
    public void onUnknownError(int requestCode, int statusCode, String errorMessage) {
        searchView.onError("Unknown error");
    }
}
