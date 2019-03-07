package com.lingxiaosuse.picture.tudimension;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiaosuse.picture.tudimension.service.InitIalizeService;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
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
        try {
            initHotFix();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
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

    private void initHotFix() throws PackageManager.NameNotFoundException {
        PackageManager manager = getPackageManager();
        PackageInfo info = manager.getPackageInfo(getPackageName(),0);
        String versionName = info.versionName;
        // initialize必须放在attachBaseContext最前面，
        // 初始化代码直接写在Application类里面，切勿封装到其他类。
        SophixManager.getInstance().setContext(this)
                .setAppVersion(versionName)
                .setAesKey(null)
                .setSecretMetaData("24790248","b1de0644fd25a27fdb5284fd19eea96f","MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCObaQANF/CT4GWYFlfTEUgGgzTp6iYUJTT1FCcIiSiQ0WjMv6sPCGHmH9uxHvEGffxQkCqfbVHtVqjUkzalD5/B0HsqeYxGnhe8hI52N4CyaR/JGsbBj0ijASpb4BAsDFS85azRKSgI/tfe1+815CGyef+E4/HSHmRc6B4sZRtnyPx3n8ZVJP6ic9s+qyGHvARsvywxmnEtsjSS7/RFmDrusnFgUBgdHH4CZi/E1wQYg0JE/DvX0mc2OmyQTSRq91uGwwdOOwFh+B5N7hosr9OL6Vl3uq2gzcmj4jBLnzQV1Uktk3jRVygxzBFpuRkKGCJ6350voqFppqPuxo7yqnBAgMBAAECggEAed6M/vPRoEVBn/dvYaC0YUSJBy4lj7cbsamxE/gPGpyvYHaI/b0x/4FYQOJ0+JYz1scW6AtKa0JdfPQ1+K3yT+VY3nV+FzQMHUnKmJ7dUMtTKstxrh0HgKEIeCaFOlTIz67Imzo4FyLSu5+oisB01TC8hmYcl6uHAhNLExr341eF70O1+8/0DpEgiXuoCWZdheS/ly8XLh5QEVmwldDKwOJPAYOv+dfwRghQxxsnz78FZ6uvcPicgyCYhN2lYzrKDpLkOVHV1uQaG2jMjLJfZZgaQns6aaQXdCgdnFbnZ0dwrWbYcbXMagUOjwRUdRykjfSoNPzEhUF4uRRePG4hxQKBgQDigecHU8iP6HX1KDIfib2Kn1LKPT7b0yGzECtllxmjbYnkNCfnJyvKyHvgOsGAfQmaSuvh8RHOgVjRJl5PenUDAyICaf1NLOZrBGPaotD4NwEb598KH64isLB7bQXVEQ18CiPJnH8HIkoLBobeDy0xDn1RujbZ/GfLqlit+JdTawKBgQCg+SfZI69qKKIUVaoZSn0FNlMURXkgfkOu1FHSGubD+hfvXF8wjz3ORKV/N71ZYekFdFjBKLses2jsvwrqmfRwJOAJFTbnakS3v8iDURnljKU6JILdnQG2ha2Fb5DNHeEOIiE83HuzycLwgB/NrlRzDFh3eI35NpWM/IZZZUlugwKBgFwUVujhpHzsEVfSOV4czpLV5gAVGcKfd+mlPx8TZ/bKBu5gTYBoqiLLkaA8AoHLUuYIYw/8wjJi3spXgKtqqrrIwUCd+b0v7ZO/uyAcZDsjkS3Y+xDQhCfSEEx8q4xMXGD6dkX1eqZZyCPnSVhobwSsUUGyI65GL6PZ4icHEe11AoGAfI1stMm0z8pS5rMF433MUH0hOV0sBOVNz3e/O3fPOLJJVM6/ZJ5g7zoUZ+QkIYXtvGeyyPqQFogxVoXXl4C6Lw+JPd3pVpPWSesd5eWZyRDTQ7K+g2UZzzWN9uJFQ0CsQ7hKyYG8QXeK23X3pbkO5NoXlZF1IPCINMer2H3TnKsCgYAQUXRTwp7ZS8ukiTQU+Ce52Ryaiqy1IigXo9WNv9XrjS+QtNECI1FaKMYDssjSb48HqZwWk/5j69FK6byf6soiEmvNIwO3kAXLmB8hwtexjGgmtgjXd0bEGGAirZ159WM9QR3wKb3QyOOWo15qiCAdV93t6YewSvwtT2Eo8aAmog==")
                .setEnableDebug(true)
                .setPatchLoadStatusStub(new PatchLoadStatusListener() {
                    @Override
                    public void onLoad(final int mode, final int code, final String info, final int handlePatchVersion) {
                        // 补丁加载回调通知
                        if (code == PatchStatus.CODE_LOAD_SUCCESS) {
                            // 表明补丁加载成功
                            LogUtils.e("补丁加载成功");
                        } else if (code == PatchStatus.CODE_LOAD_RELAUNCH) {
                            // 表明新补丁生效需要重启. 开发者可提示用户或者强制重启;
                            // 建议: 用户可以监听进入后台事件, 然后调用killProcessSafely自杀，以此加快应用补丁，详见1.3.2.3
                            ToastUtils.show("请重启app完成更新");
                        } else {
                            // 其它错误信息, 查看PatchStatus类说明
                            LogUtils.e("hotfix错误："+code);
                        }
                    }
                }).initialize();
    }
}
