package com.lingxiaosuse.picture.tudimension.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.camera.lingxiao.common.app.ContentValue;
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
import java.util.List;

public class HomeRecyclerAdapter extends BaseRecycleAdapter{
    private List<HomePageModle.Picture> mPicList;
    private List<HomePageModle.slidePic> mSlideList;
    public HomeRecyclerAdapter(List list, List slideList,int headCount, int footCount) {
        super(list, headCount, footCount);
        this.mSlideList = slideList;
    }

    @Override
    protected void bindHeaderData(BaseViewHolder holder, int position, List mList) {
        super.bindHeaderData(holder, position, mList);
        Banner banner = (Banner) holder.getView(R.id.banner_head);
        // 获取cardview的布局属性，记住这里要是布局的最外层的控件的布局属性，如果是里层的会报cast错误
        StaggeredGridLayoutManager
                .LayoutParams clp
                = (StaggeredGridLayoutManager.LayoutParams) holder
                .getView(R.id.ll_head).getLayoutParams();
        // 最最关键一步，设置当前view占满列数，这样就可以占据两列实现头部了
        clp.setFullSpan(true);
        List<String> urlList = new ArrayList<String>();
        List<String> titleList = new ArrayList<String>();
        for (int i = 0; i < mSlideList.size(); i++) {
            urlList.add(mSlideList.get(i).lcover);
            titleList.add(mSlideList.get(i).desc);
        }
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片集合
        banner.setImages(urlList);
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titleList);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(ContentValue.BANNER_TIMER);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.TITLE_BACKGROUND);
        //banner设置方法全部调用完毕时最后调用
        banner.start();
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (mOnBannerClickListener != null){
                    mOnBannerClickListener.onBannerClick(position);
                }
            }
        });
    }
    @Override
    public void bindData(BaseViewHolder holder, int position, List mList) {
        mPicList = mList;
        SimpleDraweeView imageview = (SimpleDraweeView) holder.getView(R.id.iv_home_image);
        TextView textView = (TextView) holder.getView(R.id.tv_home_des);
        final Uri uri = Uri.parse(mPicList.get(position).img + ContentValue.imgRule);
        //如果本地JPEG图，有EXIF的缩略图，image pipeline 可以立刻返回它作为一个缩略图
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(request)
                .setOldController(imageview.getController())
                .build();
        imageview.setController(controller);
        //((ViewHolder) viewHolder).imageview.setImageURI(uri);
        if (mPicList.get(position).desc.isEmpty()){
            textView.setVisibility(View.GONE);
        }else {
           textView.setVisibility(View.VISIBLE);
            textView.setText(mPicList.get(position).desc);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_page;
    }

    @Override
    public int getHeadLayoutId() {
        return R.layout.item_head;
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
