package com.enqos.atc.di;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.DataRepository;
import com.enqos.atc.launcher.SplashActivity;
import com.enqos.atc.launcher.SplashPresenter;
import com.enqos.atc.login.LoginActivity;
import com.enqos.atc.login.LoginPresenter;
import com.enqos.atc.myaccount.MyAccountActivity;
import com.enqos.atc.myaccount.MyAccountPresenter;
import com.enqos.atc.register.RegisterActivity;
import com.enqos.atc.register.RegisterPresenter;
import com.enqos.atc.storeList.SearchFragment;
import com.enqos.atc.storeList.ShopListFragment;
import com.enqos.atc.storeList.StoreListActivity;
import com.enqos.atc.storeList.StoreListPresenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, NetModule.class,SharedPreferenceModule.class})
public interface AppComponent {

    void inject(AtcApplication application);

    void inject(DataRepository dataRepository);

    void inject(LoginPresenter loginPresenter);

    void inject(LoginActivity loginActivity);

    void inject(RegisterPresenter registerPresenter);

    void inject(RegisterActivity registerActivity);

    void inject(SplashPresenter splashPresenter);

    void inject(SplashActivity splashActivity);

    void inject(StoreListPresenter storeListPresenter);

    void inject(StoreListActivity storeListActivity);

    void inject(MyAccountPresenter myAccountPresenter);

    void inject(MyAccountActivity myAccountActivity);

    void inject(SearchFragment searchFragment);

    void inject(ShopListFragment shopListFragment);
}
