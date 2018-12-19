package com.enqos.atc.ui.search;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;

import javax.inject.Inject;

public class SearchPresenter extends BasePresenter {

    @Inject
    SearchPresenter() {
        AtcApplication.getAppComponents().inject(this);
    }

}
