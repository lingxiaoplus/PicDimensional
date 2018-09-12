package com.lingxiaosuse.picture.tudimension.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.lingxiao.common.app.ContentValue;
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

public class BannerRecycleAdapter extends BaseRecycleAdapter{
    private List<BannerModle.WallpaperBean> picList;
    private int footCount = 1; //尾布局个数
    private static final int BODY_TYPE=1;
    private static final int FOOT_TYPE=2;
    private boolean isFinish;
    public BannerRecycleAdapter(List list,int headCont,int footCount){
        super(list,headCont,footCount);
    }

    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        this.picList = mList;
        SimpleDraweeView imageview = (SimpleDraweeView) holder.getView(R.id.iv_banner_image);
        Uri uri = Uri.parse(picList.get(position).getImg()
                + ContentValue.imgRule);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageview.getController())
                .build();
        imageview.setController(controller);
        //Uri.parse(picList.get(position).getImg();
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_banner;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }
}
