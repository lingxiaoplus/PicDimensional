package com.lingxiaosuse.picture.tudimension.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by lingxiao on 2017/9/21.
 * 当PhotoView 和 ViewPager 组合时 ，
 * 用双指进行放大时 是没有问题的，但是用双指进行缩小的时候，程序就会崩掉
 * 只需要自定义一个类去继承ViewPager ，然后重写Viewpager的 onInterceptTouchEvent()的方法
 */

public class PhotoViewPager extends ViewPager{
    public PhotoViewPager(Context context) {
        super(context);
    }

    public PhotoViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }
}
