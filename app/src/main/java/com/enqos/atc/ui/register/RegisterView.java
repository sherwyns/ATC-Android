package com.enqos.atc.ui.register;

import com.enqos.atc.data.response.RegisterResponse;

public interface RegisterView {

    void showMessage(String message);

    void onRegisterUser(RegisterResponse registerResponse);

    void fbLogin(String email,String accessToken);


}
