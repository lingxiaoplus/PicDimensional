package com.lingxiaosuse.picture.tudimension;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.avos.avoscloud.AVOSCloud;
import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiao.skinlibrary.SkinLib;
import com.lingxiaosuse.picture.tudimension.service.InitIalizeService;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.bugly.CrashModule;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Map;


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
