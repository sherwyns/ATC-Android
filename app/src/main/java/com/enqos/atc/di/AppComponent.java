package com.enqos.atc.di;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.DataRepository;
import com.enqos.atc.ui.filter.FilterFragment;
import com.enqos.atc.ui.filter.FilterPresenter;
import com.enqos.atc.ui.launcher.SplashActivity;
import com.enqos.atc.ui.launcher.SplashPresenter;
import com.enqos.atc.ui.login.ForgotPassPresenter;
import com.enqos.atc.ui.login.ForgotPasswordActivity;
import com.enqos.atc.ui.login.LoginActivity;
import com.enqos.atc.ui.login.LoginPresenter;
import com.enqos.atc.ui.myaccount.MyAccountActivity;
import com.enqos.atc.ui.myaccount.MyAccountPresenter;
import com.enqos.atc.ui.productdetail.ProductDetailFragment;
import com.enqos.atc.ui.register.RegisterActivity;
import com.enqos.atc.ui.register.RegisterPresenter;
import com.enqos.atc.ui.search.SearchPresenter;
import com.enqos.atc.ui.shopdetail.ShopDetailFragment;
import com.enqos.atc.ui.shopdetail.ShopDetailPresenter;
import com.enqos.atc.ui.shoppage.ShopPagePresenter;
import com.enqos.atc.ui.shoppage.StorePageFragment;
import com.enqos.atc.ui.storeList.FavouriteFragment;
import com.enqos.atc.ui.search.SearchFragment;
import com.enqos.atc.ui.storeList.ShopListFragment;
import com.enqos.atc.ui.storeList.StoreListActivity;
import com.enqos.atc.ui.storeList.StoreListPresenter;

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

    void inject(FavouriteFragment favouriteFragment);

    void inject(ShopDetailFragment shopDetailFragment);

    void inject(ShopDetailPresenter shopDetailPresenter);

    void inject(ShopPagePresenter shopPagePresenter);

    void inject(StorePageFragment storePageFragment);

    void inject(ProductDetailFragment productDetailFragment);

    void inject(SearchPresenter searchPresenter);

    void inject(ForgotPassPresenter forgotPassPresenter);

    void inject(ForgotPasswordActivity forgotPasswordActivity);

    void inject(FilterPresenter filterPresenter);

    void inject(FilterFragment filterFragment);
}
