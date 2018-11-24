package com.enqos.atc.listener;

import android.support.v4.app.Fragment;

public interface StoreActivityListener {

    void replaceFragment(Fragment fragment);

    void changeHeader(int leftResId, String text, int rightResId);

    void clickLeft();

    void clickRight();
}
