package com.enqos.atc.ui.search;

import com.enqos.atc.data.response.SearchResponse;

public interface SearchView {

    void onError(String error);
    void searchResponse(SearchResponse searchResponse);

}
