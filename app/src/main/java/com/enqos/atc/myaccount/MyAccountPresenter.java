package com.enqos.atc.myaccount;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;

import javax.inject.Inject;

public class MyAccountPresenter extends BasePresenter{

    @Inject
    MyAccountPresenter(){

        AtcApplication.getAppComponents().inject(this);
    }
}
