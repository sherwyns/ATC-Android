package com.enqos.atc.listener;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.enqos.atc.data.response.CategoryEntity;
import com.enqos.atc.data.response.Neighbourhood;

import java.util.List;

public interface StoreActivityListener {
    void replaceFragment(Fragment fragment);

    void changeHeader(int leftResId, String text, int rightResId,boolean isShow);

    Toolbar getToolbar();

    double getLatitude();

    double getLongitude();

    String getCategories(List<CategoryEntity> categoryEntities);

    String getNeighbourhoods(List<Neighbourhood> categoryEntities);

    void onFilterClick(boolean isProduct);

    void setCount(String text);

    boolean  isOnBackPressed();

}
