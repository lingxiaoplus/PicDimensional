package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.widget.BaseHolder;
import com.camera.lingxiao.common.widget.BaseRecyclerViewAdapter;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CosplayDetailModel;

import java.util.List;

public class CosplayDetailAdapter extends BaseRecyclerViewAdapter{
    private SimpleDraweeView draweeView;
    private TextView textView;
    public CosplayDetailAdapter(List<CosplayDetailModel.DataBean.ShareBean.PhotoListsBean> photoList, AdapterListener listener) {
        super(photoList,listener);

    }
    @Override
    protected int getItemViewType(int position, Object o) {
        return R.layout.list_mzitu;
    }

    @Override
    protected BaseHolder onCreateViewHolder(View root, int viewType) {
        return new CosplayHolder(root);
    }

    private class CosplayHolder extends BaseHolder<CosplayDetailModel.DataBean.ShareBean.PhotoListsBean>{

        public CosplayHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void onBind(CosplayDetailModel.DataBean.ShareBean.PhotoListsBean photoListsBean, int position) {
            draweeView = getView(R.id.iv_mzitu_image);
            textView = getView(R.id.tv_mzitu_title);
            textView.setVisibility(View.GONE);
            Uri uri = Uri.parse(photoListsBean.getPicPath());
            draweeView.setImageURI(uri);
        }
    }
}
