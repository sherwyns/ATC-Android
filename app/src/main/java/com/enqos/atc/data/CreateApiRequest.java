package com.enqos.atc.data;

import com.enqos.atc.data.request.ChangePasswordRequest;
import com.enqos.atc.data.request.GetFavouriteRequest;
import com.enqos.atc.data.request.LoginRequest;
import com.enqos.atc.data.request.RegisterRequest;
import com.enqos.atc.data.request.ResetPasswordRequest;
import com.enqos.atc.data.request.SaveFavoriteRequest;
import com.enqos.atc.data.request.UpdateFavoriteRequest;
import com.enqos.atc.data.response.ProductFavoriteEntity;
import com.enqos.atc.data.response.NetworkApiResponse;
import com.enqos.atc.data.response.StoreFavoriteEntity;

import java.util.List;

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

    public void createResetPasswordRequest(String email, String url) {
        ResetPasswordRequest resetPasswordRequest = new ResetPasswordRequest(email, url);
        dataRepository.resetPassword(networkApiResponse, resetPasswordRequest);
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

    public void createSaveFavoriteRequest(String userId, String id, String type, String isFavourite) {
        SaveFavoriteRequest saveFavoriteRequest = new SaveFavoriteRequest(userId, id, type, isFavourite);
        dataRepository.saveFavorite(networkApiResponse, saveFavoriteRequest);
    }

    public void createUpdateFavoriteRequest(String userId, List<StoreFavoriteEntity> store, List<ProductFavoriteEntity> product) {
        UpdateFavoriteRequest saveFavoriteRequest = new UpdateFavoriteRequest(store, product);
        dataRepository.updateFavorite(networkApiResponse, userId, saveFavoriteRequest);
    }

    public void createGetStoreFavorites(String userId, String type) {
        GetFavouriteRequest favouriteRequest = new GetFavouriteRequest(userId, type);
        dataRepository.getStoreFavorites(networkApiResponse, favouriteRequest);
    }

    public void createGetProductFavorites(String userId, String type) {
        GetFavouriteRequest favouriteRequest = new GetFavouriteRequest(userId, type);
        dataRepository.getProductFavorites(networkApiResponse, favouriteRequest);
    }

    public void createStoreDetailRequest(String id) {
        dataRepository.getStoreDetail(networkApiResponse, id);
    }

    public void createStorePageRequest(String id) {
        dataRepository.getStorePage(networkApiResponse, id);
    }

    public void createCategoriesRequest() {
        dataRepository.getCategories(networkApiResponse);
    }

    public void createSearchRequest(String key) {
        dataRepository.getSearch(networkApiResponse, key);
    }

    public void createChangePasswordRequest(String accessToken, String oldPassword, String newPassword) {
        ChangePasswordRequest changePasswordRequest = new ChangePasswordRequest(oldPassword, newPassword);
        dataRepository.changePassword(networkApiResponse, accessToken, changePasswordRequest);
    }
}
