package com.enqos.atc.listener;

import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

public interface StoreActivityListener {

    void replaceFragment(Fragment fragment);

    void changeHeader(int leftResId, String text, int rightResId);

    Toolbar getToolbar();
    void clickLeft();

    void clickRight();
}
