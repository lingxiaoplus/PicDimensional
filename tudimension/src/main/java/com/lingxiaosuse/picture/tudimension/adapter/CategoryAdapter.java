package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.modle.CategoryVerticalModle;

import java.util.List;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class CategoryAdapter extends BaseRecycleAdapter{

    private SimpleDraweeView pic;
    private TextView textView;
    private List<CategoryModle.CategoryBean> mCateList;
    private List<CategoryVerticalModle.CategoryBean> mVerticalList;
    private boolean isVertical = false; //判断是否为手机壁纸

    public CategoryAdapter(List list,int headCont,int footCount,boolean isVertical){
        super(list,headCont,footCount);
        this.isVertical = isVertical;
    }
    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        pic = (SimpleDraweeView) holder.getView(R.id.pic_grid_item);
        textView = (TextView) holder.getView(R.id.tv_grid_item);

        if (isVertical){
            mVerticalList = mList;
            if (mVerticalList.get(position).getCover()!=null){
                Uri uri = Uri.parse(mVerticalList.get(position).getCover());
                pic.setImageURI(uri);
            }
            if (mVerticalList.get(position).getName()!=null){
                textView.setText(mVerticalList.get(position).getName());
            }
        }else {
            mCateList = mList;
            if (mCateList.get(position).getCover()!=null){
                Uri uri = Uri.parse(mCateList.get(position).getCover());
                pic.setImageURI(uri);
            }
            if (mCateList.get(position).getName()!=null){
                textView.setText(mCateList.get(position).getName());
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.category_item;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }
}
