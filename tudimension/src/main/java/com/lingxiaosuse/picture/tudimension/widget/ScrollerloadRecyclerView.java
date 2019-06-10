package com.lingxiaosuse.picture.tudimension.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.cooltechworks.views.shimmer.ShimmerRecyclerView;
import com.facebook.drawee.backends.pipeline.Fresco;

public class ScrollerloadRecyclerView extends ShimmerRecyclerView {

    private boolean showShimmer = false;
    public ScrollerloadRecyclerView(Context context) {
        this(context,null);
    }

    public ScrollerloadRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ScrollerloadRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //init();
    }

    private void init() {
        addOnScrollListener(new ImageAutoLoadListener());
    }

    private class ImageAutoLoadListener extends OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            switch (newState){
                case SCROLL_STATE_IDLE:
                    Fresco.getImagePipeline().resume();  //空闲状态 加载图片
                    break;
                case SCROLL_STATE_DRAGGING:
                case SCROLL_STATE_SETTLING :
                    //惯性滑动和滚动 停止加载图片
                    Fresco.getImagePipeline().pause();
                    break;
                 default:
                    break;
            }
        }
    }

    @Override
    public void showShimmerAdapter() {
        super.showShimmerAdapter();
        showShimmer = true;
    }

    @Override
    public void hideShimmerAdapter() {
        super.hideShimmerAdapter();
        showShimmer = false;
    }

    public boolean isShowShimmer() {
        return showShimmer;
    }
}
