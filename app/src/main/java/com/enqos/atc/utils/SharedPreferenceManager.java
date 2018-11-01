package com.enqos.atc.utils;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class SharedPreferenceManager {

    public static final String ATC_PREF_NAME = "ATC_PREF_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String USER_ID = "USER_ID";
    public static final String TOKEN = "TOKEN";
    public static final String STRING = "STRING";
    public static final String INT = "INT";
    public static final String BOOLEAN = "BOOLEAN";


    private SharedPreferences sharedPreferences;

    @Inject
    public SharedPreferenceManager(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public void savePreferenceValue(String key, Object value) {
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

    public Object getPreferenceValue(String type, String key) {
        if (type.equalsIgnoreCase(STRING)) {
            return sharedPreferences.getString(key, "");
        } else if (type.equalsIgnoreCase(BOOLEAN)) {
            return sharedPreferences.getBoolean(key, false);
        } else {
            return sharedPreferences.getInt(key, 0);
        }
    }

    public void clearPreference() {
        sharedPreferences.edit().clear().apply();
    }
}
