package com.lingxiaosuse.picture.tudimension.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by lingxiao on 2017/8/3.
 */

public class MyApplication extends Application{
    private  static Context mContext;
    private static Handler mHandler;
    private static int mainThreadId;
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
        mContext = getApplicationContext();
        mHandler = new Handler();
        mainThreadId = android.os.Process.myPid();
    }
    public static Context getContext(){
        return mContext;
    }
    public static Handler getHandler(){
        return mHandler;
    }
    public static int getMainThreadId(){
        return mainThreadId;
    }
}
