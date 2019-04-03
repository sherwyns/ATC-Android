package com.enqos.atc.utils;

import android.content.SharedPreferences;

import com.enqos.atc.data.response.FavoriteResponse;
import com.enqos.atc.data.response.ProductEntity;
import com.enqos.atc.data.response.StoreEntity;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import javax.inject.Inject;

public class SharedPreferenceManager {

    public static final String ATC_PREF_NAME = "ATC_PREF_NAME";
    public static final String EMAIL = "EMAIL";
    public static final String IS_LOGIN = "IS_LOGIN";
    public static final String IS_TUTORIAL_SHOWN = "IS_TUTORIAL_SHOWN";
    public static final String USER_ID = "USER_ID";
    public static final String TOKEN = "TOKEN";
    public static final String STRING = "STRING";
    public static final String INT = "INT";
    public static final String BOOLEAN = "BOOLEAN";
    public static final String FAVOURITES = "FAVOURITES";
    public static final String PRODUCT_FAVOURITES = "PRODUCT_FAVOURITES";
    private SharedPreferences sharedPreferences;
    private Gson gson;

    @Inject
    public SharedPreferenceManager(SharedPreferences sharedPreferences, Gson gson) {
        this.sharedPreferences = sharedPreferences;
        this.gson = gson;
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

    public void saveProductFavourites(List<ProductEntity> favoriteResponse) {

        savePreferenceValue(PRODUCT_FAVOURITES, gson.toJson(favoriteResponse, new TypeToken<List<ProductEntity>>() {
        }.getType()));
    }

    public List<ProductEntity> getProductFavorites() {
        return gson.fromJson((String) getPreferenceValue(STRING, PRODUCT_FAVOURITES), new TypeToken<List<ProductEntity>>() {
        }.getType());
    }

    public void saveFavourites(List<StoreEntity> favoriteResponse) {

        savePreferenceValue(FAVOURITES, gson.toJson(favoriteResponse, new TypeToken<List<StoreEntity>>() {
        }.getType()));
    }

    public List<StoreEntity> getFavorites() {
        return gson.fromJson((String) getPreferenceValue(STRING, FAVOURITES), new TypeToken<List<StoreEntity>>() {
        }.getType());
    }
}
