package com.lingxiaosuse.picture.tudimension.utils;

import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import com.camera.lingxiao.common.app.ContentValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lingxiao on 2017/8/11.
 * 基于okhttp的下载工具类
 * 无法暂停和取消下载
 */

public class DownloadUtils {
    private static DownloadUtils downloadUtil;
    private final OkHttpClient okHttpClient;

    public static DownloadUtils get() {
        if (downloadUtil == null) {
            downloadUtil = new DownloadUtils();
        }
        return downloadUtil;
    }

    private DownloadUtils() {
        okHttpClient = new OkHttpClient();
    }

    /**
     * @param url 下载连接
     * @param saveDir 储存下载文件的SDCard目录
     * @param listener 下载监听
     */
    public void download(final boolean isImg,final String url, final String saveDir, final OnDownloadListener listener) {
        Request request = new Request
                .Builder()
                .addHeader("Referer", ContentValue.MZITU_URL)
                .addHeader("User-Agent",ContentValue.USER_AGENT)
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 下载失败
                listener.onDownloadFailed(e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = isExistDir(saveDir);
                File file = null;
                try {
                    is = response.body().byteStream();
                    long total = getContentLength(url);
                    long downloadLength = 0; //记录已下载的文件长度
                    if (total == 0){
                        listener.onDownloadFailed("File is null!");
                        return;
                    }else if (total == downloadLength){
                        listener.onDownloadSuccess(file);
                        return;
                    }
                    if (isImg){
                        file = new File(savePath, getNameFromUrl(url)+".jpg");
                    }else {
                        file = new File(savePath, getNameFromUrl(url));
                    }
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中
                        listener.onDownloading(progress);
                    }
                    fos.flush();
                    // 下载完成
                    listener.onDownloadSuccess(file);
                } catch (Exception e) {
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        listener.onDownloadFailed(e.toString());
                        Log.i("code", "onResponse:下载失败 ");
                    }
                }
            }
        });
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载目录是否存在
     */
    private String isExistDir(String saveDir) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory(), saveDir);
        if (!downloadFile.mkdirs()) {
            downloadFile.createNewFile();
        }
        String savePath = downloadFile.getAbsolutePath();
        return savePath;
    }

    /**
     * @param saveDir
     * @return
     * @throws IOException
     * 判断下载文件是否存在
     */
    private File isExistFile(String saveDir,String url) throws IOException {
        // 下载位置
        File downloadFile = new File(Environment.getExternalStorageDirectory()+getNameFromUrl(url));
        if (downloadFile.exists()) {
            //downloadFile.delete();
            return downloadFile;
        }
        return null;
    }

    /**
     * @param url
     * @return
     * 从下载连接中解析出文件名
     */
    @NonNull
    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
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
         *下载暂停
         */
        //void onDownloadPaused();
        /**
         *取消下载
         */
        //void onDownloadCancled();
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
}
