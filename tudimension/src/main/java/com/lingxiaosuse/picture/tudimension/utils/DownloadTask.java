package com.lingxiaosuse.picture.tudimension.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.os.Process;

import com.camera.lingxiao.common.app.ContentValue;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lingxiao on 2017/10/9.
 * 使用asynctask封装的下载工具类
 */

public class DownloadTask extends AsyncTask<String,Integer,Integer>{
    public static final int TYPE_SUCCESS = 0;
    public static final int TYPE_FAILED = 1;
    public static final int TYPE_PAUSED = 2;
    public static final int TYPE_CANCELED = 3;
    private boolean isCanceled = false;
    private boolean isPaused = false;
    private int lastProgress;
    private DownloadListener listener;
    private File mFile;

    public DownloadTask(DownloadListener listener){
        this.listener = listener;
    }
    @Override
    protected Integer doInBackground(String... params) {
        InputStream is = null;
        RandomAccessFile saveFile = null;
        mFile = null;
        try{
            long downloadLength = 0;
            String downloadUrl = params[0];
            String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
            /*String directory = Environment
                    .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .getPath();*/
            String directory = ContentValue.PATH;
            FileUtil.isExistDir(directory);
            //这里下载的图片
            mFile = new File(directory +fileName+".jpg");
            if (mFile.exists()){
                downloadLength = mFile.length();
            }
            long contentLength = getContentLength(downloadUrl);
            if (contentLength == 0){
                return TYPE_FAILED;
            }else if (contentLength == downloadLength){
                return TYPE_SUCCESS;
            }
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .addHeader("RANGE","bytes="+downloadLength+"-")
                    .url(downloadUrl)
                    .build();
            Response response = client.newCall(request).execute();
            if (response != null){
                is = response.body().byteStream();
                saveFile = new RandomAccessFile(mFile,"rw");
                saveFile.seek(downloadLength);  //跳过已下载的字节
                byte[] b = new byte[1024];
                int total = 0;
                int len;
                while ((len = is.read(b)) != -1){
                    if (isCanceled){
                        return TYPE_CANCELED;
                    }else if (isPaused){
                        return TYPE_PAUSED;
                    }else {
                        total += len;
                        saveFile.write(b,0,len);
                        //计算已经下载的百分比
                        int progress = (int) ((total +downloadLength)*100 / contentLength);
                        publishProgress(progress);
                    }
                }
                response.body().close();
                return TYPE_SUCCESS;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (is != null){
                    is.close();
                }
                if (saveFile != null){
                    saveFile.close();
                }
                if (isCanceled && mFile != null){
                    mFile.delete();
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return TYPE_FAILED;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        int progress = values[0];
        if (progress >lastProgress){
            listener.onProgress(progress);
            lastProgress = progress;
        }
    }

    @Override
    protected void onPostExecute(Integer status) {
        switch (status){
            case TYPE_SUCCESS:
                listener.onSuccess(mFile);
                break;
            case TYPE_FAILED:
                listener.onFailed();
                break;
            case TYPE_CANCELED:
                listener.onCanceled();
                break;
            case TYPE_PAUSED:
                listener.onPaused();
                break;
            default:
                break;
        }
    }
    public void setTypePaused(){
        isPaused = true;
    }
    public void setTypeCanceled(){
        isCanceled = true;
    }

    /**
     *获取文件长度
     */
    private long getContentLength(String downloadUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        Response response = client.newCall(request).execute();
        if (response != null && response.isSuccessful()){
            long contentLength = response.body().contentLength();
            response.body().close();
            return contentLength;
        }
        return 0;
    }

    public interface DownloadListener{
        void onProgress(int progress);
        void onSuccess(File file);
        void onFailed();
        void onPaused();
        void onCanceled();
    }
}
