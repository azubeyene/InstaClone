package com.example.instaclone;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(Post.class);
        ParseObject.registerSubclass(Comment.class);
        ParseObject.registerSubclass(Like.class);
        ParseObject.registerSubclass(ParseUser.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("azu-parse") // should correspond to APP_ID env variable
                .clientKey("thisIsAMasterKey34509082")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://azu-parse.herokuapp.com/parse").build());
    }
}
