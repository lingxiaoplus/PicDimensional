package com.lingxiaosuse.picture.tudimension.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lingxiao on 2018/1/21.
 */

public class ZoomableViewpager extends ViewPager implements ZoomableDrawwView.OnMovingListener{
    /**  当前子控件是否处理拖动状态  */
    private boolean mChildIsBeingDragged = false;
    public ZoomableViewpager(Context context) {
        super(context);
    }

    public ZoomableViewpager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mChildIsBeingDragged){
            return false;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void startDrag() {
        mChildIsBeingDragged = true;
    }

    @Override
    public void stopDrag() {
        mChildIsBeingDragged = false;
    }
}
