package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.widget.BaseHolder;
import com.camera.lingxiao.common.widget.BaseRecyclerViewAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CosplayDetailModel;

import java.util.List;

public class CosplayDetailAdapter extends BaseQuickAdapter<CosplayDetailModel.DataBean.ShareBean.PhotoListsBean, BaseViewHolder> {
    public CosplayDetailAdapter(int layoutResId, @Nullable List<CosplayDetailModel.DataBean.ShareBean.PhotoListsBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CosplayDetailModel.DataBean.ShareBean.PhotoListsBean item) {
        SimpleDraweeView draweeView = helper.getView(R.id.iv_mzitu_image);
        TextView textView = helper.getView(R.id.tv_mzitu_title);
        textView.setVisibility(View.GONE);
        Uri uri = Uri.parse(item.getPicPath());
        draweeView.setImageURI(uri);
    }
}
