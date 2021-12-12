package com.example.myapplication;

import static com.example.myapplication.HookApplication.mHookApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.annotation.NonNull;

import java.io.File;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.FileHandler;

public class HookManager {

    private static HashMap<String, View> targetView = new HashMap<>();
    private static HookManager mHookManager;
    private String name="wxy";


    private HookManager() {

    }
    private volatile Lock objectLock = new ReentrantLock();

    public static HookManager getInstance() {
        if (mHookManager == null) {
            synchronized (HookManager.class) {
                if (mHookManager == null) {
                    mHookManager = new HookManager();
                }
            }
        }
        return mHookManager;
    }


    public void hookActivityThread() {
        android.os.Handler.Callback mCallBack = new android.os.Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                int what = msg.what;
                if (what == 100) {
                    Object obj = msg.obj;
                    Field field = null;
                    try {
                        field = obj.getClass().getDeclaredField("intent");
                        field.setAccessible(true);
                        Intent intent = (Intent) field.get(obj);
                        String targetName = intent.getComponent().getClassName();
                        if (targetName.contains("MainActivity2")) {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        objectLock.lock();
                                        View v = LayoutInflater.from(mHookApplication).inflate(R.layout.activity2_main, null);
                                        targetView.put(targetName, v);
                                        Log.d("wangxuyang", "create View");
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        objectLock.unlock();
                                    }
                                }
                            }).start();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("wangxuyang", "111:  " + e.getMessage());
                    }
                }
                return false;
            }
        };
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Field sCurrentActivityThreadFile = activityThreadClass.getDeclaredField("sCurrentActivityThread");
            sCurrentActivityThreadFile.setAccessible(true);
            Object activityThread = sCurrentActivityThreadFile.get(null);

            Field mField = activityThreadClass.getDeclaredField("mH");
            mField.setAccessible(true);
            Handler mHObject = (Handler) mField.get(activityThread);
            Field mCallBackField = Handler.class.getDeclaredField("mCallback");
            mCallBackField.setAccessible(true);
            mCallBackField.set(mHObject, mCallBack);
            Log.d("wangxuyang", "success: ");
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("wangxuyang", "e:   " + e.getLocalizedMessage());
        }
    }

    public View getActivityView(String s) {
        View view = null;
        try {
            objectLock.lock();
            view = targetView.get(s);
            if (view.getParent()!=null){
                ViewGroup mViewParent= (ViewGroup) view.getParent();
                mViewParent.removeAllViews();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            objectLock.lock();
            return view;
        }
    }


    public String getString(){
        return name;
    }
}
