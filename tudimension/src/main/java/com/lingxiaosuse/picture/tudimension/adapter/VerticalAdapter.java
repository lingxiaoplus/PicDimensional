package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;

import com.camera.lingxiao.common.app.ContentValue;
import com.camera.lingxiao.common.utills.SpUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CategoryDetailModle;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;

import java.util.List;

/**
 * Created by lingxiao on 17-11-9.
 */

public class VerticalAdapter extends BaseQuickAdapter<VerticalModle.VerticalBean,BaseViewHolder> {

    private List<VerticalModle.VerticalBean> mBenList;
    private SimpleDraweeView draweeView;
    private List<CategoryDetailModle.ResBean.VerticalBean> cateBeanList;//分类
    private boolean isCategory = false;

    public VerticalAdapter(int layoutResId, @Nullable List<VerticalModle.VerticalBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, VerticalModle.VerticalBean item) {
        Uri uri = Uri.parse(item.getThumb());
        draweeView = (SimpleDraweeView) holder.getView(R.id.img_vertical_item);
        draweeView.setImageURI(uri);
    }
}
