package com.enqos.atc.ui.launcher;


import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.utils.SharedPreferenceManager;

import javax.inject.Inject;

public class SplashPresenter extends BasePresenter {

    @Inject
    SharedPreferenceManager sharedPreferenceManager;

    @Inject
    public SplashPresenter() {

        AtcApplication.getAppComponents().inject(this);
    }


    public void navigate(SplashView splashView) {
        boolean isLogin = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_LOGIN);
        /*if (isLogin)
            splashView.navigateStore();
        else
            splashView.navigateHome();*/
        splashView.navigateToTutorial();
    }

}
