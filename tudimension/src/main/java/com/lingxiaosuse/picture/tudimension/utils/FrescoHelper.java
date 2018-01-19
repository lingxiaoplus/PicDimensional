package com.lingxiaosuse.picture.tudimension.utils;

import android.content.Context;

import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

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
        Fresco.initialize(context, config);
    }

    private static OkHttpClient getOkHttpClientForFresco() {
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
}
