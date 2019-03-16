package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.MzituModle;

import java.util.List;

/**
 * Created by lingxiao on 2018/1/18.
 */

public class MzituRecyclerAdapter extends BaseQuickAdapter<MzituModle, BaseViewHolder> {

    private static final String TAG = "MzituRecyclerAdapter";
    private SimpleDraweeView simpleDraweeView;
    private List<String> mImgList;
    private List<String> mTitleList;

    public MzituRecyclerAdapter(int layoutResId, @Nullable List<MzituModle> data) {
        super(layoutResId, data);
    }

    public void setTitle(List<String> mTitleList){
        this.mTitleList = mTitleList;
    }

    @Override
    protected void convert(BaseViewHolder holder, MzituModle item) {
        try{
            Uri uri = Uri.parse(item.getImgUrl());
            TextView title = (TextView) holder.getView(R.id.tv_mzitu_title);
            if (null == mTitleList){
                title.setVisibility(View.GONE);
            }else {
                title.setVisibility(View.VISIBLE);
                title.setText(item.getTitle());
            }
            simpleDraweeView = (SimpleDraweeView) holder.getView(R.id.iv_mzitu_image);
            simpleDraweeView.setImageURI(uri);
        }catch (NullPointerException e){
            Log.i(TAG, e.getMessage());
        }
    }
}
