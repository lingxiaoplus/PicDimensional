package com.lingxiaosuse.picture.tudimension.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.camera.lingxiao.common.app.ContentValue;

import com.camera.lingxiao.common.exception.ApiException;
import com.camera.lingxiao.common.http.RxJavaHelper;
import com.camera.lingxiao.common.observer.HttpRxObserver;
import com.facebook.binaryresource.FileBinaryResource;
import com.facebook.cache.common.SimpleCacheKey;
import com.facebook.drawee.backends.pipeline.Fresco;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lingxiaosuse.picture.tudimension.activity.ImageLoadingActivity;
import com.lingxiaosuse.picture.tudimension.widget.ProgressDrawable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lingxiao on 2018/1/18.
 */

public class FrescoHelper {
    public static void initFresco(Context context) {
        ImagePipelineConfig config = OkHttpImagePipelineConfigFactory
                .newBuilder(context, getOkHttpClientForFresco()).build();


        Fresco.initialize(context, config);
        //Fresco.initialize(context, config);
    }

    public static OkHttpClient getOkHttpClientForFresco() {
        OkHttpClient client;
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .addInterceptor(new HeaderInterceptor())
                .connectTimeout(20, TimeUnit.SECONDS);
        client = builder.build();
        return client;
    }

    private static class HeaderInterceptor implements Interceptor {
        private HashMap<String, String> mHeadMap;

        private HeaderInterceptor() {
            mHeadMap = new HashMap<>();
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder builder = request.newBuilder();
            for (String s : mHeadMap.keySet()) {
                builder.addHeader(s, mHeadMap.get(s));
            }
            builder.addHeader("Referer", ContentValue.MZITU_URL);
            builder.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.108 Safari/537.36");
            request = builder.build();
            return chain.proceed(request);
        }
    }


    /**
     * 根据图片宽度自适应
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    /**
     * 从fresco的见缓存中获取图片
     * @param path
     */
    public static File saveImageByFresco(String path) throws IOException {
        FileBinaryResource resource = (FileBinaryResource)
                Fresco.getImagePipelineFactory()
                        .getMainFileCache()
                        .getResource(new SimpleCacheKey(path));
        File file = new File(ContentValue.PATH + "/" + System.currentTimeMillis() + ".jpg");
        FileUtil.copyFile(resource.getFile(),file);
        UIUtils.updateImageDb(file);
        return file;
    }

    public static GenericDraweeHierarchy getHierarchy(Context context){
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(context.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setProgressBarImage(new ProgressDrawable())
                .build();
        return hierarchy;
    }


}
