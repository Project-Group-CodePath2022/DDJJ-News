package com.group.ddjjnews;

import android.app.Application;

import com.group.ddjjnews.models.MyParseUser;
import com.group.ddjjnews.models.News;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;


import java.util.ArrayList;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(MyParseUser.class);
        ParseObject.registerSubclass(News.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id))
                .clientKey(getString(R.string.back4app_client_key))
                .server(getString(R.string.back4app_server_url))
                .build()
        );

        ArrayList<String> channels = new ArrayList<>();
        channels.add("req_blood_channel"); // subscribe user to req_blood_channel
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put("GCMSenderId", getString(R.string.firebase_sender_id));
        installation.put("channels", channels);
        installation.saveInBackground();
    }
}
