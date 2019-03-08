package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;

import java.util.List;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class CategoryAdapter extends BaseQuickAdapter<CategoryModle.CategoryBean,BaseViewHolder> {

    private SimpleDraweeView pic;
    private TextView textView;
    private List<CategoryModle.CategoryBean> mCateList;
    private boolean isVertical = false; //判断是否为手机壁纸

    public CategoryAdapter(int layoutResId, @Nullable List<CategoryModle.CategoryBean> data,boolean isVertical) {
        super(layoutResId, data);
        this.isVertical = isVertical;
    }

    @Override
    protected void convert(BaseViewHolder holder, CategoryModle.CategoryBean item) {
        pic = (SimpleDraweeView) holder.getView(R.id.pic_grid_item);
        textView = (TextView) holder.getView(R.id.tv_grid_item);

        if (isVertical){
            if (item.getCover()!=null){
                Uri uri = Uri.parse(item.getCover());
                pic.setImageURI(uri);
            }
            if (item.getName()!=null){
                textView.setText(item.getName());
            }
        }else {
            if (item.getCover()!=null){
                Uri uri = Uri.parse(item.getCover());
                pic.setImageURI(uri);
            }
            if (item.getName()!=null){
                textView.setText(item.getName());
            }
        }
    }
}
