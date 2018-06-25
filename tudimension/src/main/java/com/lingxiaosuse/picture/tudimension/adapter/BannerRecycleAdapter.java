package com.lingxiaosuse.picture.tudimension.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.camera.lingxiao.common.app.ContentValue;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.BannerModle;

import java.util.List;

/**
 * Created by lingxiao on 2017/9/1.
 */

public class BannerRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<BannerModle.WallpaperBean> picList;
    private int footCount = 1; //尾布局个数
    private static final int BODY_TYPE=1;
    private static final int FOOT_TYPE=2;
    private boolean isFinish;
    public BannerRecycleAdapter(List<BannerModle.WallpaperBean> picList,Context context){
        this.picList = picList;
    }
    //返回条目总数
    public int getBodySize(){
        return picList.size();
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
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType){
            case BODY_TYPE:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.list_banner,parent,false);
                BannerHolder holder = new BannerHolder(view);
                return holder;
            case FOOT_TYPE:
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.item_foot,parent,false);
                FootHolder footHolder = new FootHolder(view);
                return footHolder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof BannerHolder){
            Uri uri = Uri.parse(picList.get(position).getImg()
                    + ContentValue.imgRule);
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(((BannerHolder)holder).imageview.getController())
                    .build();
            ((BannerHolder)holder).imageview.setController(controller);
            ((BannerHolder)holder).imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null){
                        mOnItemClickListener.onItemClick(view,position,
                                Uri.parse(picList.get(position).getImg()));
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
                if (listener != null){
                    //上拉加载更多
                    listener.onLoadMore(position);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return picList==null?0:picList.size();
    }

    private class BannerHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView imageview;
        public BannerHolder(View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.iv_banner_image);
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
        void onLoadMore(int position);
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
