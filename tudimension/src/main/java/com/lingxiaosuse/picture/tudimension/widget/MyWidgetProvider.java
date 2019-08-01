package com.lingxiaosuse.picture.tudimension.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.exception.ApiException;
import com.camera.lingxiao.common.http.RxJavaHelper;
import com.camera.lingxiao.common.observer.HttpRxObserver;
import com.google.gson.Gson;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.HitokotoModle;
import com.lingxiaosuse.picture.tudimension.utils.BitmapUtils;
import com.lingxiaosuse.picture.tudimension.utils.FileUtil;
import com.lingxiaosuse.picture.tudimension.utils.HttpUtils;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.reactivex.disposables.Disposable;

/**
 * Created by lingxiao on 17-11-14.
 */

public class MyWidgetProvider extends AppWidgetProvider {
    public static final String CLICK_ACTION =
            "com.lingxiaosuse.picture.tudimension.action.CLICK"; // 点击事件的广播ACTION
    private static final String TAG = MyWidgetProvider.class.getSimpleName();

    /**
     * 每次窗口小部件被更新都调用一次该方法
     */
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        updateWallPaper(context);
    }

    /**
     * 接收窗口小部件点击时发送的广播
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (CLICK_ACTION.equals(intent.getAction())){
            updateWallPaper(context);
        }
        Log.e(TAG,"onReceive : "+intent.getAction());
    }


    private void updateWallPaper(Context context){
        ComponentName componentName = new ComponentName(context, MyWidgetProvider.class);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                R.layout.widget_layout);
        Intent intent = new Intent(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent
                .getBroadcast(context, R.id.iv_widget,
                        intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_widget, pendingIntent);
        //remoteViews.setTextViewText(R.id.tv_name,"hello");
        remoteViews.setTextColor(R.id.tv_name, Color.WHITE);
        /*for (int appWidgetId : appWidgetIds) {
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }*/
        Log.e(TAG,"onUpdate了");
        //remoteViews.setTextViewText(R.id.tv_name,"hello");
        getHeadText(componentName,remoteViews,context);
        RxJavaHelper.work(e -> {
            Log.e(TAG,"开始设置小部件壁纸");
            List<File> fileList = FileUtil.getFiles(ContentValue.PATH);
            List<String> picList = new ArrayList<>();
            if (null != fileList && fileList.size() != 0) {
                for (int i = 0; i < fileList.size(); i++) {
                    String path = fileList.get(i).getAbsolutePath();
                    if (BitmapUtils.isLandscape(path) && FileUtil.getFileSize(path) < 2L) {
                        picList.add(path);
                    }
                }
            }
            // TODO: 2018/7/31  虚拟机报参数不匹配？？
            Random random = new Random();
            int index = random.nextInt(picList.size());
            FileInputStream inputStream = new FileInputStream(picList.get(index));
            Bitmap bitmap = BitmapUtils.compressImageByResolution(BitmapFactory.decodeStream(inputStream),
                    840f, 400f);
            e.onNext(bitmap);
        }, new HttpRxObserver() {
            @Override
            protected void onStart(Disposable d) {

            }

            @Override
            protected void onError(ApiException e) {
                Log.e(TAG,"桌面小部件异常："+e.getMsg());
            }

            @Override
            protected void onSuccess(Object response) {
                Log.e(TAG,"成功设置小部件壁纸");
                Bitmap bitmap = BitmapUtils.zoomImage((Bitmap) response);  //有些图片太大 压缩为1080p的，不然会oom
                AppWidgetManager manager = AppWidgetManager.getInstance(context);

                remoteViews.setImageViewBitmap(R.id.iv_widget,bitmap);
                manager.updateAppWidget(componentName, remoteViews);
            }
        });
    }
    /**
     * 每删除一次窗口小部件就调用一次
     */
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.e(TAG,"onDeleted");
    }

    /**
     * 当最后一个该窗口小部件删除时调用该方法
     */
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    /**
     * 当该窗口小部件第一次添加到桌面时调用该方法
     */
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.e(TAG,"onEnabled");
    }

    /**
     * 当小部件大小改变时
     */
    @Override
    public void onAppWidgetOptionsChanged(Context context,
                                          AppWidgetManager appWidgetManager,
                                          int appWidgetId, Bundle newOptions) {
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    /**
     * 当小部件从备份恢复时调用该方法
     */
    @Override
    public void onRestored(Context context, int[] oldWidgetIds, int[] newWidgetIds) {
        super.onRestored(context, oldWidgetIds, newWidgetIds);
    }

    public void getHeadText(ComponentName componentName, RemoteViews remoteViews, Context context){
        HttpUtils.doGet(ContentValue.HITOKOTO_URL, new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                try {
                    String result = response.body().string();
                    Gson gson = new Gson();
                    final HitokotoModle hotModle =
                            gson.fromJson(result,HitokotoModle.class);
                    UIUtils.runOnUIThread(new Runnable() {
                        @Override
                        public void run() {
                            AppWidgetManager manager = AppWidgetManager.getInstance(context);
                            remoteViews.setTextViewText(R.id.tv_name,hotModle.getHitokoto());
                            manager.updateAppWidget(componentName, remoteViews);
                        }
                    });
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }
}
