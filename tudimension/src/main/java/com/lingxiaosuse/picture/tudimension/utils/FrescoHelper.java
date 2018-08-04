package com.lingxiaosuse.picture.tudimension.utils;

import android.content.Context;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.camera.lingxiao.common.app.ContentValue;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.util.ByteConstants;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.lingxiaosuse.picture.tudimension.widget.ZoomableDrawwView;

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
     *通过imageWidth 的宽度，自动适应高度
     */
    public static void setControllerListener(final ZoomableDrawwView simpleDraweeView, Uri imageUri, final int imageWidth){
        final ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
            @Override
            public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                if (imageInfo == null) {
                    return;
                }
                int height = imageInfo.getHeight();
                int width = imageInfo.getWidth();
                layoutParams.width = imageWidth;
                layoutParams.height = (int) ((float) (imageWidth * height) / (float) width);
                simpleDraweeView.setLayoutParams(layoutParams);
            }

            @Override
            public void onIntermediateImageSet(String id, @Nullable ImageInfo imageInfo) {

            }

            @Override
            public void onFailure(String id, Throwable throwable) {
                throwable.printStackTrace();
            }

        };
        DraweeController controller = Fresco.newDraweeControllerBuilder().setControllerListener(controllerListener).setUri(imageUri).build();
        simpleDraweeView.setController(controller);
    }
}
