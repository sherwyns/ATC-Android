package com.enqos.atc.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.enqos.atc.utils.SharedPreferenceManager;

import dagger.Module;
import dagger.Provides;

@Module
public class SharedPreferenceModule {

    private Context context;

    public SharedPreferenceModule(Context context) {
        this.context = context;
    }

    @Provides
    SharedPreferences provideSharedPreferences() {
        return context.getSharedPreferences(SharedPreferenceManager.ATC_PREF_NAME, Context.MODE_PRIVATE);
    }
}
