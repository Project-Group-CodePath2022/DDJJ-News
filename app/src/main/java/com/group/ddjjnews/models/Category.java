package com.group.ddjjnews.models;

import androidx.annotation.NonNull;

import com.parse.ParseClassName;
import com.parse.ParseCloud;

import com.parse.ParseObject;

import java.util.Collection;
import java.util.HashMap;

@ParseClassName("Category")
public class Category extends ParseObject {
    public static final String CUSTOM_ENDPOINT_LIST = "list-category";
    public static final String CUSTOM_ENDPOINT_CREATE = "create-category";

    public static final String KEY_NAME = "name";
    public static final String KEY_ID = "objectId";

    public interface Callback {
        void done(ParseObject object, Exception e);
        void done(Collection objects, Exception e);
    }

    public Category() {}

    public String getKeyName() {return getString(KEY_NAME);}

    public static void create(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE, params, (object, e) -> callback.done((ParseObject) object, e));
    }

    public static void getAll(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST, params, (objects, e) -> callback.done((Collection<? extends Category>) objects, e));
    }

    @NonNull
    @Override
    public String toString() {
        return getKeyName();
    }
}
