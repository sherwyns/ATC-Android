package com.enqos.atc.di;

import android.app.Application;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.utils.Constants;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class AppModule {

    final Application myApplication;

    public AppModule(AtcApplication myApplication) {
        this.myApplication = myApplication;
    }

    @Provides
    @Singleton
    @Named(Constants.MAIN_THREAD)
    Scheduler provideMainThreadScheduler() {
        return AndroidSchedulers.mainThread();
    }

    @Provides
    @Singleton
    @Named(Constants.NEW_THREAD)
    Scheduler provideNewThreadScheduler() {
        return Schedulers
                .newThread();
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return myApplication;
    }
}
