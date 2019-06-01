package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.utills.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;

import java.util.List;

public class TuWanAdapter extends BaseQuickAdapter<TuWanModle, BaseViewHolder> {
    public TuWanAdapter(int layoutResId, @Nullable List<TuWanModle> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, TuWanModle item) {

        ControllerListener controllerListener = new BaseControllerListener<ImageInfo>(){
            @Override
            public void onFailure(String id, Throwable throwable) {
                super.onFailure(id, throwable);
                LogUtils.d("图片加载失败："+throwable);
            }

            @Override
            public void onIntermediateImageFailed(String id, Throwable throwable) {
                super.onIntermediateImageFailed(id, throwable);
                LogUtils.d("onIntermediateImageFailed："+throwable);
            }
        };
        Uri uri = Uri.parse(item.getUrl());
        TextView title = holder.getView(R.id.tv_mzitu_title);
        title.setText(item.getTitle());
        SimpleDraweeView simpleDraweeView = holder.getView(R.id.iv_mzitu_image);
        //simpleDraweeView.setImageURI(uri);
        int width = 200, height = 200;
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(width, height))
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setControllerListener(controllerListener)
                .setImageRequest(request)
                .setUri(uri)
                // other setters
                .build();
        simpleDraweeView.setController(controller);
        simpleDraweeView.setHierarchy(FrescoHelper.getHierarchy(mContext));
    }
}
