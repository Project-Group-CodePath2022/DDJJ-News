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
    public static final String KEY_OBJECT_ID = "objectId";
    public static final String KEY_TITLE = "title";
    public static final String KEY_CONTENT = "content";
    public static final String KEY_ACTIVE = "active";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_CATEGORY = "category";

    public static final String KEY_IMAGE = "image";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_COUNT_VIEW = "viewsCount";
    public static final String KEY_CREATED_AT = "createdAt";
    public static final String KEY_UPDATED_AT = "updatedAt";
    public static final String KEY_ALAUNE = "alaune";

    ParseObject object;

    public News() {}

    public String getKeyTitle() { return getString(KEY_TITLE); }
    public boolean getKeyActive() { return getBoolean(KEY_ACTIVE); }

    public ParseObject getKeyCategory() { return getParseObject(KEY_CATEGORY); }
    public String getKeyContent() { return getString(KEY_CONTENT); }
    public boolean isAlaune() { return getBoolean(KEY_ALAUNE); }
    public ParseFile getKeyImage(){ return getParseFile(KEY_IMAGE); }

    public static void getNews(HashMap params, FunctionCallback<Object> callback) {
        ParseCloud.callFunctionInBackground("list-news", params, callback);
    }

    public static void getNewsAdmin(HashMap params, FunctionCallback<Object> callback) {
        ParseCloud.callFunctionInBackground("list-news-admin", params, callback);
    }

    public static void getDetailNews(String newsId, FunctionCallback<Object> callback) {
        HashMap<String, String> params = new HashMap();
        params.put("newsId", newsId);
        ParseCloud.callFunctionInBackground("get-news", params, callback);
    }

}
