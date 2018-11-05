package com.enqos.atc.utils;

import android.os.Bundle;
import android.util.Log;

import com.enqos.atc.login.LoginView;
import com.enqos.atc.register.RegisterView;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;

import org.json.JSONException;

public class Utility {
    public static void getUserProfile(AccessToken currentAccessToken, Object listener) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, (object, response) -> {
                    Log.d("TAG", object.toString());
                    try {
                        String first_name = object.getString("first_name");
                        String last_name = object.getString("last_name");
                        String email = object.getString("email");
                        String id = object.getString("id");
                        if (listener instanceof RegisterView) {
                            RegisterView registerView = (RegisterView) listener;
                            registerView.fbLogin(email, id);
                        } else if (listener instanceof LoginView) {
                            LoginView loginView = (LoginView) listener;
                            loginView.fbLogin(email, id);
                        }

                        //String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                        Log.i("FB_LOGIN", email);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
