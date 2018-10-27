package com.enqos.atc.di;

import android.content.SharedPreferences;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.data.DataRepository;
import com.enqos.atc.login.LoginActivity;
import com.enqos.atc.login.LoginPresenter;
import com.enqos.atc.register.RegisterActivity;
import com.enqos.atc.register.RegisterPresenter;
import com.squareup.okhttp.OkHttpClient;

import javax.inject.Singleton;

import dagger.Component;
import retrofit2.Retrofit;

@Singleton
@Component(modules = {AppModule.class, NetModule.class})
public interface AppComponent {

    void inject(AtcApplication application);

    void inject(DataRepository dataRepository);

    void inject(LoginPresenter loginPresenter);

    void inject(LoginActivity loginActivity);

    void inject(RegisterPresenter registerPresenter);

    void inject(RegisterActivity registerActivity);
}
