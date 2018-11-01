package com.enqos.atc.login;

import com.enqos.atc.data.response.LoginResponse;

public interface LoginView {
    void showMessage(String message);

    void onValidUser(LoginResponse loginResponse);

    String getStringMessage(int id);

    int getInt(int id);
}
