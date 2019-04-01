package com.enqos.atc.ui.tutorial;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;

import javax.inject.Inject;

public class TutorialPresenter extends BasePresenter {

    @Inject
    TutorialPresenter(){
        AtcApplication.getAppComponents().inject(this);
    }
}
