package com.lingxiaosuse.picture.tudimension.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.lingxiaosuse.picture.tudimension.utils.ToastUtils;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.List;


/**
 * Created by lingxiao on 2017/9/18.
 */

public class LocalImgAdapter extends BaseRecycleAdapter {
    private List<String> picList;
    private SimpleDraweeView simpleDraweeView;

    public LocalImgAdapter(List mList, int headCount, int footCount) {
        super(mList, headCount, footCount);
    }

    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        picList = mList;
        Uri uri = Uri.parse(picList.get(position));
        simpleDraweeView = (SimpleDraweeView) holder.getView(R.id.simple_download);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(UIUtils.dip2px(144),
                        UIUtils.dip2px(144)))
                .build();

        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(simpleDraweeView.getController())
                .setControllerListener(new BaseControllerListener<ImageInfo>())
                .build();
        simpleDraweeView.setController(controller);

        //用下面的加载大图卡顿
        //simpleDraweeView.setImageURI(uri);

    }

    @Override
    public int getLayoutId() {
        return R.layout.local_img_item;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }
}
