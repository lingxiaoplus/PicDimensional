package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;

import java.util.List;

/**
 * Created by lingxiao on 2018/1/18.
 */

public class MzituRecyclerAdapter extends BaseRecycleAdapter{

    private static final String TAG = "MzituRecyclerAdapter";
    private SimpleDraweeView simpleDraweeView;
    private List<String> mImgList;
    private List<String> mTitleList;
    public MzituRecyclerAdapter(List mList, int headCount, int footCount) {
        super(mList, headCount, footCount);
    }
    public void setTitle(List<String> mTitleList){
        this.mTitleList = mTitleList;
    }
    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        mImgList = mList;
        Uri uri = Uri.parse(mImgList.get(position));
        TextView title = (TextView) holder.getView(R.id.tv_mzitu_title);

        try{
            if (null == mTitleList){
                title.setVisibility(View.GONE);
            }else {
                title.setVisibility(View.VISIBLE);
                title.setText(mTitleList.get(position));
            }
            simpleDraweeView = (SimpleDraweeView) holder.getView(R.id.iv_mzitu_image);
            simpleDraweeView.setImageURI(uri);
        }catch (NullPointerException e){
            Log.i(TAG, e.getMessage());
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.list_mzitu;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }
}
