package com.enqos.atc.register;

public interface RegisterView {

    void showMessage(String message);

    void onRegisterUser();

    String getStringMessage(int id);

    int getInt(int id);
}
