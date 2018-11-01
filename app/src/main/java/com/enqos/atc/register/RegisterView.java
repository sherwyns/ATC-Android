package com.enqos.atc.register;

import com.enqos.atc.data.response.RegisterResponse;

public interface RegisterView {

    void showMessage(String message);

    void onRegisterUser(RegisterResponse registerResponse);

    String getStringMessage(int id);

    int getInt(int id);
}
