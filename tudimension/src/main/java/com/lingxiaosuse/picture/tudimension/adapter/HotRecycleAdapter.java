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

public class HotRecycleAdapter extends RecyclerView.Adapter{
    private List<HotModle.ResultsBean> resultList;
    private LayoutInflater mLayoutInflater;
    public HotRecycleAdapter(List<HotModle.ResultsBean> resultList){
        this.resultList = resultList;
    }
    public void setResultList(List<HotModle.ResultsBean> resultList){
        this.resultList = resultList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.hot_item,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);

        if (onItemClickListener != null){
            viewHolder.reView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int poisition = viewHolder.getAdapterPosition();
                    onItemClickListener.onItemClick(viewHolder.reView,poisition);
                }
            });
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        String url = resultList.get(position).getUrl();
        String time = resultList.get(position).getCreatedAt();
        String desc = resultList.get(position).getDesc();
        Uri uri = Uri.parse(url);     //图片地址

        ((ViewHolder) holder).iv_meizi_img.setImageURI(uri);
        ((ViewHolder) holder).tv_meizi_des.setText(desc);
    }

    @Override
    public int getItemCount() {
        return resultList==null?0:resultList.size();
    }
    private class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView iv_meizi_img;
        TextView tv_meizi_des;
        View reView;
        public ViewHolder(View itemView) {
            super(itemView);
            reView = itemView;
            iv_meizi_img = (SimpleDraweeView) itemView.findViewById(R.id.iv_meizi_img);
            tv_meizi_des = (TextView) itemView.findViewById(R.id.tv_meizi_des);
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View View, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
