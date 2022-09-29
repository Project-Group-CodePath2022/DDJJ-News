package com.group.ddjjnews.models;


import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

@ParseClassName("Comment")
public class Comment extends ParseObject {
    public static final String KEY_TEXT = "text";
    public static final String KEY_USER = "user";
    public static final String CUSTOM_ENDPOINT_LIST = "list-comment-for";
    public static final String CUSTOM_ENDPOINT_CREATE = "create-comment-for";

    public Comment() {}
    public String getKeyText() { return getString(KEY_TEXT); }
    public ParseUser getKeyUser() {return getParseUser(KEY_USER); }

    public static void getAllFor(String newsId, FunctionCallback<List<Object>> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("newsId", newsId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST, params, callback);
    }

    public static void createFor(String newsId, String text, FunctionCallback<Object> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("text", text);
        params.put("newsId", newsId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE, params, callback);
    }
}