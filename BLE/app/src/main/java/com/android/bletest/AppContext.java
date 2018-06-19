package com.android.bletest;

import android.app.Application;

/**
 * Created by 601042 on 2017/5/25.
 */
public class AppContext extends Application {


    /**
     * Application单例
     */
    private static AppContext sInstance;
    /**
     * ble控制类
     */
    BleHelper mBle;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;

        //初始化ble的控制类
        mBle = new BleHelper();
        mBle.init(this);
    }

    /**
     * @return Application实例
     */
    public static AppContext getInstance() {
        return sInstance;
    }

    public BleHelper getmBle() {
        return mBle;
    }

    public void setmBle(BleHelper mBle) {
        this.mBle = mBle;
    }
}
