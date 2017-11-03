package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;

import java.util.List;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class CategoryAdapter extends BaseRecycleAdapter{

    private SimpleDraweeView pic;
    private TextView textView;
    private List<CategoryModle.ResBean.CategoryBean> mCateList;
    public CategoryAdapter(List list){
        super(list);
    }
    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        mCateList = mList;
        pic = (SimpleDraweeView) holder.getView(R.id.pic_grid_item);
        textView = (TextView) holder.getView(R.id.tv_grid_item);
        if (mCateList.get(position).getCover()!=null){
            Uri uri = Uri.parse(mCateList.get(position).getCover());
            pic.setImageURI(uri);
        }
        if (mCateList.get(position).getName()!=null){
            textView.setText(mCateList.get(position).getName());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.category_item;
    }
}
