package com.enqos.atc.utils;

import android.content.Context;
import android.content.SharedPreferences;

import javax.inject.Inject;

public class SharedPreferenceManager {

    public static final String ATC_PREF_NAME = "ATC_PREF_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String USER_ID = "USER_ID";
    public static final String TOKEN = "TOKEN";
    public static final String STRING = "STRING";
    public static final String INT = "INT";
    public static final String BOOLEAN = "BOOLEAN";


    @Inject
    public SharedPreferenceManager() {

    }

    public void savePreferenceValue(Context context, String key, Object value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ATC_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        }
        editor.apply();
    }

    public Object getPreferenceValue(Context context, String type, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ATC_PREF_NAME, Context.MODE_PRIVATE);
        if (type.equalsIgnoreCase(STRING)) {
            return sharedPreferences.getString(key, "");
        } else if (type.equalsIgnoreCase(BOOLEAN)) {
            return sharedPreferences.getBoolean(key, false);
        } else {
            return sharedPreferences.getInt(key, 0);
        }
    }
}
