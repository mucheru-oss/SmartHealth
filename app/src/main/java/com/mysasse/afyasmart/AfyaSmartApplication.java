package com.mysasse.afyasmart;

import android.app.Application;

public class AfyaSmartApplication extends Application {

    private static AfyaSmartApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static AfyaSmartApplication getInstance() {
        return instance;
    }
}
