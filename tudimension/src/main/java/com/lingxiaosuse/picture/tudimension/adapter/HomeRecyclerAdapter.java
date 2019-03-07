package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.camera.lingxiao.common.app.ContentValue;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.lingxiaosuse.picture.tudimension.R;
import com.lingxiaosuse.picture.tudimension.modle.HomePageModle;
import com.lingxiaosuse.picture.tudimension.widget.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

public class HomeRecyclerAdapter extends BaseQuickAdapter<HomePageModle, BaseViewHolder> {
    private static final String TAG = HomeRecyclerAdapter.class.getSimpleName();
    List<HomePageModle.slidePic> mSlideList;
    public HomeRecyclerAdapter(int layoutResId, @Nullable List<HomePageModle> data) {
        super(layoutResId, data);
    }

    protected void bindHeaderData(BaseViewHolder holder, List mList) {
        List<HomePageModle.slidePic> slideList = mList;
        this.mSlideList = mList;
        View view = getHeaderLayout();
        Banner banner = view.findViewById(R.id.banner_head);
        // 获取cardview的布局属性，记住这里要是布局的最外层的控件的布局属性，如果是里层的会报cast错误
        /*StaggeredGridLayoutManager
                .LayoutParams clp
                = (StaggeredGridLayoutManager.LayoutParams) holder
                .getView(R.id.ll_head).getLayoutParams();
        // 最最关键一步，设置当前view占满列数，这样就可以占据两列实现头部了
        clp.setFullSpan(true);*/

        if (slideList.size() > 0){
            List<String> urlList = new ArrayList<String>();
            List<String> titleList = new ArrayList<String>();
            for (int i = 0; i < slideList.size(); i++) {
                urlList.add(slideList.get(i).lcover);
                titleList.add(slideList.get(i).desc);
            }
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader())
                    .setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE)//设置banner样式
                    .setImages(urlList)//设置图片集合
                    .setBannerAnimation(Transformer.DepthPage)
                    .setBannerTitles(titleList)//设置标题集合（当banner样式有显示title时）
                    .isAutoPlay(true) //设置自动轮播，默认为true
                    .setDelayTime(ContentValue.BANNER_TIMER)//设置轮播时间
                    .setIndicatorGravity(BannerConfig.TITLE_BACKGROUND)//设置指示器位置（当banner模式中有指示器时）
                    .start();
            banner.setOnBannerListener(new OnBannerListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (mOnBannerClickListener != null){
                        mOnBannerClickListener.onBannerClick(position);
                    }
                }
            });
        }

    }
    public void bindData(BaseViewHolder holder, List mList) {
        List<HomePageModle.Picture> picList = mList;
        int position = holder.getAdapterPosition();
        SimpleDraweeView imageview = (SimpleDraweeView) holder.getView(R.id.iv_home_image);
        TextView textView = (TextView) holder.getView(R.id.tv_home_des);
        final Uri uri = Uri.parse(picList.get(position).img + ContentValue.imgRule);
        //如果本地JPEG图，有EXIF的缩略图，image pipeline 可以立刻返回它作为一个缩略图
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageview.getController())
                .build();
        imageview.setController(controller);
        if (picList.get(position).desc.isEmpty()){
            textView.setVisibility(View.GONE);
        }else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(picList.get(position).desc);
        }
    }

    private OnBannerClickListener mOnBannerClickListener = null;

    @Override
    protected void convert(BaseViewHolder helper, HomePageModle modle) {
        bindData(helper,modle.getWallpaper());
        if (mSlideList == null){
            //首页轮播图
            List<HomePageModle.slidePic> slideList = new ArrayList<>();
            List<HomePageModle.HomeDes> homeDesList = new ArrayList<>();
            for (int i = 0; i < modle.getHomepage().size(); i++) {
                //循环遍历该集合，取出首页轮播图
                homeDesList.addAll(modle.getHomepage().get(i).items);
            }
            for (int j = 0; j < homeDesList.size(); j++) {
                if (homeDesList.get(j).isStatus() && !TextUtils.isEmpty(homeDesList.get(j).value.cover)){
                    //做一个判断是否是轮播图，因为这个数据里有广告，需要去除  如果是，在建一个集合专门放图
                    slideList.add(homeDesList.get(j).value);
                }
            }
            bindHeaderData(helper,slideList);
        }
    }

    //设置banner的点击事件
    public interface OnBannerClickListener {
        void onBannerClick(int position);
    }
    public void setOnBannerClickListener(OnBannerClickListener mOnBannerClickListener){
        this.mOnBannerClickListener = mOnBannerClickListener;
    }


    public List<HomePageModle.slidePic> getSlideList(){
        return mSlideList;
    }
}
