package com.group.ddjjnews.models;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;

import com.parse.ParseException;
import com.parse.ParseRole;
import com.parse.ParseUser;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class User extends ParseUser {
    public static final String KEY_IS_ADMIN = "isadmin";
    private static final String CUSTOM_ENDPOINT_LOG_IN = "log-in";
    private static final String CUSTOM_ENDPOINT_SIGN_UP = "sign-up";

    private static final String CUSTOM_ENDPOINT_CREATE_USER = "create-user";
    private static final String CUSTOM_ENDPOINT_LIST_USER = "list-user";
    private static final String CUSTOM_ENDPOINT_LIST_ADMIN = "list-user";
    private static final String CUSTOM_ENDPOINT_ROLE = "list-user-role";
    private static final String CUSTOM_ENDPOINT_CHANGE_ROLE = "list-user-role";
    private static final String CUSTOM_ENDPOINT_CREATE_ADMIN = "create-admin";



    private static final String KEY_ACTIVE = "active";


    public interface Callback {
        void done(User object, Exception e);
        void done(Collection object, Exception e);
    }

    public interface AuthCallback{
        void done(User object, Exception e);
    }

    public User() {}

    public boolean isAdmin() {
        return getBoolean(KEY_IS_ADMIN);
    }

    public String getRoleType() {
        return "standard";
    }
    public boolean getKeyActive() { return getBoolean(KEY_ACTIVE); }


    public static void getAll(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST_USER, params, (objects, e) -> {
           callback.done((Collection) objects, e);
        });
    }

    public static void create(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE_USER, params, (object, e) -> callback.done((User) object, e));
    }


    public static void customSignUpInBackground(String email, String password, AuthCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_SIGN_UP, params, (user, e) -> {
            if (e == null) {
                becomeInBackground(((ParseUser) user).getSessionToken(), (user1, e1) -> callback.done((User) user1, e1));
            } else {
                callback.done(null, e);
            }
        });
    }

    public static void customLogInInBackground(String email, String password, AuthCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LOG_IN, params, (user, e) -> {
            if (e == null) {
                becomeInBackground(((ParseUser) user).getSessionToken(), (user1, e1) -> callback.done((User) user1, e1));
            } else {
                callback.done(null, e);
            }
        });
    }
    public static void getAllUser(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST_ADMIN, params, (objects, e) -> callback.done((Collection<? extends User>) objects, e));
    }


    public static void getUserRoles(FunctionCallback<List<ParseRole>> callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_ROLE, new HashMap<>(), callback);
    }

    public static void createAdmin(String email, String password, String role, AuthCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("roleName", role);

        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE_ADMIN, params, (object, e) -> callback.done((User) object, e));
    }
}
