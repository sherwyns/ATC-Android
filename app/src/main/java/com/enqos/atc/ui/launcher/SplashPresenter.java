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
        boolean isTutorial = (boolean) sharedPreferenceManager.getPreferenceValue(SharedPreferenceManager.BOOLEAN, SharedPreferenceManager.IS_TUTORIAL_SHOWN);

        if (isLogin)
            splashView.navigateStore();
        else if (!isTutorial) {
            sharedPreferenceManager.savePreferenceValue(SharedPreferenceManager.IS_TUTORIAL_SHOWN, true);
            splashView.navigateToTutorial();
        } else
            splashView.navigateHome();
    }

}
