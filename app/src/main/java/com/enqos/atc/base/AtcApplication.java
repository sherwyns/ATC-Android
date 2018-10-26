package com.enqos.atc.base;

import android.app.Application;

import com.enqos.atc.data.NetworkConstants;
import com.enqos.atc.di.AppComponent;
import com.enqos.atc.di.AppModule;
import com.enqos.atc.di.DaggerAppComponent;
import com.enqos.atc.di.NetModule;

public class AtcApplication extends Application {

    public static AtcApplication INSTANCE;
    public static AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();

        INSTANCE = this;
        injectDependencies(INSTANCE);

    }

    public static void injectDependencies(AtcApplication context) {

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .netModule(new NetModule(NetworkConstants.BASE_URL))
                .build();
        appComponent.inject(context);

    }

    public static AppComponent getAppComponents() {
        return appComponent;
    }

    public static AtcApplication getApplicationInstance() {
        return INSTANCE;
    }
}
