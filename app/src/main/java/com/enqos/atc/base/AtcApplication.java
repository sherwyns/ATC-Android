package com.enqos.atc.base;

import android.app.Application;

import com.enqos.atc.data.NetworkConstants;
import com.enqos.atc.di.AppComponent;
import com.enqos.atc.di.AppModule;
import com.enqos.atc.di.DaggerAppComponent;
import com.enqos.atc.di.NetModule;
import com.enqos.atc.di.SharedPreferenceModule;
import com.squareup.leakcanary.LeakCanary;

public class AtcApplication extends Application {

    public static AtcApplication INSTANCE;
    public static AppComponent appComponent;


    @Override
    public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);*/
        // Normal app init code...
        INSTANCE = this;
        injectDependencies(INSTANCE);

    }

    public static void injectDependencies(AtcApplication context) {

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(context))
                .sharedPreferenceModule(new SharedPreferenceModule(context))
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
