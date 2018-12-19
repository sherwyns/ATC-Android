package com.enqos.atc.ui.login;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;

import javax.inject.Inject;

public class ForgotPassPresenter extends BasePresenter {

    @Inject
    ForgotPassPresenter(){
        AtcApplication.getAppComponents().inject(this);
    }

}
