package com.lingxiaosuse.picture.tudimension.service;

import android.app.Application;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.job.JobService;
import android.content.Intent;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.avos.avoscloud.AVOSCloud;
import com.camera.lingxiao.common.utills.LogUtils;
import com.lingxiao.skinlibrary.SkinLib;
import com.lingxiaosuse.picture.tudimension.App;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.CrashActivity;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.taobao.sophix.PatchStatus;
import com.taobao.sophix.SophixManager;
import com.taobao.sophix.listener.PatchLoadStatusListener;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * 初始化操作都放进这个service
 */
public class InitIalizeService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.lingxiaosuse.picture.tudimension.service.action.FOO";


    private static Context mContext;
    public InitIalizeService() {
        super("InitIalizeService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //startForeground(1,new Notification());
            startMyOwnForeground();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";
        String channelName = "My Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    public static void startInit(Context context) {
        Intent intent = new Intent(context, InitIalizeService.class);
        intent.setAction(ACTION_FOO);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }

        //context.startService(intent);
        mContext = context;
    }
    
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                performInit();
            }
        }
    }

    /**
     * 初始化操作
     */
    private void performInit() {
        //初始化dbflow
        FlowManager.init(this);
        //bugly初始化 建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback(){
            @Override
            public synchronized Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
                Intent intent  =new Intent(getApplicationContext(), CrashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("msg",errorStack);
                startActivity(intent);
                return super.onCrashHandleStart(crashType, errorType, errorMessage, errorStack);
            }
        });
        //CrashReport.initCrashReport(getApplicationContext(), "fcc0256432", true,strategy);
        Bugly.init(getApplicationContext(), "fcc0256432",true,strategy);
        SkinLib.init((App) mContext);
        initHotFix();
        stopSelf();
    }

    private void initHotFix(){
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(),0);
            String versionName = info.versionName;
            // initialize必须放在attachBaseContext最前面，
            // 初始化代码直接写在Application类里面，切勿封装到其他类。
            SophixManager.getInstance().setContext((Application) mContext)
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
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

}
