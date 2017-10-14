package com.lingxiaosuse.picture.tudimension.service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import com.lingxiaosuse.picture.tudimension.MainActivity;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.LocalImgActivity;
import com.lingxiaosuse.picture.tudimension.activity.SeeDownLoadImgActivity;
import com.lingxiaosuse.picture.tudimension.utils.AnimationUtils;
import com.lingxiaosuse.picture.tudimension.utils.DownloadTask;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.LeafLoadingView;

import java.io.File;
import java.io.FileNotFoundException;

public class DownloadService extends Service {
    private DownloadTask downloadTask;
    private String downloadUrl;
    private DownloadBinder mBinder = new DownloadBinder();
    private DownloadTask.DownloadListener listener = new DownloadTask.DownloadListener(){

        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1,getNotfifcation("下载中...",progress));
        }

        @Override
        public void onSuccess(File file) {
            //下载成功将前台服务关闭，并创建一个下载成功的通知
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotfifcation("下载成功，点击查看",-1));
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(UIUtils.getContext().getContentResolver(),
                        file.getAbsolutePath(), file.getName(), null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 最后通知图库更新
            UIUtils.getContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.fromFile(new File(file.getPath()))));
        }

        @Override
        public void onFailed() {
            downloadTask = null;
            stopForeground(true);
            getNotificationManager().notify(1,getNotfifcation("下载失败，请重试",-1));
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
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class DownloadBinder extends Binder{
        public void startDownload(String url){
            if (downloadTask == null){
                downloadUrl = url;
                downloadTask = new DownloadTask(listener);
                downloadTask.execute(downloadUrl);
                startForeground(1,getNotfifcation("下载中...",0));
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
    }

    private NotificationManager getNotificationManager(){
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }
    private Notification getNotfifcation(String title,int progress){
        Intent intent = new Intent(this, SeeDownLoadImgActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,0,intent,0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher));
        builder.setContentIntent(pi);
        builder.setContentTitle(title);
        if (progress > 0){
            builder.setContentText(progress + "%");
            builder.setProgress(100,progress,false);
        }
        return builder.build();
    }


}
