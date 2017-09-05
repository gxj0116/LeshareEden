package com.leshare.base;

import android.app.Application;

/**
 * 作者：gxj on 2017/9/5 08:18
 * 邮箱：jun18735177413@sina.com
 */
public class SuperApplication extends Application {
    private static Application mInstance;

    public static Application getInstance() {
        if (mInstance == null) {
            mInstance = new SuperApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {

    }
}
