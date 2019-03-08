package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.SpecialModle;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;

import java.util.List;

import javax.crypto.spec.PSource;

/**
 * Created by lingxiao on 2017/9/4.
 */

public class SpecialRecycleAdapter extends BaseQuickAdapter<SpecialModle.AlbumBean,BaseViewHolder> {
    private SimpleDraweeView bodyImage,nameImage;
    private TextView title,message,name,time;

    public SpecialRecycleAdapter(int layoutResId, @Nullable List<SpecialModle.AlbumBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, SpecialModle.AlbumBean item) {
        bodyImage = (SimpleDraweeView) holder.getView(R.id.iv_special_pic);
        title = (TextView) holder.getView(R.id.tv_special_title);
        message = (TextView) holder.getView(R.id.tv_special_message);
        nameImage = (SimpleDraweeView) holder.getView(R.id.iv_special_who);
        name = (TextView) holder.getView(R.id.tv_special_who);
        time = (TextView) holder.getView(R.id.tv_special_time);
        final Uri uri = Uri.parse(item.getCover());
        //如果本地JPEG图，有EXIF的缩略图，image pipeline 可以立刻返回它作为一个缩略图
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(bodyImage.getController())
                .build();
        bodyImage.setController(controller);
        title.setText(item.getName());
        message.setText(item.getDesc());
        nameImage.setImageURI(Uri.parse(item.getUser().getAvatar()));
        name.setText(item.getUser().getName());
        String data = StringUtils.strToDate(String.valueOf(item.getAtime()));
        time.setText(data);
    }
}
