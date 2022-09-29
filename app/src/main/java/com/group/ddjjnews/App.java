package com.group.ddjjnews;

import android.app.Application;

import com.group.ddjjnews.models.Alert;
import com.group.ddjjnews.models.Blood;
import com.group.ddjjnews.models.Category;
import com.group.ddjjnews.models.Comment;
import com.group.ddjjnews.models.User;
import com.group.ddjjnews.models.News;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.facebook.ParseFacebookUtils;


import java.util.ArrayList;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(User.class);
        ParseObject.registerSubclass(News.class);
        ParseObject.registerSubclass(Category.class);
        ParseObject.registerSubclass(Blood.class);
        ParseObject.registerSubclass(Alert.class);
        ParseObject.registerSubclass(Comment.class);

        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                        .enableLocalDataStore()
                .build()
        );
        ParseFacebookUtils.initialize(this);

        ArrayList<String> channels = new ArrayList<>();
        channels.add("req_blood_channel"); // subscribe user to req_blood_channel
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", getString(R.string.firebase_sender_id));
        installation.put("channels", channels);
        installation.saveInBackground();
    }
}
