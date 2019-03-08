package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CosplayModel;

import java.util.List;

public class CosplayAdapter extends BaseQuickAdapter<CosplayModel.DataBean,BaseViewHolder> {
    private SimpleDraweeView pic;
    private TextView titleText,nameText;
    public CosplayAdapter(int layoutResId, @Nullable List<CosplayModel.DataBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, CosplayModel.DataBean item) {
        pic = (SimpleDraweeView) holder.getView(R.id.pic_grid_item);
        titleText = (TextView) holder.getView(R.id.tv_grid_item);
        nameText = (TextView) holder.getView(R.id.tv_grid_name);
        Uri uri = Uri.parse(item.getDefaultImage());
        pic.setImageURI(uri);
        titleText.setText("主题："+item.getTitle());
        nameText.setText("coser："+item.getNickName());
    }
}
