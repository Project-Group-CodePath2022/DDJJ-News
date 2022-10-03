package com.group.ddjjnews.models;

import androidx.annotation.NonNull;

import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;

import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@ParseClassName("Category")
public class Category extends ParseObject {
    public static final String CUSTOM_ENDPOINT_LIST = "list-category";
    public static final String CUSTOM_ENDPOINT_CREATE = "create-category";

    public static final String KEY_NAME = "name";

    public Category() {}

    public String getKeyName() {return getString(KEY_NAME);}

    public static void create(HashMap<String, Object> params, FunctionCallback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE, params, (object, e) -> callback.done(object, e));
    }

    public static void getAll(HashMap<String, Object> params, FunctionCallback<Object> callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST, params, callback);
    }

    @NonNull
    @Override
    public String toString() {
        return getKeyName();
    }
}
