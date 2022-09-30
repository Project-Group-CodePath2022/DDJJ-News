package com.group.ddjjnews.models;

import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.Collection;
import java.util.HashMap;

@ParseClassName("News")
public class News extends ParseObject {
    public static final String CUSTOM_ENDPOINT_CREATE = "news:create";
    public static final String CUSTOM_ENDPOINT_DELETE = "news:delete";
    public static final String CUSTOM_ENDPOINT_LIST = "news:list";
    public static final String CUSTOM_ENDPOINT_ADMIN_LIST = "news-admin:list";
    public static final String CUSTOM_ENDPOINT_GET = "news:get";

    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_ALAUNE = "alaune";

    public News() {}

    public String getKeyTitle() { return getString(KEY_TITLE); }
    public boolean getKeyActive() { return getBoolean(KEY_ACTIVE); }
    public ParseObject getKeyCategory() { return getParseObject(KEY_CATEGORY); }
    public String getKeyContent() { return getString(KEY_CONTENT); }
    public boolean isAlaune() { return getBoolean(KEY_ALAUNE); }
    public ParseFile getKeyImage(){ return getParseFile(KEY_IMAGE); }

    public static void getNews(HashMap params, FunctionCallback<Object> callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_LIST, params, callback);
    }

    public static void deleteNews(String newsId, FunctionCallback<Object> callback) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("newsId", newsId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_DELETE, params, callback);
    }

    public static void getNewsAdmin(HashMap params, FunctionCallback<Object> callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_ADMIN_LIST, params, callback);
    }

    public static void createNewsAdmin(HashMap params, FunctionCallback<Object> callback) {
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_CREATE, params, callback);
    }

    public static void getDetailNews(String newsId, FunctionCallback<Object> callback) {
        HashMap<String, String> params = new HashMap();
        params.put("newsId", newsId);
        ParseCloud.callFunctionInBackground(CUSTOM_ENDPOINT_GET, params, callback);
    }
}
