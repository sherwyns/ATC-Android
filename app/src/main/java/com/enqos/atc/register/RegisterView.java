package com.enqos.atc.register;

import com.enqos.atc.data.response.RegisterResponse;

public interface RegisterView {

    void showMessage(String message);

    void onRegisterUser(RegisterResponse registerResponse);

    void fbLogin(String email,String accessToken);


}
