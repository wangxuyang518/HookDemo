package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity

class MainActivity2 : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var activityView = HookManager.getInstance().getActivityView(this.javaClass.name)
        setContentView(activityView)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            var startTime = intent.getLongExtra("startTime", 0)
            Log.d("wangxuyang",""+(System.currentTimeMillis()-startTime))
            //
            var obj = HookManager.getInstance();
            var fildName=obj.javaClass.getDeclaredField("name")
            fildName.isAccessible=true
            var name2=fildName.get(obj)
            Log.d("wangxuyang","name2:  "+name2)
            fildName.set(obj,"MainActivity2")
            Log.d("wangxuyang","HookManager.getInstance().getString(): "+HookManager.getInstance().getString()+"   \n")
        }
    }
}