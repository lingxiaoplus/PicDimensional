package com.lingxiaosuse.picture.tudimension.utils;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.BaseDataSubscriber;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.memory.PooledByteBuffer;
import com.facebook.imagepipeline.memory.PooledByteBufferInputStream;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Executors;

/**
 * Created by lingxiao on 2017/8/4.
 */

public class DownloadImgUtils {
    /**
     * 从网络下载图片
     * 1、根据提供的图片URL，获取图片数据流
     * 2、将得到的数据流写入指定路径的本地文件
     *
     * @param url            URL
     * @param loadFileResult LoadFileResult
     */
    public static void downloadImage(Context context, String url, final DownloadImageResult loadFileResult) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        Uri uri = Uri.parse(url);
        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        ImageRequestBuilder builder = ImageRequestBuilder.newBuilderWithSource(uri);
        ImageRequest imageRequest = builder.build();

        // 获取未解码的图片数据
        DataSource<CloseableReference<PooledByteBuffer>>
                dataSource = imagePipeline
                .fetchEncodedImage(imageRequest, context);
        dataSource.subscribe(new BaseDataSubscriber<CloseableReference<PooledByteBuffer>>() {
            @Override
            public void onNewResultImpl(DataSource<CloseableReference<PooledByteBuffer>> dataSource) {
                if (!dataSource.isFinished() || loadFileResult == null) {
                    return;
                }

                CloseableReference<PooledByteBuffer> imageReference = dataSource.getResult();
                if (imageReference != null) {
                    final CloseableReference<PooledByteBuffer> closeableReference = imageReference.clone();
                    try {
                        PooledByteBuffer pooledByteBuffer = closeableReference.get();
                        InputStream inputStream = new PooledByteBufferInputStream(pooledByteBuffer);
                        String photoPath = loadFileResult.getFilePath();
                        Log.i("ImageLoader", "photoPath = " + photoPath);

                        /*byte[] data = StreamTool.read(inputStream);
                        StreamTool.write(photoPath, data);*/
                        loadFileResult.onResult(photoPath);
                    } finally {
                        imageReference.close();
                        closeableReference.close();
                    }
                }
            }

            @Override
            public void onFailureImpl(DataSource dataSource) {
                if (loadFileResult != null) {
                    loadFileResult.onFail();
                }

                Throwable throwable = dataSource.getFailureCause();
                if (throwable != null) {
                    Log.e("ImageLoader", "onFailureImpl = " + throwable.toString());
                }
            }
        }, Executors.newSingleThreadExecutor());
    }
}
