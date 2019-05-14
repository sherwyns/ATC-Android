package com.enqos.atc.data;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.enqos.atc.base.AtcApplication;
import com.enqos.atc.base.BasePresenter;
import com.enqos.atc.data.remote.WebServiceApi;
import com.enqos.atc.data.request.ChangePasswordRequest;
import com.enqos.atc.data.request.GetFavouriteRequest;
import com.enqos.atc.data.request.LoginRequest;
import com.enqos.atc.data.request.ProductAnalyticsRequest;
import com.enqos.atc.data.request.RegisterRequest;
import com.enqos.atc.data.request.ResetPasswordRequest;
import com.enqos.atc.data.request.SaveFavoriteRequest;
import com.enqos.atc.data.request.StoreRequest;
import com.enqos.atc.data.request.UpdateFavoriteRequest;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.FavoriteResponse;
import com.enqos.atc.data.response.LoginResponse;
import com.enqos.atc.data.response.NeighbourhoodResponse;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.NewProductFavResponse;
import com.enqos.atc.data.response.ProductAnalyticsResponse;
import com.enqos.atc.data.response.RegisterResponse;
import com.enqos.atc.data.response.ResetPasswordResponse;
import com.enqos.atc.data.response.SaveFavouriteResponse;
import com.enqos.atc.data.response.SearchResponse;
import com.enqos.atc.data.response.StoreDetailResponse;
import com.enqos.atc.data.response.StoreFavoriteResponse;
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
    void resetPassword(NetworkApiResponse networkApiResponse, ResetPasswordRequest resetPasswordRequest) {

        Observable<ResetPasswordResponse> responseObservable = retrofit.create(WebServiceApi.class).resetPassword(resetPasswordRequest);

        responseObservable.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, resetPasswordRequest.getRequestCode()), ResetPasswordResponse.class))
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
    void changePassword(NetworkApiResponse networkApiResponse, String accessToken, ChangePasswordRequest changePasswordRequest) {

        Observable<Void> loginResponse = retrofit.create(WebServiceApi.class).changePassword(accessToken, changePasswordRequest);

        loginResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, changePasswordRequest.getRequestCode()), Void.class))
                .subscribe(response -> {
                    if (response != null) {
                        networkApiResponse.onSuccess(null);

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
    void getStore(NetworkApiResponse networkApiResponse, String neighbour, String category, double latitude, double longitude) {

        Observable<StoreResponse> storeResponse = retrofit.create(WebServiceApi.class).store(neighbour, category, latitude, longitude);

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
    void getStore(NetworkApiResponse networkApiResponse, String neighbour, String category) {

        Observable<StoreResponse> storeResponse = retrofit.create(WebServiceApi.class).store(neighbour, category);

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

        Observable<SaveFavouriteResponse> storeResponse = retrofit.create(WebServiceApi.class).saveFavorite(favoriteRequest);

        storeResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, favoriteRequest.getRequestCode()), SaveFavouriteResponse.class))
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
    void getStoreFavorites(NetworkApiResponse networkApiResponse, GetFavouriteRequest getFavouriteRequest) {

        Observable<StoreFavoriteResponse> storeResponse = retrofit.create(WebServiceApi.class).favorites(getFavouriteRequest);

        storeResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), StoreFavoriteResponse.class))
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
    void getProductFavorites(NetworkApiResponse networkApiResponse, GetFavouriteRequest getFavouriteRequest) {

        Observable<NewProductFavResponse> storeResponse = retrofit.create(WebServiceApi.class).productFavorites(getFavouriteRequest);

        storeResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), NewProductFavResponse.class))
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
    void getProductsPagination(NetworkApiResponse networkApiResponse, String neighborhood, String category, int limit, int offSet) {

        Observable<StorePageResponse> storeDetailResponse = retrofit.create(WebServiceApi.class).productsPagination(neighborhood, category, limit, offSet);

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

    @SuppressLint("CheckResult")
    void productAnalytics(NetworkApiResponse networkApiResponse, String accessToken, ProductAnalyticsRequest productAnalyticsRequest) {

        Observable<ProductAnalyticsResponse> loginResponse = retrofit.create(WebServiceApi.class).productAnalytics(accessToken, productAnalyticsRequest);

        loginResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, productAnalyticsRequest.getRequestCode()), ProductAnalyticsResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        networkApiResponse.onSuccess(null);

                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void getProductsCategories(NetworkApiResponse networkApiResponse, String id) {

        Observable<CategoryResponse> categoriesResponse = retrofit.create(WebServiceApi.class).getProductCategories(id);

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

    @SuppressLint("CheckResult")
    void getNeighbourhoods(NetworkApiResponse networkApiResponse) {

        Observable<NeighbourhoodResponse> categoriesResponse = retrofit.create(WebServiceApi.class).getNeighourhoods();

        categoriesResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, 12), NeighbourhoodResponse.class))
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
    void storeAnalytics(NetworkApiResponse networkApiResponse, String accessToken, ProductAnalyticsRequest productAnalyticsRequest) {

        Observable<ProductAnalyticsResponse> loginResponse = retrofit.create(WebServiceApi.class).storeAnalytics(accessToken, productAnalyticsRequest);

        loginResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, productAnalyticsRequest.getRequestCode()), ProductAnalyticsResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        networkApiResponse.onSuccess(null);

                    }

                }, error -> {
                });

    }

    @SuppressLint("CheckResult")
    void categoryAnalytics(NetworkApiResponse networkApiResponse, String accessToken, ProductAnalyticsRequest productAnalyticsRequest) {

        Observable<ProductAnalyticsResponse> loginResponse = retrofit.create(WebServiceApi.class).categoryAnalytics(accessToken, productAnalyticsRequest);

        loginResponse.subscribeOn(newThread)
                .observeOn(mainThread)
                .onErrorReturn(throwable -> new Gson().fromJson(getExceptionResponse(throwable, networkApiResponse, productAnalyticsRequest.getRequestCode()), ProductAnalyticsResponse.class))
                .subscribe(response -> {
                    if (response != null) {
                        networkApiResponse.onSuccess(null);

                    }

                }, error -> {
                });

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
