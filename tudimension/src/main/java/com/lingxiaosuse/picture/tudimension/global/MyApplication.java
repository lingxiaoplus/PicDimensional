package com.lingxiaosuse.picture.tudimension.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.lingxiaosuse.picture.tudimension.R;
import com.tencent.bugly.crashreport.CrashReport;


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
        //bugly初始化 建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.initCrashReport(getApplicationContext(), "fcc0256432", true);

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
