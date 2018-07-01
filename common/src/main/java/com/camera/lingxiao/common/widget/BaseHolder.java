package com.camera.lingxiao.common.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Unbinder;

public abstract class BaseHolder<Data> extends RecyclerView.ViewHolder{
    private SparseArray<View> mViews;
    public AdapterCallback<Data> callback;
    public Unbinder unbinder;
    protected Data mData;
    protected int mPosition;
    public BaseHolder(View itemView) {
        super(itemView);
        mViews = new SparseArray();
    }

    /**
     * 通过viewid查找view
     * @param viewId
     * @param <T>
     * @return
     */
    public  <T extends View> T getView(int viewId){
        View view =mViews.get(viewId);
        if (view == null){
            view = itemView.findViewById(viewId);
        }
        return (T) view;
    }

    public BaseHolder setText(int viewId, String text) {
        TextView tv = getView(viewId);
        tv.setText(text);
        return this;
    }

    public BaseHolder setImageResource(int viewId, int resId) {
        ImageView view = getView(viewId);
        view.setImageResource(resId);
        return this;
    }

   /* public BaseHolder setImageUrl(int viewId, String picPath,long time) {
        ImageView view = getView(viewId);
        GlideHelper.loadImageWithData(picPath,view,time);
        return this;
    }
*/
    public BaseHolder setOnClickListener(int viewId,
                                         View.OnClickListener listener) {
        View view = getView(viewId);
        view.setOnClickListener(listener);
        return this;
    }

    /**
     * 用于绑定数据的触发
     *
     * @param data 绑定的数据
     */
    void bind(Data data,int pos) {
        this.mData = data;
        this.mPosition = pos;
        onBind(data,pos);
    }

    /**
     * 当触发绑定数据的时候，的回掉；必须复写
     *
     * @param data 绑定的数据
     */
    protected abstract void onBind(Data data,int position);

    /**
     * Holder自己对自己对应的Data进行更新操作
     *
     * @param data Data数据
     */
    public void updateData(Data data) {
        if (this.callback != null) {
            this.callback.update(data, this);
        }
    }
}
