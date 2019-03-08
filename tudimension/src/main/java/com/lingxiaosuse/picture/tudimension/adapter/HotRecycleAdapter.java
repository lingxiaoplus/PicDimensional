package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.HotModle;

import java.util.List;

/**
 * Created by lingxiao on 2017/8/30.
 */

public class HotRecycleAdapter extends BaseQuickAdapter<HotModle.ResultsBean,BaseViewHolder> {

    public HotRecycleAdapter(int layoutResId, @Nullable List<HotModle.ResultsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, HotModle.ResultsBean item) {
        String url = item.getUrl();
        String time = item.getCreatedAt();
        String desc = item.getDesc();
        Uri uri = Uri.parse(url);     //图片地址

        SimpleDraweeView img = (SimpleDraweeView)
                holder.getView(R.id.iv_meizi_img);
        img.setImageURI(uri);
        TextView textView = (TextView)
                holder.getView(R.id.tv_meizi_des);
        textView.setVisibility(View.GONE);
    }
}
