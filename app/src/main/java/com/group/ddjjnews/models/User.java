package com.group.ddjjnews.models;

import android.util.Log;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;

import com.parse.ParseDecoder;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseRole;
import com.parse.ParseUser;

import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
public class User extends ParseUser {
    private static final String CUSTOM_ENDPOINT_LOG_IN = "log-in";
    private static final String CUSTOM_ENDPOINT_SIGN_UP = "sign-up";
    private static final String CUSTOM_ENDPOINT_DETAIL_OAUTH = "sign-up-facebook";
    public static final String CUSTOM_ENDPOINT_ROLE_LIST = "roles:list";
    private static final String CUSTOM_ENDPOINT_CREATE = "users:create";
    private static final String CUSTOM_ENDPOINT_DELETE = "users:delete";
    private static final String CUSTOM_ENDPOINT_LIST = "users:list";
    private static final String CUSTOM_ENDPOINT_UPDATE = "users:update";

    private static final String KEY_ACTIVE = "active";
    public static final String KEY_IS_ADMIN = "isadmin";

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
    public boolean getKeyActive() { return getBoolean(KEY_ACTIVE); }

    public static void create(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE, params, (object, e) -> callback.done((User) object, e));
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

    public static void deleteUser(String userId, FunctionCallback<Object> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_DELETE, params, callback);
    }

    public static void getDetailOAuth(String userId, String email, AuthCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("userId", userId);

        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_DETAIL_OAUTH, params, (user, e) -> {
            callback.done(null, e);
        });
    }

    public static ParseObject fromHash(HashMap object) {
        return ParseObject.fromJSON(new JSONObject(object), "_User", ParseDecoder.get());
    }

    public static void getAllUser(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST, params, (objects, e) -> callback.done((Collection<? extends User>) objects, e));
    }

    public static void getUserRoles(FunctionCallback<List<ParseRole>> callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_ROLE_LIST, new HashMap<>(), callback);
    }

    public static void createAdmin(String email, String password, String role, String userId, AuthCallback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("roleName", role);
        if (userId != null)
            params.put("userId", userId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE, params, (object, e) -> {
            if (e == null) {
                Log.d("OK", object.toString());
                callback.done((User) User.fromHash((HashMap) object), e);
            } else {
                callback.done(null, e);
            }
        });
    }

    public static void updateUser(boolean active, String email, String password, String role, String userId, AuthCallback callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("email", email);
        if (password != null)
            params.put("password", password);
        params.put("roleName", role);
        if (active)
            params.put("active", "true");
        else
            params.put("active", "false");
        params.put("userId", userId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_UPDATE, params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if (e == null) {
                    callback.done((User) User.fromHash((HashMap) object), e);
                } else {
                    callback.done(null, e);
                }
            }
        });
    }
}
