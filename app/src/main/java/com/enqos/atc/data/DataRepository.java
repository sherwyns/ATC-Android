package com.enqos.atc.data;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.remote.WebServiceApi;
import com.enqos.atc.data.request.LoginRequest;
import com.enqos.atc.data.request.RegisterRequest;
import com.enqos.atc.data.request.SaveFavoriteRequest;
import com.enqos.atc.data.request.UpdateFavoriteRequest;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.FavoriteResponse;
import com.enqos.atc.data.response.LoginResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.RegisterResponse;
import com.enqos.atc.data.response.SearchResponse;
import com.enqos.atc.data.response.StoreDetailResponse;
import com.enqos.atc.data.response.StorePageResponse;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.data.response.UpdateFavoriteResponse;
import com.enqos.atc.utils.Constants;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;

import javax.inject.Inject;
import javax.inject.Named;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import okhttp3.ResponseBody;
import retrofit2.HttpException;
import retrofit2.Retrofit;

public class DataRepository extends BasePresenter {

    @Inject
    @Named(Constants.MAIN_THREAD)
    Scheduler mainThread;

    @Inject
    @Named(Constants.NEW_THREAD)
    Scheduler newThread;


    @Inject
    Retrofit retrofit;

    @Inject
    DataRepository() {

        AtcApplication.getAppComponents().inject(this);

    }


    @SuppressLint("CheckResult")
    void registerUser(NetworkApiResponse networkApiResponse, RegisterRequest registerRequest) {

        Observable<RegisterResponse> registerResponse = retrofit.create(WebServiceApi.class).register(registerRequest);

        registerResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, registerRequest.getRequestCode()), RegisterResponse.class))
                .subscribe(response -> {
                    if (response.getError() != null) {
                        networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                    } else {
                        networkApiResponse.onSuccess(response);
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void socialRegisterUser(NetworkApiResponse networkApiResponse, RegisterRequest registerRequest) {

        Observable<RegisterResponse> registerResponse = retrofit.create(WebServiceApi.class).socialNetworkSignUp(registerRequest);

        registerResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getSocialExceptionResponse(throwable, networkApiResponse, registerRequest.getRequestCode()), RegisterResponse.class))
                .subscribe(response -> {
                    if (response.getError() != null || TextUtils.isEmpty(response.getId())) {
                        networkApiResponse.onFailure("user already exists", 121, 200);
                    } else {
                        networkApiResponse.onSuccess(response);
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void authenticateUser(NetworkApiResponse networkApiResponse, LoginRequest loginRequest) {

        Observable<LoginResponse> loginResponse = retrofit.create(WebServiceApi.class).authenticate(loginRequest);

        loginResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, loginRequest.getRequestCode()), LoginResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void authenticateSocialUser(NetworkApiResponse networkApiResponse, LoginRequest loginRequest) {

        Observable<LoginResponse> loginResponse = retrofit.create(WebServiceApi.class).socialNetworkSignIn(loginRequest);

        loginResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getSocialExceptionResponse(throwable, networkApiResponse, loginRequest.getRequestCode()), LoginResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null || TextUtils.isEmpty(response.getId())) {
                            networkApiResponse.onFailure("user already exists", 121, 200);
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void getStore(NetworkApiResponse networkApiResponse) {

        Observable<StoreResponse> storeResponse = retrofit.create(WebServiceApi.class).store();

        storeResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), StoreResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void saveFavorite(NetworkApiResponse networkApiResponse, SaveFavoriteRequest favoriteRequest) {

        Observable<FavoriteResponse> storeResponse = retrofit.create(WebServiceApi.class).saveFavorite(favoriteRequest);

        storeResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, favoriteRequest.getRequestCode()), FavoriteResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void updateFavorite(NetworkApiResponse networkApiResponse, String id, UpdateFavoriteRequest favoriteRequest) {

        Observable<UpdateFavoriteResponse> storeResponse = retrofit.create(WebServiceApi.class).updateFavorite(id, favoriteRequest);

        storeResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, favoriteRequest.getRequestCode()), UpdateFavoriteResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void getFavorites(NetworkApiResponse networkApiResponse, String id) {

        Observable<FavoriteResponse> storeResponse = retrofit.create(WebServiceApi.class).favorites(id);

        storeResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), FavoriteResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void getStoreDetail(NetworkApiResponse networkApiResponse, String id) {

        Observable<StoreDetailResponse> storeDetailResponse = retrofit.create(WebServiceApi.class).storeDetail(id);

        storeDetailResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), StoreDetailResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void getStorePage(NetworkApiResponse networkApiResponse, String id) {

        Observable<StorePageResponse> storeDetailResponse = retrofit.create(WebServiceApi.class).storePage(id);

        storeDetailResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), StorePageResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void getSearch(NetworkApiResponse networkApiResponse, String key) {

        Observable<SearchResponse> storeDetailResponse = retrofit.create(WebServiceApi.class).getSearch(key);

        storeDetailResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), SearchResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void getCategories(NetworkApiResponse networkApiResponse) {

        Observable<CategoryResponse> categoriesResponse = retrofit.create(WebServiceApi.class).getCategories();

        categoriesResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), CategoryResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        if (response.getError() != null) {
                            networkApiResponse.onFailure(response.getError().getMessage(), response.getError().getRequestCode(), response.getError().getStatusCode());
                        } else {
                            networkApiResponse.onSuccess(response);
                        }
                    }
                }, error -> {
                });

    }

    private String getExceptionResponse(Throwable throwable, NetworkApiResponse networkResponse, int requestCode) {
        String errorMessage = "";
        try {
            if (throwable instanceof HttpException) {
                ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                int statusCode = ((HttpException) throwable).response().code();
                if (responseBody != null) {
                    errorMessage = getErrorMessage(responseBody);
                    networkResponse.onFailure(errorMessage, requestCode, statusCode);

                }

            } else if (throwable instanceof SocketTimeoutException) {
                networkResponse.onTimeOut(requestCode);
            } else if (throwable instanceof IOException) {
                networkResponse.onNetworkError(requestCode);
            } else {
                final int unknownErrorCode = 1112;
                networkResponse.onUnknownError(requestCode, unknownErrorCode, throwable.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorMessage;
    }

    private String getSocialExceptionResponse(Throwable throwable, NetworkApiResponse networkResponse, int requestCode) {
        String errorMessage = "";
        try {
            if (throwable instanceof HttpException) {
                ResponseBody responseBody = ((HttpException) throwable).response().errorBody();
                int statusCode = ((HttpException) throwable).response().code();
                if (responseBody != null) {
                    errorMessage = getSocialErrorMessage(responseBody);
                    networkResponse.onFailure(errorMessage, requestCode, statusCode);

                }

            } else if (throwable instanceof SocketTimeoutException) {
                networkResponse.onTimeOut(requestCode);
            } else if (throwable instanceof IOException) {
                networkResponse.onNetworkError(requestCode);
            } else {
                final int unknownErrorCode = 1112;
                networkResponse.onUnknownError(requestCode, unknownErrorCode, throwable.getMessage());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return errorMessage;
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            JSONObject erroObj = jsonObject.getJSONObject("error");
            return erroObj.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    private String getSocialErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }
}
