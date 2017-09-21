package com.lingxiaosuse.picture.tudimension.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.image.ImageInfo;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.utils.BitmapUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by lingxiao on 2017/9/18.
 */

public class LocalImgAdapter extends RecyclerView.Adapter<LocalImgAdapter.ViewHolder>{
    private List<String> picList;
    public LocalImgAdapter(List<String> picList){
        this.picList = picList;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.local_img_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Uri uri = Uri.parse(picList.get(position));
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(UIUtils.dip2px(144),
                        UIUtils.dip2px(144)))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(holder.img_download.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        holder.img_download.setController(controller);
        holder.img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null){
                    onItemClickListener.onItemClick(view,position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return picList==null?0:picList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView img_download;
        public ViewHolder(View itemView) {
            super(itemView);
            img_download = itemView.findViewById(R.id.simple_download);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View View, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
