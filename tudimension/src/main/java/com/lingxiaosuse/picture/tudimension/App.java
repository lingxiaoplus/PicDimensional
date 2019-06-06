package com.lingxiaosuse.picture.tudimension;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.lingxiaosuse.picture.tudimension.service.InitIalizeService;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;

/**
 * Created by lingxiao on 2017/8/3.
 */

public class App extends Application{
    private  static Context mContext;
    private static Handler mHandler;
    private static int mainThreadId;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FrescoHelper.initFresco(this);
        //Fresco.initialize(this);
        mContext = getApplicationContext();
        mHandler = new Handler();
        mainThreadId = android.os.Process.myPid();
        //LeakCanary.install(this);
        InitIalizeService.startInit(this);

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
