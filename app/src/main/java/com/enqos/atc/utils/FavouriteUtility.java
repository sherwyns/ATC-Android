package com.enqos.atc.utils;

import com.enqos.atc.data.CreateApiRequest;
import com.enqos.atc.data.response.BaseResponse;
import com.enqos.atc.data.response.NetworkApiResponse;

public class FavouriteUtility {

    public static void saveFavourite(String userId, String id,String type,String isFavourite) {
        CreateApiRequest createApiRequest = new CreateApiRequest(networkApiResponse);
        createApiRequest.createSaveFavoriteRequest(userId, id,type,isFavourite);
    }


    private static NetworkApiResponse networkApiResponse = new NetworkApiResponse() {
        @Override
        public void onSuccess(BaseResponse response) {

        }

        @Override
        public void onFailure(String errorMessage, int requestCode, int statusCode) {

        }

        @Override
        public void onTimeOut(int requestCode) {

        }

        @Override
        public void onNetworkError(int requestCode) {

        }

        @Override
        public void onUnknownError(int requestCode, int statusCode, String errorMessage) {

        }
    };

}
