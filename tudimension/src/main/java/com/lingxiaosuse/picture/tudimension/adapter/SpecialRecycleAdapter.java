package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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

public class SpecialRecycleAdapter extends BaseRecycleAdapter{
    private List<SpecialModle.ResBean.AlbumBean> mAlbumList;
    private int footCount = 1;
    private static final int BODY_TYPE=1;
    private static final int FOOT_TYPE=2;
    private boolean isFinish;   //是否加载完成 -- 隐藏布局
    private SimpleDraweeView bodyImage,nameImage;
    private TextView title,message,name,time;

    public SpecialRecycleAdapter(List mList, int headCount, int footCount) {
        super(mList, headCount, footCount);
    }

    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        mAlbumList = mList;

        bodyImage = (SimpleDraweeView) holder.getView(R.id.iv_special_pic);
        title = (TextView) holder.getView(R.id.tv_special_title);
        message = (TextView) holder.getView(R.id.tv_special_message);
        nameImage = (SimpleDraweeView) holder.getView(R.id.iv_special_who);
        name = (TextView) holder.getView(R.id.tv_special_who);
        time = (TextView) holder.getView(R.id.tv_special_time);
        final Uri uri = Uri.parse(mAlbumList.get(position).getCover());
        //如果本地JPEG图，有EXIF的缩略图，image pipeline 可以立刻返回它作为一个缩略图
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(bodyImage.getController())
                .build();
        bodyImage.setController(controller);
        title.setText(mAlbumList.get(position).getName());
        message.setText(mAlbumList.get(position).getDesc());
        nameImage.setImageURI(Uri.parse(mAlbumList.get(position).getUser().getAvatar()));
        name.setText(mAlbumList.get(position).getUser().getName());
        String data = StringUtils.strToDate(String.valueOf(mAlbumList.get(position).getAtime()));
        time.setText(data);


    }

    @Override
    public int getLayoutId() {
        return R.layout.list_special;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }

}
