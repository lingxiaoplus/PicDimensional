package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.HotModle;

import java.util.List;

/**
 * Created by lingxiao on 2017/8/30.
 */

public class HotRecycleAdapter extends BaseRecycleAdapter{
    private List<HotModle.ResultsBean> resultList;
    private LayoutInflater mLayoutInflater;

    public HotRecycleAdapter(List mList, int headCount, int footCount) {
        super(mList, headCount, footCount);
    }

    public void setResultList(List<HotModle.ResultsBean> resultList){
        this.resultList = resultList;
    }
    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        resultList = mList;
        String url = resultList.get(position).getUrl();
        String time = resultList.get(position).getCreatedAt();
        String desc = resultList.get(position).getDesc();
        Uri uri = Uri.parse(url);     //图片地址

        SimpleDraweeView img = (SimpleDraweeView)
               holder.getView(R.id.iv_meizi_img);
        img.setImageURI(uri);
        TextView textView = (TextView)
                holder.getView(R.id.tv_meizi_des);
        textView.setVisibility(View.GONE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.hot_item;
    }

    @Override
    public int getHeadLayoutId() {
        return 0;
    }

}
