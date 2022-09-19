package com.group.ddjjnews.models;

import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Collection;
import java.util.Date;
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
    public static final String KEY_CREATED_AT = "createdAt";

    HashMap params;

    public interface Callback {
        void done(Blood object, Exception e);
        void done(Collection objects, Exception e);
    }

    public Blood() {}

    public String getText() {
        String w = String.format("Request group ( %s ) blood, for %s ", getString(KEY_GROUP_BLOOD), getString(KEY_FOR_NAME));
        return w;
    }

    public String getKeyDescription() {
        return getString(KEY_DESCRIPTION);
    }

//    public Date getKeyCreatedAT() {
//        return getDate(KEY_CREATED_AT);
//    }


    public static void create(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE, params, (object, e) -> callback.done((Blood) object, e));
    }

    public static void getAll(HashMap<String, Object> params, Callback callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST,
                params,
                new FunctionCallback<Object>() {
                    @Override
                    public void done(Object object, ParseException e) {
                        callback.done((Collection<? extends Blood>) object, e);
                    }
                });
    }

    public static void getOne(String bloodId, Callback callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("bloodId", bloodId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_GET, params, (object, e) -> callback.done((Collection<? extends Blood>)object, e));
    }
}
