package com.enqos.atc.data;

import com.enqos.atc.data.request.LoginRequest;
import com.enqos.atc.data.request.RegisterRequest;
import com.enqos.atc.data.response.NetworkApiResponse;

public class CreateApiRequest {

    private final DataRepository dataRepository;
    private final NetworkApiResponse networkApiResponse;

    public CreateApiRequest(NetworkApiResponse networkApiResponse) {
        this.networkApiResponse = networkApiResponse;
        dataRepository = new DataRepository();

    }

    public void createLoginRequest(String email, String userPass) {
        LoginRequest loginInfo = new LoginRequest(email, userPass);
        dataRepository.authenticateUser(networkApiResponse, loginInfo);
    }

    public void createSocialLoginRequest(String email, String externalId, String provider) {
        LoginRequest loginInfo = new LoginRequest(email, externalId, provider);
        dataRepository.authenticateSocialUser(networkApiResponse, loginInfo);
    }

    public void createSocialRegisterRequest(String email, String externalId, String provider) {
        RegisterRequest registerRequest = new RegisterRequest(email, email, externalId, provider);
        dataRepository.socialRegisterUser(networkApiResponse, registerRequest);
    }

    public void createRegisterRequest(String email, String userPass) {
        RegisterRequest registerRequest = new RegisterRequest(email, email, userPass, true);
        dataRepository.registerUser(networkApiResponse, registerRequest);
    }

    public void createStoreRequest() {
        dataRepository.getStore(networkApiResponse);
    }

}
