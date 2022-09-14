package com.group.ddjjnews.models;

import android.util.Log;

import com.parse.FunctionCallback;
import com.parse.LogInCallback;
import com.parse.ParseCloud;

import com.parse.ParseUser;

import java.util.HashMap;

public class MyParseUser extends ParseUser {
    private static final String CUSTOM_LOG_IN = "log-in";
    private static final String CUSTOM_SIGN_UP = "sign-up";

    public MyParseUser() {}

    public boolean isAdmin() {
        return false;
    }

    public String getRoleType() {
        return "standard";
    }


    public static void customSignUpInBackground(String email, String password, FunctionCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        ParseCloud.callFunctionInBackground(CUSTOM_SIGN_UP, params, callback);
    }

    public static void customLogInInBackground(String email, String password, LogInCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        ParseCloud.callFunctionInBackground(CUSTOM_LOG_IN, params, (user, e) -> {
            if (e == null) {
                becomeInBackground(((ParseUser)user).getSessionToken(), callback);
            } else {
                Log.d("MParseUser", e.toString(), e);
            }
        });
    }
}
