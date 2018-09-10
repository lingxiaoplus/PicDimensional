package com.lingxiaosuse.picture.tudimension.service;

import android.app.IntentService;
import android.app.Notification;
import android.content.Intent;
import android.content.Context;
import android.os.Build;

import com.avos.avoscloud.AVOSCloud;
import com.lingxiao.skinlibrary.SkinLib;
import com.lingxiaosuse.picture.tudimension.App;
import com.raizlabs.android.dbflow.config.FlowManager;
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
            startForeground(1,new Notification());
        }
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
        FlowManager.init(mContext);
        //bugly初始化 建议在测试阶段建议设置成true，发布时设置为false。
        CrashReport.UserStrategy strategy = new CrashReport.UserStrategy(this);
        strategy.setCrashHandleCallback(new CrashReport.CrashHandleCallback(){
            @Override
            public synchronized Map<String, String> onCrashHandleStart(int crashType, String errorType, String errorMessage, String errorStack) {
                /*Intent intent  =new Intent(getApplicationContext(), CrashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("msg",errorStack);
                startActivity(intent);*/
                return super.onCrashHandleStart(crashType, errorType, errorMessage, errorStack);
            }
        });
        CrashReport.initCrashReport(getApplicationContext(), "fcc0256432", true,strategy);
        SkinLib.init((App) mContext);
    }

}
