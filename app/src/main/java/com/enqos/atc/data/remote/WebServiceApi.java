package com.enqos.atc.data.remote;

import com.enqos.atc.data.request.LoginRequest;
import com.enqos.atc.data.request.RegisterRequest;
import com.enqos.atc.data.response.LoginResponse;
import com.enqos.atc.data.response.RegisterResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceApi {

    @POST("api/Users")
    Observable<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("api/Users/login")
    Observable<LoginResponse> authenticate(@Body LoginRequest loginRequest);

    @POST("api/socialUsers/signup")
    Observable<RegisterResponse> socialNetworkSignUp(@Body RegisterRequest registerRequest);

    @POST("api/socialUsers/signin")
    Observable<LoginResponse> socialNetworkSignIn(@Body LoginRequest loginRequest);


}
