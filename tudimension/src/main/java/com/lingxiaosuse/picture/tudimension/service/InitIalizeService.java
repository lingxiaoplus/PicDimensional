package com.lingxiaosuse.picture.tudimension.service;

import android.app.Application;
import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.lingxiao.skinlibrary.SkinLib;
import com.lingxiaosuse.picture.tudimension.global.App;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.tencent.bugly.crashreport.CrashReport;

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

    public static void startInit(Context context) {
        Intent intent = new Intent(context, InitIalizeService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
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
        CrashReport.initCrashReport(getApplicationContext(), "fcc0256432", true);
        SkinLib.init((App) mContext);
    }

}
