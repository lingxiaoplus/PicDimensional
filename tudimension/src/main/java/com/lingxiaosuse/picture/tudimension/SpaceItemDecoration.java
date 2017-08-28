package com.lingxiaosuse.picture.tudimension;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * Created by lingxiao on 2017/8/3.
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration{
    private int space;
    private StaggeredGridLayoutManager.LayoutParams lp;

    public SpaceItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left=space;
        outRect.right=space;
        outRect.bottom=space;
        if(parent.getChildAdapterPosition(view)==0){
            //用于设置第一个位置跟顶部的距离
            outRect.top=space;
        }/*else {
            lp = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            if (lp.getSpanIndex() == 0) {
                // 用于设同行两个间隔间跟其距离左右屏幕间隔相同
                outRect.right = 0;
            }
        }*/
    }
}
