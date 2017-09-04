package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.SpecialModle;
import com.lingxiaosuse.picture.tudimension.utils.StringUtils;

import java.util.List;

import javax.crypto.spec.PSource;

/**
 * Created by lingxiao on 2017/9/4.
 */

public class SpecialRecycleAdapter extends RecyclerView.Adapter{
    private List<SpecialModle.ResBean.AlbumBean> mList;
    private int footCount = 1;
    private static final int BODY_TYPE=1;
    private static final int FOOT_TYPE=2;
    private boolean isFinish;   //是否加载完成 -- 隐藏布局
    public SpecialRecycleAdapter(List<SpecialModle.ResBean.AlbumBean> mList){
        this.mList = mList;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == BODY_TYPE){
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.list_special,parent,false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }else if (viewType == FOOT_TYPE){
            view = LayoutInflater
                    .from(parent.getContext())
                    .inflate(R.layout.item_foot,parent,false);
            FootHolder holder = new FootHolder(view);
            return holder;
        }
        return null;
    }

    //获取总共条目数
    public int getBodySize(){
        return mList.size();
    }
    //判断尾布局
    public boolean isFoot(int position){
        return footCount!=0&&(position>=getBodySize()-1);
    }

    @Override
    public int getItemViewType(int position) {
        if (isFoot(position)){
            return FOOT_TYPE;
        }else {
            return BODY_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder){
            final Uri uri = Uri.parse(mList.get(position).getCover());
            //如果本地JPEG图，有EXIF的缩略图，image pipeline 可以立刻返回它作为一个缩略图
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(((ViewHolder) holder).bodyImage.getController())
                    .build();
            ((ViewHolder) holder).bodyImage.setController(controller);
            ((ViewHolder) holder).title.setText(mList.get(position).getName());
            ((ViewHolder) holder).message.setText(mList.get(position).getDesc());
            ((ViewHolder) holder).nameImage.setImageURI(Uri.parse(mList.get(position).getUser().getAvatar()));
            ((ViewHolder) holder).name.setText(mList.get(position).getUser().getName());
            String data = StringUtils.strToDate(String.valueOf(mList.get(position).getAtime()));
            ((ViewHolder) holder).time.setText(data);
            ((ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(view,position,uri);
                    }
                }
            });

        }else if (holder instanceof FootHolder){
            if (isFinish){
                ((FootHolder) holder).loadingLayout.setVisibility(View.GONE);
                ((FootHolder) holder).finishLayout.setVisibility(View.VISIBLE);
            }else {
                ((FootHolder) holder).loadingLayout.setVisibility(View.VISIBLE);
                ((FootHolder) holder).finishLayout.setVisibility(View.GONE);
            }
            if (listener != null){
                //上拉加载更多
                listener.onLoadMore();
            }
        }

    }

    @Override
    public int getItemCount() {
        return mList==null?0:mList.size();
    }
    class ViewHolder extends RecyclerView.ViewHolder{
        SimpleDraweeView bodyImage,nameImage;
        TextView title,message,name,time;
        public ViewHolder(View itemView) {
            super(itemView);
            bodyImage = itemView.findViewById(R.id.iv_special_pic);
            nameImage = itemView.findViewById(R.id.iv_special_who);
            title = itemView.findViewById(R.id.tv_special_title);
            message = itemView.findViewById(R.id.tv_special_message);
            time = itemView.findViewById(R.id.tv_special_time);
            name = itemView.findViewById(R.id.tv_special_who);
        }
    }
    private class FootHolder extends RecyclerView.ViewHolder{
        RelativeLayout rlayout;
        LinearLayout loadingLayout,finishLayout;
        public FootHolder(View itemView) {
            super(itemView);
            rlayout = itemView.findViewById(R.id.rl_foot);
            loadingLayout = itemView.findViewById(R.id.ll_loading);
            finishLayout = itemView.findViewById(R.id.ll_finish);
        }
    }

    private onLoadmoreListener listener;
    public interface onLoadmoreListener{
        void onLoadMore();
    }
    public void setRefreshListener(onLoadmoreListener listener){
        this.listener = listener;
    }

    public void isFinish(boolean isFinish){
        this.isFinish = isFinish;
    }
    private OnItemClickListener mOnItemClickListener = null;
    //设置item的点击事件
    public interface OnItemClickListener {
        void onItemClick(View view, int position, Uri uri);
    }
    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
