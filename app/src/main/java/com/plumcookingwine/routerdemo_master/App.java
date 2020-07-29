package com.plumcookingwine.routerdemo_master;

import android.app.Application;

import com.plumcookingwine.irouter.api.manager.RouterManager;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RouterManager.init(this);
    }
}
