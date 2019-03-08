package com.lingxiaosuse.picture.tudimension.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.lingxiao.common.app.ContentValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;

import java.util.List;

/**
 * Created by lingxiao on 2017/9/1.
 */

public class BannerRecycleAdapter extends BaseQuickAdapter<BannerModle.WallpaperBean,BaseViewHolder> {

    public BannerRecycleAdapter(int layoutResId, @Nullable List<BannerModle.WallpaperBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, BannerModle.WallpaperBean item) {
        SimpleDraweeView imageview = (SimpleDraweeView) holder.getView(R.id.iv_banner_image);
        Uri uri = Uri.parse(item.getImg()
                + ContentValue.imgRule);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageview.getController())
                .build();
        imageview.setController(controller);
    }
}
