package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CosplayModel;

import java.util.List;

public class CosplayAdapter extends BaseRecycleAdapter{
    private SimpleDraweeView pic;
    private TextView titleText,nameText;
    private List<CosplayModel.DataBean> cosplayModels;
    public CosplayAdapter(List mList, int headCount, int footCount) {
        super(mList, headCount, footCount);
    }

    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        pic = (SimpleDraweeView) holder.getView(R.id.pic_grid_item);
        titleText = (TextView) holder.getView(R.id.tv_grid_item);
        nameText = (TextView) holder.getView(R.id.tv_grid_name);
        cosplayModels = mList;
        Uri uri = Uri.parse(cosplayModels.get(position).getDefaultImage());
        pic.setImageURI(uri);
        titleText.setText("主题："+cosplayModels.get(position).getTitle());
        nameText.setText("coser："+cosplayModels.get(position).getNickName());

    }

    @Override
    public int getLayoutId() {
        return R.layout.cosplay_item;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }

}
