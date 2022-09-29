package com.group.ddjjnews.models;
import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.HashMap;
import java.util.List;


@ParseClassName("Alert")
public class Alert extends ParseObject {
    private static final String CUSTOM_ENDPOINT = "alert-model";
    private static final String KEY_IMAGE = "image";

    public Alert() {}

    public ParseFile getKeyImage(){ return getParseFile(KEY_IMAGE); }

    public static void FindAll(HashMap<String, Object> params, FunctionCallback<Object> callback) {
        params.put("method", "LIST");
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT, params, callback);
    }

}

