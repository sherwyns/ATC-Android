package com.enqos.atc.data.remote;

import com.enqos.atc.data.request.ChangePasswordRequest;
import com.enqos.atc.data.request.GetFavouriteRequest;
import com.enqos.atc.data.request.LoginRequest;
import com.enqos.atc.data.request.RegisterRequest;
import com.enqos.atc.data.request.SaveFavoriteRequest;
import com.enqos.atc.data.request.UpdateFavoriteRequest;
import com.enqos.atc.data.response.CategoryResponse;
import com.enqos.atc.data.response.FavoriteResponse;
import com.enqos.atc.data.response.LoginResponse;
import com.enqos.atc.data.response.RegisterResponse;
import com.enqos.atc.data.response.SaveFavouriteResponse;
import com.enqos.atc.data.response.SearchDataResponse;
import com.enqos.atc.data.response.SearchResponse;
import com.enqos.atc.data.response.StoreDetailResponse;
import com.enqos.atc.data.response.StoreFavoriteResponse;
import com.enqos.atc.data.response.StorePageResponse;
import com.enqos.atc.data.response.StoreResponse;
import com.enqos.atc.data.response.UpdateFavoriteResponse;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface WebServiceApi {

    @POST("api/Users")
    Observable<RegisterResponse> register(@Body RegisterRequest registerRequest);

    @POST("api/Users/login")
    Observable<LoginResponse> authenticate(@Body LoginRequest loginRequest);

    @POST("api/socialUsers/signup")
    Observable<RegisterResponse> socialNetworkSignUp(@Body RegisterRequest registerRequest);

    @POST("api/socialUsers/signin")
    Observable<LoginResponse> socialNetworkSignIn(@Body LoginRequest loginRequest);

    @POST("api/Users/change-password?")
    Observable<Void> changePassword(@Query("access_token") String accessToken, @Body ChangePasswordRequest changePasswordRequest);

    @GET("api/Store/getstores")
    Observable<StoreResponse> store();

    @POST("api/favorite/list")
    Observable<StoreFavoriteResponse> favorites(@Body GetFavouriteRequest favouriteRequest);

    @POST("api/favorite/save")
    Observable<SaveFavouriteResponse> saveFavorite(@Body SaveFavoriteRequest saveFavorite);

    @POST("api/favorites/update?")
    Observable<UpdateFavoriteResponse> updateFavorite(@Query("where[user_id]") String id, @Body UpdateFavoriteRequest saveFavorite);

    @GET("api/store/getstore/{store_id}")
    Observable<StoreDetailResponse> storeDetail(@Path("store_id") String storeId);

    @GET("api/products/getproductbystore/{store_id}")
    Observable<StorePageResponse> storePage(@Path("store_id") String storeId);

    @GET("api/categories/list")
    Observable<CategoryResponse> getCategories();

    @GET("api/search/by/{search_key}")
    Observable<SearchResponse> getSearch(@Path("search_key") String key);
}
