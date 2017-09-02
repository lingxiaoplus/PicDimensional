package com.lingxiaosuse.picture.tudimension.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.global.ContentValue;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;

import java.util.List;

/**
 * Created by lingxiao on 2017/9/1.
 */

public class BannerRecycleAdapter extends RecyclerView.Adapter<BannerRecycleAdapter.BannerHolder>{
    private List<BannerModle.ResBean.WallpaperBean> picList;
    public BannerRecycleAdapter(List<BannerModle.ResBean.WallpaperBean> picList,Context context){
        this.picList = picList;
    }
    @Override
    public BannerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.list_banner,parent,false);
        BannerHolder holder = new BannerHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BannerHolder holder, int position) {
        Uri uri = Uri.parse(picList.get(position).getImg()
                + ContentValue.imgRule);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.imageview.getController())
                .build();
        holder.imageview.setController(controller);
    }

    @Override
    public int getItemCount() {
        return picList==null?0:picList.size();
    }

    class BannerHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView imageview;
        public BannerHolder(View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.iv_banner_image);
        }
    }
}
