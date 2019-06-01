package com.lingxiaosuse.picture.tudimension.service;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.SeeDownLoadImgActivity;
import com.lingxiaosuse.picture.tudimension.receiver.NotificationReceiver;
import com.lingxiaosuse.picture.tudimension.utils.DownloadTask;
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.liuguangqiang.cookie.CookieBar;

import java.io.File;
import java.io.FileNotFoundException;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadBinder mBinder = new DownloadBinder();
    private DownloadTask.DownloadListener listener = new DownloadTask.DownloadListener(){

        @Override
        public void onProgress(int progress) {
            if (mDownloadListener != null){
                mDownloadListener.onDownloading(progress);
            }
            getNotificationManager().notify(1,getNotfifcation("下载中...",progress));
        }

        @Override
        public void onSuccess(File file) {
            //下载成功将前台服务关闭，并创建一个下载成功的通知
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotfifcation("下载成功，点击查看",-1));
            // 最后通知图库更新
            UIUtils.updateImageDb(file);
            ToastUtils.show("下载成功");
            if (mDownloadListener != null){
                mDownloadListener.onDownloadSuccess(file);
            }
        }

        @Override
        public void onFailed(String msg) {
            downloadTask = null;
            stopForeground(true);
            ToastUtils.show("下载失败:" + msg);
            getNotificationManager().notify(1,getNotfifcation("下载失败，请重试",-1));
            if (mDownloadListener != null){
                mDownloadListener.onDownloadFailed(msg);
            }
        }

        @Override
        public void onPaused() {
            downloadTask = null;
        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ONE_ID = this.getPackageName();
                NotificationChannel channel = new NotificationChannel(CHANNEL_ONE_ID, "xxx", NotificationManager.IMPORTANCE_LOW);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (manager == null)
                    return;
                manager.createNotificationChannel(channel);
                Notification notification = new NotificationCompat.Builder(this, CHANNEL_ONE_ID)
                        .setChannelId(CHANNEL_ONE_ID)
                        .setAutoCancel(true)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setOngoing(true)
                        .setPriority(NotificationManager.IMPORTANCE_LOW)
                        .build();
                startForeground(101, notification);
                //startForeground(1,new Notification());
            }
        }

    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DownloadBinder extends Binder{
        public DownloadService getService(){
            return DownloadService.this;
        }
    }

    public void startDownload(String url){
        if (downloadTask == null){
            downloadUrl = url;
            downloadTask = new DownloadTask(listener);
            downloadTask.execute(downloadUrl);
            startForeground(1,getNotfifcation("下载中...",0));
            if (mDownloadListener != null){
                mDownloadListener.onStartDownload();
            }
        }
    }
    public void pauseDownload(){
        if (downloadTask != null){
            downloadTask.setTypePaused();
        }
    }
    public void cancelDownload(){
        if (downloadTask != null){
            downloadTask.setTypeCanceled();
        }else {
            if (downloadUrl != null){
                String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
                String directory = Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        .getPath();
                File file = new File(directory +fileName);
                if (file.exists()){
                    file.delete();
                }
                getNotificationManager().cancel(1);
                stopForeground(true);
            }
        }
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNotfifcation(String title,int progress){
        //Intent intent = new Intent(this, SeeDownLoadImgActivity.class);
        //PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);


        Intent intentClick = new Intent(this, NotificationReceiver.class);
        intentClick.setAction("notification_clicked");
        intentClick.putExtra(NotificationReceiver.TYPE, 1);
        //intentClick.putExtra("username",msg.getUserName());
        PendingIntent pendingIntentClick = PendingIntent.getBroadcast(this, 0, intentClick, PendingIntent.FLAG_ONE_SHOT);

        Intent intentCancel = new Intent(this, NotificationReceiver.class);
        intentCancel.setAction("notification_cancelled");
        intentCancel.putExtra(NotificationReceiver.TYPE, 1);
        PendingIntent pendingIntentCancel = PendingIntent.getBroadcast(this, 0, intentCancel, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder builder;
        String name = "my_package_channel";//渠道名字
        String id = "my_package_channel_1"; // 渠道ID
        String description = "my_package_first_channel"; // 渠道解释说明
        //判断是否是8.0上设备
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = null;
            if (mChannel == null) {
                mChannel = new NotificationChannel(id, name, importance);
                mChannel.setDescription(description);
                mChannel.enableLights(true); //是否在桌面icon右上角展示小红点
                getNotificationManager().createNotificationChannel(mChannel);
            }
            builder = new NotificationCompat.Builder(this,id);
            builder.setChannelId(id);
        }else {
            builder = new NotificationCompat.Builder(this);
        }

        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pendingIntentClick);
        builder.setDeleteIntent(pendingIntentCancel);
        builder.setContentTitle(title);
        if (progress > 0){
            builder.setContentText(progress + "%");

            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }
    private OnDownloadListener mDownloadListener;

    public void setDownloadListener(OnDownloadListener listener) {
        this.mDownloadListener = listener;
    }

    public interface OnDownloadListener {
        /**
         * 下载成功
         */
        void onDownloadSuccess(File file);

        /**
         * @param progress
         * 下载进度
         */
        void onDownloading(int progress);

        /**
         * 下载失败
         */
        void onDownloadFailed(String error);
        /**
         * 开始下载
         */
        void onStartDownload();
        /**
         *下载暂停
         */
        //void onDownloadPaused();
        /**
         *取消下载
         */
        //void onDownloadCancled();
    }

}
