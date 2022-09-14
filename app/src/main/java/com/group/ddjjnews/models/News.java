package com.group.ddjjnews.models;

import com.parse.FunctionCallback;
import com.parse.ParseClassName;
import com.parse.ParseCloud;
import com.parse.ParseObject;

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

    public static News from(ParseObject object) {
        News n = new News();
        n.object = object;
        return n;
    }

    public String getKeyTitle() { return this.object.getString(KEY_TITLE); }
    public String getKeyObjectId() {return this.object.getString(KEY_OBJECT_ID); }
    public String getKeyDescription() { return this.object.getString(KEY_DESCRIPTION); }
    public String getKeyContent() { return this.object.getString(KEY_CONTENT); }
    public boolean isAlaune() { return this.object.getBoolean(KEY_ALAUNE); }

    public static void getNews(HashMap params, FunctionCallback<Object> callback) {
        ParseCloud.callFunctionInBackground("list-news", params, callback);
    }

    public static void getDetailNews(String newsId, FunctionCallback<Object> callback) {
        HashMap<String, String> params = new HashMap();
        params.put("newsId", newsId);
        ParseCloud.callFunctionInBackground("get-news", params, callback);
    }
}