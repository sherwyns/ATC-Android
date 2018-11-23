package com.enqos.atc.ui.login;

import com.enqos.atc.data.response.LoginResponse;

public interface LoginView {
    void showMessage(String message);

    void onValidUser(LoginResponse loginResponse);

    void fbLogin(String email,String id);

}
