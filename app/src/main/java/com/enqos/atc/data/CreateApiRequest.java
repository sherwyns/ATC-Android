package com.enqos.atc.data;

import android.content.Context;

import com.enqos.atc.data.request.LoginRequest;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.utils.Constants;

public class CreateApiRequest {

    private final DataRepository dataRepository;
    private final NetworkApiResponse networkApiResponse;
    private final Context context;

    public CreateApiRequest(NetworkApiResponse networkApiResponse, Context context) {
        this.networkApiResponse = networkApiResponse;
        this.context = context;

        dataRepository = new DataRepository();

    }

    public void createLoginRequest(String email, String userPass) {
        LoginRequest loginInfo = new LoginRequest(email, userPass);
        dataRepository.authenticateUser(networkApiResponse, loginInfo);
    }

}
