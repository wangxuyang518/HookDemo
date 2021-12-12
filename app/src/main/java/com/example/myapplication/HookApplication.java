package com.example.myapplication;

import android.app.Application;
import android.content.Context;

public class HookApplication extends Application {

    public static HookApplication mHookApplication;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        mHookApplication=this;
        HookManager.getInstance().hookActivityThread();
    }
}
