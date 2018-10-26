package com.enqos.atc.login;

public interface LoginView {
    void showMessage(String message);

    void onValidUser();

    String getStringMessage(int id);

    int getInt(int id);
}
