package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.CategoryModle;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;

import java.util.List;

/**
 * Created by lingxiao on 2017/8/28.
 */

public class GridCategoryAdapter extends BaseAdapter{

    private SimpleDraweeView pic;
    private TextView textView;
    private List<CategoryModle.ResBean.CategoryBean> mList;
    public GridCategoryAdapter(List<CategoryModle.ResBean.CategoryBean> mList){
        this.mList = mList;
    }
    @Override
    public int getCount() {
        return mList==null?0:mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View contentview, ViewGroup viewGroup) {
        View view = UIUtils.inflate(R.layout.gridview_item);
        pic = view.findViewById(R.id.pic_grid_item);
        textView = view.findViewById(R.id.tv_grid_item);
        if (mList.get(i).getCover()!=null){
            Uri uri = Uri.parse(mList.get(i).getCover());
            pic.setImageURI(uri);
        }
        if (mList.get(i).getName()!=null){
            textView.setText(mList.get(i).getName());
        }
        return view;
    }
}
