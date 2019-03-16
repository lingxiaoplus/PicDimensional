package com.lingxiaosuse.picture.tudimension.adapter;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.camera.lingxiao.common.app.ContentValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.utils.FrescoHelper;
import com.lingxiaosuse.picture.tudimension.widget.GlideImageLoader;
import com.lingxiaosuse.picture.tudimension.widget.ProgressDrawable;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class HomeRecyclerAdapter extends BaseQuickAdapter<HomePageModle.Picture, BaseViewHolder> {
    private static final String TAG = HomeRecyclerAdapter.class.getSimpleName();
    private boolean first;
    public HomeRecyclerAdapter(int layoutResId, @Nullable List<HomePageModle.Picture> data) {
        super(layoutResId, data);
        first = true;
    }

    @Override
    protected void convert(BaseViewHolder helper, HomePageModle.Picture modle) {
        SimpleDraweeView imageview = (SimpleDraweeView) helper.getView(R.id.iv_home_image);
        TextView textView = (TextView) helper.getView(R.id.tv_home_des);
        Uri uri = Uri.parse(modle.img + ContentValue.imgRule);
        //如果本地JPEG图，有EXIF的缩略图，image pipeline 可以立刻返回它作为一个缩略图
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageview.getController())
                .build();
        imageview.setController(controller);


        imageview.setHierarchy(FrescoHelper.getHierarchy(mContext));

        if (modle.desc.isEmpty()){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(modle.desc);
        }
    }

}
