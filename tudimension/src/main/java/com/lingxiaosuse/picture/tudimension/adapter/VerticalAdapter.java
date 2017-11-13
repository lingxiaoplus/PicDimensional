package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CategoryDetailModle;
import com.lingxiaosuse.picture.tudimension.modle.VerticalModle;

import java.util.List;

/**
 * Created by lingxiao on 17-11-9.
 */

public class VerticalAdapter extends BaseRecycleAdapter{

    private List<VerticalModle.ResBean.VerticalBean> mBenList;
    private SimpleDraweeView draweeView;
    private List<CategoryDetailModle.ResBean.VerticalBean> cateBeanList;//分类
    private boolean isCategory = false;
    public VerticalAdapter(List mList, int headCount, int footCount,boolean isCategory) {
        super(mList, headCount, footCount);
        this.isCategory = isCategory;
    }

    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        Uri uri = null;
        if (isCategory){
            cateBeanList = mList;
            uri = Uri.parse(cateBeanList.get(position).getThumb());
        }else {
            mBenList = mList;
            uri = Uri.parse(mBenList.get(position).getThumb());
        }
        draweeView = (SimpleDraweeView) holder.getView(R.id.img_vertical_item);
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
