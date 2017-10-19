package com.lingxiaosuse.picture.tudimension.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.activity.BannerDetailActivity;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.utils.UIUtils;
import com.lingxiaosuse.picture.tudimension.view.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lingxiao on 2017/7/26.
 */

public class MyRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private ArrayList<HomePageModle.Picture> list = new ArrayList<>();
    private ArrayList<HomePageModle.slidePic> slideList = new ArrayList<>();
    private ArrayList<Integer> mHeights;
    private int headCount = 1; //头布局个数
    private int footCount = 1; //尾布局个数
    private static final int HEAD_TYPE=1;
    private static final int BODY_TYPE=2;
    private static final int FOOT_TYPE=3;
    private LayoutInflater mLayoutInflater;
    private boolean isFinish;   //是否加载完成 -- 隐藏布局

    private String imgRule = "?imageView2/3/h/230"; //图片规则，从服务器取230大小的图片
    public MyRecycleViewAdapter(ArrayList<HomePageModle.Picture> list,
                                ArrayList<HomePageModle.slidePic> slideList,
                                Context context){
        this.list = list;
        this.slideList = slideList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void getRandomHeight(ArrayList<HomePageModle.Picture> mList){
        mHeights = new ArrayList<>();
        for(int i=0; i < list.size();i++){
            //随机的获取一个范围为200-600直接的高度
            mHeights.add((int)(300+Math.random()*400));
        }
    }

    //获取总共条目数
    public int getBodySize(){
        return list.size();
    }

    //判断头布局
    public boolean isHead(int position){
        return headCount!=0&&position<headCount;
    }
    //判断尾布局
    public boolean isFoot(int position){
        return footCount!=0&&(position>=getBodySize()-1);
    }

    @Override
    public int getItemViewType(int position) {
        if (isHead(position)){
            return HEAD_TYPE;
        }else if (isFoot(position)){
            return FOOT_TYPE;
        }else {
            return BODY_TYPE;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){
            case HEAD_TYPE:
                view = mLayoutInflater.inflate(R.layout.item_head,parent,false);
                HeadHolder headHolder = new HeadHolder(view);
                return headHolder;
            case FOOT_TYPE:
                view = mLayoutInflater.inflate(R.layout.item_foot,parent,false);
                FootHolder footHolder = new FootHolder(view);
                return footHolder;
            case BODY_TYPE:
                view = mLayoutInflater.inflate(R.layout.list_page,parent,false);
                ViewHolder holder = new ViewHolder(view);
                return holder;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) throws NullPointerException{
        if (viewHolder instanceof FootHolder){
            StaggeredGridLayoutManager.LayoutParams clp
                    = (StaggeredGridLayoutManager.LayoutParams) ((FootHolder) viewHolder).rlayout.getLayoutParams();
            clp.setFullSpan(true);
            if (listener != null){
                //上拉加载更多
                listener.onLoadMore();
            }

            if (isFinish){
                ((FootHolder) viewHolder).loadingLayout.setVisibility(View.GONE);
                ((FootHolder) viewHolder).finishLayout.setVisibility(View.VISIBLE);
            }else {
                ((FootHolder) viewHolder).loadingLayout.setVisibility(View.VISIBLE);
                ((FootHolder) viewHolder).finishLayout.setVisibility(View.GONE);
            }
        }else if (viewHolder instanceof HeadHolder){
            // 根据view类型处理不同的操作
            // 获取cardview的布局属性，记住这里要是布局的最外层的控件的布局属性，如果是里层的会报cast错误
            StaggeredGridLayoutManager.LayoutParams clp
                    = (StaggeredGridLayoutManager.LayoutParams) ((HeadHolder) viewHolder).llayout.getLayoutParams();
            // 最最关键一步，设置当前view占满列数，这样就可以占据两列实现头部了
            clp.setFullSpan(true);
            List<String> urlList = new ArrayList<String>();
            List<String> titleList = new ArrayList<String>();
            for (int i = 0; i < slideList.size(); i++) {
                urlList.add(slideList.get(i).lcover);
                titleList.add(slideList.get(i).desc);
            }
            //设置图片加载器
            ((HeadHolder) viewHolder).banner.setImageLoader(new GlideImageLoader());
            //设置banner样式
            ((HeadHolder) viewHolder).banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
            //设置图片集合
            ((HeadHolder) viewHolder).banner.setImages(urlList);
            ((HeadHolder) viewHolder).banner.setBannerAnimation(Transformer.DepthPage);
            //设置标题集合（当banner样式有显示title时）
            ((HeadHolder) viewHolder).banner.setBannerTitles(titleList);
            //设置自动轮播，默认为true
            ((HeadHolder) viewHolder).banner.isAutoPlay(true);
            //设置轮播时间
            ((HeadHolder) viewHolder).banner.setDelayTime(1500);
            //设置指示器位置（当banner模式中有指示器时）
            ((HeadHolder) viewHolder).banner.setIndicatorGravity(BannerConfig.TITLE_BACKGROUND);
            //banner设置方法全部调用完毕时最后调用
            ((HeadHolder) viewHolder).banner.start();
            ((HeadHolder) viewHolder).banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (mOnBannerClickListener != null){
                        mOnBannerClickListener.onBannerClick(position);
                    }
                }
            });
        }else if (viewHolder instanceof ViewHolder){
            final Uri uri = Uri.parse(list.get(position).img +imgRule);

            //如果本地JPEG图，有EXIF的缩略图，image pipeline 可以立刻返回它作为一个缩略图
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setLocalThumbnailPreviewsEnabled(true)
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(request)
                    .setOldController(((ViewHolder) viewHolder).imageview.getController())
                    .build();
            ((ViewHolder) viewHolder).imageview.setController(controller);
            //((ViewHolder) viewHolder).imageview.setImageURI(uri);
            ((ViewHolder) viewHolder).textView.setText(list.get(position).desc);
            //设置点击事件
            if (mOnItemClickListener != null){
                ((ViewHolder) viewHolder).imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnItemClickListener.onItemClick(view,position,uri);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        return list == null?0:list.size();
    }
    private class ViewHolder extends RecyclerView.ViewHolder{
        private SimpleDraweeView imageview;
        private TextView textView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.iv_home_image);
            textView = itemView.findViewById(R.id.tv_home_des);
        }
    }
    private class HeadHolder extends RecyclerView.ViewHolder{
        LinearLayout llayout;
        /*ViewPager viewpager;*/
        private Banner banner;
        public HeadHolder(View itemView) {
            super(itemView);
            llayout = itemView.findViewById(R.id.ll_head);
            /*viewpager = itemView.findViewById(R.id.vp_head);
            viewpager.setAdapter(new HomePagerAdapter(slideList));*/
            banner = itemView.findViewById(R.id.banner_head);
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

    private OnBannerClickListener mOnBannerClickListener = null;
    //设置banner的点击事件
    public interface OnBannerClickListener {
        void onBannerClick(int position);
    }
    public void setOnBannerClickListener(OnBannerClickListener mOnBannerClickListener){
        this.mOnBannerClickListener = mOnBannerClickListener;
    }
}
