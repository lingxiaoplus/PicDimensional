package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.TuWanModle;

import java.util.List;

public class TuWanAdapter extends BaseQuickAdapter<TuWanModle.ImageData, BaseViewHolder> {
    public TuWanAdapter(int layoutResId, @Nullable List<TuWanModle.ImageData> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, TuWanModle.ImageData item) {
        Uri uri = Uri.parse(item.getHeader());
        TextView title = holder.getView(R.id.tv_mzitu_title);
        title.setText(item.getTitle());
        SimpleDraweeView simpleDraweeView = holder.getView(R.id.iv_mzitu_image);
        simpleDraweeView.setImageURI(uri);
    }
}
