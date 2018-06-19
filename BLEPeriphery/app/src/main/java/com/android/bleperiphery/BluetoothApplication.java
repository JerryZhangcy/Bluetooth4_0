package com.android.bleperiphery;

import android.app.Application;
import android.content.Context;

public class BluetoothApplication extends Application {
    public static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();
    }
}
