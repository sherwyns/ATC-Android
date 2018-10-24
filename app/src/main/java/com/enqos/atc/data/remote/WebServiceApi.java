package com.enqos.atc.data.remote;

import com.enqos.atc.data.request.RegisterRequest;
import com.enqos.atc.data.response.RegisterResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface WebServiceApi {

    @POST("api/Users")
    Observable<RegisterResponse> register(@Body RegisterRequest registerRequest);
}
