package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;

import java.util.List;

/**
 * Created by lingxiao on 17-11-9.
 */

public class VerticalAdapter extends BaseRecycleAdapter{

    private List<VerticalModle.ResBean.VerticalBean> mBenList;
    private SimpleDraweeView draweeView;

    public VerticalAdapter(List mList, int headCount, int footCount) {
        super(mList, headCount, footCount);
    }

    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        mBenList = mList;
        draweeView = (SimpleDraweeView) holder.getView(R.id.img_vertical_item);
        Uri uri = Uri.parse(mBenList.get(position).getThumb());
        draweeView.setImageURI(uri);
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_vertical;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }
}
