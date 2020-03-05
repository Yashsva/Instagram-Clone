package com.example.instagramclone;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("Hhm777TKWTeYvMJTncXaltKpre1PtcEMtqIGQs2J")
                // if defined
                .clientKey("Ib5HtT9AMwGvQHc5KlvxdgHyLRARkG6j6sGjje7H")
                .server("https://parseapi.back4app.com/")
                .build()
        );


    }
}
