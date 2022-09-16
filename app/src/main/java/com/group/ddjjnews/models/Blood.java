package com.group.ddjjnews.models;

import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;
@ParseClassName("Blood")
public class Blood extends ParseObject {
    public static final String CUSTOM_ENDPOINT_CREATE = "create-blood";
    public static final String CUSTOM_ENDPOINT_LIST = "list-blood";
    public static final String CUSTOM_ENDPOINT_GET = "get-blood";

    public static final String KEY_GROUP_BLOOD = "groupBlood";
    public static final String KEY_USER = "user";
    public static final String KEY_FOR_NAME = "forName";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_HOSPITAL = "hospital";

    HashMap params;

    public interface Callback {
        void done(ParseObject object, Exception e);
        void done(List<ParseObject> objects, Exception e);
    }

    public Blood() {}


    public static void create(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE, params, (object, e) -> callback.done((Blood) object, e));
    }

    public static void getAll(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST, params, (FunctionCallback<List<Object>>) (objects, e) -> callback.done((Blood) objects, e));
    }

    public static void getOne(String bloodId, Callback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bloodId", bloodId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_GET, params, (object, e) -> callback.done((Blood) object, e));
    }
}
