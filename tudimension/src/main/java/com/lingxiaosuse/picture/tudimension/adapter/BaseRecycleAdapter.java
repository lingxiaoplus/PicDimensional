package com.lingxiaosuse.picture.tudimension.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lingxiaosuse.picture.tudimension.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lingxiao on 2017/9/30.
 */

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<BaseRecycleAdapter.BaseViewHolder>{
    private List<T> mList;
    public BaseRecycleAdapter(List<T> mList){
        this.mList = mList;
    }
    @Override
    public BaseRecycleAdapter.BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(getLayoutId(),parent,false);
        final BaseViewHolder holder = new BaseViewHolder(view);
        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int poisition = holder.getAdapterPosition();
                    onItemClickListener.onItemClick(holder.itemView,poisition);
                }
            });
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseRecycleAdapter.BaseViewHolder holder, int position) {
        bindData(holder,position,mList);
    }

    public abstract void bindData(BaseViewHolder holder, int position, List<T> mList);
    /**
     *获取布局文件
     */
    public abstract int getLayoutId();
    @Override
    public int getItemCount() {
        return mList == null?0:mList.size();
    }
    public class BaseViewHolder extends RecyclerView.ViewHolder{
        private Map<Integer,View> mViewMap;
        public BaseViewHolder(View itemView) {
            super(itemView);
            mViewMap = new HashMap<>();
        }
        public View getView(int id){
            View view = mViewMap.get(id);
            if (view == null){
                view = itemView.findViewById(id);
                mViewMap.put(id,view);
            }
            return view;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View View, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
    /**
     *刷新数据
     */
    public void onRefresh(List<T> datas){
        this.mList.clear();
        this.mList = datas;
        notifyDataSetChanged();
    }
    public void addData(List<T> datas){
        this.mList.addAll(datas);
        notifyDataSetChanged();
    }
}
